package com.qsj.qsjMain.remote.service.model.vo;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class NotifyDataVO {
    public static class NotifyValue{
        private String value;

        public NotifyValue(String value){
            this.value = value;
        }
    }

    @SerializedName("character_string4")
    private  NotifyValue orderNumber;
    private  NotifyValue thing11 = new NotifyValue("您的餐品已制作完毕，请前往吧台取餐");

}
