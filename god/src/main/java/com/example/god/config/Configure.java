package com.example.god.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import sun.management.HotspotThreadMBean;
import sun.management.ManagementFactoryHelper;

import java.lang.management.*;
import java.util.Collection;

@Component
@Data
public class Configure {

    @Value("${god.agent.path:/Users/liule/Documents/agent/}")
    private String agentPath;

    @Value("${god.cache.path:/Users/liule/Documents}")
    private String cachePath;

    private RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
    private ClassLoadingMXBean classLoadingMXBean = ManagementFactory.getClassLoadingMXBean();
    private CompilationMXBean compilationMXBean = ManagementFactory.getCompilationMXBean();
    private Collection<GarbageCollectorMXBean> garbageCollectorMXBeans = ManagementFactory.getGarbageCollectorMXBeans();
    private Collection<MemoryManagerMXBean> memoryManagerMXBeans = ManagementFactory.getMemoryManagerMXBeans();
    private MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
    //    private Collection<MemoryPoolMXBean> memoryPoolMXBeans = ManagementFactory.getMemoryPoolMXBeans();
    private OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
    private ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();


    private HotspotThreadMBean hotspotThreadMBean;

    @Bean
    public void initHotspotThreadMBean() {
        hotspotThreadMBean = ManagementFactoryHelper.getHotspotThreadMBean();
    }

}
