package com.qsj.qsjMain.model.vo;

import lombok.Data;


@Data
public class LoginResultVO {

    private String credential; // 用户凭据

    private Boolean needUserInfo; // 是否需要用户信息

}
