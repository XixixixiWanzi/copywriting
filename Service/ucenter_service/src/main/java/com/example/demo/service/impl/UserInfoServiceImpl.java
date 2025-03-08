package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.CWException;
import com.example.demo.entity.UserInfo;
import com.example.demo.mapper.UserInfoMapper;
import com.example.demo.service.IUserInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.reback.ResultCode;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author XixixixiWanzi
 * @since 2023-12-03
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements IUserInfoService {
    @Override
    public boolean update(String id, UserInfo newInfo) {
        UserInfo oldInfo = getById(id);
        BeanUtils.copyProperties(newInfo, oldInfo, "serialVersionUID", "id", "version");
        return baseMapper.updateById(oldInfo) > 0;
    }


    /**
     * 不返回值的原因：
     *      因为该方法如果出错，那么用户请求就算失败，而且失败后系统不需要其他操作，所以直接抛出错误，交给全局错误管理
     *      exist和checksSecret方法，是需要后续操作，所以需要返回值，不能使用抛出错误的方法
     *      总结：
     *          若service方法是一个BOOLEAN性质方法，如果False系统需要后续操作，那就要返回值；如果不需要后续操作，就直接抛出错误
     */
    @Override
    public void addInfo(UserInfo info) {
        if(baseMapper.insert(info) < 1)
            throw new CWException(ResultCode.SERVER_ERROR);
    }
}
