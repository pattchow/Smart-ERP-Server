package com.eatnote.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public abstract class BaseEntity implements Serializable {

    /**
     * 序列化版本号
     */
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 创建时间
     */
    @TableField(value = "created_time", fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    /**
     * 更新时间
     */
    @TableField(value = "updated_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;

//    /**
//     * 创建者ID
//     */
//    @TableField(value = "created_by", fill = FieldFill.INSERT)
//    private String createdBy;
//
//    /**
//     * 更新者ID
//     */
//    @TableField(value = "updated_by", fill = FieldFill.INSERT_UPDATE)
//    private String updatedBy;
//
//    /**
//     * 是否已删除
//     */
//    @TableField(value = "deleted", fill = FieldFill.INSERT)
//    private Boolean deleted;
}