package com.example.god.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.god.config.Configure;
import com.example.god.model.clazz.*;
import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;
import net.bytebuddy.agent.ByteBuddyAgent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.lang.instrument.Instrumentation;
import java.lang.management.ManagementFactory;
import java.util.*;

/**
 * class以及classLoader信息查询修改入口
 * @author liule
 */
@RestController
@RequestMapping("/class")
public class ClassController {

    @Resource
    private Configure configure;

    @GetMapping("/search")
    public String getClazz(@RequestParam(value = "hashCode", required = false) String hashCode,
                         @RequestParam(value = "classLoaderClass", required = false) String classLoaderClass,
                         @RequestParam(value = "className") String className,
                         @RequestParam(value = "isDetail", defaultValue = "true") boolean isDetail,
                         @RequestParam(value = "isField", defaultValue = "true") boolean isField) {
        Instrumentation inst = ByteBuddyAgent.install();

        if (hashCode == null && classLoaderClass != null) {
            List<ClassLoader> matchedClassLoaders = ClassLoaderUtils.getClassLoaderByClassName(inst, classLoaderClass);
            if (matchedClassLoaders.size() == 1) {
                hashCode = Integer.toHexString(matchedClassLoaders.get(0).hashCode());
            } else if (matchedClassLoaders.size() > 1) {
                return "Found more than one classloader by class name";
            } else {
                return null;
            }
        }

        List<Class<?>> matchedClasses = new ArrayList<>(SearchUtils.searchClass(inst, className, hashCode));
        matchedClasses.sort(Comparator.comparing(ClassUtils::classname));

        SearchClassModel searchClassModel = null;

        if (isDetail) {
            for (Class<?> clazz : matchedClasses) {
                ClassDetailVO classInfo = ClassUtils.createClassInfo(clazz, isField);
                searchClassModel = new SearchClassModel(classInfo, isDetail, isField);
            }
        } else {
            int pageSize = 20;
            List<String> nameList = new ArrayList<>();
            if (matchedClasses.size() < pageSize) {
                pageSize = matchedClasses.size();
            }
            for (int i = 0; i < pageSize; i++) {
                String name = matchedClasses.get(i).getName();
                nameList.add(name);
            }
            searchClassModel = new SearchClassModel(nameList);
        }

        return JSONObject.toJSONString(searchClassModel);
    }

    public void test() throws Exception{

        String name = ManagementFactory.getRuntimeMXBean().getName();
        String pid = name.split("@")[0];

        VirtualMachineDescriptor virtualMachineDescriptor = null;
        for (VirtualMachineDescriptor descriptor : VirtualMachine.list()) {
            String descId = descriptor.id();
            if (descId.equals(pid)) {
                virtualMachineDescriptor = descriptor;
                break;
            }
        }
        VirtualMachine virtualMachine = null;
        try {
            if (null == virtualMachineDescriptor) {
                virtualMachine = VirtualMachine.attach(pid);
            } else {
                virtualMachine = VirtualMachine.attach(virtualMachineDescriptor);
            }

            Properties targetSystemProperties = virtualMachine.getSystemProperties();

            String agentPath = configure.getAgentPath();
            virtualMachine.loadAgent(agentPath,
                    null);

        } finally {
            if (null != virtualMachine) {
                virtualMachine.detach();
            }
        }
    }

}
