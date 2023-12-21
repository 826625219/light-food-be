package com.qsj.qsjMain.remote.service.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 微信支付用于支付的响应预订单数据
 */
@Data
@AllArgsConstructor
@Accessors(chain = true)
public class PreOrderSignResult {
    private String timestamp;
    private String nonceStr;
    private String packageStr;
    private String signType;
    private String paySign;
}
