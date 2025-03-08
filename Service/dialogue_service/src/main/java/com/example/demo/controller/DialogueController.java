package com.example.demo.controller;

import com.example.demo.entity.Template;
import com.example.demo.service.IBaiduService;
import com.example.demo.service.IDialogueService;
import com.example.demo.service.ITemplateService;
import com.example.demo.util.JsonStrUtil;
import com.example.reback.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.reback.Reback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author XixixixiWanzi
 * @since 2023-12-19
 */
@RestController
@CrossOrigin
@RequestMapping("dialogueService/dialogue")
@Tag(name = "对话")
public class DialogueController {
    @Autowired
    private IDialogueService dialogueService;
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
//    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
//    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Autowired
    private IBaiduService baiduService;

    @Autowired
    private ITemplateService templateService;

    @Operation(summary = "获得用户输入关键字" )
    @PostMapping("getKeyWorks")
    public Reback getKeyWorks(@RequestParam String templateId,
                              @RequestParam String text){
        Template template = templateService.getById(templateId);
        String queries = template.getQueries();

        HashMap<String, ArrayList<String>> keyWords = baiduService.getKeyWords(text, JsonStrUtil.JsonStr2Obj(queries, HashMap.class));

        Map<String, String> map = new HashMap<>();
        map.put("user", text);
        map.put("AI", JsonStrUtil.obj2JsonStr(keyWords));

        try {
            dialogueService.createDialogue(templateId, map);
        }catch (Exception e){
            return  R.FAIL.message("请重试");
        }

        return R.SUCCESS.data("data", keyWords);
    }

    @Operation(summary = "根据用户选择的参数获取prompt模版，然后获得结果" )
    @PostMapping("getWritingText")
    public Reback getWritingText(@RequestBody Map<String, String> params,
                              @RequestParam String templateId){
        Template template = templateService.getById(templateId);
        String promptText = baiduService.getPromptText(params, template.getPromptId());
        String writing = baiduService.getWriting(promptText);
        Map<String,String> map =new HashMap<>();

        try {
            map.put("user", JsonStrUtil.obj2JsonStr(params));
            map.put("AI",writing);
            dialogueService.addContent(map, templateId);
        }catch (Exception e){
            System.out.println("追加聊天记录出错了");
        }
        return R.SUCCESS.data("text", writing);
    }


    @Operation(summary = "根据用户选择的" )
    @GetMapping("getWriting")
    public Reback getWriting(@RequestParam String promptText){
        String writing = baiduService.getWriting(promptText);
        return R.SUCCESS.data("text", writing);
    }

    @Operation(summary = "获得聊天列表" )
    @GetMapping("getDialogueList")
    public Reback getDialogueList(){
        List<Map<String, String>> dialugeList = dialogueService.getDialugeList();
        return R.SUCCESS.data("data",dialugeList);
    }

    @Operation(summary = "获得聊天记录详情" )
    @GetMapping("getDialogue")
    public Reback getDialogue(@RequestParam String id){
        List<Map<String, String>> dialuge = dialogueService.getDialuge(id);
        return R.SUCCESS.data("data",dialuge);
    }
}
