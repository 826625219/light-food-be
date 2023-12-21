package com.qsj.qsjMain.remote.service.model.vo;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class JuDanKeQueryDeliveryFeeResult {

    List<brandMsg> list;

    @SerializedName("expect_pickup_time")
    String expectPickupTime;

    @SerializedName("expect_finish_time")
    String expectFinishTime;

    public class brandMsg {
        private String brand;

        private String totalFee;

        private String distance;

        private String selected;
    }


}
