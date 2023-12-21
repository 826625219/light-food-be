package com.qsj.qsjMain.model.vo;

import lombok.Data;

import java.util.List;

@Data
public class HeatRecordResultVO {
    List<Integer>weekHeat;

    Double averageHeat;

    Integer maxHeat;
}
