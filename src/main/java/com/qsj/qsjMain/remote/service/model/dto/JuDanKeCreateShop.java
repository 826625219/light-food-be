package com.qsj.qsjMain.remote.service.model.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class JuDanKeCreateShop {

    @SerializedName("name")
    private String name;

    @SerializedName("phone")
    private String phone;

    @SerializedName("province")
    private String province;

    @SerializedName("district")
    private String district;

    @SerializedName("city")
    private String city;

    @SerializedName("address")
    private String address;

    @SerializedName("lng")
    private String lng;

    @SerializedName("lat")
    private String lat;

    @SerializedName("goods_type_id")
    private String goodsTypeId;

    @SerializedName("contact_person")
    private String contactPerson;

}
