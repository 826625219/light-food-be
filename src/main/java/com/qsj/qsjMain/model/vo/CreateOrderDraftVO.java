package com.qsj.qsjMain.model.vo;

import com.qsj.qsjMain.model.entity.Order;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@ApiModel("创建订单草稿")
public class CreateOrderDraftVO {

    @ApiModelProperty("订单关联的收货地址，对于非堂食订单来说是必须字段")
    @NotNull(message = "收货id不能为空")
    private Long deliveryAddressId; // 关联收货地址id

    @ApiModelProperty("订单类型")
    @NotNull(message = "订单类型不能为空")
    private Order.DeliveryType deliveryType; // 订单类型

    @ApiModelProperty("订单关联请求门店")
    @NotNull(message = "店铺id不能为空")
    private Long shopId; // 下订店铺id

    @ApiModelProperty("订单关联的商品")
    @NotNull(message = "订单商品不能为空")
    private List<OrderCreateItemVO> orderItems; // 订单商品列表

}
