package com.qsj.qsjMain.model.vo;

import com.qsj.qsjMain.model.entity.Order;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class SubmitOrderVO {

    @NotNull(message = "订单id不能为空")
    private String orderId; // 订单id

    @NotNull(message = "配送方式不能为空")
    private Order.DeliveryType deliveryType; // 配送方式

    private Long deliveryTime; // 预约配送时间, 配送方式为立刻配送时, 该字段为空, 配送方式为预约配送时, 该字段为预约时间

    private String useDiscountCouponId; // 使用优惠券id

    @NotNull(message = "使用积分不能为空")
    private Integer useIntegral; // 使用积分数量

    @NotNull(message = "支付方式不能为空")
    private Order.PayType payType; // 支付方式

    private String remark; // 订单备注

}
