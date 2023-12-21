package com.qsj.qsjMain.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qsj.qsjMain.exception.DadaDeliveryCallError;
import com.qsj.qsjMain.model.entity.Order;
import com.qsj.qsjMain.model.vo.*;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

/**
 *
 */
public interface OrderService extends IService<Order> {

    /**
     * 创建外卖订单草稿
     *
     * @param createOrderDraftVO 订单提交信息
     * @return 提交结果
     */
    @Transactional(rollbackFor = Exception.class)
    OrderDraftCreateResultVO createDraft(Long userId, CreateOrderDraftVO createOrderDraftVO) throws DadaDeliveryCallError;

    /**
     * 提交订单
     */
    @Transactional(rollbackFor = Exception.class)
    PrepayResultWithSignVO submitOrder(Long userId, SubmitOrderVO orderSubmitVO) throws InvalidKeyException, NoSuchAlgorithmException, SignatureException, DadaDeliveryCallError;


    PrepayResultWithSignVO payOrder(Long userId, PayOrderVO vo) throws DadaDeliveryCallError;

    /**
     * 测试获得access_token
     *
     * @return access token的值
     */
    String getQRCode(String couponSn, Long itemId, String sign) throws IOException;

    Boolean reportMtOrder(MTExternalOrderVO vo);

    QRCodeResultVO receiveQRCodeData(Long userId, QRCodeDataVO data);

     boolean dropOrder();

    Boolean checkDeliveryStatus() throws IOException, DadaDeliveryCallError;

    HeatRecordResultVO getHeatRecord(Long userId,HeatRecordVO vo);

}
