package com.example.demo.service;

import com.example.demo.entity.Dialogue;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author XixixixiWanzi
 * @since 2023-12-19
 */
public interface IDialogueService extends IService<Dialogue> {
    String createDialogue (String templateId, Map<String, String> map);

    void addContent(Map<String, String> map, String templateId);


    List<Map<String, String>> getDialugeList();

    List<Map<String, String>> getDialuge(String id);
}
