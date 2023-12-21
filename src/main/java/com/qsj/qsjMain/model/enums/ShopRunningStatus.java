package com.qsj.qsjMain.model.enums;

/**
 */
public enum ShopRunningStatus {

    NOT_OPEN(0, "未开业"),
    RUNNING(1, "营业中"),
    REST(2, "休息"),
    OFFLINE(3, "停业"),
    ;


    private final int statusCode;
    private final String statusDesc;

    ShopRunningStatus(int statusCode, String statusDesc) {
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
