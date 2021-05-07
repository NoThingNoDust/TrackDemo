package com.example.demo.job;

import com.example.demo.service.AAAService;
import com.example.demo.service.CCCService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class JobTestB {

    @Autowired
    private AAAService aaaService;
    @Autowired
    private CCCService cccService;

    @Scheduled(cron = "*/10 * * * * ?")
    public void BJobTest1() {
        try {
            aaaService.AAA1();
            aaaService.AAA2();
            cccService.CCC1();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Scheduled(cron = "*/10 * * * * ?")
    public void BJobTest2() {
        try {
            aaaService.AAA1();
            cccService.CCC1();
            aaaService.AAA3();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
