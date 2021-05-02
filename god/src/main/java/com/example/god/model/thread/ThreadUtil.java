package com.example.god.model.thread;

import sun.management.ManagementFactoryHelper;

import java.lang.management.*;
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

    private static Map<String, Long> getInternalThreadCpuTimes() {
        return ManagementFactoryHelper.getHotspotThreadMBean().getInternalThreadCpuTimes();
    }

    public static Map<ThreadVO, Long> getThreadsCpuTimes(List<ThreadVO> threads, boolean includeInternalThreads) {
        Map<ThreadVO, Long> lastCpuTimes = new HashMap<>();
        for (ThreadVO thread : threads) {
            if (thread.getId() > 0) {
                long cpu = ManagementFactory.getThreadMXBean().getThreadCpuTime(thread.getId());
                lastCpuTimes.put(thread, cpu);
                thread.setTime(cpu / 1000000);
            }
        }

        // add internal threads
        if (includeInternalThreads) {
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
        }

        //sort by time
        threads.sort((o1, o2) -> {
            long l1 = o1.getTime();
            long l2 = o2.getTime();
            return Long.compare(l2, l1);
        });
        return lastCpuTimes;


    }

    public static List<ThreadVO> addCpuUsage(long intervalTimeNanos, Map<ThreadVO, Long> oldCpuTimes, Map<ThreadVO, Long> newCpuTimes, List<ThreadVO> threads) {

        // Compute delta time
        final Map<ThreadVO, Long> deltas = new HashMap<>(threads.size());
        for (ThreadVO thread : newCpuTimes.keySet()) {
            Long t = oldCpuTimes.get(thread);
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

        // Compute cpu usage
        final HashMap<ThreadVO, Double> cpuUsages = new HashMap<>(threads.size());
        for (ThreadVO thread : threads) {
            double cpu = deltas.get(thread) * 10000 / intervalTimeNanos / 100.0;
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
        return threads;
    }

    public static BlockingLockInfo findMostBlockingLock() {
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        ThreadInfo[] infos = threadMXBean.dumpAllThreads(threadMXBean.isObjectMonitorUsageSupported(),
                threadMXBean.isSynchronizerUsageSupported());

        // a map of <LockInfo.getIdentityHashCode, number of thread blocking on this>
        Map<Integer, Integer> blockCountPerLock = new HashMap<>();
        // a map of <LockInfo.getIdentityHashCode, the thread info that holding this lock
        Map<Integer, ThreadInfo> ownerThreadPerLock = new HashMap<>();

        for (ThreadInfo info: infos) {
            if (info == null) {
                continue;
            }

            LockInfo lockInfo = info.getLockInfo();
            if (lockInfo != null) {
                // the current thread is blocked waiting on some condition
                if (blockCountPerLock.get(lockInfo.getIdentityHashCode()) == null) {
                    blockCountPerLock.put(lockInfo.getIdentityHashCode(), 0);
                }
                int blockedCount = blockCountPerLock.get(lockInfo.getIdentityHashCode());
                blockCountPerLock.put(lockInfo.getIdentityHashCode(), blockedCount + 1);
            }

            for (MonitorInfo monitorInfo: info.getLockedMonitors()) {
                // the object monitor currently held by this thread
                if (ownerThreadPerLock.get(monitorInfo.getIdentityHashCode()) == null) {
                    ownerThreadPerLock.put(monitorInfo.getIdentityHashCode(), info);
                }
            }

            for (LockInfo lockedSync: info.getLockedSynchronizers()) {
                // the ownable synchronizer currently held by this thread
                if (ownerThreadPerLock.get(lockedSync.getIdentityHashCode()) == null) {
                    ownerThreadPerLock.put(lockedSync.getIdentityHashCode(), info);
                }
            }
        }

        // find the thread that is holding the lock that blocking the largest number of threads.
        int mostBlockingLock = 0; // System.identityHashCode(null) == 0
        int maxBlockingCount = 0;
        for (Map.Entry<Integer, Integer> entry: blockCountPerLock.entrySet()) {
            if (entry.getValue() > maxBlockingCount && ownerThreadPerLock.get(entry.getKey()) != null) {
                // the lock is explicitly held by anther thread.
                maxBlockingCount = entry.getValue();
                mostBlockingLock = entry.getKey();
            }
        }

        if (mostBlockingLock == 0) {
            // nothing found
            return new BlockingLockInfo();
        }

        BlockingLockInfo blockingLockInfo = new BlockingLockInfo();
        blockingLockInfo.setThreadInfo(ownerThreadPerLock.get(mostBlockingLock));
        blockingLockInfo.setLockIdentityHashCode(mostBlockingLock);
        blockingLockInfo.setBlockingThreadCount(blockCountPerLock.get(mostBlockingLock));
        return blockingLockInfo;
    }
}
