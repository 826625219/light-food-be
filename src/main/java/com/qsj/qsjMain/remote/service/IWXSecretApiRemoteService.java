package com.qsj.qsjMain.remote.service;

import com.qsj.qsjMain.remote.service.model.vo.WXAccessTokenVO;
import com.qsj.qsjMain.remote.service.model.vo.WXJSCode2SessionResp;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * wx retrofit service
 */
public interface IWXSecretApiRemoteService {

    @POST("/cgi-bin/token")
    Call<WXAccessTokenVO> getAccessToken();

    @GET("/sns/jscode2session")
    Call<WXJSCode2SessionResp> loginByCode(@Query("js_code") String jsCode);
}
