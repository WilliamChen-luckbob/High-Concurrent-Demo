package com.wwstation.authorization;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(scanBasePackages = {"com.wwstation"})
@EnableDiscoveryClient
@Slf4j
public class ShiroApplication {
    public static void main(String[] args) {
        log.info("shiro is now running ...");
        SpringApplication.run(ShiroApplication.class, args);
    }
}
