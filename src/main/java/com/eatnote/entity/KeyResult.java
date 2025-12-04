package com.eatnote.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.eatnote.Enum.ConfidenceLevel;
import com.eatnote.Enum.OkrStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_key_results")
public class KeyResult  extends BaseEntity  {
    private String objectiveId;
    private String ownerId;
    private String title;
    private String description;
    //0-100
    private String progress;
    private OkrStatus status;
    private ConfidenceLevel confidenceLevel;
    private LocalDateTime startDate;
    private LocalDateTime dueDate;
}