package com.qsj.qsjMain.remote.service.model.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SendCSMessageDTO {
    @SerializedName("touser")
    private String toUser;

    @SerializedName("msgtype")
    private String msgType;

    @SerializedName("text")
    private SendCSMessageDetailDTO text;
}

