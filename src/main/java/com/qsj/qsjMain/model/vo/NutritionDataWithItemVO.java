package com.qsj.qsjMain.model.vo;

import com.qsj.qsjMain.model.entity.MaterialGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class NutritionDataWithItemVO extends NutritionDataVO {

    private String name;

    private String i18nName;

    private Integer count;

    private String imgUrl;

    private String spec;

    private Integer price;

    private String i18nSpec;

    private List<MaterialGroup>material;
}
