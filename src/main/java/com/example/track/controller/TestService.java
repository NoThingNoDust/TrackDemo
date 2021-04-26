package com.example.track.controller;

import org.springframework.stereotype.Service;

@Service
public class TestService {


    public void testBBB() throws InterruptedException {
        Thread.sleep(20);
    }

    public void testCCC() throws InterruptedException {
        Thread.sleep(10);
    }
}
