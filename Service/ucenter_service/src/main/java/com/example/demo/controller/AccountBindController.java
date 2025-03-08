package com.example.demo.controller;


import com.example.CurrentAuthenticationUtils;
import com.example.demo.entity.UserInfo;
import com.example.demo.service.IAccountService;
import com.example.demo.service.IUserInfoService;
import com.example.reback.R;
import com.example.reback.Reback;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@CrossOrigin
@RequestMapping("ucenter/forBind")
@Tag(name = "账号绑定",description = "该类主要用于账号绑定,绑定过程不写入日志" +
        "<br>待定完成  手机号绑定    微信绑定")
public class AccountBindController {
    @Autowired
    private IAccountService accountService;
    @Autowired
    private IUserInfoService infoService;

    @Operation(summary = "微信绑定")
    @PostMapping("bindWechat/{wechatID}")
    @Transactional
    public Reback bindWechat(@PathVariable String wechatID){
        if (accountService.existWechat(wechatID))
            return R.FAIL.message("该账号已经被注册");

        String ID = CurrentAuthenticationUtils.getID();
        LocalDateTime bindTime = accountService.bindWechat(wechatID, ID);

        UserInfo info = new UserInfo();
        info.setWechatActivatedTime(bindTime);

        if (!infoService.update(ID, info))
            return R.FAIL;
        return R.SUCCESS;
    }

    @Operation(summary = "手机号绑定")
    @PostMapping("bindPhone/{phone}")
    @Transactional
    public Reback bindPhone(@PathVariable String phone,
                            @RequestParam String secret){
        if (accountService.existPhone(phone))
            return R.FAIL.message("该账号已经被注册");

        String ID = CurrentAuthenticationUtils.getID();
        LocalDateTime bindTime = accountService.bindPhone(phone, secret, ID);

        UserInfo info = new UserInfo();
        info.setWechatActivatedTime(bindTime);

        if (!infoService.update(ID, info))
            return R.FAIL;
        return R.SUCCESS;
    }
}
