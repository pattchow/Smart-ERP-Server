package com.eatnote.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_users")
public class User extends BaseEntity {
    @TableField("username")
    private String username;

    @TableField("full_name")
    private String fullName;

    @TableField("email")
    private String email;

    @TableField("org_id")
    private String orgId;
}