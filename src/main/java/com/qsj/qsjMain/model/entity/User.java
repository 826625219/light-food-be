package com.qsj.qsjMain.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@TableName(value = "user")
@Accessors(chain = true)
@Data
public class User implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id; // 自增id

    private String wxOpenId; // 微信openId

    private String wxUnionId; // 微信unionId

    private String wxSessionKey; // 微信sessionKey

    private String wxNickName; // 微信昵称

    private String wxAvatarUrl; // 微信头像

    private Integer status; // 用户状态, 0:正常, 1:禁用

    private Long lastLoginTime; // 最后登录时间

    private String lastLoginIp; // 最后登录ip

    private String lastLoginDevice; // 最后登录设备

    private Long nutritionPlanId; // 关联营养计划id

    private Boolean hasFilledHealthInfo; // 是否填写过健康信息, 0:否, 1:是

    private String phone; // 手机号

    private String birthday; // 生日

    private Integer gender; // 性别

    private Boolean isVip; // 是否是vip

    private Long vipEndTime; // vip结束时间

    private Integer vipLevel; // 会员等级

    private Integer integral; // 积分

    private Integer balance; // 余额, 单位:分

    private Integer height; // 身高, 单位:cm

    private Integer weight; // 体重, 单位:kg

    private Integer practise; // 运动量, 0:无, 1:低, 2:中, 3: 高

    private String credentialSalt; // 凭据盐，自动生成，修改后用户凭据自动失效

    @TableField(fill = FieldFill.INSERT)
    private Long createTime; // 创建时间

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateTime; // 更新时间
    @Version
    @JsonIgnore
    private Integer version = 0; // 乐观锁

    public String getMaskedNickName() {
        if (wxNickName == null) {
            return "";
        }
        if (wxNickName.length() == 1) {
            return wxNickName;
        } else if (wxNickName.length() == 2) {
            return wxNickName.charAt(0) + "*";
        } else if (wxNickName.length() == 3) {
            return wxNickName.charAt(0) + "*" + wxNickName.charAt(2);
        } else {
            return wxNickName.charAt(0) + "**" + wxNickName.substring(wxNickName.length() - 1);
        }
    }

    public Integer getEnergyRef() {
        Integer age = getAgeByBirth();
        int height = Optional.ofNullable(this.height).orElse(170);
        int weight = Optional.ofNullable(this.weight).orElse(60);
        boolean isMale = Optional.ofNullable(this.gender).orElse(0) == 0;
        Integer intensity = Optional.ofNullable(this.practise).orElse(0);
        double ratio = switch (intensity) {
            case 1 -> 1.375;
            case 2 -> 1.55;
            case 3 -> 1.725;
            default -> 1.2;
        };

        if (isMale) {
            return (int) ((66 + 13.7 * weight + 5 * height - 6.8 * age) * ratio);
        } else {
            return (int) ((655 + 9.6 * weight + 1.8 * height - 4.7 * age) * ratio);
        }

    }

    public Integer getAgeByBirth() {
        String birthStr = this.birthday;
        if(birthStr == null || birthStr.equals("")){
            return 30;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date birthDay;
        try{
            birthDay = sdf.parse(birthStr);
        }
        catch (ParseException e){
            return 30;
        }

        Calendar cal = Calendar.getInstance();

        if (cal.before(birthDay)) {
            throw new IllegalArgumentException(
                    "The birthDay is before Now.It's unbelievable!");
        }
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH);
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
        cal.setTime(birthDay);

        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

        int age = yearNow - yearBirth;

        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                //monthNow==monthBirth
                if (dayOfMonthNow < dayOfMonthBirth) age--;
            } else {
                //monthNow>monthBirth
                age--;
            }
        }
        return age;
    }
}

