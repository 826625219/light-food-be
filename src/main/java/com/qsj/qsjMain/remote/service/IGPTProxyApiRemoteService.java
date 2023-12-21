package com.qsj.qsjMain.remote.service;

import com.qsj.qsjMain.remote.service.model.dto.ChatMessageDTO;
import com.qsj.qsjMain.remote.service.model.vo.GptMessageResp;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;


/**
 * wx retrofit service
 */
public interface IGPTProxyApiRemoteService {
    @POST("/api/chat")
    Call<GptMessageResp> chat(@Body ChatMessageDTO dto);
}
