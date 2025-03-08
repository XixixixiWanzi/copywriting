package com.example.demo.utils;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "jwt-app")
public class JWTPrefix {
    private String secret ;
    private long milliseconds ;

}
