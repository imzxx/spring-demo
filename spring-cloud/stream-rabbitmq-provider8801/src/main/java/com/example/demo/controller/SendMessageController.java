package com.example.demo.controller;

import com.example.demo.server.IMessageProvider;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @ClassName SendMessageController
 * @Description TODO
 * @Author zxx
 * @Date 2021/6/17 23:27
 * @Version 1.0
 **/
@RestController
public class SendMessageController {

    @Resource
    private IMessageProvider iMessageProvider;

    @RequestMapping("send ")
    public String send() {
        return iMessageProvider.send();
    }
}
