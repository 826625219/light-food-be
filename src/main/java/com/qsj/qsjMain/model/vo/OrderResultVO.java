package com.qsj.qsjMain.model.vo;
import com.qsj.qsjMain.model.entity.Order;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@ApiModel(value = "订单列表每一项VO")
public class OrderResultVO {

    @ApiModelProperty(value = "订单地址")
    private String userAddrName;

    @ApiModelProperty(value = "订单联系人")
    private String contactName;

    @ApiModelProperty(value = "订单关联电话")
    private String contactPhone;

    @ApiModelProperty(value = "订单时间")
    private Long orderTime;

    @ApiModelProperty(value = "配送类型")
    private Integer deliveryType;

    @ApiModelProperty(value = "自提码")
    private String pickUpNumber;

    @ApiModelProperty(value = "订单状态")
    private Integer orderStatus;

    @ApiModelProperty(value = "优惠的金额")
    private Integer discountAmount;

    @ApiModelProperty(value = "配送费")
    private Integer deliveryFee;

    @ApiModelProperty(value = "支付下单时间")
    private Long payTime;

    @ApiModelProperty(value = "菜品准备好的时间")
    private Long preparedTime;

    @ApiModelProperty(value = "完成时间")
    private Long finishTime;

    @ApiModelProperty(value = "订单总价")
    private Integer totalPrice;

    @ApiModelProperty(value = "订单ID")
    private String orderId;

    @ApiModelProperty(value = "订单物品列表")
    private List<OrderItemResultVO> orderItemResultVOList;

    @ApiModelProperty(value = "支付类型")
    private Order.PayType payType;

}
