package com.qsj.qsjMain.model.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.qsj.qsjMain.utils.DigestUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DadaDeliveryCallbackVO {
    private String signature;

    @JsonProperty("client_id")
    private String clientId;

    @JsonProperty("order_id")
    private String orderId;

    @JsonProperty("order_status")
    private Integer orderStatus;

    @JsonProperty("cancel_reason")
    private String cancelReason;

    @JsonProperty("cancel_from")
    private Integer cancelFrom;

    @JsonProperty("dm_id")
    private Integer dmId;

    @JsonProperty("dm_name")
    private String dmName;

    @JsonProperty("dm_mobile")
    private String dmMobile;

    @JsonProperty("update_time")
    private Long updateTime;

    @JsonProperty("transporter_lng")
    private Double transporterLng;

    @JsonProperty("transporter_lat")
    private Double transporterLat;

    @JsonProperty("transporter_type")
    private Integer transporterType;

    @JsonProperty("finish_code")
    private String finishCode;


    public boolean verify() {
        List<String> valuesToSign = new ArrayList<>();
        valuesToSign.add(clientId == null ? "" : clientId);
        valuesToSign.add(orderId == null ? "" : orderId);
        valuesToSign.add(updateTime == null ? "" : updateTime.toString());
        Collections.sort(valuesToSign);
        String joinedFields = String.join("", valuesToSign);
        String sign = DigestUtils.md5(joinedFields);
        if (sign == null) {
            return false;
        }
        return sign.equals(signature);
    }

}
