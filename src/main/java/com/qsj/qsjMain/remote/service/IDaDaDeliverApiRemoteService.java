package com.qsj.qsjMain.remote.service;

import com.qsj.qsjMain.remote.service.model.dto.AddTipDTO;
import com.qsj.qsjMain.remote.service.model.dto.DaDaCommonReq;
import com.qsj.qsjMain.remote.service.model.dto.QueryDeliveryFee;
import com.qsj.qsjMain.remote.service.model.dto.SubmitDeliveryOrder;
import com.qsj.qsjMain.remote.service.model.vo.DaDaCommonResp;
import com.qsj.qsjMain.remote.service.model.vo.QueryDeliveryFeeResult;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;


/**
 * wx retrofit service
 */
public interface IDaDaDeliverApiRemoteService {
    @POST("/api/order/queryDeliverFee")
    Call<DaDaCommonResp<QueryDeliveryFeeResult>> queryDeliverFee(@Body DaDaCommonReq<QueryDeliveryFee> body);

    @POST("/api/order/addAfterQuery")
    Call<DaDaCommonResp<String>> submitOrder(@Body DaDaCommonReq<SubmitDeliveryOrder> body);

    @POST("/api/order/addOrder")
    Call<DaDaCommonResp<QueryDeliveryFeeResult>> addOrder(@Body DaDaCommonReq<QueryDeliveryFee> body);

    @POST("/api/order/reAddOrder")
    Call<DaDaCommonResp<QueryDeliveryFeeResult>> reAddOrder(@Body DaDaCommonReq<QueryDeliveryFee> body);

    @POST("/api/order/addTip")
    Call<DaDaCommonResp<QueryDeliveryFeeResult>> addTip(@Body DaDaCommonReq<AddTipDTO> body);
}
