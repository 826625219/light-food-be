package com.qsj.qsjMain.model.vo;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class WechatPaySuccessCallbackResource {
    @SerializedName("transaction_id")
    private String transactionId; // 微信支付订单号

    @SerializedName("out_trade_no")
    private String outTradeNo; // 商户订单号

    @SerializedName("success_time")
    private String successTime; // 支付成功时间

    @SerializedName("mchid")
    private String mchId; // 商户号



}
