package com.qsj.qsjMain.model.vo;

import lombok.Data;

import java.util.List;

@Data
public class MTExternalOrderCartDetailVO {
    private String cartName;

    private Double cartAmount;

    private List<MTExternalOrderCartItemDetailVO> details;
    
}
