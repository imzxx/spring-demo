package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @ClassName StreamRabbitApplication
 * @Description TODO
 * @Author zxx
 * @Date 2021/6/17 23:01
 * @Version 1.0
 **/
@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
public class StreamRabbitApplication {
    public static void main(String[] args) {
        SpringApplication.run(StreamRabbitApplication.class);
    }
}
