package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.CWException;
import com.example.demo.entity.LoginLog;
import com.example.demo.entity.UserInfo;
import com.example.demo.mapper.LoginLogMapper;
import com.example.demo.service.ILoginLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.reback.ResultCode;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author XixixixiWanzi
 * @since 2023-12-03
 */
@Service
public class LoginLogServiceImpl extends ServiceImpl<LoginLogMapper, LoginLog> implements ILoginLogService {
    @Override
    public String addRecord(String UID, String ip) {
        LoginLog log = new LoginLog();
        String id = UUID.randomUUID().toString().replace("-", "");

        log.setId(id);
        log.setAccountId(UID);
        log.setLogTime(LocalDateTime.now());
        log.setIp(ip);

        if (baseMapper.insert(log) < 1)
            throw new CWException(ResultCode.SERVER_ERROR);

        return id;
    }

    @Override
    public void setStatusTure(String ID) {
        LoginLog log = getById(ID);
        log.setStatus(true);
        if (baseMapper.updateById(log) < 1)
            throw new CWException(ResultCode.SERVER_ERROR);
    }

    @Override
    public String getCurrentLogID(String UID){
        LambdaQueryWrapper<LoginLog> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(LoginLog::getAccountId, UID)
                .eq(LoginLog::getStatus, true)
                .orderByDesc(LoginLog::getLogTime)
                .last("LIMIT 1");  // 只取第一条记录
        LoginLog log = getOne(wrapper);
        return log.getId();
    }

    @Override
    public void insertUpdateInfo(String logID, String obj2JsonStr) {
        LoginLog log = getById(logID);
        log.setUpdateInfo(log.getUpdateInfo() +
                "-" +
                obj2JsonStr);
        baseMapper.updateById(log);
    }
}
