package com.example.demo.controller;


import com.example.CWException;
import com.example.CurrentAuthenticationUtils;
import com.example.demo.entity.Account;
import com.example.demo.service.IAccountService;
import com.example.reback.R;
import com.example.reback.Reback;
import com.example.reback.ResultCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("ucenter/forPassword")
@Tag(name = "修改密码",description = "该类主要是用于用户密码修改 ，通过手机验证码修改密码" +
        "<br>修改密码不会留下日志")
public class PasswordController {
    @Autowired
    private IAccountService accountService;

    //    找回密码的流程： 先验证验证码，就当是登陆了，然后直接跳转到修改密码的页面
    @Operation(summary = "修改密码", description = "验证码已经验证通过")
    @PostMapping("passwordChange")
    public Reback passwordChange(@RequestParam String newPassword,@RequestParam String phone){
        String ID = accountService.getIDByPhone(phone);
        accountService.changeSecret(ID, newPassword);
        return R.SUCCESS;
    }
    @Operation(summary = "验证手机号是否注册")
    @GetMapping("Registered")
    public Reback Registered(@RequestParam String phone) {
        String ID = null;
        try{
            ID = accountService.getIDByPhone(phone);
        }catch (Exception e){}
        if (ID == null)
            return R.FAIL.message("该账号未注册");
        return R.SUCCESS;

    }

    @Operation(summary = "验证手机号与当前账号一致性", description = "密码修改过程：<br>先验证验证码是否正确 <br>再验证手机号是否和当前登录账号是否应一致")
    @GetMapping("verifyPhone")
    public Reback verifyPhone(@RequestParam String phone) {
        String ID = CurrentAuthenticationUtils.getID();
        Account account = accountService.getById(ID);
        if (account.getPhone().compareToIgnoreCase(phone) != 0)
            return R.FAIL.message("当前号码与当前账户不一致");
        return R.SUCCESS;

    }

}
