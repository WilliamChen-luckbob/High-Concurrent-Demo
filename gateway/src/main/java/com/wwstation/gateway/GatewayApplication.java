package com.wwstation.gateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {"com.wwstation"})
@EnableDiscoveryClient
@EnableFeignClients
@Slf4j
public class GatewayApplication {
    public static void main(String[] args) {
        log.info("gateway is now running...");
        SpringApplication.run(GatewayApplication.class, args);
    }
}
