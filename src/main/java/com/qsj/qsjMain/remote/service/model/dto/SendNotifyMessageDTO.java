package com.qsj.qsjMain.remote.service.model.dto;

import com.google.gson.annotations.SerializedName;
import com.qsj.qsjMain.remote.service.model.vo.NotifyDataVO;
import lombok.Data;

@Data
public class SendNotifyMessageDTO {

    private  String template_id = "BG6IZWq9l6Hz0gE8Yjd7LNym3Req4dQ0XNIZzbAwmRU"; // 所需下发的订阅模板id
    private  String page = "index"; // 点击模板卡片后的跳转页面，仅限本小程序内的页面。支持带参数,（示例index?foo=bar）。该字段不填则模板无跳转

    private String touser = ""; //接收者（用户）的 openid


    private  NotifyDataVO data; //模板内容，格式形如 { "key1": { "value": any }, "key2": { "value": any } }的object


    @SerializedName("miniprogram_state")
    private static String miniprogramState = "trial";

    private String lang = "zh_CN"; // 语言


}

