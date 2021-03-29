# RabbitMQ笔记

## 简介

> RabbitMQ：基于AMQP协议，erlang语言开发，是部署最广泛的开源消息中间件，是最受欢迎的开源消息中间件之一。

> rabbitmq采用生产者和消费者模型的方式，生产者和消费者连接虚拟主机（Virtual Host），虚拟主机相当于数据库中的库，可以每个是每个微服务对应一个虚拟主机，便于隔离。虚拟主机内部是交换机（exchange）和队列（quene）。

## RabbiMq的6中模型

### 直连模型

> 是一种最简单的，一对一的成产者消费者模型。

#### 生产者案例

```java
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
    public void testSendMsg() throws IOException, TimeoutException {
        // 获取连接
        Connection connection = getConnection();
        // 获取连接通道
        Channel channel = connection.createChannel();
        // 通道绑定对应消息队列进行消息发送
        //参数1：消息队列名称，如果队列不存在则自动创建
        //参数2：定义队列特性是否要持久化 true：持久化 false：不持久化
        //参数3：是否独占队列 true：独占队列 false：不独占
        //参数4：是否在消费完成后自动删除队列 true：自动删除 false：不自动删除
        //参数5：额外附加参数
        channel.queueDeclare("hello", false, false, false, null);

        //发布消息
        //参数1：交换机名称
        //参数2：路由
        //参数3：传递消息额外设置 MessageProperties.PERSISTENT_TEXT_PLAIN 将消息持久化到磁盘
        //参数4：消息的具体内容
        channel.basicPublish("","hello", MessageProperties.PERSISTENT_TEXT_PLAIN,"hello rabbitmq".getBytes());

        // 关闭
        channel.close();
        connection.close();
    }
}
```

#### 消费者案例

```java
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
```

### Work queues工作队列模型

> 让多个消费者绑定到一个队列中，共同消费队列中的消息。队列中的消息一旦被消费就会消失，不会被造成重复消费的现象。

#### 生产者案例

```java
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
```

#### 消费者案例

```java
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
```

### fanout广播模型

> 广播模型：可以有多个消费者，每个消费者都有自己的队列，每个队列都绑定到一个交换机上。生产者只需要将消息发送到交换机中，由交换机觉得来觉得要发给哪个队列，生产者自己无法决定。同时队列中的消费都会拿到消息，实现一条消息被多个消费者消费。

#### 生产者案例

```java
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
```

#### 消费者案例

```java
public class ConsumerDemo01 {
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
```

### Routing订阅模型

> Routing模型：在fanout模型的基础上，交换机不再把消息交给每一个绑定的队列，而是根据消息的RoutingKey进行判断，只有队列中的RoutingKey与消息的RoutingKey一致的时候才会接收消息。

#### 生产者案例

```java
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
        //参数2：交换机类型，direct:路由模式
        channel.exchangeDeclare("direct", "direct");

        //发布消息
        //定义路由
        String routingKey = "consumer_2";
        //参数1：交换机名称
        //参数2：路由
        //参数3：传递消息额外设置 MessageProperties.PERSISTENT_TEXT_PLAIN 将消息持久化到磁盘
        //参数4：消息的具体内容
        for (int i = 0; i < 5; i++) {
            TimeUnit.MILLISECONDS.sleep(500);
            channel.basicPublish("direct", routingKey, MessageProperties.PERSISTENT_TEXT_PLAIN, (i + "rabbitmq direct hello").getBytes());
        }
        // 关闭
        channel.close();
        connection.close();
    }
}
```

#### 消费者案例

```java
public class Consumer01 {
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        // 获取连接
        Connection connection = getConnection();
        // 获取连接通道
        Channel channel = connection.createChannel();
        // 通道绑定交换机
        channel.exchangeDeclare("direct", "direct");
        // 因为广播中会生成临时队列，所有先要获取一个临时队列的名称
        String queueName = channel.queueDeclare().getQueue();
        // 将交换机于队列绑定
        //参数1：队列名称
        //参数2：交换机名称
        //参数3：路由
        channel.queueBind(queueName, "direct", "consumer_1");
        // 在路由模式下可以绑定多个
        channel.queueBind(queueName, "direct", "consumer_2");

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
```

### Topics模型

> Topics模型相比于路由模型的区别是Topics模型可以使用通配符来进行路由，使路由更加灵活。
>
> ‘*’ 表示匹配一个词。
>
> ‘#’表示匹配零个或多个词。

#### 生产者案例

