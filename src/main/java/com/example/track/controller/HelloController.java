package com.example.track.controller;

import com.example.track.service.HelloService;
import com.example.track.service.TestService;
import com.sun.tools.attach.VirtualMachine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.InetAddress;
import java.net.UnknownHostException;

@RestController
public class HelloController {

    @Autowired
    private HelloService helloService;

    @Autowired
    private TestService testService;


    @GetMapping("/hello")
    public String hello() throws UnknownHostException, InterruptedException {
//        testService.testBBB();
//        testService.testCCC();
        helloService.testAAA();
//        try {
//            Thread.sleep(1095);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        return InetAddress.getLocalHost().getHostName();
    }


    @GetMapping("/replace")
    public String replace() throws Exception {
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        String pid = runtimeMXBean.getName().split("@")[0];
        VirtualMachine virtualMachine = VirtualMachine.attach(pid);
        virtualMachine.loadAgent("/Users/liule/Documents/project/TrackDemo/agent/target/agent-0.0.1-SNAPSHOT.jar", "HelloService.class");
        virtualMachine.detach();
        return "OK";
    }
}
