package com.example.demo.controller;

import com.example.demo.entity.Template;
import com.example.demo.service.ITemplateService;
import com.example.demo.util.JsonStrUtil;
import com.example.reback.R;
import com.example.reback.Reback;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

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
@Tag(name = "获取文案模版")
@RequestMapping("dialogueService/template")
public class TemplateController {

    @Autowired
    private ITemplateService templateService;
    @Operation(summary = "获取模版列表")
    @GetMapping("getTemplateList")
    public Reback getTemplateList(){
        try{
            for (Template template : templateService.getBaseMapper().selectList(null)) {
                HashMap hashMap = JsonStrUtil.JsonStr2Obj(template.getParams(), HashMap.class);
            }

            List<Template> templates = templateService.getBaseMapper().selectList(null);
            return R.SUCCESS.data("templates", templates);
        } catch (Exception e){
            e.printStackTrace();
            return R.FAIL.message("获取列表失败");
        }
    }

}
