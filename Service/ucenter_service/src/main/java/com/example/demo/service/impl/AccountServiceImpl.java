package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.CWException;
import com.example.demo.utils.CoustomPasswordEncoder;
import com.example.demo.utils.JwtTokenProvider;
import com.example.demo.entity.Account;
import com.example.demo.mapper.AccountMapper;
import com.example.demo.service.IAccountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.reback.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author XixixixiWanzi
 * @since 2023-12-03
 */
@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements IAccountService {
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public boolean existPhone(String phoneNumber) {
        QueryWrapper<Account> wrapper = new QueryWrapper<>();
        wrapper.eq("phone", phoneNumber);
        return baseMapper.exists(wrapper);
    }

    @Override
    public boolean existWechat(String wechatID) {
        QueryWrapper<Account> wrapper = new QueryWrapper<>();
        wrapper.eq("wechat", wechatID);
        return baseMapper.exists(wrapper);
    }

    @Override
    public String logBySecret(String id, String secret) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                id,
                secret));   // 该认证是进行密码认证。
        SecurityContextHolder.getContext().setAuthentication(authentication);   // token认证之后的其他认证，暂时不需要。
        String token = jwtTokenProvider.generateToken(authentication);
        if (token == null)
            throw new CWException().setMessage("密码错误");
        return token;
    }

    @Override
    public String logByPhone(String id) {
        Authentication authentication = new PreAuthenticatedAuthenticationToken(id, null);
        String token = jwtTokenProvider.generateToken(authentication);
        if (token == null)
            throw new CWException(ResultCode.SERVER_ERROR);
        return token;
    }


    @Override
    public String getIDByPhone(String phone) {
        QueryWrapper<Account> wrapper = new QueryWrapper<>();
        wrapper.eq("phone", phone);
        Account one = getOne(wrapper);
        return one.getId();
    }

    @Override
    public String getIDByWechat(String wechatID) {
        QueryWrapper<Account> wrapper = new QueryWrapper<>();
        wrapper.eq("wechat", wechatID);
        Account one = getOne(wrapper);
        return one.getId();
    }


    @Override
    public void changeSecret(String id, String newPassword) {
        Account account = getById(id);
        account.setSecret(CoustomPasswordEncoder.encrypt(newPassword));
        if (baseMapper.updateById(account) < 1)
            throw new CWException(ResultCode.SERVER_ERROR);
    }

    @Override
    public LocalDateTime addAccountByPhone(String phone, String password) {
        Account account = new Account();
        account.setPhone(phone);
        LocalDateTime time = LocalDateTime.now();
        account.setActivatedTime(time);
        account.setSecret(CoustomPasswordEncoder.encrypt(password));
        if(baseMapper.insert(account) < 0)
            throw new CWException(ResultCode.SERVER_ERROR);
        return time;
    }

    @Override
    public LocalDateTime addAccountByWechat(String wechatId) {
        Account account = new Account();
        account.setPhone(wechatId);
        LocalDateTime time = LocalDateTime.now();
        account.setActivatedTime(time);
        if(baseMapper.insert(account) < 0)
            throw new CWException(ResultCode.SERVER_ERROR);
        return time;
    }

    @Override
    public LocalDateTime bindWechat(String wechatId, String id) {
        Account account = getById(id);
        account.setWechat(wechatId);
        if (baseMapper.updateById(account) < 1)
            throw new CWException(ResultCode.SERVER_ERROR);
        return LocalDateTime.now();
    }

    @Override
    public LocalDateTime bindPhone(String phoneNumber, String password, String id) {
        Account account = getById(id);
        account.setPhone(phoneNumber);
        account.setSecret(CoustomPasswordEncoder.encrypt(password));
        if (baseMapper.updateById(account) < 1)
            throw new CWException(ResultCode.SERVER_ERROR);
        return LocalDateTime.now();
    }



}
