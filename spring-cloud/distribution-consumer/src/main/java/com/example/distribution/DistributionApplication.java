package com.example.distribution;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @ClassName DistributionApplication
 * @Description TODO
 * @Author zxx
 * @Date 2021/3/28 16:02
 * @Version 1.0
 **/
@SpringBootApplication
@EnableTransactionManagement
@MapperScan("com.example.distribution.mapper")
public class DistributionApplication {
    public static void main(String[] args) {
        SpringApplication.run(DistributionApplication.class);
    }
}
