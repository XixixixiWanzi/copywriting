package com.example.demo.controller;


import com.example.CurrentAuthenticationUtils;
import com.example.demo.config.AvatarPros;
import com.example.demo.utils.JsonStrUtil;
import com.example.demo.client.AliClient;
import com.example.demo.entity.UserInfo;
import com.example.demo.service.ILoginLogService;
import com.example.demo.service.IUserInfoService;
import com.example.reback.R;
import com.example.reback.Reback;
import com.example.reback.ResultCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("ucenter/forRevise")
@Tag(name = "修改接口",description = "该方法主要是用于用户信息修改，包括：" +
        "<br>头像修改" +
        "<br>网名修改" +
        "<br>生日修改" +
        "<br>性别修改" +
        "<br>!!!!!!!不包括密码修改")
public class UpdateController {
    @Autowired
    private IUserInfoService userInfoService;
    @Autowired
    private AliClient aliClient;
    @Autowired
    private ILoginLogService logService;
    @Autowired
    private AvatarPros avatarPros;

    @Operation(summary = "头像修改")
    @PostMapping(value = "updateAvatar", consumes = "multipart/form-data")
    @Transactional
    public Reback updateAvatar(@RequestPart(value = "file", required = false) MultipartFile file){
//        调用阿里OSS
        String avatarUrl = null;
        if (file != null)
            avatarUrl = aliClient.uploadPicture(file).getData().get("url").toString();
//        从token中获取用户id
        String UID = CurrentAuthenticationUtils.getID();
        UserInfo oldInfo = userInfoService.getById(UID);

        if(avatarUrl ==  null){
            if (oldInfo.getSex().compareToIgnoreCase("0") == 0)
                avatarUrl = avatarPros.getGirl();
            else if (oldInfo.getSex().compareToIgnoreCase("1") == 0)
                avatarUrl = avatarPros.getBoy();
            else avatarUrl = avatarPros.getDefaul();
        }
//        更新用户信息
        UserInfo newInfo = new UserInfo();
        newInfo.setAvatarUrl(avatarUrl);
        Boolean status = userInfoService.update(UID, newInfo);
//        写入修改日志到loginLog中
        String logID = logService.getCurrentLogID(UID);
        Map<String,Object> clas = new HashMap();
        clas.put("status",status);
        clas.put("time", LocalDateTime.now().toString());
        clas.put("oldInfo",oldInfo);
        clas.put("newInfo",newInfo);

        logService.insertUpdateInfo(logID, JsonStrUtil.obj2JsonStr(clas));

        return status ? R.SUCCESS : R.FAIL.setCode(ResultCode.SERVER_ERROR);
    }

    @Operation(summary = "其他信息修改")
    @PostMapping("updateInfo")
    @Transactional
    public Reback updateInfo(@RequestBody UserInfo newInfo){
//        从token中获取用户id
        String UID = CurrentAuthenticationUtils.getID();
//        更新用户信息
        Boolean status = userInfoService.update(UID, newInfo);
//         写日志
        String logID = logService.getCurrentLogID(UID);
        UserInfo oldInfo = userInfoService.getById(UID);
        Map<String,Object> clas = new HashMap();
        clas.put("status",status);
        clas.put("time", LocalDateTime.now().toString());
        clas.put("oldInfo",oldInfo);
        clas.put("newInfo",newInfo);
        logService.insertUpdateInfo(logID, JsonStrUtil.obj2JsonStr(clas));

        return status ? R.SUCCESS : R.FAIL.setCode(ResultCode.SERVER_ERROR);
    }
}
