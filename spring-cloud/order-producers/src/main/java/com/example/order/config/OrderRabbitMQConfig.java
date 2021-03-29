package com.example.order.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName OrderRabbitMQConfig
 * @Description TODO
 * @Author zxx
 * @Date 2021/3/28 23:03
 * @Version 1.0
 **/
@Configuration
public class OrderRabbitMQConfig {

    @Bean
    public DirectExchange orderExchange(){
        return new DirectExchange("order_message",true,false);
    }

    @Bean
    public DirectExchange deadOrderExchange(){
        return new DirectExchange("dead_order_message", true, false);
    }

    @Bean
    public Queue deadQueue(){
        return new Queue("dead_order_queue", true,false,false);
    }

    @Bean
    public Binding deadBind(){
        return BindingBuilder.bind(deadQueue()).to(deadOrderExchange()).with("dead.order");
    }

    @Bean
    public Queue orderQueue(){
        Map<String, Object> map = new HashMap<>();
        map.put("x-dead-letter-exchange","dead_order_message");
        map.put("x-dead-letter-routing-key", "dead.order");
        return new Queue("order_queue", true,false,false,map);
    }

    @Bean
    public Binding orderBind(){
        return BindingBuilder.bind(orderQueue()).to(orderExchange()).with("order");
    }

}
