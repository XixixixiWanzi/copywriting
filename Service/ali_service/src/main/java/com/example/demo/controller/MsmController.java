package com.example.demo.controller;


import com.example.CWException;
import com.example.demo.service.IMsmService;
import com.example.demo.utils.RandomUtil;
import com.example.reback.R;
import com.example.reback.ResultCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import com.example.reback.Reback;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("aliService/msm")
@CrossOrigin
@Tag(name="验证码", description = "获取4位或者6位验证码，缓存在redis中，有效时间3min, 以及验证码的确认")
public class MsmController {
    private final Integer TIME = 5;
    @Autowired
    private IMsmService msmService;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Operation(summary="获取4位验证码")
    @GetMapping("setVerify")
    public Reback setVerify(@RequestParam String phone,
                            @RequestParam String icode) throws ExecutionException, InterruptedException {

        String code = redisTemplate.opsForValue().get(phone);
        if(!StringUtils.isEmpty(code)) {
            return R.SUCCESS;
        }
        try {
            redisTemplate.opsForValue().set(phone, icode, TIME, TimeUnit.MINUTES);
        }catch (Exception e){
            throw new CWException(ResultCode.SERVER_ERROR);
        }

        return R.SUCCESS;
    }

    @Operation(summary="获取4位验证码")
    @GetMapping("send4Verify")
    public Reback send4Verify(@RequestParam String phone) throws ExecutionException, InterruptedException {

        String code = redisTemplate.opsForValue().get(phone);
        if(!StringUtils.isEmpty(code)) {
            return R.SUCCESS;
        }

        code = RandomUtil.getFourBitRandom();
        msmService.send(code,phone);

        try {
            redisTemplate.opsForValue().set(phone, code, TIME, TimeUnit.MINUTES);
        }catch (Exception e){
            throw new CWException(ResultCode.SERVER_ERROR);
        }

        return R.SUCCESS;
    }

    @Operation(summary="获取6位验证码")
    @GetMapping("send6Verify")
    public Reback send6Verify(@RequestParam String phone) throws ExecutionException, InterruptedException {
        System.out.println("phone = " + phone);
        String code = redisTemplate.opsForValue().get(phone);
        if(!StringUtils.isEmpty(code)) {
            return R.SUCCESS;
        }

        code = RandomUtil.getSixBitRandom();
        msmService.send(code, phone);

        try {
            redisTemplate.opsForValue().set(phone, code, TIME, TimeUnit.MINUTES);
        }catch (Exception e){
            throw new CWException(ResultCode.SERVER_ERROR);
        }

        return R.SUCCESS;
    }

    @Operation(summary="验证验证码",description = "不论该手机号是否已经注册")
    @GetMapping("verify")
    public Reback verify(@RequestParam String phone,
                         @RequestParam String verifyCode){
        String code = redisTemplate.opsForValue().get(phone);
        if (StringUtils.isEmpty(code))
            return R.FAIL.setCode(ResultCode.RESOURCE_EXPIRED);

        if (code.compareToIgnoreCase(verifyCode) != 0)
            return R.FAIL.setCode(ResultCode.FAIL.setMessage("验证码错误"));
        return R.SUCCESS;
    }



}
