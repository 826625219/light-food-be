package com.qsj.qsjMain.controller;

import com.qsj.qsjMain.model.Message;
import com.qsj.qsjMain.model.vo.ShopFeLoginResultVO;
import com.qsj.qsjMain.model.vo.ShopUnprocessedOrderVO;
import com.qsj.qsjMain.service.impl.ShopFeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * 门店fe服务控制器
 */
@RestController
@RequestMapping("api/v1/shop")
@CrossOrigin(origins =
        {
                "https://dev.shop-fe.lightmeal-service.com",
                "https://shop-fe.lightmeal-service.com"})
@Api(tags = "门店fe对接接口")
public class ShopController {
    private final ShopFeService shopFeService;

    public ShopController(ShopFeService shopFeService) {
        this.shopFeService = shopFeService;
    }

//    @RequestMapping(value = "/login", method = RequestMethod.GET)
//    @ApiOperation("门店登录")
//    public Message<ShopFeLoginResultVO> shopLogin(@RequestParam String credential) {
//        return Message.ok(shopFeService.feLogin(credential));
//    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    @ApiOperation("门店登录")
    public Message<ShopFeLoginResultVO> shopLogin(@RequestParam String code, @RequestParam Long targetShopId) throws IOException {
        return Message.ok(shopFeService.feLogin(code, targetShopId));
    }

    @RequestMapping(value = "/orders", method = RequestMethod.GET)
    @ApiOperation("获取门店未处理订单列表")
    public Message<List<ShopUnprocessedOrderVO>> shopUnProcessed(@RequestParam String connectionKey, @RequestParam Long shopId) {
        return Message.ok(shopFeService.getUnprocessedOrder(shopId, connectionKey));
    }

    @RequestMapping(value = "/process", method = RequestMethod.GET)
    @ApiOperation("门店单个货物处理完成")
    public Message<Boolean> shopItemProcess(@RequestParam String connectionKey, @RequestParam Long shopId, @RequestParam Long itemId) {
        return Message.ok(shopFeService.processOrderItem(shopId, connectionKey, itemId));
    }

    @RequestMapping(value = "/ackPrint", method = RequestMethod.GET)
    @ApiOperation("门店单据打印完成")
    public Message<Boolean> shopItemProcess(@RequestParam String connectionKey, @RequestParam Long shopId, @RequestParam String orderId) {
        return Message.ok(shopFeService.ackPrint(shopId, connectionKey, orderId));
    }
}
