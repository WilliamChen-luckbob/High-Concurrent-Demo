package com.william_workstation.high_concurrent_demo.order;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {"com.william_workstation.high_concurrent_demo.order","com.william_workstation.high_concurrent_demo.config"})
@EnableFeignClients
@EnableDiscoveryClient
@MapperScan(basePackages = {"com.william_workstation.high_concurrent_demo.order.mapper"})
public class OrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }
}
