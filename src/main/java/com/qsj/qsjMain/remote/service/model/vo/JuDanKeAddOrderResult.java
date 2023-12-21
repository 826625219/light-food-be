package com.qsj.qsjMain.remote.service.model.vo;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class JuDanKeAddOrderResult {

    @SerializedName("order_id")
    String juDanKeDeliveryOrderId;
}
