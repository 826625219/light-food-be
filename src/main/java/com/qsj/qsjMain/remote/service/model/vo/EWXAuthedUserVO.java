package com.qsj.qsjMain.remote.service.model.vo;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class EWXAuthedUserVO {
    @SerializedName("errcode")
    private Integer errCode;

    @SerializedName("errmsg")
    private String errMsg;

    @SerializedName("userid")
    private String userId;

}
