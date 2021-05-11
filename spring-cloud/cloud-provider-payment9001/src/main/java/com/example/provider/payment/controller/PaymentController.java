package com.example.provider.payment.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName PaymentController
 * @Description TODO
 * @Author zxx
 * @Date 2021/4/20 23:26
 * @Version 1.0
 **/
@RestController
@RefreshScope
public class PaymentController {

    @Value("${server.port}")
    private String serverPort;

    @Value("${config.info}")
    private String configInfo;

    @GetMapping("/getServerPort")
    public String getServerPort(){
        System.out.println(configInfo);
        return serverPort+"==="+configInfo;
    }
}
