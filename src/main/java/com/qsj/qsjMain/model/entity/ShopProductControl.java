package com.qsj.qsjMain.model.entity;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 显式控制商品在门店的状态
 */
@Data
@TableName(value = "shop_product_control")
public class ShopProductControl {
    private Long id; // 自增id
    private Long shopId; // 门店id
    private Long productId; // 商品id
    @ApiModelProperty("""
            店铺商品状态
                    OFF_SHELF(0, "下架"),
                    ON_SHELF(1, "上架"),
                    SOLD_OUT(2, "售罄")""")
    private Status status; // 商品状态, 0:下架, 1:上架, 2:售罄

    public enum Status {
        OFF_SHELF(0, "下架"),
        ON_SHELF(1, "上架"),
        SOLD_OUT(2, "售罄");

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
}
