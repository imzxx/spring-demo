package com.example.rabbitmq.service;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

/**
 * @ClassName FanoutEmailService
 * @Description TODO
 * @Author zxx
 * @Date 2021/3/24 2:55
 * @Version 1.0
 **/

@RabbitListener(queues = {"email.fanout.queue"})
@Service
public class FanoutEmailService {

    @RabbitHandler
    public void reviceMessage(String message) {
        System.out.println("email 监听到消息-------》"+message);
    }

}
