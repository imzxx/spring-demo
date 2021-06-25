package com.example.demo.server.impl;

import com.example.demo.server.IMessageProvider;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * @ClassName IMessageProviderImpl
 * @Description TODO
 * @Author zxx
 * @Date 2021/6/17 23:18
 * @Version 1.0
 **/
@EnableBinding(Source.class)
public class MessageProviderImpl implements IMessageProvider {

    //消息发送管道
    @Resource
    private MessageChannel output;

    @Override
    public String send() {

        String uuid = UUID.randomUUID().toString();
        output.send(MessageBuilder.withPayload(("发送消息：" + uuid)).build());
        System.out.println("消息发送成功："+uuid);
        return null;
    }
}
