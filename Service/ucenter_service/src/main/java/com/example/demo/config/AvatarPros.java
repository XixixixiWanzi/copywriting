package com.example.demo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Data
@ConfigurationProperties(prefix = "avatar")
@Component
public class AvatarPros {
    private String defaul;
    private String boy;
    private String girl;

}
