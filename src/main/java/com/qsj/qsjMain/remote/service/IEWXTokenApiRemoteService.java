package com.qsj.qsjMain.remote.service;

import com.qsj.qsjMain.remote.service.model.vo.EWXAuthedUserVO;
import com.qsj.qsjMain.remote.service.model.vo.EWXUserInfoVO;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * wx retrofit service
 */
public interface IEWXTokenApiRemoteService {
    @GET("/cgi-bin/user/get")
    Call<EWXUserInfoVO> getUserInfo(@Query("userid") String userId);

    @GET("/cgi-bin/auth/getuserinfo")
    Call<EWXAuthedUserVO> getAuthedUser(@Query("code") String code);
}
