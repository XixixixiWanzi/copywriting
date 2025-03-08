package com.example.demo.controller;

import com.example.ClientIp;
import com.example.CurrentAuthenticationUtils;
import com.example.demo.utils.JwtTokenProvider;
import com.example.demo.service.IAccountService;
import com.example.demo.service.ILoginLogService;
import com.example.reback.R;
import com.example.reback.Reback;
import com.example.reback.ResultCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import java.util.concurrent.TimeUnit;

@RestController
@CrossOrigin
@RequestMapping("ucenter/forLogin")
@Tag(name = "登录接口",description = "该方法主要是用于登录使用，包括密码登录、验证码登录、微信登录三种登录方式。" +
        "<br>不包括注册、密码修改、修改信息、账号绑定等方法")
public class LoginController {
    @Autowired
    private IAccountService accountService;
    @Autowired
    private ILoginLogService loginLogService;

    @Operation(summary = "密码登录", description = "使用手机号和密码登录")
    @GetMapping("bySecret")
    public Reback phoneSecreteLogin(HttpServletRequest request,
                                    @RequestParam String phone,
                                    @RequestParam String secret){
        if(!accountService.existPhone(phone))
            return R.FAIL.setCode(ResultCode.UNAUTHORIZED);

        String ID = accountService.getIDByPhone(phone);

        String ipAddress = ClientIp.getClientIP(request);
        String logID = loginLogService.addRecord(ID, ipAddress);

        String token = accountService.logBySecret(ID, secret);

        loginLogService.setStatusTure(logID);
        return R.SUCCESS.data("token",token);
    }


    @Operation(summary = "验证码登录", description = "验证码已经验证通过，该方法用于获取token，并填写日志信息")
    @GetMapping("byVerifyCode")
    public Reback verifyCodeLogin(HttpServletRequest request,
                                  @RequestParam String phone){
        if(!accountService.existPhone(phone))
            return R.FAIL.setCode(ResultCode.UNAUTHORIZED);

        String ID = accountService.getIDByPhone(phone);

        String ipAddress = ClientIp.getClientIP(request);
        String logID = loginLogService.addRecord(ID, ipAddress);

        String token = accountService.logByPhone(ID);

        loginLogService.setStatusTure(logID);

        return R.SUCCESS.data("token",token);
    }

    @Operation(summary = "微信登录", description = "使用手机号和验证码进行登录，验证码有效时间3min")
    @GetMapping("wechatLogin")
    public Reback wechatLogin(){
//      Todo  微信登录等完善的时候再进行
        return R.SUCCESS.data("token","");
    }

    @Operation(summary = "检查token")
    @GetMapping("checkToken")
    public Reback checkToken(){
        String ID = null;
        try{
            ID = CurrentAuthenticationUtils.getID();
        }catch (Exception e){
            ;
        }

        if (ID == null)
            return R.FAIL;
        return R.SUCCESS.data("token","");
    }






























    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @GetMapping("test")
    public Reback test(){
        redisTemplate.opsForValue().set("18161011898","1234",1, TimeUnit.MINUTES);
        return R.SUCCESS;
    }

}


/**
  projectName(spring boot 项目)
    --Commons（maven 模块  pom）
        --common_utils   （maven  模块）
            --com.example.demo
                --CustomUserDetailsService.java
                --JwtAuthenticationEntryPoint.java
                --JwtAuthenticationFilter.java
                --JwtTokenProvider.java
        --service_base   （maven  模块）
            --com.example.demo
                --securityConfig.java
    --Service（maven  模块  pom）
        --ucenter_service   （maven 模块）
            --com.example.demo
                --mapper
                --service
                --controller
                    --controller.java(该类Autowire注入JwtTokenProvider、AuthenticationManager)
                --entity





 */


























