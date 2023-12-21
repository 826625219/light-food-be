package com.qsj.qsjMain.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "上传步数活动VO")
public class UpdateStepVO {
    @ApiModelProperty(value = "步数的加密数据")
    private String encryptedData;
    @ApiModelProperty(value = "加密算法的初始向量")
    private String iv;
    @ApiModelProperty(value = "绑定的活动id")
    private Long activityId;

}
