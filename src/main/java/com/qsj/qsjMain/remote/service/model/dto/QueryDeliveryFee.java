package com.qsj.qsjMain.remote.service.model.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class QueryDeliveryFee {
    @SerializedName("shop_no")
    private String shopNo;

    @SerializedName("origin_id")
    private String originId;

    @SerializedName("city_code")
    private String cityCode;

    @SerializedName("cargo_price")
    private Double cargoPrice;

    @SerializedName("is_prepay")
    private Integer isPrepay;

    @SerializedName("receiver_name")
    private String receiverName;

    @SerializedName("receiver_address")
    private String receiverAddress;

    private String callback;

    @SerializedName("cargo_weight")
    private Double cargoWeight;

    @SerializedName("receiver_lat")
    private Double receiverLat;

    @SerializedName("receiver_lng")
    private Double receiverLng;

    @SerializedName("receiver_phone")
    private String receiverPhone;

    @SerializedName("receiver_tel")
    private String receiverTel;

    private Double tips;

    private String info;

    @SerializedName("cargo_type")
    private Integer cargoType = 1;

    @SerializedName("cargo_num")
    private Integer cargoNum;

    @SerializedName("invoice_title")
    private String invoiceTitle;

    @SerializedName("origin_mark")
    private String originMark; // elm, mt, jdwl, bd

    @SerializedName("origin_mark_no")
    private String originMarkNo; // 订单来源编号




}
