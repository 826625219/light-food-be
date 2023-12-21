package com.qsj.qsjMain.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@TableName(value = "sale_activity")
@Data
public class SaleActivity {

    @TableId(type = IdType.AUTO)
    private Long id; // 自增id

    private String name; // 活动名称

    private String type; // 活动类型, STEP_RANK: 步数，BIRTH: 生日，NEW_USER: 新用户，RECHARGE: 充值，INVITE: 邀请，SIGN: 签到，SHARE: 分享，REWARD: 奖励，OTHER: 其他

    private Long startTime; // 活动开始时间

    private Long endTime; // 活动结束时间

    private Boolean enabled; // 活动是否启用

}
