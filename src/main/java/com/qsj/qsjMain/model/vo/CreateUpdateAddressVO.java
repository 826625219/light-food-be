package com.qsj.qsjMain.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@ApiModel("创建或更新地址VO")
public class CreateUpdateAddressVO {

    @ApiModelProperty("地址id,为空则创建，不为空则更新")
    private Long id; // 地址id

    @ApiModelProperty("收货人姓名")
    private String name; // 联系人称呼

    @ApiModelProperty("用户性别，0为男，1为女")
    private Integer gender; // 性别

    @ApiModelProperty("收货人电话")
    private String phone; // 电话

    @ApiModelProperty("详细地址, 如: 北京市海淀区中关村大街30号天安数码大厦，从拾图器上获得")
    private String addressDetail; // 收货人地址

    @ApiModelProperty("经度")
    private Double longitude; // 经度

    @ApiModelProperty("纬度")
    private Double latitude; // 纬度

    @ApiModelProperty("详细地址，门牌号，如 1号楼2单元301室")
    private String roomNumber; // 门牌号

    @ApiModelProperty("是否默认地址, 0:否, 1:是")
    private Boolean isDefault; // 是否默认地址, 0:否, 1:是

    @ApiModelProperty("地址类型, 0:家庭, 1:公司, 2:学校, 3:其他")
    private Integer addressTag; // 地址类型, 0:家庭, 1:公司, 2:学校, 3:其他
}
