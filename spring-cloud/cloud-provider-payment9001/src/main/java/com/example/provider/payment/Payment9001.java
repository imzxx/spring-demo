package com.example.provider.payment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @ClassName Payment9001
 * @Description TODO
 * @Author zxx
 * @Date 2021/4/20 23:17
 * @Version 1.0
 **/
@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
@EnableDiscoveryClient
public class Payment9001 {
    public static void main(String[] args) {
        SpringApplication.run(Payment9001.class, args);
    }
}
