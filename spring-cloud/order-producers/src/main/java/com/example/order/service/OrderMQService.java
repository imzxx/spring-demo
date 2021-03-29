package com.example.order.service;

import com.example.order.domain.OrderMessage;
import com.example.order.mapper.OrderMapper;
import com.example.order.mapper.OrderMessageMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.net.SocketTimeoutException;

/**
 * @ClassName OrderMQService
 * @Description TODO
 * @Author zxx
 * @Date 2021/3/28 22:33
 * @Version 1.0
 **/
@Service
public class OrderMQService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private OrderMessageMapper orderMessageMapper;


    //PostConstruct:java提供的注解，用来修饰一个非静态的void()方法，被PostConstruct修饰的方法会在服务器加载Servlet
    //的时候运行，并且只会被服务器执行一次，PostConstruct在构造函数之后执行，init()方法之前执行
    @PostConstruct
    public void confirmCallback() {
        //消息发送成功后，给予生产者的消息回执，来确保生产者的可靠性
        //RabbitTemplate.ConfirmCallback用来确认消息是否到达服务器。也就是确认消息是否到达exchange中。
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                System.out.println("correlationData:" + correlationData);
                System.out.println("ack:" + ack);
                System.out.println("cause:" + cause);
                //如果ack为true代表消息已经收到
                if (!ack) {
                    String orderId = correlationData.getId();
                    System.out.println("MQ队列应答失败，orderId是" + orderId);
                    OrderMessage orderMessage = new OrderMessage();
                    orderMessage.setStatus("2");
                    orderMessage.setId(orderId);
                    int count = orderMessageMapper.updateOrderStatus(orderMessage);
                    System.out.println("本地状态修改：" + count);
                    return;
                }
            }
        });
    }

    @PostConstruct
    public void returnCallback() {
        //通过实现ReturnCallback接口，启动消息失败后返回。
        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
            @Override
            public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
                System.out.println("message:" + message);
                System.out.println("replyCode:" + replyCode);
                System.out.println("replyText:" + replyText);
                System.out.println("exchange:" + exchange);
                System.out.println("routingKey:" + routingKey);
            }
        });
    }

}
