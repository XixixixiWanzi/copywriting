package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author XixixixiWanzi
 * @since 2023-12-19
 */
@Getter
@Setter
@TableName("dialogue")
@ApiModel(value = "Dialogue对象", description = "")
public class Dialogue implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private String id;

    @TableField("account_id")
    private String accountId;

    @TableField("login_id")
    private String loginId;

    @TableField("template_id")
    private String templateId;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("content")
    private String content;

    @TableField("deleted")
    @TableLogic
    private Boolean deleted;

    @TableField("version")
    @Version
    private Integer version;
}
