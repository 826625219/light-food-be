package com.qsj.qsjMain.remote.service.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.qsj.qsjMain.utils.DigestUtils;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class JuDanKeCommonReq<T> {

    public JuDanKeCommonReq(String api) {
        this.api = api;
    }

    @SerializedName("app_id")
    private String appId = "100156";
    @JsonIgnore
    private String appKey = "6039db73a4d790db5dbf0043211146fa0a0856d9";
    @JsonIgnore
    private String api;

    private String nonce = "hjgfshGHjkkdjhgfhgs";
    private String ts = String.valueOf(System.currentTimeMillis());
    private String sign;

    @SerializedName("data")
    private String requestBody; // json encoded string

    private transient T body;

    public JuDanKeCommonReq<T> prepareBody() {
        this.requestBody = new GsonBuilder().create().toJson(this.body);
        String strToSigned = DigestUtils.sha1(this.ts + this.appKey + this.api + this.appId + this.nonce);
        this.sign = strToSigned;
        return this;
    }

}
