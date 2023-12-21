package com.qsj.qsjMain.model.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UserAddIntegralVO {
    @NotNull(message = "券码不能为空")
    private String couponSn; // 券码Sn

}
