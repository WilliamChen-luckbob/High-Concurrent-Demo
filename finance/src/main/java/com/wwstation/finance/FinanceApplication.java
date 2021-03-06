package com.wwstation.finance;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {"com.wwstation"},
exclude = DataSourceAutoConfiguration.class)
@EnableDiscoveryClient
@EnableFeignClients
@MapperScan(basePackages = "com.wwstation.finance.mapper")
public class FinanceApplication {
    public static void main(String[] args) {
        SpringApplication.run(FinanceApplication.class, args);
    }
}
