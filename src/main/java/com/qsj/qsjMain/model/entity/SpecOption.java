package com.qsj.qsjMain.model.entity;

import lombok.Data;

@Data
public class SpecOption {
    private Boolean selected;
    private String name;
    private String i18nName;
    private Integer price;
}
