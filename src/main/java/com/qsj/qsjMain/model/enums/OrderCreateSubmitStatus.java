package com.qsj.qsjMain.model.enums;

/**
 */
public enum OrderCreateSubmitStatus {
    UNPAY(0, "成功待支付"),
    PAYED(1, "成功已支付"),
    USER_NOT_EXIST(10, "用户不存在"),
    SHOP_NOT_EXIST(11, "店铺不存在"),
    SHOP_NOT_OPEN(12, "店铺未开业"),
    INTEGRAL_NOT_ENOUGH(20, "积分不足"),
    DISCOUNT_COUPON_NOT_AVAILABLE(21, "优惠券不可用"),
    ADDRESS_NOT_EXIST(22, "地址不存在"),
    ADDRESS_NOT_BELONG_TO_USER(23, "地址不属于当前用户"),
    COUPON_CONFLICT(24, "优惠券冲突，积分不可与优惠券一起使用"),
    BALANCE_NOT_ENOUGH(25, "余额不足"),
    PRODUCT_NOT_AVAILABLE(26, "商品不可购买"),
    PRODUCT_LIST_EMPTY(27, "商品列表为空"),
    ;


    private final int statusCode;
    private final String statusDesc;

    OrderCreateSubmitStatus(int statusCode, String statusDesc) {
        this.statusCode = statusCode;
        this.statusDesc = statusDesc;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

}
