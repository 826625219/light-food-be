package com.qsj.qsjMain.remote.service;

import com.qsj.qsjMain.remote.service.model.dto.SendEWXRobotMessageDTO;
import com.qsj.qsjMain.remote.service.model.vo.WXAccessTokenVO;
import com.qsj.qsjMain.remote.service.model.vo.WXJSCode2SessionResp;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * wx retrofit service
 */
public interface IEWXRobotApiRemoteService {

    @POST("/cgi-bin/webhook/send")
    Call<ResponseBody> sendMessage(@Body SendEWXRobotMessageDTO dto);

}
