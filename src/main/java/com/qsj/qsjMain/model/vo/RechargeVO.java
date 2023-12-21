package com.qsj.qsjMain.model.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value = "recharge amount")
public class RechargeVO {
    private Integer amount;

    private Boolean increaseFlag;
}
