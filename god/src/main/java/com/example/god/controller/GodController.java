package com.example.god.controller;

import com.sun.tools.attach.VirtualMachine;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

@RestController("/god")
public class GodController {

    @PostMapping("/redefine")
    public String redefine() {
        return "OK";
    }

    @GetMapping("/replace")
    public String replace() throws Exception {
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        String pid = runtimeMXBean.getName().split("@")[0];
        VirtualMachine virtualMachine = VirtualMachine.attach(pid);
        virtualMachine.loadAgent("/Users/liule/Documents/project/TrackDemo/agent/target/agent-0.0.2.jar", "/Users/liule/Documents/HelloService.class");
//        virtualMachine.loadAgentPath("agentPath", "newClassPath");
        virtualMachine.detach();
        return "OK";
    }

    @PostMapping("compiler")
    public String compiler() {
        //获取系统Java编译器
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        //获取Java文件管理器
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
        //定义要编译的源文件
        File file = new File("/Users/liule/Documents/HelloController.java");
        //通过源文件获取到要编译的Java类源码迭代器，包括所有内部类，其中每个类都是一个 JavaFileObject，也被称为一个汇编单元
        Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjects(file);
        //生成编译任务
        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, null, null, null, compilationUnits);
        //执行编译任务
        task.call();
        return "OK";
    }


}
