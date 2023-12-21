package com.qsj.qsjMain.remote.service.model.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class GetWXCodeUnLimitDTO {
    private String scene = ""; // 二维码携带的参数,卷码id和积分数，订单Id
    private Integer width = 1280; // 二维码宽度

    private String page = "pages/qrShow/qrShow";

    @SerializedName("check_path")
    private Boolean checkPath = false;


    @SerializedName("is_hyaline")
    private Boolean isHyaline = true; // 是否需要透明底色

    @SerializedName("env_version")
    private String envVersion = "trial"; // 版本环境
}
