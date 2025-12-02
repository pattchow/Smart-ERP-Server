package com.eatnote.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("organization")
public class Organization extends BaseEntity{
    @TableField("name")
    private String name;

    @TableField("parent_id")
    private String parentId;
}
