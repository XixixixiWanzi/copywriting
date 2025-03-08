package com.example.demo.controller;


import com.example.demo.client.AliClient;
import com.example.demo.config.AvatarPros;
import com.example.demo.entity.UserInfo;
import com.example.demo.service.IAccountService;
import com.example.demo.service.IUserInfoService;
import com.example.reback.R;
import com.example.reback.Reback;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.ibatis.annotations.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@RestController
@CrossOrigin
@RequestMapping("ucenter/forRegister")
@Tag(name = "用户注册",description = "该类主要用于用户注册" +
        "<br>现在先完成手机号注册" +
        "<br>待定完成  手机号绑定")

public class RegisterController {
    @Autowired
    private IAccountService accountService;
    @Autowired
    private IUserInfoService userInfoService;
    @Autowired
    private AvatarPros avatarPros;
    @Operation(summary = "手机号注册", description = "手机验证码已经确认无误" +
            "<br>密码前端已经确认密码符合要求" +
            "<br>验证码、密码准备好后，不代表已经注册账号，填写基本信息后才算注册账号成功")
    @PostMapping(value = "registerByPhone")
    @Transactional
    public Reback registerByPhone(
            @RequestParam String phone,
            @RequestParam String password,
            @RequestBody UserInfo info){
//        先检查该账号是否已经注册
        if(accountService.existPhone(phone))
            return R.FAIL.message("用户已存在");
        String url = avatarPros.getDefaul();
//          再写入account
        LocalDateTime verifyData= accountService.addAccountByPhone(phone, password);
//        最后写入info
        info.setId(accountService.getIDByPhone(phone));
        info.setAvatarUrl(url);
        info.setPhoneActivatedTime(verifyData);

        userInfoService.addInfo(info);

        return R.SUCCESS;
    }


    @Operation(summary = "微信注册注册")
    @PostMapping("registerByWechat")
    @Transactional
    public Reback registerByWechat(){
        return R.SUCCESS;
    }

}
