package com.example.demo.controller;


import com.example.demo.service.IBaiduService;
import com.example.reback.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import com.example.reback.Reback;


@RestController
@RequestMapping("dialogueService/test")
@CrossOrigin
@Tag(name="测试")
public class Test {
    @Autowired
    private IBaiduService baiduService;

    @Operation(summary="更新token")
    @GetMapping("refreshToken")
    public Reback refreshToken(){
        baiduService.getErnieToken();
        baiduService.getNLPToken();
        return R.SUCCESS;
    }

    @Scheduled(fixedRate = 2 * 60 * 60 * 1000)
    private void timeTask(){
        refreshToken();
    }
}
