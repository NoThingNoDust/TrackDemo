package com.example.god.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.god.config.Configure;
import com.example.god.model.jvm.JvmUtil;
import com.example.god.model.thread.BusyThreadInfo;
import com.example.god.model.thread.ThreadModel;
import com.example.god.model.thread.ThreadUtil;
import com.example.god.model.thread.ThreadVO;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/jvm")
public class JvmController {

    @Resource
    private JvmUtil jvmUtil;

    @Resource
    private Configure configure;

    @GetMapping("/info")
    public String jvm() {
        return JSONObject.toJSONString(jvmUtil.getJvmDetails().getJvmInfo());
    }

    @GetMapping("/thread")
    public String thread(@RequestParam(value = "busy", defaultValue = "5") Integer busy,
                         @RequestParam(value = "interval", defaultValue = "2000") Integer interval) {
        if (busy <= 0) {
            return "error";
        }
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        long lastSampleTimeNanos = System.nanoTime();
        Map<ThreadVO, Long> lastCpuTimes = ThreadUtil.snapshot(threadMXBean);
        try {
            Thread.sleep(interval);
        } catch (InterruptedException e) {
            return e.getMessage();
        }
        List<ThreadVO> topNThreads = ThreadUtil.mergeAndSort(threadMXBean, lastCpuTimes, lastSampleTimeNanos);
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
