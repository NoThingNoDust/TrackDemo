package com.example.track.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
        testService.testBBB();
        testService.testCCC();
        helloService.testAAA();
        try {
            Thread.sleep(1095);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return InetAddress.getLocalHost().getHostName();
    }
}
