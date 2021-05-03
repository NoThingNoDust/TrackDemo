package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AAAService {

    @Autowired
    private BBBService bbbService;
    @Autowired
    private CCCService cccService;

    public void AAA1() throws InterruptedException {
        bbbService.BBB1();
        bbbService.BBB2();
        Thread.sleep(19);
    }

    public void AAA2() throws InterruptedException {
        Thread.sleep(19);
    }

    public void AAA3() throws InterruptedException {
        Thread.sleep(19);
    }
}
