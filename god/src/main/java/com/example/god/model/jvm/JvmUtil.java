package com.example.god.model.jvm;

import com.example.god.config.Configure;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.management.*;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;


@Component
public class JvmUtil {

    @Resource
    private Configure configure;

    public JvmModel getJvmDetails() {
        JvmModel jvmModel = new JvmModel();
        addRuntimeInfo(jvmModel);

        addClassLoading(jvmModel);

        addCompilation(jvmModel);

        addGarbageCollectors(jvmModel);

        addMemoryManagers(jvmModel);

        addMemory(jvmModel);

        addOperatingSystem(jvmModel);

        addThread(jvmModel);

        addFileDescriptor(jvmModel);

        return jvmModel;

    }


    private void addFileDescriptor(JvmModel jvmModel) {
        OperatingSystemMXBean operatingSystemMXBean = configure.getOperatingSystemMXBean();
        String group = "FILE-DESCRIPTOR";
        jvmModel.generator(group, "MAX-FILE-DESCRIPTOR-COUNT", invokeFileDescriptor(operatingSystemMXBean, "getMaxFileDescriptorCount"))
                .generator(group,"OPEN-FILE-DESCRIPTOR-COUNT", invokeFileDescriptor(operatingSystemMXBean, "getOpenFileDescriptorCount"));
    }

    private long invokeFileDescriptor(OperatingSystemMXBean os, String name) {
        try {
            final Method method = os.getClass().getDeclaredMethod(name);
            method.setAccessible(true);
            return (Long) method.invoke(os);
        } catch (Exception e) {
            return -1;
        }
    }

