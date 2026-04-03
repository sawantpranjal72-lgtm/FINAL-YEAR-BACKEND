package com.digitalfir.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/")
    public String home() {
        return "Digital FIR App is Running!";
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello from Digital FIR Backend!";
    }

    @GetMapping("/test")
    public String test(){
        return "Application Running Successfully!";
    }
}
