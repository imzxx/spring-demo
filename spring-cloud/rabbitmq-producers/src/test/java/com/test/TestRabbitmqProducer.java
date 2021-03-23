package com.test;

import com.example.rabbitmq.RabbitmqProducerApplication;
import com.example.rabbitmq.service.OrderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @ClassName TestRabbitmqProducer
 * @Description TODO
 * @Author zxx
 * @Date 2021/3/23 22:51
 * @Version 1.0
 **/
@SpringBootTest(classes = RabbitmqProducerApplication.class)
@RunWith(SpringRunner.class)
public class TestRabbitmqProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private OrderService orderService;

    @Test
    public void test01(){
        rabbitTemplate.convertAndSend("hello","spring boot rabbitmq");
    }

    @Test
    public void orderService(){
        orderService.makeOrder("1","1",1);
    }

    @Test
    public void orderDirectService(){
        orderService.makeDirectOrder("1","1",1);
    }

    @Test
    public void orderTopicService(){
        orderService.makeTopicOrder("1", "1", 1);
    }
}
