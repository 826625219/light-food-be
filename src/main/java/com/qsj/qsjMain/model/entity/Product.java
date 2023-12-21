package com.qsj.qsjMain.model.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.qsj.qsjMain.handler.MaterialHandler;
import com.qsj.qsjMain.handler.SpecOptionsObjectHandler;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 商品表
 */
@TableName(value = "product", autoResultMap = true)
@Data
public class Product implements Serializable {

    private Long id; // 自增id

    private String name; // 商品名称

    private String i18nName; // 商品国际化名称

    private String sku; // 商品sku

    private String category; // 商品分类

    private String description; // 商品描述

    private String i18nDescription; // 商品描述

    private String imageUrl; // 商品图片

    private String bannerUrl; // 商品banner

    private Integer price; // 商品价格

    private Integer cost; // 成本

    @JsonProperty("originPrice")
    private Integer originalPrice; // 商品原价

    private Integer status; // 状态, 0:正常, 1:禁用

    private String unit; // 商品单位

    private String spec; // 基本规格

    @TableField(typeHandler = SpecOptionsObjectHandler.class)
    private List<SpecGroup> specOptions; // 规格选项

    @TableField(typeHandler = MaterialHandler.class)
    private List<MaterialGroup> material; // 原料

    private String remark; // 备注

    private Integer energy; // 能量

    private Integer protein; // 蛋白质

    private Integer fat; // 脂肪

    private Integer saturatedFat; // 饱和脂肪

    private Integer nonSaturatedFat; // 不饱和脂肪

    private Integer fiber; // 纤维

    private String tag; // 标签

    @TableField(exist = false)
    private boolean recommend = false; // 是否推荐

    private Boolean qrShow; // 是否显示二维码营养数据

    @TableField(exist = false)
    @ApiModelProperty("""
            店铺商品状态
                    OFF_SHELF(0, "下架"),
                    ON_SHELF(1, "上架"),
                    SOLD_OUT(2, "售罄")""")
    private ShopProductControl.Status shopStatus = ShopProductControl.Status.ON_SHELF; // 店铺商品状态

    private Integer carbohydrate; // 碳水化合物

    @TableField(fill = FieldFill.INSERT)
    private Long createTime; // 创建时间

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateTime; // 更新时间



}
