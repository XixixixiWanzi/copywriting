package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;


//http://localhost:8081/copywriting/swagger-ui/index.html

@SpringBootApplication
@Slf4j
@EnableDiscoveryClient
@EnableFeignClients
@ComponentScan(basePackages = "com.example")

@MapperScan("com/example/demo/mapper")
public class UCenterApplication {
    public static void main(String[] args) {
        SpringApplication.run(UCenterApplication.class,args);
    }

}
