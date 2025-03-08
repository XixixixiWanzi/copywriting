package com.example.demo.client;

import com.example.reback.Reback;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.ExecutionException;

@Component
@FeignClient("ali-service")
public interface AliClient {
    @PostMapping(value = "copywriting/aliService/oss/uploadPicture", consumes = "multipart/form-data")
    Reback uploadPicture(@RequestPart("file") MultipartFile file);

    @GetMapping("copywriting/aliService/oss/testNacos")
    public Reback testNacos();

}
