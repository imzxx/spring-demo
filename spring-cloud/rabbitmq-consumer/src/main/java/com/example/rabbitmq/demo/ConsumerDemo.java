package com.example.rabbitmq.demo;

import com.rabbitmq.client.*;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @ClassName ConsumerDemo
 * @Description RabbitMQ消费者案例演示
 * @Author zxx
 * @Date 2021/3/22 0:14
 * @Version 1.0
 **/
public class ConsumerDemo {

    public static void main(String[] args) throws IOException, TimeoutException {
        // 获取连接
        Connection connection = getConnection();
        // 获取连接通道
        Channel channel = connection.createChannel();
        // 消费消息
        //参数1：将要消费队列的名称
        //参数2：开始消息的自动确认机制
        //参数3：消费时的回调接口
        channel.basicConsume("hello", true, new DefaultConsumer(channel) {
            //参数4：获取消费队列中的消息
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("成功消费消息："+new String(body));
            }
        });
        //关闭通道以后程序会结束
        //channel.close();
        //connection.close();
    }

    private static Connection getConnection() throws IOException, TimeoutException {
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

}
