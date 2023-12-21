package com.qsj.qsjMain.remote.service.model.vo;

import lombok.Data;

@Data
public class QueryDeliveryFeeResult {
    private Double distance;

    private String deliveryNo;

    private Double fee;

    private Double deliverFee;

    private Double couponFee;

    private Double tips;

    private Double insuranceFee;

}
