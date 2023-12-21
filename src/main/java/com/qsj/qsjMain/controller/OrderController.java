package com.qsj.qsjMain.controller;

import com.qsj.qsjMain.exception.DadaDeliveryCallError;
import com.qsj.qsjMain.exception.OrderStatusException;
import com.qsj.qsjMain.model.Message;
import com.qsj.qsjMain.model.PaginationResp;
import com.qsj.qsjMain.model.ResultCode;
import com.qsj.qsjMain.model.entity.Order;
import com.qsj.qsjMain.model.vo.*;
import com.qsj.qsjMain.service.impl.OrderServiceImpl;
import com.qsj.qsjMain.utils.CredentialUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.io.IOException;

/**
 * @author wuweiliang
 * order controller
 */
@RestController
@RequestMapping("api/v1/order")
@Api(tags = "订单接口")
public class OrderController {

    private final OrderServiceImpl orderService;


    public OrderController(OrderServiceImpl orderService) {
        this.orderService = orderService;
    }

    @RequestMapping(value = "getOrder", method = RequestMethod.GET)
    @Deprecated
    @ApiOperation("按订单ID，获取单个订单信息")
    public Message<Order> getOrderStaled(@RequestAttribute @ApiIgnore Long userId, @RequestParam String orderId) {
        return Message.ok(orderService.getOrderById(orderId, userId));
    }

    @RequestMapping(value = "order", method = RequestMethod.GET)
    @ApiOperation("按订单ID，获取单个订单信息")
    public Message<OrderResultVO> getOrder(@RequestAttribute @ApiIgnore Long userId, @RequestParam String orderId) {
        return Message.ok(orderService.getOrderByUserOrderId(userId, orderId));
    }

    @RequestMapping(value = "orderStatus", method = RequestMethod.GET)
    @ApiOperation("按订单ID，刷新单个订单的信息")
    public Message<OrderStatusVO> refreshOrderStatus(@RequestAttribute @ApiIgnore Long userId, @RequestParam String orderId) {
        return Message.ok(orderService.getOrderStatusByOrderId(userId, orderId));
    }

    @RequestMapping(value = "orders", method = RequestMethod.GET)
    @ApiOperation("获取订单列表")
    public Message<PaginationResp<OrderResultVO>> getOrders(@RequestAttribute @ApiIgnore Long userId,
                                                            @RequestParam int page,
                                                            @RequestParam int pageSize) {
        return Message.ok(orderService.getOrdersByUserId(userId, page, pageSize));
    }

    @RequestMapping(value = "createDraft", method = RequestMethod.POST)
    @ApiOperation("创建订单草稿，返回预订单的详细信息")
    public Message<OrderDraftCreateResultVO> createOrderDraft(@RequestAttribute @ApiIgnore Long userId, @RequestBody CreateOrderDraftVO createOrderDraftVO) throws DadaDeliveryCallError {
        return Message.ok(orderService.createDraft(userId, createOrderDraftVO));
    }

    @RequestMapping(value = "submitOrder", method = RequestMethod.POST)
    @ApiOperation("提交订单，如果是微信支付，返回预支付信息；如果是余额支付，直接返回支付结果")
    public Message<PrepayResultWithSignVO> submitOrder(@RequestAttribute @ApiIgnore Long userId, @RequestBody SubmitOrderVO submitOrderVO) throws DadaDeliveryCallError {
        return Message.ok(orderService.submitOrder(userId, submitOrderVO));
    }

    @RequestMapping(value = "payNotify", method = RequestMethod.POST)
    @ApiOperation("微信支付回调")
    public Message<String> payNotify(@RequestBody String requestBody,
                                     @RequestHeader(name = "Wechatpay-Signature" ) String sign,
                                     @RequestHeader(name = "Wechatpay-Nonce" ) String nonce,
                                     @RequestHeader(name = "Wechatpay-Timestamp" ) String ts,
                                     @RequestHeader(name = "Wechatpay-Serial" ) String serial,
                                     @RequestHeader(name = "Wechatpay-Signature-Type",
                                             defaultValue = "WECHATPAY2-SHA256-RSA2048") String signType) throws DadaDeliveryCallError, OrderStatusException {
        orderService.payCallback(requestBody, sign, nonce, ts, serial, signType);
        return Message.ok();
    }

    @RequestMapping(value = "deliveryCallback", method = RequestMethod.POST)
    @ApiOperation("达达配送回调")
    public Message<String> deliveryCallback(@RequestBody DadaDeliveryCallbackVO vo) throws DadaDeliveryCallError {
        orderService.deliveryCallback(vo);
        return Message.ok();
    }

    @RequestMapping(value = "receiveQRCodeData", method = RequestMethod.POST)
    @ApiOperation("执行二维码扫描后逻辑")
    public Message<QRCodeResultVO> receiveQRCodeData(@RequestAttribute @ApiIgnore Long userId, @RequestBody QRCodeDataVO vo) {
        return Message.ok(orderService.receiveQRCodeData(userId, vo));
    }

    @RequestMapping(value = "payOrder", method = RequestMethod.POST)
    @ApiOperation("十五分钟内对待支付订单进行支付")
    public Message<PrepayResultWithSignVO> payOrder(@RequestAttribute @ApiIgnore Long userId,
                                                    @RequestBody PayOrderVO vo) throws DadaDeliveryCallError {
        return Message.ok(orderService.payOrder(userId, vo));
    }

    @RequestMapping(value = "dropOrder", method = RequestMethod.POST)
    @ApiOperation("十五分钟超过十五分钟不支付的order无效")
    public Message<Boolean> dropOrder(@RequestAttribute @ApiIgnore Long userId) {
        if(!CredentialUtils.isAdminRequest(userId)) {
            return Message.error(ResultCode.BAD_REQUEST);
        }
        return Message.ok(orderService.dropOrder());
    }

    @RequestMapping(value = "resetPickupNumber", method = RequestMethod.POST)
    @ApiOperation("每天清空所有门店的单")
    public Message<Boolean> resetPickupNumber(@RequestAttribute @ApiIgnore Long userId) {
        if (!CredentialUtils.isAdminRequest(userId)) {
            return Message.error(ResultCode.BAD_REQUEST);
        }
        return Message.ok(orderService.resetPickupNumber());
    }

    @RequestMapping(value = "checekDeliveryStatus", method = RequestMethod.GET)
    @ApiOperation("每分钟检查已支付的单是否配送状态符合预期")
    public Message<Boolean> checkDeliveryStatus(@RequestAttribute @ApiIgnore Long userId) throws IOException, DadaDeliveryCallError {
        if (!CredentialUtils.isAdminRequest(userId)) {
            return Message.error(ResultCode.BAD_REQUEST);
        }
        return Message.ok(orderService.checkDeliveryStatus());
    }

    @RequestMapping(value = "getHeatRecord", method = RequestMethod.POST)
    @ApiOperation("执行二维码扫描后逻辑")
    public Message<HeatRecordResultVO>getHeatRecord(@RequestAttribute @ApiIgnore Long userId,@RequestBody HeatRecordVO vo) {
        return Message.ok(orderService.getHeatRecord(userId,vo));
    }
}
