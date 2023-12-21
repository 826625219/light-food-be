package com.qsj.qsjMain.remote.service;

import com.qsj.qsjMain.remote.service.model.dto.JuDanKeCommonReq;
import com.qsj.qsjMain.remote.service.model.dto.JuDanKeCreateShop;
import com.qsj.qsjMain.remote.service.model.dto.JuDanKeQueryDeliveryFee;
import com.qsj.qsjMain.remote.service.model.vo.JuDanKeAddOrderResult;
import com.qsj.qsjMain.remote.service.model.vo.JuDanKeCommonResp;
import com.qsj.qsjMain.remote.service.model.vo.JuDanKeCreateShopResult;
import com.qsj.qsjMain.remote.service.model.vo.JuDanKeQueryDeliveryFeeResult;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface IJuDanKeDeliveryRemoteService {

    //发单
    @POST("/Order/publish")
    Call<JuDanKeCommonResp<JuDanKeAddOrderResult>> addOrder(@Body JuDanKeCommonReq<JuDanKeQueryDeliveryFee> body);

    //查价
    @POST("/Order/quotation")
    Call<JuDanKeCommonResp<JuDanKeQueryDeliveryFeeResult>> queryDeliverFee(@Body JuDanKeCommonReq<JuDanKeQueryDeliveryFee> body);

    //创店
    @POST("/Shop/create")
    Call<JuDanKeCommonResp<JuDanKeCreateShopResult>> createShop(@Body JuDanKeCommonReq<JuDanKeCreateShop> body);
}
