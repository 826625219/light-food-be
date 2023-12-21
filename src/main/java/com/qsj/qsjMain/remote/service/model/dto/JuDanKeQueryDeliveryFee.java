package com.qsj.qsjMain.remote.service.model.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class JuDanKeQueryDeliveryFee {
    @SerializedName("shop_id")
    private String shopId;

    @SerializedName("third_order_no")
    private String thirdOrderNo;

    @SerializedName("sender_address")
    private String senderAddress;


    @SerializedName("receiver_name")
    private String receiverName;

    @SerializedName("receiver_phone")
    private String receiverPhone;

    @SerializedName("receiver_province")
    private String receiverProvince;

    @SerializedName("receiver_city")
    private String receiverCity;

    @SerializedName("receiver_district")
    private String receiverDistrict;

    @SerializedName("receiver_address")
    private String receiverAddress;


    @SerializedName("goods_weight")
    private Integer goodsWeight; // 单位 : 克
   

    @SerializedName("goods_price")
    private Integer goodsPrice; // 单位 : 分

    @SerializedName("goods_quantity")
    private Integer goodsQuantity;

    @SerializedName("goods_type_id")
    private Integer goodsTypeId;

    @SerializedName("receiver_lat")
    private String receiverLat;

    @SerializedName("receiver_lng")
    private String receiverLng;

    @SerializedName("delivery_remark")
    private String deliveryRemark;

    @SerializedName("delivery_brands")
    private List deliveryBrands;

}
