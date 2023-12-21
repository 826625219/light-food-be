package com.qsj.qsjMain.model.enums;

public enum IntegralCouponStatus {

    OK(0, "未使用"),
    USED(1, "已使用"),
    EXPIRED(2, "过期")
    ;


    private final int statusCode;
    private final String statusDesc;

    IntegralCouponStatus(int statusCode, String statusDesc) {
        this.statusCode = statusCode;
        this.statusDesc = statusDesc;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getStatusDesc() {
        return statusDesc;
    }


}
