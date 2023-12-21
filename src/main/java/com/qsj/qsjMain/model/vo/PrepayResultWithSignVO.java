package com.qsj.qsjMain.model.vo;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.qsj.qsjMain.remote.service.model.dto.PreOrderSignResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@AllArgsConstructor
public class PrepayResultWithSignVO {
    private String timeStamp;
    private String nonceStr;
    private String packageStr;
    private String signType;
    private String paySign;
    private PayStatus status;

    public static PrepayResultWithSignVO fail(PayStatus status) {
        return new PrepayResultWithSignVO(null, null, null, null, null,status);
    }

    public static PrepayResultWithSignVO preOrderSuccess(PreOrderSignResult result) {
        return new PrepayResultWithSignVO(result.getTimestamp(), result.getNonceStr(), result.getPackageStr(), result.getSignType(), result.getPaySign(), PayStatus.UNPAY);
    }

    public static PrepayResultWithSignVO orderSuccess() {
        return new PrepayResultWithSignVO(null, null, null, null, null,PayStatus.PAYED);
    }

    public enum PayStatus {
        PAYED(0, "已支付"),
        UNPAY(1, "成功待支付"),
        NOT_EXIST(2, "订单不存在"),
        ORDER_STATUS_ERROR(3, "订单状态错误"),

        BALANCE_NOT_ENOUGH(4, "余额不足"),

        INTEGRAL_NOT_ENOUGH(5, "积分不足"),
        DISCOUNT_COUPON_NOT_AVAILABLE(6, "优惠券不可用"),
        INVALID_AMOUNT(7, "无效的充值金额"),

        SHOP_NOT_OPEN(8, "店铺打烊了"),
        ;

        @EnumValue
        @JsonValue
        private final Integer code;
        private final String desc;

        PayStatus(Integer code, String desc) {
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
