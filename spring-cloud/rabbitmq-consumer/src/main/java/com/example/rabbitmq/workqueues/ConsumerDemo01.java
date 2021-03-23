package com.example.rabbitmq.workqueues;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @ClassName ConsumerDemo01
 * @Description 工作队列消费者案例演示
 * @Author zxx
 * @Date 2021/3/22 3:38
 * @Version 1.0
 **/
public class ConsumerDemo01 {
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {

        TimeUnit.SECONDS.sleep(1);
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
        // 让通道每次只能消费一个消息，未被消费的消息仍在消息队列中，防止消费者突然宕机后消息丢失。
        channel.basicQos(1);
        // 消费消息
        //参数1：将要消费队列的名称
        //参数2：开始消息的自动确认机制，true 消费者自动向RabbitMQ确认消息消费了
        //参数3：消费时的回调接口
        channel.basicConsume("work", false, new DefaultConsumer(channel) {
            //参数4：获取消费队列中的消息
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("消费者1===成功消费消息："+new String(body));
                // 关闭自动确认消息以后，需要手动确认消息是否被消费
                //参数1：确认消费队列中的哪一个具体的消息
                //参数2：是否开启多个消息同时消费
                channel.basicAck(envelope.getDeliveryTag(),false);
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
