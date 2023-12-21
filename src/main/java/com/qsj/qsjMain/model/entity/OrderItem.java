package com.qsj.qsjMain.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 订单商品表
 */
@TableName(value = "order_item")
@Accessors(chain = true)
@Data
public class OrderItem implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id; // 自增id

    private String orderId; // 订单id

    private Long productId; // 商品id

    private String productName; // 商品名称

    private String productI18nName; // 商品国际化名称

    private String productI18nSpec; // 商品国际化名称

    private String productImageUrl; // 商品图片

    private Integer productOriginalPrice; // 商品价格

    private Integer productActualPrice; // 商品价格

    private Integer productQuantity; // 商品数量

    private String productUnit; // 商品单位

    private Integer status; // 状态, 0:正常, 1:退货

    private Integer prodStatus; // 生产状态, 0:未制作, 1:已制作

    private Integer cost; // 成本

    private String spec; // 规格

    private String qrCode; // 用户使用的二维码

    private Integer scanedQr; // 1:扫过了


    @TableField(fill = FieldFill.INSERT)
    private Long createTime; // 创建时间

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateTime; // 更新时间

    @Version
    @JsonIgnore
    private Integer version = 0; // 乐观锁

}
