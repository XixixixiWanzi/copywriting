package com.example.demo;


import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;



//http://localhost:8082/copywriting/swagger-ui/index.html

@SpringBootApplication
@Slf4j
@EnableDiscoveryClient
@ComponentScan(basePackages = "com.example")
@MapperScan("com/example/demo/mapper")

public class AliApplication {
    public static void main(String[] args) {
        SpringApplication.run(AliApplication.class,args);
    }
}
