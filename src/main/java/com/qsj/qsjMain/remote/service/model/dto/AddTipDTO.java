package com.qsj.qsjMain.remote.service.model.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class AddTipDTO {

    @SerializedName("order_id")
    private String orderId;

    private Float tips;
}
