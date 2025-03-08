package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author XixixixiWanzi
 * @since 2023-12-09
 */
@Getter
@Setter
@TableName("account")
public class Account implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private String id;

    @TableField("secret")
    private String secret;

    @TableField("wechat")
    private String wechat;

    @TableField("phone")
    private String phone;

    @TableField("activated_time")
    private LocalDateTime activatedTime;

    @TableField("version")
    @Version
    private Integer version;
}
