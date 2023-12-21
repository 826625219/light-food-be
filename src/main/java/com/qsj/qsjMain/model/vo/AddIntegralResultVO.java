package com.qsj.qsjMain.model.vo;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddIntegralResultVO {
    private Status status; // 积分券兑换结果, 具体参考enum OrderSubmitResultEnum
    private Integer integral; // 兑换得到的积分

    public static AddIntegralResultVO fail(Status status) {
        return new AddIntegralResultVO(status, 0);
    }

    public enum Status {

        OK(0, "兑换成功"),
        USED(1, "已使用"),
        EXPIRED(2, "过期"),
        NOT_EXIST(3, "券码不存在"),
        ;

        @JsonValue
        private final int statusCode;
        private final String statusDesc;

        Status(int statusCode, String statusDesc) {
            this.statusCode = statusCode;
            this.statusDesc = statusDesc;
        }

        @SuppressWarnings("unused")
        public int getStatusCode() {
            return statusCode;
        }
        @SuppressWarnings("unused")
        public String getStatusDesc() {
            return statusDesc;
        }


    }

}
