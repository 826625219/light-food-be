package com.qsj.qsjMain.model.entity;

import lombok.Data;

import java.util.List;

@Data
public class SpecGroup {
    private String name;
    private String i18nName;
    private List<SpecOption> options;
}
