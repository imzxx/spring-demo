package com.example.rabbitmq.workqueues;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @ClassName ProviderDemo
 * @Description 工作队列生产者案例演示
 * @Author zxx
 * @Date 2021/3/22 3:24
 * @Version 1.0
 **/
public class ProviderDemo {

    private Connection getConnection() throws IOException, TimeoutException {
        // 创建RabbitMQ的连接工厂对象
        ConnectionFactory connectionFactory = new ConnectionFactory();
        // 设置要连接的主机
        connectionFactory.setHost("192.168.10.239");
        // 设置端口号
        connectionFactory.setPort(5672);
        // 设置虚拟主机
        connectionFactory.setVirtualHost("/msg");
        // 设置用户名和密码
        connectionFactory.setUsername("root");
        connectionFactory.setPassword("root");
        // 获取连接对象
        Connection connection = connectionFactory.newConnection();
        return connection;
    }

    @Test
    public void testSendMsg() throws IOException, TimeoutException, InterruptedException {
        // 获取连接
        Connection connection = getConnection();
        // 获取连接通道
        Channel channel = connection.createChannel();
        // 通道绑定对应消息队列
        //参数1：消息队列名称，如果队列不存在则自动创建
        //参数2：定义队列特性是否要持久化 true：持久化 false：不持久化
        //参数3：是否独占队列 true：独占队列 false：不独占
        //参数4：是否在消费完成后自动删除队列 true：自动删除 false：不自动删除
        //参数5：额外附加参数
        channel.queueDeclare("work", true, false, false, null);

        //发布消息
        //参数1：交换机名称
        //参数2：路由
        //参数3：传递消息额外设置 MessageProperties.PERSISTENT_TEXT_PLAIN 将消息持久化到磁盘
        //参数4：消息的具体内容
        for (int i = 0; i < 100; i++) {
            TimeUnit.MILLISECONDS.sleep(500);
            channel.basicPublish("","work", MessageProperties.PERSISTENT_TEXT_PLAIN,(i+"rabbitmq work queues").getBytes());
        }

        // 关闭
        channel.close();
        connection.close();
    }
}
