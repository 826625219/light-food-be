package com.qsj.qsjMain.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "更新微信用户信息VO")
public class UpdateWXProfileVO {
    private String rawData; // 微信用户信息

    private String signature; // 微信签名

}
