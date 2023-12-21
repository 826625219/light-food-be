package com.qsj.qsjMain.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;

import java.io.Serializable;

/**
 * 积分优惠券
 */
@TableName(value = "integral_coupon")
@Data
public class IntegralCoupon implements Serializable {

    @TableId(type = IdType.INPUT)
    private String sn; // 券码实际编号

    private Integer integral; // 积分

    private Status status; // 券码状态, 0:未使用, 1:已使用, 2:已过期

    private String sourceId; // 来源关联id，如订单id

    private Long usedUserId; // 核销用户id

    private Long startTime; // 券码开始时间

    private Long endTime; // 券码结束时间

    private Long createShop; // 券码创建店铺id

    private Source couponSource; // 券码生成来源

    @TableField(fill = FieldFill.INSERT)
    private Long createTime; // 创建时间

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateTime; // 更新时间

    @Version
    @JsonIgnore
    private Integer version = 0; // 乐观锁

    public enum Status {

        OK(0, "未使用"),
        USED(1, "已使用"),
        EXPIRED(2, "过期")
        ;

        @EnumValue
        @JsonValue
        private final int statusCode;
        private final String statusDesc;

        Status(int statusCode, String statusDesc) {
            this.statusCode = statusCode;
            this.statusDesc = statusDesc;
        }
        @SuppressWarnings("unused")
        public int getStatusCode() {
            return statusCode;
        }
        @SuppressWarnings("unused")
        public String getStatusDesc() {
            return statusDesc;
        }

    }

    public enum Source {

        SELF(0, "自营"),
        MT(1, "美团"),
        ELE(2, "饿了么")
        ;

        @EnumValue
        @JsonValue
        private final int statusCode;
        private final String statusDesc;

        Source(int statusCode, String statusDesc) {
            this.statusCode = statusCode;
            this.statusDesc = statusDesc;
        }
        @SuppressWarnings("unused")
        public int getStatusCode() {
            return statusCode;
        }
        @SuppressWarnings("unused")
        public String getStatusDesc() {
            return statusDesc;
        }

    }
}
