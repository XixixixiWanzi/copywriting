package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;


//http://localhost:8083/copywriting/swagger-ui/index.html

@SpringBootApplication
@Slf4j
@EnableDiscoveryClient
@EnableFeignClients
@ComponentScan(basePackages = "com.example")
//eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJkOGM5MDc0Njc5MTc0ZjQ4OTVhZGRhZmRhNmMwMjY3NiIsImlhdCI6MTcwMjk5MTQxMiwiZXhwIjoxNzAzNTk2MjEyfQ.s9CN3ThJso0W2nxOD3tthkcELIWGnF2HcTvQMyMlqbBJiGZpWsVZnKDSt6hcl2uB

@MapperScan("com/example/demo/mapper")
public class DialogueApplication{
    public static void main(String[] args) {
        SpringApplication.run(DialogueApplication.class,args);
    }
}
