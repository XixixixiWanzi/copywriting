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
 * @since 2023-12-22
 */
@Getter
@Setter
@TableName("login_log")
@ApiModel(value = "LoginLog对象", description = "")
public class LoginLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private String id;

    @TableField("account_id")
    private String accountId;

    @TableField("log_time")
    private LocalDateTime logTime;

    @TableField("ip")
    private String ip;

    @TableField("update_info")
    private String updateInfo;

    @TableField("status")
    private Byte status;

    @TableField("deleted")
    @TableLogic
    private Boolean deleted;

    @TableField("version")
    @Version
    private Integer version;
}
