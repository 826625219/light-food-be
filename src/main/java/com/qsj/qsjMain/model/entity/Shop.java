package com.qsj.qsjMain.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.qsj.qsjMain.model.enums.ShopRunningStatus;
import lombok.Data;

@TableName(value = "shop")
@Data
public class Shop {

    @TableId(type = IdType.AUTO)
    private Long id; // 自增id

    private String name; // 店铺名称

    private Double longitude; // 经度, 高德坐标

    private Double latitude; // 纬度，高德坐标

    private String manager; // 门店负责人姓名

    private String phone; // 门店电话

    private String dadaShopNo; // 达达门店编号

    private Long mtPoiId; // 美团门店编号

    private String addressDetail; // 详细地址

    private Integer status; // 门店, 0:未开业,1: 营业, 2:休息中, 3:已下线

    private String description; // 门店简介

    private String credential; // 登录凭据

    private Integer stopDelivery;

    private Integer autoStatusLock;

    @TableField(fill = FieldFill.INSERT)
    private Long createTime; // 创建时间

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateTime; // 更新时间

    public boolean isRunning() {
        return this.getStatus() == ShopRunningStatus.RUNNING.getStatusCode();
    }
}
