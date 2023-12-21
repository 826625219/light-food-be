package com.qsj.qsjMain.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ShopFeLoginResultVO {
    private Long shopId;

    private String shopName;

    private String connectionKey;

    private Boolean success;
}
