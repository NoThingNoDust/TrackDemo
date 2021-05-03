package com.example.demo.controller;

import com.example.demo.service.AAAService;
import com.example.demo.service.BBBService;
import com.example.demo.service.CCCService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;

@RestController
@RequestMapping("/demo")
public class HelloController {

   @Autowired
   private AAAService aaaService;


    @Autowired
    private BBBService bbbService;

    @Autowired
    private CCCService cccService;



    @GetMapping("/hello")
    public String hello() throws UnknownHostException, InterruptedException {
        aaaService.AAA1();
        aaaService.AAA2();
        aaaService.AAA3();
        bbbService.BBB1();
        bbbService.BBB2();
        cccService.CCC1();
        return InetAddress.getLocalHost().getHostName();
    }


    @GetMapping("/hi")
    public String hi() throws UnknownHostException, InterruptedException {
        cccService.CCC1();
        bbbService.BBB2();
        bbbService.BBB1();
        aaaService.AAA3();
        aaaService.AAA2();
        aaaService.AAA1();
        return InetAddress.getLocalHost().getHostName();
    }


}
