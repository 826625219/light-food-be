package com.qsj.qsjMain.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@TableName(value = "user_address_book")
@Accessors(chain = true)
@Data
public class UserAddressBook implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id; // 自增id

    private Long userId; // 用户id

    private String name; // 联系人称呼

    private Integer gender; // 联系人性别

    private String phone; // 联系人电话

    private String addressDetail; // 详细地址

    private String roomNumber; // 门牌号

    private Boolean isDefault; // 是否默认地址, 0:否, 1:是

    private Integer addressTag; // 地址类型, 0:家庭, 1:公司, 2:学校, 3:其他

    private Double longitude; // 经度

    private Double latitude; // 纬度

    @TableField(fill = FieldFill.INSERT)
    private Long createTime; // 创建时间

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateTime; // 更新时间

}
