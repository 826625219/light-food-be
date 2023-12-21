package com.qsj.qsjMain.model.entity;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;


@TableName(value = "sku_mapping")
@Data
public class SkuMapping {
    private Long id; // 自增id

    private Long shopId; // 店铺id

    private Long itemId; // 商品id

    private Long mtSkuId; // 美团商品id

    private Long eleSkuId; // ele商品id

    public enum MapType {

        MT(0, "美团"),
        ELE(1, "饿了么")
        ;

        @EnumValue
        @JsonValue
        private final Integer code;
        private final String desc;

        MapType(Integer code, String desc) {
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
