package com.qsj.qsjMain.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 核心订单表
 */
@TableName(value = "user_order")
@Accessors(chain = true)
@Data
public class Order implements Serializable {

    @TableId(type = IdType.INPUT)
    private String id; // 订单编号

    @ApiModelProperty("用户id")
    private Long userId; // 用户id

    @ApiModelProperty("订单店铺id")
    private Long shopId; // 店铺id

    @ApiModelProperty("订单来源")
    private Source source;

    @ApiModelProperty("外部订单号")
    private String extOrderId; // 外部订单号

    @ApiModelProperty("关联使用的优惠券sn")
    private String discountCouponSn; // 关联优惠券SN

    @ApiModelProperty("订单使用的收货地址")
    private String address; // 收货地址

    @ApiModelProperty("订单收货地址门牌号")
    private String roomNumber; // 门牌号

    @ApiModelProperty("订单收货地址经度，不会存入数据库")
    private Double addressLongitude; // 收货地址经度

    @ApiModelProperty("订单收货地址纬度，不会存入数据库")
    private Double addressLatitude; // 收货地址纬度

    @ApiModelProperty("联系人姓名")
    private String contactName; // 联系人姓名

    @ApiModelProperty("联系人电话")
    private String contactPhone; // 联系人电话

    @ApiModelProperty("""
            订单状态
                             DRAFT(0, "草稿"),
                              UNPAY(1, "待支付"),
                              PAID(2, "已支付"),
                              ALLOCATED(3, "门店已接单"),
                              GOODS_READY(4, "商品已备齐"),
                              DELIVERING(5, "配送中"),
                              FINISH(6, "已完成"),
                              CANCEL(7, "已取消/关闭"),
                              AUTO_DELETE(8, "自动删除")""")
    private Status status; // 订单状态, 0:草稿, 1:待支付, 2:已支付, 3:已完成, 4:已取消/关闭, 5:自动删除

    @ApiModelProperty("订单是否已经被门店打印过")
    private Boolean isPrinted; // 是否已打印

    @ApiModelProperty(""" 
            配送方式
                    SELF(0, "自提"),
                    IMMEDIATELY(1, "立刻配送"),
                    APPOINTMENT(2, "预约配送")""")
    private DeliveryType deliveryType; // 配送方式, 0:自提, 1:立刻配送, 2:预约配送

    @ApiModelProperty("自提/堂食取餐码")
    private String pickupNumber; // 自提取餐码

    @ApiModelProperty("订单预约配送时间戳")
    private Long deliveryTime; // 预约配送时间

    @ApiModelProperty("""
            订单配送状态
                    INQUIRY(0, "已询价"),
                    WAIT(1, "已支付待配送"),
                    DELIVERING(2, "配送中"),
                    FINISH(3, "已完成"),
                    CANCEL(4, "已取消")""")
    private DeliveryStatus deliveryStatus; // 配送状态, 0:待配送, 1:配送中, 2:已完成, 3:已取消, 4:已询价

    private Integer deliveryDistance; // 配送距离, 单位:米

    @ApiModelProperty("订单配送费用")
    private Integer actualDeliveryFee; // 和平台结算的实际配送费

    @ApiModelProperty("""
            订单配送渠道
                    SELF(0, "自配送"),
                    DADA(1, "达达"),
                    ELEME(2, "饿了么"),
                    MEITUAN(3, "美团")""")
    private DeliveryPlatform deliveryPlatform; // 配送平台, 0:自配送, 1:达达, 2:饿了么, 3:美团

    @ApiModelProperty("订单配送渠道订单号")
    private String deliveryPreOrderId; // 配送平台预订单id

    @ApiModelProperty("""
            订单支付方式
                    WECHAT(0, "微信"),
                    BALANCE(1, "余额")""")
    private PayType payType; // 支付方式, 0:微信, 1:余额

    @ApiModelProperty("外部支付系统的支付凭据号")
    private String payOrderNo; // 支付订单号

