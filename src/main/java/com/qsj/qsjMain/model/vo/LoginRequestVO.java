package com.qsj.qsjMain.model.vo;

import lombok.Data;


@Data
public class LoginRequestVO {

    private String code; // 微信code

    private Long shareFrom; // 邀请来源用户
}
