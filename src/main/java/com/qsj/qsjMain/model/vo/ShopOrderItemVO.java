package com.qsj.qsjMain.model.vo;

import lombok.Data;

@Data
public class ShopOrderItemVO {
    private Long id;

    private Long productId;

    private String name;

    private Integer count;

    private String spec;

    private Integer status;

    private String qrCode;
}
