package com.example.demo.service;

import com.example.demo.entity.UserInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author XixixixiWanzi
 * @since 2023-12-03
 */
public interface IUserInfoService extends IService<UserInfo> {

    boolean update(String id, UserInfo newInfo);

    void addInfo(UserInfo info);
}
