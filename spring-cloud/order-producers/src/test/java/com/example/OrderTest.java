package com.example;

import com.alibaba.druid.support.json.JSONUtils;
import com.example.order.OrderApplication;
import com.example.order.domain.Distribution;
import com.example.order.domain.Order;
import com.example.order.domain.OrderMessage;
import com.example.order.mapper.OrderMapper;
import com.example.order.mapper.OrderMessageMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.tools.json.JSONUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * @ClassName OrderTest
 * @Description TODO
 * @Author zxx
 * @Date 2021/3/28 16:28
 * @Version 1.0
 **/
@SpringBootTest(classes = OrderApplication.class)
@RunWith(SpringRunner.class)
public class OrderTest {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderMessageMapper orderMessageMapper;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;


    @Test
    public void Test01() {
        /*List<Order> orders=orderMapper.findAll();
        System.out.println(orders);*/
        Order order = new Order();
        order.setId("1003");
        order.setContent("插入测试");
        order.setRegDate("2021-03-28");
        orderMapper.insertOrder(order);
    }

    @Test
    public void Test02() {
        Order order = new Order();
        order.setId("2223");
        order.setContent("新增一条订单");
        order.setRegDate("2021-03-28");
        ResponseEntity<Distribution> responseEntity = restTemplate.postForEntity("http://127.0.0.1:1001/save/order", order, Distribution.class);
        orderMapper.insertOrder(order);

        OrderMessage orderMessage = new OrderMessage();
        orderMessage.setId("3333");
        orderMessage.setContent("冗余表新增");
        orderMessage.setStatus("0");
        orderMessage.setRegDate("2021-03-28");
        orderMessageMapper.insertOrder(orderMessage);

    }

    @Test
    public void test03(){
        OrderMessage orderMessage = new OrderMessage();
        orderMessage.setId("3333");
        orderMessage.setStatus("2");
        orderMessageMapper.updateOrderStatus(orderMessage);
    }

    @Test
    public void test04() throws JsonProcessingException, InterruptedException {
        Order order = new Order();
        order.setId("2223");
        order.setContent("新增一条订单");
        order.setRegDate("2021-03-28");
        ObjectMapper objectMapper = new ObjectMapper();


        rabbitTemplate.convertAndSend("order_message","order",objectMapper.writeValueAsString(order));


        orderMapper.insertOrder(order);

        OrderMessage orderMessage = new OrderMessage();
        orderMessage.setId("3333");
        orderMessage.setContent("冗余表新增");
        orderMessage.setStatus("0");
        orderMessage.setRegDate("2021-03-28");
        orderMessageMapper.insertOrder(orderMessage);

        Thread.sleep(2000L);
    }
}
