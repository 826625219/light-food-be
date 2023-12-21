package com.qsj.qsjMain.remote.service.model.vo;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class WXJSCode2SessionResp {

    @SerializedName("openid")
    private String openId;

    @SerializedName("session_key")
    private String sessionKey;

    @SerializedName("unionid")
    private String unionId;

    @SerializedName("errcode")
    private Integer errCode;

    @SerializedName("errmsg")
    private String errMsg;

}
