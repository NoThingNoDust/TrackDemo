package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HelloService {

    @Autowired
    private TestService testService;


    public void testAAA() throws InterruptedException {
//        testService.testCCC();
//        testService.testBBB();
        Thread.sleep(19);
        System.out.println("=======未替换");
    }
}
