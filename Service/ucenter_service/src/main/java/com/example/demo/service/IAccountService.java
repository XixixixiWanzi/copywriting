package com.example.demo.service;

import com.example.demo.entity.Account;
import com.baomidou.mybatisplus.extension.service.IService;

import java.time.LocalDateTime;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author XixixixiWanzi
 * @since 2023-12-03
 */
public interface IAccountService extends IService<Account> {
    boolean existPhone(String phoneNumber);

    boolean existWechat(String wechatID);


    String logBySecret(String concatenateStrings, String secret);
    String logByPhone(String id);


    String getIDByPhone(String phone);

    String getIDByWechat(String wechatID);

    void changeSecret(String id, String newPassword);

    LocalDateTime addAccountByPhone(String phone, String password);
    LocalDateTime addAccountByWechat(String wechatId);
    LocalDateTime bindWechat(String wechatId, String id);
    LocalDateTime bindPhone(String phoneNumber, String password ,String id);


}
