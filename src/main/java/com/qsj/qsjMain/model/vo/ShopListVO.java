package com.qsj.qsjMain.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Data
@AllArgsConstructor
public class ShopListVO implements Comparable<ShopListVO>{

    private Long shopId;

    private String name; // 店铺名称

    private Double longitude; // 经度, 高德坐标

    private Double latitude; // 纬度，高德坐标

    private String manager; // 门店负责人姓名

    private String phone; // 门店电话

    private String addressDetail; // 详细地址

    private Integer status; // 门店, 0:未开业,1: 营业, 2:休息中, 3:已下线

    private String description; // 门店简介

    private Double distance; //距离用户的距离

    public ShopListVO() {

    }

    @Override
    public int compareTo(@NotNull ShopListVO o) {
        if (this.getStatus() < o.getStatus()) { //从大到小的营业状态1->0
            return 1;
        } else if (this.getStatus() > o.getStatus()) {
            return -1;
        } else {
            return this.distance.compareTo(o.getDistance());
        }
    }
}
