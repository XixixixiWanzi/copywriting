package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.CurrentAuthenticationUtils;
import com.example.demo.entity.Dialogue;
import com.example.demo.entity.Template;
import com.example.demo.mapper.DialogueMapper;
import com.example.demo.service.IDialogueService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.service.ILoginLogService;
import com.example.demo.service.ITemplateService;
import com.example.demo.util.JsonStrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author XixixixiWanzi
 * @since 2023-12-19
 */
@Service
public class DialogueServiceImpl extends ServiceImpl<DialogueMapper, Dialogue> implements IDialogueService {
    @Autowired
    private ILoginLogService logService;
    @Autowired
    private ITemplateService templateService;

    @Override
    public String createDialogue(String templateId, Map<String, String> map) {
        String UID = CurrentAuthenticationUtils.getID();
        String currentLogID = logService.getCurrentLogID(UID);
        String id = UUID.randomUUID().toString().replace("-", "");
        map.put("time",LocalDateTime.now().toString());
        List<Map<String, String>> content = new ArrayList<>();
        content.add(map);
        String contentStr = JsonStrUtil.obj2JsonStr(content);

        Dialogue dialogue = new Dialogue();
        dialogue.setId(id);
        dialogue.setTemplateId(templateId);
        dialogue.setCreateTime(LocalDateTime.now());
        dialogue.setAccountId(UID);
        dialogue.setLoginId(currentLogID);
        dialogue.setContent(contentStr);

        baseMapper.insert(dialogue);

        return id;
    }

    @Override
    public void addContent(Map<String, String> map, String templateId) {
        String UID = CurrentAuthenticationUtils.getID();

        LambdaQueryWrapper<Dialogue> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Dialogue::getAccountId, UID)
                .eq(Dialogue::getTemplateId, templateId)
                .orderByDesc(Dialogue::getCreateTime)
                .last("LIMIT 1");  // 只取第一条记录
        Dialogue dialogue = getOne(wrapper);

        String contentStr = dialogue.getContent();
        List<Map<String, String>> content = (List<Map<String, String>>) JsonStrUtil.JsonStr2Obj(contentStr,Object.class);
        map.put("time",LocalDateTime.now().toString());
        content.add(map);
        contentStr = JsonStrUtil.obj2JsonStr(content);

        updateContent(dialogue.getId(), contentStr);
    }
    protected void updateContent(String id, String newContent){
        Dialogue dialogue = baseMapper.selectById(id);
        dialogue.setContent(newContent);
        baseMapper.updateById(dialogue);
    }

    @Override
    public  List<Map<String, String>> getDialugeList(){
        String UID = CurrentAuthenticationUtils.getID();
        LambdaQueryWrapper<Dialogue> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Dialogue::getAccountId, UID);

        List<Map<String, String>> res =new ArrayList<>();
        for (Dialogue dialogue : list(wrapper)) {
            Map<String, String> map = new HashMap<>();
            String templateId = dialogue.getTemplateId();
            Template template = templateService.getById(templateId);
            map.put("id",dialogue.getId());
            map.put("time",dialogue.getCreateTime().toString());
            map.put("name",template.getTemplateName());
            res.add(map);
        }
        return res;
    }

    @Override
    public List<Map<String, String>> getDialuge(String id){
        Dialogue dialogue = getById(id);
        String contentStr = dialogue.getContent();
        List<Map<String, String>> content = (List<Map<String, String>>) JsonStrUtil.JsonStr2Obj(contentStr,Object.class);

        return content;
    }
}
