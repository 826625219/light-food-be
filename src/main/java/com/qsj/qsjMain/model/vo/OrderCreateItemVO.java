package com.qsj.qsjMain.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("订单商品项")
public class OrderCreateItemVO {
    @ApiModelProperty("商品id")
    private Long productId; // 商品id

    @ApiModelProperty("商品数量")
    private Integer productQuantity; // 商品数量

    @ApiModelProperty("商品规格")
    private List<Integer> specIdx; // 商品规格
}
