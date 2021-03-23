package com.example.rabbitmq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 *@ClassName RabbitmqProducerApplication
 *@Description RabbitMQ生产者启动类
 *@Author zxx
 *@Date 2021/3/21 22:53
 *@Version 1.0
 **/
@SpringBootApplication
public class RabbitmqProducerApplication {

    public static void main(String[] args) {
        SpringApplication.run(RabbitmqProducerApplication.class, args);
    }
}
