package com.qsj.qsjMain.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "修改营养计划VO")
public class SwitchNutritionPlanVO {
    @ApiModelProperty(value = "营养计划id")
    private Long id;

    @ApiModelProperty(value = "身高")
    private Integer height;

    @ApiModelProperty(value = "体重")
    private Integer weight;

    @ApiModelProperty(value = "生日")
    private String birthday;

    @ApiModelProperty(value = "性别")
    private Integer gender; // 0:男 1:女

    private Integer practise; // 0:无运动 1:轻度运动 2:中度运动 3:重度运动
}
