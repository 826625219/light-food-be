package com.qsj.qsjMain.remote.service.model.vo;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class EWXUserInfoVO {
    @SerializedName("errcode")
    private Integer errCode;

    @SerializedName("errmsg")
    private String errMsg;

    @SerializedName("userid")
    private String userId;

    @SerializedName("name")
    private String name;

    @SerializedName("extattr")
    private ExtAttr extAttr;

    public String getAttr(String name) {
        if (extAttr == null || extAttr.getAttrs() == null) {
            return null;
        }
        for (Attr attr : extAttr.getAttrs()) {
            if (name.equals(attr.getName())) {
                return attr.getValue();
            }
        }
        return null;
    }

    @Data
    class ExtAttr {
        @SerializedName("attrs")
        private Attr[] attrs;
    }

    @Data
    class Attr {
        @SerializedName("name")
        private String name;

        @SerializedName("value")
        private String value;
    }
}
