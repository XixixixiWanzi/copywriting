package com.example.demo.service;

import com.example.demo.entity.LoginLog;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author XixixixiWanzi
 * @since 2023-12-22
 */
public interface ILoginLogService extends IService<LoginLog> {

    String getCurrentLogID(String UID);
}
