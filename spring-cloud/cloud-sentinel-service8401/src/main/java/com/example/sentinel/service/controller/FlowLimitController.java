package com.example.sentinel.service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName FlowLimitController
 * @Description TODO
 * @Author zxx
 * @Date 2021/4/22 23:02
 * @Version 1.0
 **/
@RestController
public class FlowLimitController {

    @GetMapping("/testA")
    public String testA(){
        return "---->A";
    }

    @GetMapping("/testB")
    public String testB(){
        return "---->B";
    }
}
