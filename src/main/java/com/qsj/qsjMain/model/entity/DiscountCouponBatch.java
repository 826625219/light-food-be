package com.qsj.qsjMain.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;

@TableName(value = "discount_coupon_batch", autoResultMap = true)
@Data
public class DiscountCouponBatch {
    @TableId(type = IdType.INPUT)
    private String batchKey; // 券批次名称key，用于唯一标识券批次，可以是有意义的前缀+时间戳

    private String batchName; // 券批次名称

    private String couponName; // 券名称

    private CouponType couponType; // 券类型, 0:满减, 1:折扣，2: 指定商品券

    @TableField(typeHandler = JacksonTypeHandler.class)
    private DiscountCouponRule discountCouponRule; // 券规则

    private Integer total; // 券总数

    private Integer sent; // 已发放券数

    private Integer used; // 已使用券数

    private String snPrefix; // 券码前缀

    private Boolean permanent; // 是否永久有效

    private Long startTime; // 券码开始领取时间

    private Long endTime; // 券码结束领取时间

    public enum CouponType {
        FULL_REDUCTION(0, "满减券"),
        DISCOUNT(1, "折扣券"),
        SPECIFIED_GOODS(2, "指定商品券");

        @EnumValue
        @JsonValue
        private final Integer code;

        private final String desc;

        CouponType(Integer code, String desc) {
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
