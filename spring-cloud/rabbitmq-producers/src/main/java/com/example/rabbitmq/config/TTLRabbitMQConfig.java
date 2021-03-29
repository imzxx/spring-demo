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
 * @ClassName TTLRabbitMQConfig
 * @Description TODO
 * @Author zxx
 * @Date 2021/3/27 10:44
 * @Version 1.0
 **/
@Configuration
public class TTLRabbitMQConfig {

    //声明交换机
    @Bean
    public DirectExchange ttlDirectExchange(){
        return new DirectExchange("ttl_direct_order_exchange",true,false);
    }

    //声明队列
    @Bean
    public Queue ttlEmailQueue(){
        //设置过期时间
        Map<String, Object> map = new HashMap<>();
        map.put("x-message-ttl", 5000);
        //设置死信队列的交换机
        map.put("x-dead-letter-exchange", "dead_order_exchange");
        //如果有路由key，设置死信队列的路由key
        map.put("x-dead-letter-routing-key", "dead");
        return new Queue("ttl.email.direct.queue", true,false,false,map);
    }

    //关系绑定
    @Bean
    public Binding ttlBinding(){
        return BindingBuilder.bind(ttlEmailQueue()).to(ttlDirectExchange()).with("email");
    }

}
