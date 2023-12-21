package com.qsj.qsjMain.remote.service;

import com.qsj.qsjMain.remote.service.model.dto.GetWXCodeUnLimitDTO;

import com.qsj.qsjMain.remote.service.model.dto.SendCSMessageDTO;
import com.qsj.qsjMain.remote.service.model.dto.SendNotifyMessageDTO;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

/**
 * wx retrofit service
 */
public interface IWXTokenApiRemoteService {
    @POST("/wxa/getwxacodeunlimit")
    Call<ResponseBody> getWXACodeUnLimit(@Body GetWXCodeUnLimitDTO dto);

    @POST("/cgi-bin/message/subscribe/send")
    Call<ResponseBody> sendNotifyMessage(@Body SendNotifyMessageDTO dto);

    @POST("/cgi-bin/message/custom/send")
    Call<ResponseBody> sendCSMessage(@Body SendCSMessageDTO dto);

}
