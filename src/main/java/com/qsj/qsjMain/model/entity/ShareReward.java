package com.qsj.qsjMain.model.entity;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@TableName(value = "share_reward")
@Data
public class ShareReward {
    @TableId(type = IdType.AUTO)
    private Long id; // 自增id

    @ApiModelProperty("用户id")
    private Long shareUserId; // 用户id

    @ApiModelProperty("被分享用户id")
    private Long sharedUserId; // 用户id

    @ApiModelProperty("用户下单总金额")
    private Integer totalAmount; // 用户下单总金额 单位:分

    @ApiModelProperty("被分享用户的状态")
    private Status status; // 用户id

    public enum Status {
        Invite(0, "邀请未下单"),
        First_Order(1, "首次下单"),
        Reward_OK(2, "满足返现"),
        Rewarded(3, "已返现");

        @EnumValue
        @JsonValue
        private final Integer code;

        private final String desc;

        Status(Integer code, String desc) {
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