    @ApiModelProperty("订单支付时间戳")
    private Long payTime; // 支付时间

    @ApiModelProperty("订单完成备餐的时间")
    private Long preparedTime;

    @ApiModelProperty("订单完成的时间点")
    private Long finishTime; // 完成时间

    @ApiModelProperty("订单取消的时间点")
    private Long cancelTime; // 取消时间

    @ApiModelProperty("订单取消的原因分类")
    private Integer cancelReasonType; // 取消原因分类

    @ApiModelProperty("订单取消的原因")
    private String cancelReason; // 取消原因

    @ApiModelProperty("订单总金额（不含运费）")
    private Integer totalAmount; // 订单总金额

    @ApiModelProperty("订单实际支付金额")
    private Integer payAmount; // 实际支付金额

    @ApiModelProperty("订单直接优惠金额")
    private Integer discountAmount; // 优惠金额

    @ApiModelProperty("订单优惠券优惠金额")
    private Integer couponAmount; // 优惠券抵扣金额

    @ApiModelProperty("预计积分使用的量")
    private Integer useIntegral; // 使用积分的量

    @ApiModelProperty("订单积分实际抵扣金额")
    private Integer integralAmount; // 实际使用积分的量

    @ApiModelProperty("和用户结算的运费金额，显示给用户")
    private Integer deliveryFeeFromUser; // 用户结算的运费金额

    @ApiModelProperty("余额使用的金额")
    private Integer balanceAmount; // 余额使用金额

    @ApiModelProperty("订单备注")
    private String remark; // 订单备注

    @TableField(fill = FieldFill.INSERT)
    private Long createTime; // 创建时间

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateTime; // 更新时间

    @Version
    @JsonIgnore
    private Integer version = 0; // 乐观锁

    public enum Status {
        DRAFT(0, "草稿"),
        UNPAY(1, "待支付"),
        PAID(2, "已支付"),
        ALLOCATED(3, "门店已接单"),
        GOODS_READY(4, "商品已备齐"),
        DELIVERING(5, "配送中"),
        FINISH(6, "已完成"),
        CANCEL(7, "已取消/关闭"),
        AUTO_DELETE(8, "自动删除");

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
    public enum DeliveryStatus {
        INQUIRY(0, "已询价"),
        WAIT(1, "已支付待接单"),
        WAIT_PICKUP(2, "已接单待取货"),
        COURIER_ARRIVED(100, "骑士已到店"),
        DELIVERING(3, "配送中"),
        FINISH(4, "已完成"),
        CANCEL(5, "已取消"),
        USER_REFUSE(6, "妥投异常之物品返回中"),
        USER_REFUSE_FINISH(7, "妥投异常之物品返回完成");

        @EnumValue
        @JsonValue
        private final Integer code;

        @SuppressWarnings({"unused", "FieldCanBeLocal"})
        private final String desc;

        DeliveryStatus(Integer code, String desc) {
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

    public enum DeliveryType {
        SELF(0, "自提"),
        IMMEDIATELY(1, "立刻配送"),
        APPOINTMENT(2, "预约配送");

        @EnumValue
        @JsonValue
        @SuppressWarnings({"unused", "FieldCanBeLocal"})
        private final Integer code;

        @SuppressWarnings({"unused", "FieldCanBeLocal"})
        private final String desc;

        DeliveryType(Integer code, String desc) {
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

    public enum DeliveryPlatform{
        SELF(0, "自配送"),
        DADA(1, "达达"),
        ELEME(2, "饿了么"),
        MEITUAN(3, "美团");

        @EnumValue
        @JsonValue
        private final Integer code;

        private final String desc;

        DeliveryPlatform(Integer code, String desc) {
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

    public enum PayType{
        WECHAT(0, "微信"),
        BALANCE(1, "余额");

        @EnumValue
        @JsonValue
        private final Integer code;

        private final String desc;

        PayType(Integer code, String desc) {
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
