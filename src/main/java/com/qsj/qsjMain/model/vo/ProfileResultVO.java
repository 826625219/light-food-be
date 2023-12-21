package com.qsj.qsjMain.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@ApiModel(value = "用户信息VO")
public class ProfileResultVO {
    @ApiModelProperty(value = "用户生日，格式为yyyy-MM-dd")
    private String birthday;

    @ApiModelProperty(value = "用户id")
    private Long userId;
    @ApiModelProperty(value = "用户性别，0为男，1为女")
    private Integer gender;
    @ApiModelProperty(value = "用户昵称")
    private String nickName;
    @ApiModelProperty(value = "用户头像")
    private String avatarUrl;
    @ApiModelProperty(value = "用户状态")
    private Integer Status;
    @ApiModelProperty(value = "用户是否为vip")
    private Boolean isVip;
    @ApiModelProperty(value = "用户vip等级")
    private Integer vipLevel;
    @ApiModelProperty(value = "用户vip过期时间")
    private Long vipExpireTime;
    @ApiModelProperty(value = "用户积分")
    private Integer integral;
    @ApiModelProperty(value = "用户余额")
    private Integer balance;
    @ApiModelProperty(value = "用户绑定的营养计划id")
    private Long nutritionPlanId;

    @ApiModelProperty(value = "用户升高")
    private Integer height;

    @ApiModelProperty(value = "用户体重")
    private Integer weight;

    @ApiModelProperty(value = "用户运动强度")
    private Integer practise;

    @ApiModelProperty(value = "能量参考值")
    private Integer energyRef;
    @ApiModelProperty(value = "已有优惠券个数")
    private Long couponNum;
}
