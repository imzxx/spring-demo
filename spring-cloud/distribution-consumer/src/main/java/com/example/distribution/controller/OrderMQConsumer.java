package com.example.distribution.controller;

import com.example.distribution.domain.Distribution;
import com.example.distribution.domain.Order;
import com.example.distribution.mapper.DistributionMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Correlation;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @ClassName OrderMQConsumer
 * @Description TODO
 * @Author zxx
 * @Date 2021/3/28 23:23
 * @Version 1.0
 **/
@RabbitListener(queues = {"order_queue"})
@Service
public class OrderMQConsumer {

    @Autowired
    private DistributionMapper distributionMapper;

    private int count;

    //解决消息重试的几种方案
    //1.控制重发的次数
    //2.try+catch+手动ack
    //3.try+catch+手动ack+死信队列处理
    @RabbitHandler
    public void consumerMessage(String orderMsg, Channel channel, CorrelationData correlationData, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        try {
            //获取队列中的消息
            System.out.println("order队列收到的消息是" + orderMsg + ",count:" + count++);
            ObjectMapper objectMapper = new ObjectMapper();
            Order order = objectMapper.readValue(orderMsg, Order.class);
            Distribution distribution = new Distribution();
            distribution.setDistriId("3333");
            distribution.setOrderId(order.getId());
            distribution.setOrderContent(order.getContent());
            distribution.setRegDate(order.getRegDate());
            //int a=1/0;
            distributionMapper.insert(distribution);
            channel.basicAck(tag,false);
        } catch (Exception e) {
            System.out.println("订单保存失败");
            //如果出现异常，根据实际情况去重发
            //参数1：消息的tag，参数2：多条处理，参数3：是否重发
            //false:不重发，会将消息放到死信队列，true:会一直重复发送，建议如果使用true的话，不要加try、catch否则就会造成死循环
            channel.basicNack(tag, false, false);
        }
    }
}
