package com.qsj.qsjMain.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;

/**
 * 营养计划表
 */
@TableName(value = "nutrition_plan")
@Data
public class NutritionPlan implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id; // 自增id

    private String name; // 营养计划名称

    private String description; // 营养计划描述

    private Integer energy; // 能量

    private Integer protein; // 蛋白质

    private Integer fat; // 脂肪

    private Integer carbohydrate; // 碳水化合物

    private Integer status; // 状态, 1:正常, 0:禁用

    private String remark; // 备注
}
