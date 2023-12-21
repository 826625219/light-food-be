package com.qsj.qsjMain.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@ApiModel("删除地址VO")
public class RemoveAddressVO {

    @ApiModelProperty("地址id")
    private Long id; // 地址id


}
