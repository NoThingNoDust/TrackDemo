package com.example.demo.service;

import org.springframework.stereotype.Service;

@Service
public class CCCServiceImpl implements CCCService{
    @Override
    public void CCC1() {
        try {
            Thread.sleep(111L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
