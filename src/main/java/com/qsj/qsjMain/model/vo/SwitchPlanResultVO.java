package com.qsj.qsjMain.model.vo;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SwitchPlanResultVO {
    private Boolean success; // 是否切换成功
    private Integer integral; // 切换得到的积分
    private Boolean firstSwitch; // 是否是第一次切换
}
