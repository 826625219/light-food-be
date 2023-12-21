package com.qsj.qsjMain.remote.service.model.vo;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class GetWXCodeUnLimitResp {

    @SerializedName("errcode")
    private String errcode; // 错误码

    @SerializedName("errmsg")
    private String errmsg; // 错误信息

    private String buffer; // 二维码图片的base64编码

}
