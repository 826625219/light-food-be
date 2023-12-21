package com.qsj.qsjMain.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.experimental.Accessors;

@TableName(value = "step_rank")
@Accessors(chain = true)
@Data
public class StepRank {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id; // 自增id

    private Long activityId; // 活动id

    private Long userId; // 用户id

    private Integer step; // 用户上传的步数

    @TableField(fill = FieldFill.INSERT)
    private Long createTime; // 创建时间

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateTime; // 更新时间
    @Version
    @JsonIgnore
    private Integer version = 0; // 乐观锁
}
