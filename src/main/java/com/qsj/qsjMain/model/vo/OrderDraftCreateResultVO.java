package com.qsj.qsjMain.model.vo;

import com.fasterxml.jackson.annotation.JsonValue;
import com.qsj.qsjMain.model.entity.DiscountCoupon;
import com.qsj.qsjMain.model.entity.Order;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDraftCreateResultVO {

    @ApiModelProperty("订单草稿")
    private Order order; // 订单草稿数据

    @ApiModelProperty("""
            订单草稿的提交结果，0为成功，其他都为失败
            USER_NOT_EXIST(10, "用户不存在"),
            SHOP_NOT_EXIST(11, "店铺不存在"),
            SHOP_NOT_OPEN(12, "店铺未开业"),
            ADDRESS_NOT_EXIST(22, "地址不存在"),
            ADDRESS_NOT_BELONG_TO_USER(23, "地址不属于当前用户"),
            PRODUCT_NOT_AVAILABLE(26, "商品不可购买"),
            PRODUCT_LIST_EMPTY(27, "商品列表为空")""")
    private Status status;

    @ApiModelProperty("可用的优惠券列表")
    private List<DiscountCoupon> coupons; // 可用优惠券列表

    @ApiModelProperty("全部可用积分")
    private Integer integral; // 全部可用积分

    public static OrderDraftCreateResultVO success(Order order, List<DiscountCoupon> coupons, Integer remainIntegral) {

        return new OrderDraftCreateResultVO(order, Status.SUCCESS, coupons, remainIntegral);
    }

    public static OrderDraftCreateResultVO fail(Status status) {
        return new OrderDraftCreateResultVO(null, status, new ArrayList<>(), null);
    }

    public enum Status {
        SUCCESS(0, "成功待支付"),
        USER_NOT_EXIST(10, "用户不存在"),
        SHOP_NOT_EXIST(11, "店铺不存在"),
        SHOP_NOT_OPEN(12, "店铺未开业"),

        DISTANCE_TOO_FAR(13, "配送距离过远"),
        ADDRESS_NOT_EXIST(22, "地址不存在"),
        ADDRESS_NOT_BELONG_TO_USER(23, "地址不属于当前用户"),
        PRODUCT_NOT_AVAILABLE(26, "商品不可购买"),
        PRODUCT_LIST_EMPTY(27, "商品列表为空"),

        STOP_DELIVERY(28, "外卖业务暂停");


        @JsonValue
        private final Integer code;
        private final String desc;

        Status(Integer code, String desc) {
            this.code = code;
            this.desc = desc;
        }
        @SuppressWarnings("unused")
        public Integer getCode() {
            return code;
        }
        @SuppressWarnings("unused")
        public String getDesc() {
            return desc;
        }
    }
}
