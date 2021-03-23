package com.example.rabbitmq.fanout;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @ClassName ConsumerDemo01
 * @Description TODO
 * @Author zxx
 * @Date 2021/3/23 20:16
 * @Version 1.0
 **/
public class ConsumerDemo02 {
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {

        TimeUnit.SECONDS.sleep(1);
        // 获取连接
        Connection connection = getConnection();
        // 获取连接通道
        Channel channel = connection.createChannel();
        // 通道绑定交换机
        channel.exchangeDeclare("fanout", "fanout");
        // 因为广播中会生成临时队列，所有先要获取一个临时队列的名称
        String queueName = channel.queueDeclare().getQueue();
        // 将交换机于队列绑定
        //参数1：队列名称
        //参数2：交换机名称
        //参数3：路由
        channel.queueBind(queueName, "fanout", "");

        // 让通道每次只能消费一个消息，未被消费的消息仍在消息队列中，防止消费者突然宕机后消息丢失。
        channel.basicQos(1);
        // 消费消息
        //参数1：将要消费队列的名称
        //参数2：开始消息的自动确认机制，true 消费者自动向RabbitMQ确认消息消费了
        //参数3：消费时的回调接口
        channel.basicConsume(queueName, false, new DefaultConsumer(channel) {
            //参数4：获取消费队列中的消息
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("消费者2===成功消费消息："+new String(body));
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
