package com.qsj.qsjMain.remote.service.model.dto;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SendEWXRobotMessageText {

    private String content;

}
