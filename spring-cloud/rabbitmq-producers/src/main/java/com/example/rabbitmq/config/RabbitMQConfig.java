package com.example.rabbitmq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName RabbitMQConfig
 * @Description TODO
 * @Author zxx
 * @Date 2021/3/24 2:18
 * @Version 1.0
 **/
//@Configuration
public class RabbitMQConfig {

    //声明交换机
    @Bean
    public FanoutExchange fanoutExchange(){
        return new FanoutExchange("fanout_order_exchange",true,false);
    }

    //声明队列
    @Bean
    public Queue emailQueue(){
        return new Queue("email.fanout.queue", true);
    }

    @Bean
    public Queue smsQueue(){
        return new Queue("sms.fanout.queue", true);
    }

    @Bean
    public Queue wxQueue(){
        return new Queue("wx.fanout.queue", true);
    }

    //关系绑定
    @Bean
    public Binding emailBinding(){
        return BindingBuilder.bind(emailQueue()).to(fanoutExchange());
    }

    @Bean
    public Binding smsBinding(){
        return BindingBuilder.bind(smsQueue()).to(fanoutExchange());
    }

    @Bean
    public Binding wxBinding(){
        return BindingBuilder.bind(wxQueue()).to(fanoutExchange());
    }

}
