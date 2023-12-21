package com.qsj.qsjMain.remote.service.model.dto;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.qsj.qsjMain.utils.DigestUtils;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Objects;

@Data
@Accessors(chain = true)
public class DaDaCommonReq<T> {
    @SerializedName("app_key")
    private String appKey = "dada146c0ba305ff48b";

    private transient String appSecret = "028075183607d0038d88afba821c3a30";

    private String format = "json";
    private String v = "1.0";
    @SerializedName("source_id")
    private String sourceId = "1934292756";
    private String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
    private String signature;

    @SerializedName("body")
    private String requestBody; // json encoded string

    private transient T body;

    public DaDaCommonReq<T> prepareBody() {
        this.requestBody = new GsonBuilder().create().toJson(this.body);
        String strToSigned = getAppSecret() +
                "app_key" + this.appKey + "body" + this.requestBody + "format" + this.format +
                "source_id" + this.sourceId + "timestamp" + this.timestamp + "v" + this.v + getAppSecret();
        this.signature = Objects.requireNonNull(DigestUtils.md5(strToSigned)).toUpperCase();
        return this;
    }

}
