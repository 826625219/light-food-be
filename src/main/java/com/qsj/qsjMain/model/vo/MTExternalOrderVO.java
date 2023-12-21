package com.qsj.qsjMain.model.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class MTExternalOrderVO {
    private Integer id;

    @JsonProperty("wm_poi_id")
    private Integer wmPoiId;

    @JsonProperty("wm_day_seq")
    private String wmDaySeq;

    @JsonProperty("wm_order_id_view")
    private String wmOrderIdView;

    @JsonProperty("total_after")
    private Double totalAfter;

    @JsonProperty("total_before")
    private Double totalBefore;

    @JsonProperty("shipping_fee")
    private Double shippingFee;

    private List<MTExternalOrderCartDetailVO> cartDetailVos;

}
