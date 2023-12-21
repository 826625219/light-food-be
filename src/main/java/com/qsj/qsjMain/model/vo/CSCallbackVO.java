package com.qsj.qsjMain.model.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CSCallbackVO {
    @JsonProperty("ToUserName")
    private String toUserName;

    @JsonProperty("FromUserName")
    private String fromUserName;

    @JsonProperty("CreateTime")
    private Long createTime;

    @JsonProperty("Content")
    private String content;

    @JsonProperty("MsgId")
    private Long msgId;

    @JsonProperty("MsgType")
    private String msgType;

    @JsonProperty("Event")
    private String event;

}
