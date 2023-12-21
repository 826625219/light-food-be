package com.qsj.qsjMain.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class ShopUnprocessedOrderVO {
    private String orderId;
    private String orderTime;
    private Boolean isPrinted;
    private String remark;
    private String customerName;
    private String phoneSuffix;
    private String customerAddr;
    private String pickUpNumber;
    private String extOrderId;
    private Integer source;
    private Integer deliveryType;
    private List<ShopOrderItemVO> items;
}
