package com.example.demo.controller;


import com.example.CurrentAuthenticationUtils;
import com.example.demo.entity.UserInfo;
import com.example.demo.service.IUserInfoService;
import com.example.reback.R;
import com.example.reback.Reback;
import com.example.reback.ResultCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("ucenter/getInfo")
@Tag(name = "获取用户数据",description = "该方法主要是用于获得用户的各种数据，包括基本信息，dialogue")
public class UserInfoController {
    @Autowired
    private IUserInfoService userInfoService;

    @Operation(summary = "获取用户信息", description = "登录成功后获取用户基本信息")
    @GetMapping("getInfo")
    public Reback getInfo(){
        String ID = CurrentAuthenticationUtils.getID();
        UserInfo info = userInfoService.getById(ID);
        return R.SUCCESS.data("info", info);
    }
}
