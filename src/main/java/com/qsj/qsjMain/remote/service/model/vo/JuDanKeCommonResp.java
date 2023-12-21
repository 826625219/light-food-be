package com.qsj.qsjMain.remote.service.model.vo;

import lombok.Data;

@Data
public class JuDanKeCommonResp<T> {
    private Integer code;

    private  String msg;

    private String uid;

    private String data;

}
