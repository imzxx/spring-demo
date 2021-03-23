package com.example.rabbitmq.service;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Service;

/**
 * @ClassName TopicEmailService
 * @Description TODO
 * @Author zxx
 * @Date 2021/3/24 5:27
 * @Version 1.0
 **/
@RabbitListener(bindings = @QueueBinding(
        value = @Queue(value = "sms.top.queue",durable = "true",autoDelete = "false"),
        exchange = @Exchange(value = "topic_order_exchange",type = ExchangeTypes.TOPIC),
        key = "#.email.#"

))
@Service
public class TopicEmailService {

    @RabbitHandler
    public void reviceMessage(String message) {
        System.out.println("email 监听到消息-------》"+message);
    }
}
