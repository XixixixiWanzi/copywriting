package com.example.demo.controller;


import com.example.demo.service.IOssService;
import com.example.reback.R;
import com.example.reback.Reback;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("aliService/oss")
@CrossOrigin
@Tag(name="对象存储", description = "")
public class OssController {
    @Autowired
    private IOssService ossService;

    @PostMapping(value = "uploadPicture", consumes = "multipart/form-data")
    public Reback uploadPicture(@RequestPart("file") MultipartFile file){
        String url = ossService.upload(file);
        return R.SUCCESS.data("url",url);
    }
}
