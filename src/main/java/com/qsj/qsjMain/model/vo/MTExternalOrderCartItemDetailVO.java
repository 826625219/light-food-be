package com.qsj.qsjMain.model.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MTExternalOrderCartItemDetailVO {

    @JsonProperty("wm_food_id")
    private String wmFoodId;

    @JsonProperty("food_name")
    private String foodName;

    @JsonProperty("food_price")
    private Double foodPrice;

    private Integer count;

    private String unit;


}
