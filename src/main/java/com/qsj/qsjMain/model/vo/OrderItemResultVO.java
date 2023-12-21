package com.qsj.qsjMain.model.vo;

import lombok.Data;

@Data
public class OrderItemResultVO {

    private String foodName;

    private String foodI18nName;

    private String spec;

    private String i18nSpec;

    private Integer foodNum;

    private Integer price;

    private String imgUrl;
}
