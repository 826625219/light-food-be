package com.qsj.qsjMain.model.vo;

import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("余额流水")
public class BalanceRecordVO {

    private Integer amount; //花费或者充值的数目

    private Long time;//时间

    private BalanceRecordStatus status;

//    @Override
//    public int compareTo(@NotNull BalanceRecordVO o) {
//        return Long.compare(o.getTime(), this.time);
//    }


    public enum BalanceRecordStatus {
        OUT(0, "消费"), IN(1, "充值"),
        ;

        @JsonValue
        private final Integer code;
        private final String desc;

        BalanceRecordStatus(Integer code, String desc) {
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