    private void addRuntimeInfo(JvmModel jvmModel) {
        String bootClassPath = "";
        RuntimeMXBean runtimeMXBean = configure.getRuntimeMXBean();
        try {
            bootClassPath = runtimeMXBean.getBootClassPath();
        } catch (Exception e) {
            // under jdk9 will throw UnsupportedOperationException, ignore
        }
        String group = "RUNTIME";
        jvmModel.generator(group,"MACHINE-NAME", runtimeMXBean.getName());
        jvmModel.generator(group, "JVM-START-TIME", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(runtimeMXBean.getStartTime())));
        jvmModel.generator(group, "MANAGEMENT-SPEC-VERSION", runtimeMXBean.getManagementSpecVersion());
        jvmModel.generator(group, "SPEC-NAME", runtimeMXBean.getSpecName());
        jvmModel.generator(group, "SPEC-VENDOR", runtimeMXBean.getSpecVendor());
        jvmModel.generator(group, "SPEC-VERSION", runtimeMXBean.getSpecVersion());
        jvmModel.generator(group, "VM-NAME", runtimeMXBean.getVmName());
        jvmModel.generator(group, "VM-VENDOR", runtimeMXBean.getVmVendor());
        jvmModel.generator(group, "VM-VERSION", runtimeMXBean.getVmVersion());
        jvmModel.generator(group, "INPUT-ARGUMENTS", runtimeMXBean.getInputArguments());
        jvmModel.generator(group, "CLASS-PATH", runtimeMXBean.getClassPath());
        jvmModel.generator(group, "BOOT-CLASS-PATH", bootClassPath);
        jvmModel.generator(group, "LIBRARY-PATH", runtimeMXBean.getLibraryPath());
    }

    private void addClassLoading(JvmModel jvmModel) {
        ClassLoadingMXBean classLoadingMXBean = configure.getClassLoadingMXBean();
        String group = "CLASS-LOADING";
        jvmModel.generator(group, "LOADED-CLASS-COUNT", classLoadingMXBean.getLoadedClassCount());
        jvmModel.generator(group, "TOTAL-LOADED-CLASS-COUNT", classLoadingMXBean.getTotalLoadedClassCount());
        jvmModel.generator(group, "UNLOADED-CLASS-COUNT", classLoadingMXBean.getUnloadedClassCount());
        jvmModel.generator(group, "IS-VERBOSE", classLoadingMXBean.isVerbose());
    }

    private void addCompilation(JvmModel jvmModel) {
        CompilationMXBean compilationMXBean = configure.getCompilationMXBean();
        if (compilationMXBean == null) {
            return;
        }
        String group = "COMPILATION";
        jvmModel.generator(group, "NAME", compilationMXBean.getName());
        if (compilationMXBean.isCompilationTimeMonitoringSupported()) {
            jvmModel.generator(group, "TOTAL-COMPILE-TIME", compilationMXBean.getTotalCompilationTime(), "time (ms)");
        }
    }

    private void addGarbageCollectors(JvmModel jvmModel) {
        Collection<GarbageCollectorMXBean> garbageCollectorMXBeans = configure.getGarbageCollectorMXBeans();
        if (garbageCollectorMXBeans.isEmpty()) {
            return;
        }
        String group = "GARBAGE-COLLECTORS";
        for (GarbageCollectorMXBean gcMXBean : garbageCollectorMXBeans) {
            Map<String, Object> gcInfo = new LinkedHashMap<String, Object>();
            gcInfo.put("name", gcMXBean.getName());
            gcInfo.put("collectionCount", gcMXBean.getCollectionCount());
            gcInfo.put("collectionTime", gcMXBean.getCollectionTime());

            jvmModel.generator(group, gcMXBean.getName(), gcInfo, "count/time (ms)");
        }
    }

    private void addMemoryManagers(JvmModel jvmModel) {
        Collection<MemoryManagerMXBean> memoryManagerMXBeans = configure.getMemoryManagerMXBeans();
        if (memoryManagerMXBeans.isEmpty()) {
            return;
        }
        String group = "MEMORY-MANAGERS";
        for (final MemoryManagerMXBean memoryManagerMXBean : memoryManagerMXBeans) {
            if (memoryManagerMXBean.isValid()) {
                final String name = memoryManagerMXBean.isValid()
                        ? memoryManagerMXBean.getName()
                        : memoryManagerMXBean.getName() + "(Invalid)";
                jvmModel.generator(group, name, memoryManagerMXBean.getMemoryPoolNames());
            }
        }
    }

    private void addMemory(JvmModel jvmModel) {
        MemoryMXBean memoryMXBean = configure.getMemoryMXBean();
        String group = "MEMORY";
        MemoryUsage heapMemoryUsage = memoryMXBean.getHeapMemoryUsage();
        Map<String, Object> heapMemoryInfo = getMemoryUsageInfo("heap", heapMemoryUsage);
        jvmModel.generator(group, "HEAP-MEMORY-USAGE", heapMemoryInfo, "memory in bytes");

        MemoryUsage nonHeapMemoryUsage = memoryMXBean.getNonHeapMemoryUsage();
        Map<String, Object> nonheapMemoryInfo = getMemoryUsageInfo("nonheap", nonHeapMemoryUsage);
        jvmModel.generator(group,"NO-HEAP-MEMORY-USAGE", nonheapMemoryInfo, "memory in bytes");

        jvmModel.generator(group,"PENDING-FINALIZE-COUNT", memoryMXBean.getObjectPendingFinalizationCount());
    }

    private Map<String, Object> getMemoryUsageInfo(String name, MemoryUsage heapMemoryUsage) {
        Map<String, Object> memoryInfo = new LinkedHashMap<>();
        memoryInfo.put("name", name);
        memoryInfo.put("init", heapMemoryUsage.getInit());
        memoryInfo.put("used", heapMemoryUsage.getUsed());
        memoryInfo.put("committed", heapMemoryUsage.getCommitted());
        memoryInfo.put("max", heapMemoryUsage.getMax());
        return memoryInfo;
    }

    private void addOperatingSystem(JvmModel jvmModel) {
        OperatingSystemMXBean operatingSystemMXBean = configure.getOperatingSystemMXBean();
        String group = "OPERATING-SYSTEM";
        jvmModel.generator(group,"OS", operatingSystemMXBean.getName())
                .generator(group,"ARCH", operatingSystemMXBean.getArch())
                .generator(group,"PROCESSORS-COUNT", operatingSystemMXBean.getAvailableProcessors())
                .generator(group,"LOAD-AVERAGE", operatingSystemMXBean.getSystemLoadAverage())
                .generator(group,"VERSION", operatingSystemMXBean.getVersion());
    }

    private void addThread(JvmModel jvmModel) {
        ThreadMXBean threadMXBean = configure.getThreadMXBean();
        String group = "THREAD";
        jvmModel.generator(group, "COUNT", threadMXBean.getThreadCount())
                .generator(group, "DAEMON-COUNT", threadMXBean.getDaemonThreadCount())
                .generator(group, "PEAK-COUNT", threadMXBean.getPeakThreadCount())
                .generator(group, "STARTED-COUNT", threadMXBean.getTotalStartedThreadCount())
                .generator(group, "DEADLOCK-COUNT",getDeadlockedThreadsCount(threadMXBean));
    }

    private int getDeadlockedThreadsCount(ThreadMXBean threads) {
        final long[] ids = threads.findDeadlockedThreads();
        if (ids == null) {
            return 0;
        } else {
            return ids.length;
        }
    }

}
