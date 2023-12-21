package com.qsj.qsjMain.remote.service;

import com.qsj.qsjMain.remote.service.model.vo.EWXAccessTokenVO;
import retrofit2.Call;
import retrofit2.http.POST;

/**
 * ewx retrofit service
 */
public interface IEWXSecretApiRemoteService {

    @POST("/cgi-bin/gettoken")
    Call<EWXAccessTokenVO> getAccessToken();

}
