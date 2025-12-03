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
     * Serialization version UID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Primary key ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * Creation time
     */
    @TableField(value = "created_time", fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    /**
     * Update time
     */
    @TableField(value = "updated_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;

//    /**
//     * Creator ID
//     */
//    @TableField(value = "created_by", fill = FieldFill.INSERT)
//    private String createdBy;
//
//    /**
//     * Updater ID
//     */
//    @TableField(value = "updated_by", fill = FieldFill.INSERT_UPDATE)
//    private String updatedBy;
//
//    /**
//     * Whether it is deleted
//     */
//    @TableField(value = "deleted", fill = FieldFill.INSERT)
//    private Boolean deleted;
}