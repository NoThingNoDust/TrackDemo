package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HelloService {

    @Autowired
    private TestService testService;
    @Autowired
    private HelloService helloService;


    public void testAAA() throws InterruptedException {
        Thread.sleep(19);
        System.out.println("a");
        this.testBBB();
        helloService.testCCC();
    }

    public void testBBB() throws InterruptedException {
        Thread.sleep(19);
        System.out.println("b");
    }

    public void testCCC() throws InterruptedException {
        Thread.sleep(19);
        System.out.println("c");
    }
}
