package com.example.god.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.lang.management.*;
import java.util.Collection;

@Component
@Data
public class Configure {

    @Value("${god.agent.path:/Users/liule/Documents/agent/agent-0.0.2.jar}")
    private String agentPath;

    private RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
    private ClassLoadingMXBean classLoadingMXBean = ManagementFactory.getClassLoadingMXBean();
    private CompilationMXBean compilationMXBean = ManagementFactory.getCompilationMXBean();
    private Collection<GarbageCollectorMXBean> garbageCollectorMXBeans = ManagementFactory.getGarbageCollectorMXBeans();
    private Collection<MemoryManagerMXBean> memoryManagerMXBeans = ManagementFactory.getMemoryManagerMXBeans();
    private MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
    //    private Collection<MemoryPoolMXBean> memoryPoolMXBeans = ManagementFactory.getMemoryPoolMXBeans();
    private OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
    private ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();

}
