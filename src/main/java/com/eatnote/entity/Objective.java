package com.eatnote.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.eatnote.Enum.ConfidenceLevel;
import com.eatnote.Enum.OkrStatus;
import com.eatnote.Enum.Visibility;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_objectives")
public class Objective extends BaseEntity {
    private String ownerId;
    private OkrStatus status;
    private ConfidenceLevel confidenceLevel;
    private Visibility visibility;
    //0-100
    private String weight;
    private String score;
    private String title;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime dueDate;
    //another objective id
    private String alignedTo;
}
