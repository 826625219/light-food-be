package com.qsj.qsjMain.model.entity;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("优惠券的使用规则")
@Data
public class DiscountCouponRule {

    @ApiModelProperty("使用门槛类型, 0:满减, 1:折扣，2：指定商品券")
    private ConditionType conditionType; // 使用门槛类型, 0:满减, 1:折扣，2：指定商品券

    @ApiModelProperty("使用门槛值，如果是满减/折扣，就是订单商品的价格总和")
    private Integer conditionValue; // 使用门槛值

    @ApiModelProperty("扣减值, 满减为金额数值, 折扣为折扣率，如5折则为50")
    private Integer value; // 扣减值

    @ApiModelProperty("商品id，如果是指定商品券，需要指定这个字段")
    private Long commodityId; // 商品id，如果是指定商品券，需要指定这个字段

    @ApiModelProperty("预留，是否是独占券，独占券只能使用一张，非独占券可以使用多张")
    private Boolean isExclusive; // 预留，是否是独占券，独占券只能使用一张，非独占券可以使用多张

    @ApiModelProperty("预留，是否是累加券，累加券可以和其他券一起使用，非累加券只能单独使用")
    private Boolean isCumulative; // 预留，是否是累加券，累加券可以和其他券一起使用，非累加券只能单独使用

    @ApiModelProperty("券有效时间类型，0:固定时间，1:领取后多少天")
    private Integer validDurationType; // 券有效时间类型，0:固定时间，1:领取后多少天

    @ApiModelProperty("券有效时间值，如果是固定时间，这个字段是结束时间，如果是领取后多少天，这个字段是天数对应的秒数")
    private Long validDateValue; // 券有效时间值，如果是固定时间，这个字段是结束时间，如果是领取后多少天，这个字段是天数对应的秒数

    public enum ConditionType {

        FULL_REDUCTION(0, "满减"),
        DISCOUNT(1, "折扣"),
        SPECIFIED_GOODS(2, "指定商品券");

        @EnumValue
        @JsonValue
        private final Integer code;

        private final String desc;

        ConditionType(Integer code, String desc) {
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
