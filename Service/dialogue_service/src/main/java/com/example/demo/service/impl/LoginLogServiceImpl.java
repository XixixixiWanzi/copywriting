package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.demo.entity.LoginLog;
import com.example.demo.mapper.LoginLogMapper;
import com.example.demo.service.ILoginLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author XixixixiWanzi
 * @since 2023-12-22
 */
@Service
public class LoginLogServiceImpl extends ServiceImpl<LoginLogMapper, LoginLog> implements ILoginLogService {
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
}
