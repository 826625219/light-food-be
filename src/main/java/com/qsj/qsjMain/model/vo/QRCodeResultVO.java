package com.qsj.qsjMain.model.vo;

import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "二维码扫描结果VO")
public class QRCodeResultVO {
    private Boolean success;
    @ApiModelProperty("""
            券码兑换状态
                            SUCCESS(0, "成功兑换"),
                            ALREADY_USED(1, "该券码已经兑换过"),
                            EXPIRED(2, "该券码已经过期"),
                            NOT_FOUND(3, "该券码不存在")""")
    private CouponExchangeStatus status;

    @ApiModelProperty("如果获得了积分，具体获得积分的量")
    private Integer gainIntegral = 0;

    private Boolean qrShow;

    @ApiModelProperty("订单的总营养量")
    private NutritionDataVO orderNutrition;

    @ApiModelProperty("订单扫描物品的营养量")
    private NutritionDataWithItemVO currentItemNutrition;

    @ApiModelProperty("订单扫描各个详细物品的营养量")
    private List<NutritionDataWithItemVO> orderItemNutrition;

    public static QRCodeResultVO fail() {
        QRCodeResultVO result = new QRCodeResultVO();
        result.setSuccess(false);
        return result;
    }

    public enum CouponExchangeStatus {
        SUCCESS(0, "成功兑换"),
        ALREADY_USED(1, "该券码已经兑换过"),
        EXPIRED(2, "该券码已经过期"),
        NOT_FOUND(3, "该券码不存在")
        ;

        @JsonValue
        private final Integer code;
        private final String desc;
        CouponExchangeStatus(Integer code, String desc) {
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
