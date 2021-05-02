package com.example.god.model.thread;

import sun.management.ManagementFactoryHelper;

import java.lang.management.ThreadMXBean;
import java.util.*;


public class ThreadUtil {

    /**
     * get thread group
     */
    public static ThreadGroup getRoot() {
        ThreadGroup group = Thread.currentThread().getThreadGroup();
        ThreadGroup parent;
        while ((parent = group.getParent()) != null) {
            group = parent;
        }
        return group;
    }

    /**
     * get all thread
     */
    public static List<ThreadVO> getThreads() {
        ThreadGroup root = getRoot();
        Thread[] threads = new Thread[root.activeCount()];
        while (root.enumerate(threads, true) == threads.length) {
            threads = new Thread[threads.length * 2];
        }
        List<ThreadVO> list = new ArrayList<>(threads.length);
        for (Thread thread : threads) {
            if (thread != null) {
                ThreadVO threadVO = createThreadVO(thread);
                list.add(threadVO);
            }
        }
        return list;
    }

    private static ThreadVO createThreadVO(Thread thread) {
        ThreadGroup group = thread.getThreadGroup();
        ThreadVO threadVO = new ThreadVO();
        threadVO.setId(thread.getId());
        threadVO.setName(thread.getName());
        threadVO.setGroup(group == null ? "" : group.getName());
        threadVO.setPriority(thread.getPriority());
        threadVO.setState(thread.getState());
        threadVO.setInterrupted(thread.isInterrupted());
        threadVO.setDaemon(thread.isDaemon());
        return threadVO;
    }

    private static ThreadVO createThreadVO(String name) {
        ThreadVO threadVO = new ThreadVO();
        threadVO.setId(-1);
        threadVO.setName(name);
        threadVO.setPriority(-1);
        threadVO.setDaemon(true);
        threadVO.setInterrupted(false);
        return threadVO;
    }

    public static Map<ThreadVO, Long> snapshot(ThreadMXBean threadMXBean) {
        Map<ThreadVO, Long> lastCpuTimes = new HashMap<>();
        List<ThreadVO> threads = getThreads();
        for (ThreadVO thread : threads) {
            if (thread.getId() > 0) {
                long cpu = threadMXBean.getThreadCpuTime(thread.getId());
                lastCpuTimes.put(thread, cpu);
                thread.setTime(cpu / 1000000);
            }
        }

        // add internal threads
        Map<String, Long> internalThreadCpuTimes = getInternalThreadCpuTimes();
        if (internalThreadCpuTimes != null) {
            for (Map.Entry<String, Long> entry : internalThreadCpuTimes.entrySet()) {
                String key = entry.getKey();
                ThreadVO thread = createThreadVO(key);
                thread.setTime(entry.getValue() / 1000000);
                threads.add(thread);
                lastCpuTimes.put(thread, entry.getValue());
            }
        }

        //sort by time
        threads.sort((o1, o2) -> {
            long l1 = o1.getTime();
            long l2 = o2.getTime();
            return Long.compare(l2, l1);
        });
        return lastCpuTimes;
    }


    private static Map<String, Long> getInternalThreadCpuTimes() {
        return ManagementFactoryHelper.getHotspotThreadMBean().getInternalThreadCpuTimes();
    }

    public static List<ThreadVO> mergeAndSort(ThreadMXBean threadMXBean, Map<ThreadVO, Long> lastCpuTimes, long lastSampleTimeNanos) {

        long newSampleTimeNanos = System.nanoTime();
        List<ThreadVO> threads = getThreads();

        // Resample
        Map<ThreadVO, Long> newCpuTimes = new HashMap<>(threads.size());
        for (ThreadVO thread : threads) {
            if (thread.getId() > 0) {
                long cpu = threadMXBean.getThreadCpuTime(thread.getId());
                newCpuTimes.put(thread, cpu);
            }
        }
        // internal threads
        Map<String, Long> newInternalThreadCpuTimes = getInternalThreadCpuTimes();
        if (newInternalThreadCpuTimes != null) {
            for (Map.Entry<String, Long> entry : newInternalThreadCpuTimes.entrySet()) {
                ThreadVO threadVO = createThreadVO(entry.getKey());
                threads.add(threadVO);
                newCpuTimes.put(threadVO, entry.getValue());
            }
        }

        // Compute delta time
        final Map<ThreadVO, Long> deltas = new HashMap<>(threads.size());
        for (ThreadVO thread : newCpuTimes.keySet()) {
            Long t = lastCpuTimes.get(thread);
            if (t == null) {
                t = 0L;
            }
            long time1 = t;
            long time2 = newCpuTimes.get(thread);
            if (time1 == -1) {
                time1 = time2;
            } else if (time2 == -1) {
                time2 = time1;
            }
            long delta = time2 - time1;
            deltas.put(thread, delta);
        }

        long sampleIntervalNanos = newSampleTimeNanos - lastSampleTimeNanos;

        // Compute cpu usage
        final HashMap<ThreadVO, Double> cpuUsages = new HashMap<>(threads.size());
        for (ThreadVO thread : threads) {
            double cpu = sampleIntervalNanos == 0 ? 0 : (deltas.get(thread) * 10000 / sampleIntervalNanos / 100.0);
            cpuUsages.put(thread, cpu);
        }

        // Sort by CPU time
        threads.sort((o1, o2) -> {
            long l1 = deltas.get(o1);
            long l2 = deltas.get(o2);
            return Long.compare(l2, l1);
        });

        for (ThreadVO thread : threads) {
            long timeMills = newCpuTimes.get(thread) / 1000000;
            long deltaTime = deltas.get(thread) / 1000000;
            double cpu = cpuUsages.get(thread);

            thread.setCpu(cpu);
            thread.setTime(timeMills);
            thread.setDeltaTime(deltaTime);
        }
        lastCpuTimes = newCpuTimes;
        lastSampleTimeNanos = newSampleTimeNanos;
        return threads;
    }
}
