package com.qsj.qsjMain.remote.service.model.vo;

import lombok.Data;

@Data
public class DaDaCommonResp<T> {
    private String status;

    private Integer code;

    private String msg;

    private Integer errorCode;

    private T result;
}
