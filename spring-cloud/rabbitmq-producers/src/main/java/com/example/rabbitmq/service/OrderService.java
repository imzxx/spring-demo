package com.example.rabbitmq.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.java2d.pipe.SpanIterator;

import java.util.UUID;

/**
 * @ClassName OrderService
 * @Description TODO
 * @Author zxx
 * @Date 2021/3/24 2:08
 * @Version 1.0
 **/
@Service
public class OrderService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 模拟订单发送
     * @param userId
     * @param productId
     * @param num
     */
    public void makeOrder(String userId, String productId, Integer num) {

        //模拟生成订单号
        String orderId = UUID.randomUUID().toString();
        System.out.println("orderId:" + orderId);
        //模拟使用MQ来发送消息
        //参数1：exchange 交换机
        //参数2：路由key/队列queue
        //参数3：消息内容
        String exchange = "fanout_order_exchange";
        String routingKey = "";
        rabbitTemplate.convertAndSend(exchange, routingKey, orderId);
        System.out.println("发送完成");
    }

    public void makeDirectOrder(String userId, String productId, Integer num) {

        //模拟生成订单号
        String orderId = UUID.randomUUID().toString();
        System.out.println("orderId:" + orderId);
        //模拟使用MQ来发送消息
        //参数1：exchange 交换机
        //参数2：路由key/队列queue
        //参数3：消息内容
        String exchange = "direct_order_exchange";
        rabbitTemplate.convertAndSend(exchange, "email", orderId);
        rabbitTemplate.convertAndSend(exchange, "sms", orderId);
        System.out.println("发送完成");
    }

    public void makeTopicOrder(String userId, String productId, Integer num) {

        //模拟生成订单号
        String orderId = UUID.randomUUID().toString();
        System.out.println("orderId:" + orderId);
        //模拟使用MQ来发送消息
        //参数1：exchange 交换机
        //参数2：路由key/队列queue
        //参数3：消息内容
        String exchange = "topic_order_exchange";
        String routingKey = "com.email.order";
        rabbitTemplate.convertAndSend(exchange, routingKey, orderId);
        System.out.println("发送完成");
    }

}
