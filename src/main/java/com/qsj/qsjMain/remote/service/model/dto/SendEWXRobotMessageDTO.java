package com.qsj.qsjMain.remote.service.model.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class SendEWXRobotMessageDTO {

    @SerializedName("msgtype")
    private String msgType = "text";

    private SendEWXRobotMessageText text;

}
