package com.example.rabbitmq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName DeadRabbitMQConfig
 * @Description TODO
 * @Author zxx
 * @Date 2021/3/28 14:41
 * @Version 1.0
 **/
@Configuration
public class DeadRabbitMQConfig {

    //声明交换机
    @Bean
    public DirectExchange deadExchange(){
        return new DirectExchange("dead_order_exchange",true,false);
    }

    //声明队列
    @Bean
    public Queue deadQueue(){
        return new Queue("dead.queue", true,false,false);
    }

    //关系绑定
    @Bean
    public Binding deadBinding(){
        return BindingBuilder.bind(deadQueue()).to(deadExchange()).with("dead");
    }
}
