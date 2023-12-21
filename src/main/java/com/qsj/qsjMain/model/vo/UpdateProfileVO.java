package com.qsj.qsjMain.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "更新用户信息VO")
public class UpdateProfileVO {
    @ApiModelProperty(value = "用户生日，格式为yyyy-MM-dd")
    private String birthday;
    @ApiModelProperty(value = "用户性别，0为男，1为女")
    private Integer gender;

    private Integer height;

    private Integer weight;

    private Integer practise;

    private String nickName;

    private String avatarUrl;

}
