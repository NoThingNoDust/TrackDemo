package com.example.demo.controller;

import com.example.demo.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;

@RestController
public class HelloController {

   @Autowired
   private HelloService helloService;

    @GetMapping("/hello")
    public String hello() throws UnknownHostException, InterruptedException {
        helloService.testAAA();
        return InetAddress.getLocalHost().getHostName();
    }

}
