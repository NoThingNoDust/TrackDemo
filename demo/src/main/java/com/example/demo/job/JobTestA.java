package com.example.demo.job;

import com.example.demo.service.AAAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class JobTestA {

    @Autowired
    private AAAService aaaService;

    @Scheduled(cron = "*/30 * * * * ?")
    public void AJobTest1() {
        try {
            aaaService.AAA1();
            aaaService.AAA2();
            aaaService.AAA3();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Scheduled(cron = "*/30 * * * * ?")
    public void AJobTest2() {
        try {
            aaaService.AAA1();
            aaaService.AAA2();
            aaaService.AAA3();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
