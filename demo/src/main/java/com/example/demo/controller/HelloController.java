package com.example.demo.controller;

import com.example.demo.service.HelloService;
import com.example.god.model.jvm.JvmUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;

@RestController
public class HelloController {

   @Autowired
   private HelloService helloService;

   @Resource
   private JvmUtil jvmUtil;

    @GetMapping("/hello")
    public String hello() throws UnknownHostException, InterruptedException {
        helloService.testAAA();
        return InetAddress.getLocalHost().getHostName();
    }


    @GetMapping("compiler")
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

    @GetMapping("/jvm")
    public Object jvm() {
        return jvmUtil.getJvmDetails();

    }

}
