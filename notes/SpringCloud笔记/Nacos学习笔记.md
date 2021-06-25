# Nacos学习笔记

## 简介

[Nacos官网](https://nacos.io/zh-cn/docs)

## Demo

### 启动Nacos

> 在github中下载nacos的对应jar包。
>
> 在虚拟机centos7中将jar包解压。进入bin目录中，运行命令`startup.sh -m standalone`启动nacos。
>
> Nacos启动以后，访问地址：127.0.0.1:8848/nacos。进入nacos页面。

> nacos使用MySQL
>
> 1. 在本地新建数据库nacos_config。
>
> 2. 修改配置文件：conf/application.properties
>
>    spring.datasource.platform=mysql
>
>    db.num=1
>    db.url.0=jdbc:mysql://127.0.0.1:3306/nacos_devtest?**serverTimezone=UTC&**characterEncoding=utf8&connectTimeout=1000&socketTimeout=3000&autoReconnect=true
>    db.user=root
>    db.password=root
>
>    注意：serverTimezone=UTC&：如果不添加时区，nacos启动时会报错，连接不到数据库。

### Maven地址

```xml
<!--nacos做服务注册与发现-->
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
</dependency>
<!--nacos做服务服务配置中心-->
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
</dependency>
```

### 配置文件

```yaml
server:
  port: 9001

spring:
  profiles:
    active: dev

  application:
    name: cloud-provider-payment-service
  cloud:
    nacos:
      discovery:
      	# 注册中心连接地址
        server-addr: 192.168.132.137:8848
      config:
        # 配置中心连接地址
        server-addr: 192.168.132.137:8848
        name: Public #命名空间
        group: DEFAULT_GROUP #默认组
        file-extension: yaml
management:
  endpoints:
    web:
      exposure:
        include: '*'
## 配置中心文件配置规则
## ${spring.application.name}-${spring.profiles.active}.${file-extension}
## cloud-provider-payment-service-dev.yml
```

### 启动类

```java
@SpringBootApplication
@EnableDiscoveryClient // 开启注册中心配置
public class Payment9001 {
    public static void main(String[] args) {
        SpringApplication.run(Payment9001.class, args);
    }
}
```

### 测试类

```java
@RestController
@RefreshScope // 动态刷新配置注解
public class PaymentController {

    @Value("${server.port}")
    private String serverPort;

    @Value("${config.info}")
    private String configInfo;

    @GetMapping("/getServerPort")
    public String getServerPort(){
        System.out.println(configInfo);
        return serverPort+"==="+configInfo;
    }
}
```

