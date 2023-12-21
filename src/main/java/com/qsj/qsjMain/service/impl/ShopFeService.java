package com.qsj.qsjMain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.qsj.qsjMain.model.entity.Order;
import com.qsj.qsjMain.model.entity.OrderItem;
import com.qsj.qsjMain.model.entity.Shop;
import com.qsj.qsjMain.model.vo.ShopFeLoginResultVO;
import com.qsj.qsjMain.model.vo.ShopOrderItemVO;
import com.qsj.qsjMain.model.vo.ShopUnprocessedOrderVO;
import com.qsj.qsjMain.remote.service.EWXAppRemoteService;
import com.qsj.qsjMain.remote.service.WXApiRemoteService;
import com.qsj.qsjMain.remote.service.model.dto.SendNotifyMessageDTO;
import com.qsj.qsjMain.remote.service.model.vo.EWXAuthedUserVO;
import com.qsj.qsjMain.remote.service.model.vo.EWXUserInfoVO;
import com.qsj.qsjMain.remote.service.model.vo.NotifyDataVO;
import com.qsj.qsjMain.service.OrderItemService;
import com.qsj.qsjMain.service.OrderService;
import com.qsj.qsjMain.service.ShopService;
import com.qsj.qsjMain.service.UserService;
import com.qsj.qsjMain.utils.DigestUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import retrofit2.Response;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class ShopFeService {
    private final static String CONNECTION_KEY_SALT = "qsj*$*@)!_";
    private final ShopService shopService;

    private final OrderService orderService;

    private final UserService userService;

    private final OrderItemService orderItemService;

    private final WXApiRemoteService wxApiRemoteService;

    private final EWXAppRemoteService ewxAppRemoteService;

    public ShopFeService(ShopService shopService, OrderService orderService, UserService userService, OrderItemService orderItemService, WXApiRemoteService wxApiRemoteService,
                         EWXAppRemoteService ewxAppRemoteService) {
        this.shopService = shopService;
        this.orderService = orderService;
        this.userService = userService;
        this.orderItemService = orderItemService;
        this.wxApiRemoteService = wxApiRemoteService;
        this.ewxAppRemoteService = ewxAppRemoteService;
    }

//    public ShopFeLoginResultVO feLogin(String shopCredential) {
//        Shop shop = shopService.getOne(new LambdaQueryWrapper<Shop>().eq(Shop::getCredential, shopCredential));
//        if (shop == null) {
//            return new ShopFeLoginResultVO(null, null, null, false);
//        }
//        String connectionKeySource = CONNECTION_KEY_SALT + shop.getId();
//        String connectionKey = DigestUtils.md5(connectionKeySource);
//        return new ShopFeLoginResultVO(1L, shop.getName(), connectionKey, true);
//    }

    public ShopFeLoginResultVO feLogin(String code, Long targetShopId) throws IOException {
        // 使用code换取企业微信用户信息
        Response<EWXAuthedUserVO> response = ewxAppRemoteService.getTokenApiRemoteService().getAuthedUser(code).execute();
        EWXAuthedUserVO ewxAuthedUserVO = response.body();
        assert ewxAuthedUserVO != null;
        String userId = ewxAuthedUserVO.getUserId();
        // 根据userId获取用户信息
        Response<EWXUserInfoVO> response1 = ewxAppRemoteService.getTokenApiRemoteService().getUserInfo(userId).execute();
        EWXUserInfoVO ewxUserInfoVO = response1.body();
        assert ewxUserInfoVO != null;
        String mgmtShop = ewxUserInfoVO.getAttr("mgmtShop");
        if(mgmtShop == null){
            return new ShopFeLoginResultVO(null, null, null, false);
        }
        Long shopId = null;
        // 特权用户，可以为所有店铺进行登录
        if(mgmtShop.equals("ALL")) {
            shopId = targetShopId;
        }
        String[] mgtShops = mgmtShop.split(",");

        for (String mgtShop : mgtShops) {
            if (mgtShop.equals(targetShopId.toString())) {
                shopId = Long.parseLong(mgtShop);
                break;
            }
        }

        if (shopId == null) {
            return new ShopFeLoginResultVO(null, null, null, false);
        }

        Shop shop = shopService.getById(shopId);
        if (shop == null) {
            return new ShopFeLoginResultVO(null, null, null, false);
        }
        String connectionKeySource = CONNECTION_KEY_SALT + shop.getId();
        String connectionKey = DigestUtils.md5(connectionKeySource);
        return new ShopFeLoginResultVO(shopId, shop.getName(), connectionKey, true);
    }

    public boolean verifyConnectionKey(Long shopId, String connectionKey) {
        String connectionKeySource = CONNECTION_KEY_SALT + shopId.toString();
        String connectionKeyExpected = DigestUtils.md5(connectionKeySource);
        return Objects.equals(connectionKeyExpected, connectionKey);
    }


    public List<ShopUnprocessedOrderVO> getUnprocessedOrder(Long shopId, String connectionKey) {
        if (!verifyConnectionKey(shopId, connectionKey)) {
            return null;
        }
        List<ShopUnprocessedOrderVO> result = new ArrayList<>();
        List<Order> orders = orderService.list(new LambdaQueryWrapper<Order>().eq(Order::getShopId, shopId).eq(Order::getStatus, Order.Status.PAID));
        if (orders == null || orders.size() == 0) {
            return result;
        }
        List<OrderItem> orderItems = orderItemService.list(new LambdaQueryWrapper<OrderItem>().in(OrderItem::getOrderId, orders.stream().map(Order::getId).toArray()));
        for (Order order : orders) {
            List<ShopOrderItemVO> shopOrderItemVOS = new ArrayList<>();
            for (OrderItem orderItem : orderItems) {
                if (orderItem.getOrderId().equals(order.getId())) {
                    ShopOrderItemVO itemVO = new ShopOrderItemVO();
                    itemVO.setName(orderItem.getProductName());
                    itemVO.setProductId(orderItem.getProductId());
                    itemVO.setId(orderItem.getId());
                    itemVO.setCount(orderItem.getProductQuantity());
                    itemVO.setSpec(orderItem.getSpec());
                    itemVO.setStatus(orderItem.getProdStatus());
                    if (!order.getIsPrinted()) {
                        itemVO.setQrCode(orderItem.getQrCode());
                    }
                    shopOrderItemVOS.add(itemVO);
                }
            }
            if (shopOrderItemVOS.size() == 0) {
                continue;
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            ShopUnprocessedOrderVO unVo = new ShopUnprocessedOrderVO();
            unVo.setOrderId(order.getId()).setOrderTime(sdf.format(order.getCreateTime()))
                    .setIsPrinted(order.getIsPrinted())
                    .setRemark(order.getRemark())
                    .setPickUpNumber(order.getPickupNumber())
                    .setDeliveryType(order.getDeliveryType().getCode())
                    .setExtOrderId(order.getExtOrderId())
                    .setItems(shopOrderItemVOS);
            if(!order.getDeliveryType().equals(Order.DeliveryType.SELF)) {
                unVo.setCustomerAddr(order.getAddress() + " " + order.getRoomNumber());
                unVo.setPhoneSuffix(order.getContactPhone());
                unVo.setCustomerName(order.getContactName());
            }

            result.add(unVo);
        }
        return result;
    }

    public Boolean processOrderItem(Long shopId, String connectionKey, Long itemId) {
        if (!verifyConnectionKey(shopId, connectionKey)) {
            return false;
        }
        OrderItem orderItem = orderItemService.getById(itemId);
        if (orderItem == null) {
            return false;
        }
        // 检查关联订单是否存在
        Order order = orderService.getById(orderItem.getOrderId());
        if (order == null) {
            return false;
        }
        // 检查关联订单是否属于该店铺
        if (!Objects.equals(order.getShopId(), shopId)) {
            return false;
        }
        // 检查订单状态是否为已支付
        if (!Objects.equals(order.getStatus(), Order.Status.PAID)) {
            return false;
        }

        orderItem.setProdStatus(1);
        orderItemService.updateById(orderItem);
        long currTimeStamp = new Date().getTime();
        // 检查订单是否已经全部处理完毕
        List<OrderItem> orderItems = orderItemService.list(new LambdaQueryWrapper<OrderItem>().eq(OrderItem::getOrderId, orderItem.getOrderId()));
        boolean allProcessed = true;
        for (OrderItem item : orderItems) {
            if (item.getProdStatus() != 1) {
                allProcessed = false;
                break;
            }
        }
        // 如果订单货物都备齐了并且是自提订单那么发送微信通知和改变订单状态
        if (allProcessed && Order.DeliveryType.SELF == order.getDeliveryType()) {

            order.setStatus(Order.Status.FINISH)
                    .setPreparedTime(currTimeStamp)
                    .setFinishTime(currTimeStamp);
            orderService.updateById(order);
            try {
                SendNotifyMessageDTO dto = new SendNotifyMessageDTO();
                NotifyDataVO notifyDataVO = new NotifyDataVO();
                notifyDataVO.setOrderNumber(new NotifyDataVO.NotifyValue(order.getPickupNumber()));
                dto.setTouser(userService.getById(order.getUserId()).getWxOpenId());
                dto.setData(notifyDataVO);
                wxApiRemoteService.getTokenService().sendNotifyMessage(dto).execute();
            } catch (Exception e) {
                log.error(e.getMessage());
                e.printStackTrace();
            }

        } else if (allProcessed && Order.DeliveryType.IMMEDIATELY == order.getDeliveryType()) {
            order.setStatus(Order.Status.GOODS_READY).setPreparedTime(currTimeStamp);
            orderService.updateById(order);
        }
        return true;
    }

    public Boolean ackPrint(Long shopId, String connectionKey, String orderId) {
        if (!verifyConnectionKey(shopId, connectionKey)) {
            return false;
        }
        Order order = orderService.getById(orderId);
        if (order == null) {
            return false;
        }
        // 检查关联订单是否存在
        // 检查关联订单是否属于该店铺
        if (!Objects.equals(order.getShopId(), shopId)) {
            return false;
        }

        order.setIsPrinted(true);
        // 对于美团订单，打印后直接订单修改为已完成，物品已备齐，不需要现场扫码确认
        // TODO: 如果接了美团API之后，这逻辑考虑去掉
        if(Order.Source.MT.equals(order.getSource())) {
            order.setStatus(Order.Status.FINISH);
            order.setFinishTime(new Date().getTime());
            LambdaUpdateWrapper<OrderItem> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(OrderItem::getOrderId, orderId);
            updateWrapper.set(OrderItem::getProdStatus, 1);
            orderItemService.update(updateWrapper);
        }
        orderService.updateById(order);
        return true;

    }
}
