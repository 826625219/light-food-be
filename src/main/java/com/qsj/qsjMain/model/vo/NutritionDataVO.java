package com.qsj.qsjMain.model.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode
@Accessors(chain = true)
public class NutritionDataVO {

    private Integer protein;

    private Integer fat;

    private Integer fiber;

    private Integer saturatedFat;

    private Integer nonSaturatedFat;

    private Integer carbohydrate;

    private Integer energy;

    private Integer percentProtein;

    private Integer percentFat;

    private Integer percentCarbohydrate;

    private Integer percentEnergy;

    private Integer percentFiber;


}
