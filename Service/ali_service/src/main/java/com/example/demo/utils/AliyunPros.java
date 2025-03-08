package com.example.demo.utils;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Data
@ConfigurationProperties(prefix = "aliyun")
@Component
public class AliyunPros {
    private String keyId;
    private String keySecret;
    private OssProperties oss;
    private MsmProperties msm;

    @Data
    public static class OssProperties{
        private String endPoint;
        private String buketName;
        private String baseUrl;
    }
    @Data
    public static class MsmProperties{
        private String templateCode;
        private String signName;
        private String endpoint;
    }
}
