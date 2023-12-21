package com.qsj.qsjMain.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 折扣优惠券
 */
@TableName(value = "discount_coupon")
@Data
public class DiscountCoupon {

    @TableId(type = IdType.INPUT)
    private String sn; // 券码sn

    private Long userId; // 关联的用户id

    private Long batchId; // 关联的券批次id
    @ApiModelProperty("""
            券码状态
                            OK(0, "未使用"),
                            USED(1, "已使用"),
                            EXPIRED(2, "过期"),
                            ALLOT(3, "已分配,未领取")""")
    private Status status; // 券码状态, 0:未使用, 1:已使用, 2:已过期

    private Long orderId; // 使用的订单id

    private Long receiveTime; // 领取时间

    private Long validStartTime; // 有效开始时间

    private Long validEndTime; // 有效结束时间

    private Long usedTime; // 使用时间

    @TableField(exist = false)
    private DiscountCouponRule rule; // 关联的券规则

    @Version
    @JsonIgnore
    private Integer version = 0; // 乐观锁

    public enum Status {

        OK(0, "未使用"),
        USED(1, "已使用"),
        EXPIRED(2, "过期"),
        ALLOT(3, "已分配,未领取")
        ;

        @EnumValue
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
