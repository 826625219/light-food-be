package com.qsj.qsjMain.model.vo;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;

@Data
public class HeatRecordVO {
    private Long week;
    public enum Week {
        THIS_WEEk(0, "本周"),
        Last_WEEK(1, "上周"),
        Last_TWO_WEEK(2, "前周"),
        Last_THREE_WEEK(3, "三个星期前");

        @JsonValue
        private final Integer code;
        private final String desc;

        Week(Integer code, String desc) {
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
