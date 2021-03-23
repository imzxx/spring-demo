package com.example.rabbitmq.fanout;

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
 * @Description 广播模型案例
 * @Author zxx
 * @Date 2021/3/23 20:02
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

        // 将通道绑定到交换机中
        //参数1：交换机名称
        //参数2：交换机类型，fanout:广播模型
        channel.exchangeDeclare("fanout","fanout");

        //发布消息
        //参数1：交换机名称
        //参数2：路由
        //参数3：传递消息额外设置 MessageProperties.PERSISTENT_TEXT_PLAIN 将消息持久化到磁盘
        //参数4：消息的具体内容
        for (int i = 0; i < 50; i++) {
            TimeUnit.MILLISECONDS.sleep(500);
            channel.basicPublish("fanout","", MessageProperties.PERSISTENT_TEXT_PLAIN,(i+"rabbitmq fanout hello").getBytes());
        }

        // 关闭
        channel.close();
        connection.close();
    }

}
