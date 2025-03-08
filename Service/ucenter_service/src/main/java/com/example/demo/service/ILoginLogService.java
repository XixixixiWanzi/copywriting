package com.example.demo.service;

import com.example.demo.entity.LoginLog;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author XixixixiWanzi
 * @since 2023-12-03
 */
public interface ILoginLogService extends IService<LoginLog> {

    String addRecord(String UID, String ip);

    void setStatusTure(String ID);

    String getCurrentLogID(String UID);

    void insertUpdateInfo(String logID, String obj2JsonStr);
}