```java
public class ProviderDome {
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
        //参数2：交换机类型，direct:路由模式
        channel.exchangeDeclare("topic", "topic");

        //发布消息
        //定义路由
        String routingKey = "consumer.save";
        //参数1：交换机名称
        //参数2：路由
        //参数3：传递消息额外设置 MessageProperties.PERSISTENT_TEXT_PLAIN 将消息持久化到磁盘
        //参数4：消息的具体内容
        for (int i = 0; i < 5; i++) {
            TimeUnit.MILLISECONDS.sleep(500);
            channel.basicPublish("topic", routingKey, MessageProperties.PERSISTENT_TEXT_PLAIN, (i + "rabbitmq topic hello").getBytes());
        }
        // 关闭
        channel.close();
        connection.close();
    }
}
```

#### 消费者案例

```java
public class ConsumerDemo {
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        // 获取连接
        Connection connection = getConnection();
        // 获取连接通道
        Channel channel = connection.createChannel();
        // 通道绑定交换机
        channel.exchangeDeclare("topic", "topic");
        // 因为广播中会生成临时队列，所有先要获取一个临时队列的名称
        String queueName = channel.queueDeclare().getQueue();
        // 将交换机于队列绑定
        //参数1：队列名称
        //参数2：交换机名称
        //参数3：路由
        channel.queueBind(queueName, "topic", "*.save");

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
```

###RabbitMQ整合SpringBoot

### 引入依赖

```xml
<!--RabbitMQ依赖-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-amqp</artifactId>
</dependency>
```

### 添加配置

```yml
rabbitmq:
    host: 192.168.10.239
    port: 5672
    virtual-host: /msg
    username: root
    password: root
```

### 编写配置类

```java 
/**
 * @ClassName RabbitMQConfig
 * @Description TODO
 * @Author zxx
 * @Date 2021/3/24 2:18
 * @Version 1.0
 **/
@Configuration
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
```

### 消息生产者案例

```java
@Autowired
private RabbitTemplate rabbitTemplate;

/**
         * 模拟订单发送
         * @param userId
         * @param productId
         * @param num
         */
public void makeOrder(String userId, String productId, Integer num) {

    //模拟生成订单号
    String orderId = UUID.randomUUID().toString();
    System.out.println("orderId:" + orderId);
    //模拟使用MQ来发送消息
    //参数1：exchange 交换机
    //参数2：路由key/队列queue
    //参数3：消息内容
    String exchange = "fanout_order_exchange";
    String routingKey = "";
    rabbitTemplate.convertAndSend(exchange, routingKey, orderId);
    System.out.println("发送完成");
}
```

### 消息消费者案例

```java 
//监听消息队列
@RabbitListener(queues = {"email.fanout.queue"}) 
@Service //为了让springboot加载这个类
public class FanoutEmailService {

    @RabbitHandler
    public void reviceMessage(String message) {
        System.out.println("email 监听到消息-------》"+message);
    }

}
```

### 使用注解绑定队列

```java
@RabbitListener(bindings = @QueueBinding(
        value = @Queue(value = "sms.top.queue",durable = "true",autoDelete = "false"),
        exchange = @Exchange(value = "topic_order_exchange",type = ExchangeTypes.TOPIC),
        key = "#.email.#"

))
@Service
public class TopicEmailService {

    @RabbitHandler
    public void reviceMessage(String message) {
        System.out.println("email 监听到消息-------》"+message);
    }
}
```

## RabbitMQ高级

### 过期时间TTL

> 过期时间TTL表示可以对消息设置预期的时间，在这个时间内都可以被消费者接收获取，过来之后消息将自动删除，RabbitMQ可以对消息和队列设置TTL。
>
> 1. 通过队列属性设置，队列中所有消息都有相同的过期时间。
> 2. 对消息进行单独设置，每条消息TTL可以不同。

> 如果上述两种方法同时使用，则消息的过期时间以两者之间TTL较小的那个值为准。消息在队列中一旦超过设置的TTL值，就称为dead message被投递到死信队列中，消费者将无法再收到该消息。

```java
//给队列设置过期时间：
//声明队列
@Bean
public Queue ttlEmailQueue(){
    //设置过期时间
    Map<String, Object> map = new HashMap<>();
    map.put("x-message-ttl", 5000);
    return new Queue("ttl.email.direct.queue", true,false,false,map);
}
```

```java
// 给消息设置过期时间
MessagePostProcessor messagePostProcessor=new MessagePostProcessor() {
    @Override
    public Message postProcessMessage(Message message) throws AmqpException {
        //过期时间为5秒
        message.getMessageProperties().setExpiration("5000");
        message.getMessageProperties().setContentEncoding("UTF-8");
        return message;
    }
};
rabbitTemplate.convertAndSend(exchange, "email", orderId,messagePostProcessor);
```

### 死信队列

