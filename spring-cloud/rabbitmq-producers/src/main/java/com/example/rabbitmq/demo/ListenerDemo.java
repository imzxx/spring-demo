package com.example.rabbitmq.demo;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @ClassName ListenerDemo
 * @Description TODO
 * @Author zxx
 * @Date 2021/3/23 23:18
 * @Version 1.0
 **/
@Component
public class ListenerDemo {

    @RabbitListener(queues={"hello"})
    @RabbitHandler
    public void test01(String message){
        System.out.println("spring boot 监听 hello队列："+message);
    }
}
