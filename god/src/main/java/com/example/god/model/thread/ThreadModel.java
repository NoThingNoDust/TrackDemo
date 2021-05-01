package com.example.god.model.thread;

import java.lang.management.ThreadInfo;
import java.util.List;
import java.util.Map;

public class ThreadModel {

    private ThreadInfo threadInfo;

    private BlockingLockInfo blockingLockInfo;

    private List<BusyThreadInfo> busyThreads;

    private List<ThreadVO> threadStats;
    private Map<Thread.State, Integer> threadStateCount;
    private boolean all;

    public ThreadModel() {
    }

    public ThreadModel(ThreadInfo threadInfo) {
        this.threadInfo = threadInfo;
    }

    public ThreadModel(BlockingLockInfo blockingLockInfo) {
        this.blockingLockInfo = blockingLockInfo;
    }

    public ThreadModel(List<BusyThreadInfo> busyThreads) {
        this.busyThreads = busyThreads;
    }

    public ThreadModel(List<ThreadVO> threadStats, Map<Thread.State, Integer> threadStateCount, boolean all) {
        this.threadStats = threadStats;
        this.threadStateCount = threadStateCount;
        this.all = all;
    }

    public ThreadInfo getThreadInfo() {
        return threadInfo;
    }

    public void setThreadInfo(ThreadInfo threadInfo) {
        this.threadInfo = threadInfo;
    }

    public BlockingLockInfo getBlockingLockInfo() {
        return blockingLockInfo;
    }

    public void setBlockingLockInfo(BlockingLockInfo blockingLockInfo) {
        this.blockingLockInfo = blockingLockInfo;
    }

    public List<BusyThreadInfo> getBusyThreads() {
        return busyThreads;
    }

    public void setBusyThreads(List<BusyThreadInfo> busyThreads) {
        this.busyThreads = busyThreads;
    }

    public List<ThreadVO> getThreadStats() {
        return threadStats;
    }

    public void setThreadStats(List<ThreadVO> threadStats) {
        this.threadStats = threadStats;
    }

    public Map<Thread.State, Integer> getThreadStateCount() {
        return threadStateCount;
    }

    public void setThreadStateCount(Map<Thread.State, Integer> threadStateCount) {
        this.threadStateCount = threadStateCount;
    }

    public boolean isAll() {
        return all;
    }

    public void setAll(boolean all) {
        this.all = all;
    }
}
