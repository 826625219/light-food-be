package com.qsj.qsjMain.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@TableName(value = "recharge_order")
@Accessors(chain = true)
@Data
public class RechargeOrder {

    @TableId(type = IdType.INPUT)
    private String orderId;

    private Long userId; // 充值的用户id

    private Integer amountPay; // 实际支付金额, 单位分

    private Integer amountCharge;  // 充值得到的余额, 单位分

    private OrderType rechargeType; // 充值类型

    private OrderStatus orderStatus;

    @Version
    private Integer version;
    @TableField(fill = FieldFill.INSERT)
    private Long createTime; // 创建时间

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateTime; // 更新时间

    public enum OrderStatus {
        UNPAY(1, "待支付"),
        PAID(2, "已支付"),
        ;
        @EnumValue
        @JsonValue
        private final Integer code;

        private final String desc;

        OrderStatus(Integer code, String desc) {
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

    public enum OrderType {
        //充值类型 0:vip 1:增值 2:纯充值
        VIP(0, "vip开通"),
        VIP_INCREASE(1, "vip加量包服务"),
        BALANCE(2, "余额充值"),
        ;
        @EnumValue
        @JsonValue
        private final Integer code;

        private final String desc;

        OrderType(Integer code, String desc) {
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

    @TableField(exist = false)
    public final static List<Integer> AMOUNTS = List.of(10000, 20000, 30000, 50000); // 用于控制用户的充值档位金额

    /**
     * 将用户充值的金额转化为充值得到的余额
     *
     * @param amount 用户充值的金额
     * @return 充值得到的余额
     */
    public static Integer mapAmountToCharge(Integer amount) {
        if (amount == null) {
            return null;
        }
        if (amount == 10000) {
            return 11000;
        }
        if (amount == 20000) {
            return 22000;
        }
        if (amount == 30000) {
            return 33000;
        }
        if (amount == 50000) {
            return 55000;
        }
        return null;
    }
}
