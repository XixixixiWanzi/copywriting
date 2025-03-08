package com.example.demo.util;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties(prefix = "baidu")
@Component
public class BaiduProps {
    private String wenxinKey;
    private String wenxinSecret;
    private String promptUrl;
    private String ernieBotUrl;
    private String txtMonetUrl;
    private String nlpKey;
    private String nlpSecret;
    private String keywordsUrl;
    private String tokenUrl;
}
