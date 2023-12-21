package com.qsj.qsjMain.model.vo;

import com.qsj.qsjMain.utils.DigestUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Objects;

@Data
@ApiModel(value = "二维码扫描提交请求VO")
public class QRCodeDataVO {

    @ApiModelProperty("扫描的物品的子id")
    private String orderItemId;

    @ApiModelProperty("兑换的券码id")
    private String couponSn;

    @ApiModelProperty("签名")
    private String sign;

    public boolean validateSign() {
        String seq = "QSJ_BOUND" + orderItemId + "QSJ_BOUND" + couponSn;
        String calculatedSign = DigestUtils.md5(seq);
        return calculatedSign.substring(0, 2).equals(sign);
    }

    public void genSign() {
        String seq = "QSJ_BOUND" + orderItemId + "QSJ_BOUND" + couponSn;
        sign = Objects.requireNonNull(DigestUtils.md5(seq)).substring(0, 2);
    }

}
