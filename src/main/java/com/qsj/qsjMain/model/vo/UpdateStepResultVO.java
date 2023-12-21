package com.qsj.qsjMain.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateStepResultVO {
    private Integer status; //0:正常 1：失败
}