```java
//创建死信队列
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
```

```java
//将其他队列绑定一个死信队列
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
```

### 分布式事务

#### 可靠生产

```java
@Test
public void test04() throws JsonProcessingException, InterruptedException {
    Order order = new Order();
    order.setId("2223");
    order.setContent("新增一条订单");
    order.setRegDate("2021-03-28");
    ObjectMapper objectMapper = new ObjectMapper();
  rabbitTemplate.convertAndSend("order_message","order",objectMapper.writeValueAsString(order));
    orderMapper.insertOrder(order);
    OrderMessage orderMessage = new OrderMessage();
    orderMessage.setId("3333");
    orderMessage.setContent("冗余表新增");
    orderMessage.setStatus("0");
    orderMessage.setRegDate("2021-03-28");
    orderMessageMapper.insertOrder(orderMessage);

    Thread.sleep(2000L);
}
```

> 创建一条订单消息，将订单消息发送至RabbitMQ供派单系统消费。
>
> 为了生成者消费信息的可靠投递，使用手动ack应答模式，如果消息没有到达交换机，则重发。

```java
//PostConstruct:java提供的注解，用来修饰一个非静态的void()方法，被PostConstruct修饰的方法会在服务器加载Servlet
//的时候运行，并且只会被服务器执行一次，PostConstruct在构造函数之后执行，init()方法之前执行
@PostConstruct
public void confirmCallback(){
    //消息发送成功后，给予生产者的消息回执，来确保生产者的可靠性
    //RabbitTemplate.ConfirmCallback用来确认消息是否到达服务器。也就是确认消息是否到达exchange中。
    rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
        @Override
        public void confirm(CorrelationData correlationData, boolean ack, String cause) {
            //如果ack为true代表消息已经收到
            if (!ack) {
                String orderId = correlationData.getId();
                System.out.println("MQ队列应答失败，orderId是"+orderId);
                OrderMessage orderMessage = new OrderMessage();
                orderMessage.setStatus("2");
                orderMessage.setId(orderId);
                int count= orderMessageMapper.updateOrderStatus(orderMessage);
                System.out.println("本地状态修改："+count);
                return;
            }
        }
    });
}
```

> 为了可以开启手动ack，需要打开配置

```yaml
rabbitmq:
    host: 192.168.10.239
    port: 5672
    virtual-host: /msg
    username: root
    password: root
    listener:
      simple:
        acknowledge-mode: manual  #manual模式是开启手动ack
    publisher-confirm-type: correlated #correlated值是发布消息成功到交换器后会触发回调方法
```

#### 可靠消费

```yaml
rabbitmq:
    host: 192.168.10.239
    port: 5672
    virtual-host: /msg
    username: root
    password: root
    # 开启消息确认
    publisher-confirm-type: correlated
    #开启手动ack
    listener:
      simple:
        acknowledge-mode: manual
        retry:
          enabled: true #开启重试
          max-attempts: 5 #最大重试次数
          initial-interval: 2000ms #重试间隔时间
```

> 开启手动确认机制，如果消息接收异常，则重新发送。

```java
//解决消息重试的几种方案
//1.控制重发的次数
//2.try+catch+手动ack
//3.try+catch+手动ack+死信队列处理
@RabbitHandler
public void consumerMessage(String orderMsg, Channel channel, CorrelationData correlationData, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
    try {
        //获取队列中的消息
        System.out.println("order队列收到的消息是" + orderMsg + ",count:" + count++);
        ObjectMapper objectMapper = new ObjectMapper();
        Order order = objectMapper.readValue(orderMsg, Order.class);
        Distribution distribution = new Distribution();
        distribution.setDistriId("3333");
        distribution.setOrderId(order.getId());
        distribution.setOrderContent(order.getContent());
        distribution.setRegDate(order.getRegDate());
        //int a=1/0;
        distributionMapper.insert(distribution);
        channel.basicAck(tag,false);
    } catch (Exception e) {
        System.out.println("订单保存失败");
        //如果出现异常，根据实际情况去重发
        //参数1：消息的tag，参数2：多条处理，参数3：是否重发
        //false:不重发，会将消息放到死信队列，true:会一直重复发送，建议如果使用true的话，不要加try、catch否则就会造成死循环
        channel.basicNack(tag, false, false);
    }
}
```

> 为了避免异常消息重复发送造成死循环，创建一个死信队列，用于监听异常消息。

```java
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
```

> 监听到死信队列以后可以将消息回传到生产者系统中，生产者系统中进行业务数据回滚，保证数据一致性。如果死信队列出现异常，则将消息保存，进行人工干预，同时调用channel.basicNack(tag, false, false)将消息移除。