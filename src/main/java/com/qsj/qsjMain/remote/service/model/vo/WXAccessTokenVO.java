package com.qsj.qsjMain.remote.service.model.vo;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class WXAccessTokenVO {

    @SerializedName("access_token")
    private String accessToken;

    @SerializedName("expires_in")
    private Integer expiresIn;
}
