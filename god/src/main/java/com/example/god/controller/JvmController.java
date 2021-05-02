package com.example.god.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.god.config.Configure;
import com.example.god.model.jvm.JvmUtil;
import com.example.god.model.thread.*;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.lang.management.ThreadInfo;
import java.util.*;

@RestController
@RequestMapping("/jvm")
public class JvmController {

    public static final List<String> threadStates = Arrays.asList("NEW", "RUNNABLE", "BLOCKED", "WAITING", "TIMED_WAITING", "TERMINATED");

    @Resource
    private JvmUtil jvmUtil;

    @Resource
    private Configure configure;

    @GetMapping("/info")
    public String jvm() {
        return JSONObject.toJSONString(jvmUtil.getJvmDetails().getJvmInfo());
    }

    @GetMapping("/thread/busy")
    public String thread(@RequestParam(value = "top", defaultValue = "5") Integer busy,
                         @RequestParam(value = "interval", defaultValue = "2000") Integer interval) {
        if (busy <= 0) {
            return "error";
        }

        List<ThreadVO> oldThreads = ThreadUtil.getThreads();
        long oldGetCupTimeNanos = System.nanoTime();
        Map<ThreadVO, Long> oldCpuTimes = ThreadUtil.getThreadsCpuTimes(oldThreads, true);
        try {
            Thread.sleep(interval);
        } catch (InterruptedException e) {
            return e.getMessage();
        }
        List<ThreadVO> newThreads = ThreadUtil.getThreads();
        long newGetCupTimeNanos = System.nanoTime();
        Map<ThreadVO, Long> newCpuTimes = ThreadUtil.getThreadsCpuTimes(newThreads, true);
        List<ThreadVO> topNThreads = ThreadUtil.addCpuUsage(newGetCupTimeNanos- oldGetCupTimeNanos, oldCpuTimes, newCpuTimes, newThreads);
        if (CollectionUtils.isEmpty(topNThreads)) {
            return null;
        }
        int limit = Math.min(topNThreads.size(), busy);

        topNThreads = topNThreads.subList(0, limit);

        List<Long> tids = new ArrayList<>(topNThreads.size());
        for (ThreadVO thread : topNThreads) {
            if (thread.getId() > 0) {
                tids.add(thread.getId());
            }
        }

        ThreadInfo[] threadInfos = configure.getThreadMXBean().getThreadInfo(this.unboxing(tids), true, true);
        if (tids.size()> 0 && threadInfos == null) {
            return "get top busy threads failed";
        }

        //threadInfo with cpuUsage
        List<BusyThreadInfo> busyThreadInfos = new ArrayList<>(topNThreads.size());
        for (ThreadVO thread : topNThreads) {
            ThreadInfo threadInfo = null;
            for (ThreadInfo info : threadInfos) {
                if (info.getThreadId() == thread.getId()) {
                    threadInfo = info;
                    break;
                }
            }
            BusyThreadInfo busyThread = new BusyThreadInfo(thread, threadInfo);
            busyThreadInfos.add(busyThread);
        }
        return JSONObject.toJSONString(new ThreadModel(busyThreadInfos));
    }

    @GetMapping("/thread/state")
    public String geThread(@RequestParam(value = "state") String state,
                           @RequestParam(value = "interval", defaultValue = "2000") Integer interval) {
        List<ThreadVO> threads = ThreadUtil.getThreads();

        // 统计各种线程状态
        Map<Thread.State, Integer> stateCountMap = new LinkedHashMap<>();
        for (Thread.State s : Thread.State.values()) {
            stateCountMap.put(s, 0);
        }

        for (ThreadVO thread : threads) {
            Thread.State threadState = thread.getState();
            Integer count = stateCountMap.get(threadState);
            stateCountMap.put(threadState, count + 1);
        }

        List<ThreadVO> resultThreads = new ArrayList<>();
        boolean includeInternalThreads = true;
        if (state != null && !"".equals(state) && threadStates.contains(state.toUpperCase())) {
            String stateUpperCase = state.toUpperCase();
            includeInternalThreads = false;
            for (ThreadVO thread : threads) {
                if (thread.getState() != null && stateUpperCase.equals(thread.getState().name())) {
                    resultThreads.add(thread);
                }
            }
        } else {
            resultThreads = threads;
        }

        //thread stats
        long oldGetCupTimeNanos = System.nanoTime();
        Map<ThreadVO, Long> oldCpuTimes = ThreadUtil.getThreadsCpuTimes(resultThreads, includeInternalThreads);

        try {
            Thread.sleep(interval);
        } catch (InterruptedException e) {
            return e.getMessage();
        }

        long newGetCupTimeNanos = System.nanoTime();
        Map<ThreadVO, Long> newCpuTimes = ThreadUtil.getThreadsCpuTimes(resultThreads, includeInternalThreads);

        List<ThreadVO> threadStats = ThreadUtil.addCpuUsage(newGetCupTimeNanos- oldGetCupTimeNanos, oldCpuTimes, newCpuTimes, resultThreads);

        return JSONObject.toJSONString(new ThreadModel(threadStats, stateCountMap, includeInternalThreads));
    }

    @GetMapping("/thread/{id}")
    public String threadInfo(@PathVariable long id) {
        if (id <= 0) {
            return null;
        }
        ThreadInfo[] threadInfos = configure.getThreadMXBean().getThreadInfo(new long[]{id}, true, true);
        if (threadInfos == null || threadInfos.length < 1 || threadInfos[0] == null) {
            return null;
        }
        return JSONObject.toJSONString(new ThreadModel(threadInfos[0]));
    }


    @GetMapping("/thread/block")
    public String threadBlocking() {
        BlockingLockInfo blockingLockInfo = ThreadUtil.findMostBlockingLock();
        if (blockingLockInfo.getThreadInfo() == null) {
            return null;
        }
        return JSONObject.toJSONString(new ThreadModel(blockingLockInfo));
    }

    private long[] unboxing(List<Long> list) {
        if (CollectionUtils.isEmpty(list)) {
            return new long[0];
        }
        final long[] result = new long[list.size()];
        for (int i = 0; i < list.size(); i++) {
            result[i] = list.get(i);
        }
        return result;
    }


}
