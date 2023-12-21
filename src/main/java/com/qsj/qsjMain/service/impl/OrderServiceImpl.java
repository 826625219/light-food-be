package com.qsj.qsjMain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.qsj.qsjMain.config.ProfileVariableConfig;
import com.qsj.qsjMain.exception.DadaDeliveryCallError;
import com.qsj.qsjMain.exception.OrderStatusException;
import com.qsj.qsjMain.model.PaginationResp;
import com.qsj.qsjMain.model.entity.*;
import com.qsj.qsjMain.model.mapper.OrderMapper;
import com.qsj.qsjMain.model.vo.*;
import com.qsj.qsjMain.remote.service.DaDaDeliveryRemoteService;
import com.qsj.qsjMain.remote.service.EWXRobotRemoteService;
import com.qsj.qsjMain.remote.service.WXApiRemoteService;
import com.qsj.qsjMain.remote.service.model.dto.*;
import com.qsj.qsjMain.remote.service.model.vo.DaDaCommonResp;
import com.qsj.qsjMain.remote.service.model.vo.QueryDeliveryFeeResult;
import com.qsj.qsjMain.service.*;
import com.qsj.qsjMain.utils.DigestUtils;
import com.qsj.qsjMain.utils.FileUtil;
import com.qsj.qsjMain.utils.PickupSerialUtils;
import com.qsj.qsjMain.utils.SerialUtils;
import com.wechat.pay.java.core.notification.RequestParam;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;


/**
 * 订单服务实现类
 */
@Slf4j
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Resource
    private RedisTemplate<String, Long> redisTemplate;

    private final Gson gson = new Gson();

    private final WXApiRemoteService wxApiRemoteService;
    private final OrderItemService orderItemService;
    private final NutritionPlanService nutritionPlanService;
    private final WechatPayService wechatPayService;
    private final UserService userService;
    private final ShopService shopService;
    private final DiscountCouponService discountCouponService;
    private final UserAddressBookService userAddressBookService;
    private final ProductService productService;
    private final IntegralCouponService integralCouponService;
    private final DiscountCouponBatchService discountCouponBatchService;
    private final DaDaDeliveryRemoteService daDaDeliveryRemoteService;
    private final RechargeOrderService rechargeOrderService;

    private final ProfileVariableConfig profileVariableConfig;

    private final SkuMappingService skuMappingService;

    private final ShareRewardServiceImpl shareRewardService;

    private final PickupSerialUtils pickupSerialUtils;

    private final EWXRobotRemoteService ewxRobotRemoteService;

    public OrderServiceImpl(WXApiRemoteService wxApiRemoteService, OrderItemService orderItemService, NutritionPlanService nutritionPlanService, WechatPayService wechatPayService, UserService userService, ShopService shopService, DiscountCouponService discountCouponService, UserAddressBookService userAddressBookService, ProductService productService, IntegralCouponService integralCouponService, DiscountCouponBatchService discountCouponBatchService, DaDaDeliveryRemoteService daDaDeliveryRemoteService, RechargeOrderService rechargeOrderService, ProfileVariableConfig profileVariableConfig, SkuMappingService skuMappingService, ShareRewardServiceImpl shareRewardService, PickupSerialUtils pickupSerialUtils, EWXRobotRemoteService ewxRobotRemoteService) {
        this.wxApiRemoteService = wxApiRemoteService;
        this.orderItemService = orderItemService;
        this.nutritionPlanService = nutritionPlanService;
        this.wechatPayService = wechatPayService;
        this.userService = userService;
        this.shopService = shopService;
        this.discountCouponService = discountCouponService;
        this.userAddressBookService = userAddressBookService;
        this.productService = productService;
        this.integralCouponService = integralCouponService;
        this.discountCouponBatchService = discountCouponBatchService;
        this.daDaDeliveryRemoteService = daDaDeliveryRemoteService;
        this.rechargeOrderService = rechargeOrderService;
        this.profileVariableConfig = profileVariableConfig;
        this.skuMappingService = skuMappingService;
        this.shareRewardService = shareRewardService;
        this.pickupSerialUtils = pickupSerialUtils;
        this.ewxRobotRemoteService = ewxRobotRemoteService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderDraftCreateResultVO createDraft(Long userId, CreateOrderDraftVO submitVO) throws DadaDeliveryCallError {


        // 检查商品列表是否为空
        if (submitVO.getOrderItems() == null || submitVO.getOrderItems().isEmpty()) {
            return OrderDraftCreateResultVO.fail(OrderDraftCreateResultVO.Status.PRODUCT_LIST_EMPTY);
        }

        // 检查商品是否可以购买
        if (!productService.canBuyProduct(submitVO.getOrderItems(), submitVO.getShopId())) {
            return OrderDraftCreateResultVO.fail(OrderDraftCreateResultVO.Status.PRODUCT_NOT_AVAILABLE);
        }
        // 生成订单草稿
        Order order = new Order();
        order.setId(SerialUtils.generateOrder()).setUserId(userId).setStatus(Order.Status.DRAFT)
                .setDeliveryType(submitVO.getDeliveryType())
                .setSource(Order.Source.SELF)
                .setDeliveryFeeFromUser(0).setActualDeliveryFee(0);

        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderCreateItemVO orderItemVO : submitVO.getOrderItems()) {
            Product product = productService.getById(orderItemVO.getProductId());
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(order.getId())
                    .setProductId(orderItemVO.getProductId())
                    .setProductActualPrice(product.getPrice())
                    .setCost(product.getCost())
                    .setProductOriginalPrice(product.getOriginalPrice())
                    .setProductI18nName(product.getI18nName())
                    .setProductQuantity(orderItemVO.getProductQuantity())
                    .setProductName(product.getName())
                    .setProductImageUrl(product.getImageUrl())
                    .setProductUnit(product.getUnit())
                    .setStatus(0);
            // 如果带有规格选项，需要将规格选项信息写入订单商品，并且按照规格选项叠加的价格计算商品价格
            if (orderItemVO.getSpecIdx() != null && !orderItemVO.getSpecIdx().isEmpty()
                    && product.getSpecOptions() != null && !product.getSpecOptions().isEmpty() &&
                    orderItemVO.getSpecIdx().size() == product.getSpecOptions().size()) {
                List<String> specs = new ArrayList<>();
                List<String> i18nSpecs = new ArrayList<>();
                for (int i = 0; i < orderItemVO.getSpecIdx().size(); i++) {
                    SpecOption option = product.getSpecOptions().get(i).getOptions().get(orderItemVO.getSpecIdx().get(i));
                    specs.add(option.getName());
                    i18nSpecs.add(option.getI18nName());
                    // 增加规格价格到商品价格
                    orderItem.setProductActualPrice(orderItem.getProductActualPrice() + option.getPrice());
                    orderItem.setProductOriginalPrice(orderItem.getProductOriginalPrice() + option.getPrice());
                }
                orderItem.setSpec(String.join("/", specs));
                orderItem.setProductI18nSpec(String.join("/", i18nSpecs));
            }
            orderItems.add(orderItem);
        }
        order.setTotalAmount(calculateOrderPrice(orderItems));

        //外卖的情况下
        //检查用户地址参数,计算预订单配送费
        if (submitVO.getDeliveryType() != Order.DeliveryType.SELF) {

            if (userAddressBookService.getById(submitVO.getDeliveryAddressId()) == null) {
                return OrderDraftCreateResultVO.fail(OrderDraftCreateResultVO.Status.ADDRESS_NOT_EXIST);
            }

            // 检查地址是否属于当前用户
            if (!Objects.equals(userAddressBookService.getById(submitVO.getDeliveryAddressId()).getUserId(), userId)) {
                return OrderDraftCreateResultVO.fail(OrderDraftCreateResultVO.Status.ADDRESS_NOT_BELONG_TO_USER);
            }

            UserAddressBook addressBook = userAddressBookService.getById(submitVO.getDeliveryAddressId());
            order.setAddress(addressBook.getAddressDetail())
                    .setAddressLatitude(addressBook.getLatitude())
                    .setAddressLongitude(addressBook.getLongitude())
                    .setRoomNumber(addressBook.getRoomNumber())
                    .setContactName(addressBook.getName())
                    .setContactPhone(addressBook.getPhone());

            //先选举 选举出的门店都没开业 只能和用户说没有营业的门店
            Long bestShopId = getBestShop(order);
            if (!shopService.getById(bestShopId).isRunning()) {
                return OrderDraftCreateResultVO.fail(OrderDraftCreateResultVO.Status.SHOP_NOT_OPEN);
            }

            // 检查门店是否开启配送服务
            if(shopService.getById(bestShopId).getStopDelivery()==1){
                return OrderDraftCreateResultVO.fail(OrderDraftCreateResultVO.Status.STOP_DELIVERY);
            }
            order.setShopId(bestShopId);

            // 请求创建预配送单 配送费超出15视为超额订单
            if (!requestDeliveryPreOrder(order)) {
                return OrderDraftCreateResultVO.fail(OrderDraftCreateResultVO.Status.DISTANCE_TOO_FAR);
            }

        } else { //自提
            Shop shop = shopService.getById(submitVO.getShopId());
            // 检查数据库中店铺是否存在
            if (shop == null) {
                return OrderDraftCreateResultVO.fail(OrderDraftCreateResultVO.Status.SHOP_NOT_EXIST);
            }
            // 检查店铺是否开业
            if (!shop.isRunning()) {
                return OrderDraftCreateResultVO.fail(OrderDraftCreateResultVO.Status.SHOP_NOT_OPEN);
            }
            order.setAddress(shop.getName())
                    .setShopId(submitVO.getShopId())
                    .setRoomNumber("")
                    .setContactName(shop.getAddressDetail())
                    .setContactPhone(shop.getPhone());

        }
        orderItemService.saveBatch(orderItems);

        // 过滤出可用的优惠券
        QueryWrapper<DiscountCoupon> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(DiscountCoupon::getUserId, userId)
                .in(DiscountCoupon::getStatus, DiscountCoupon.Status.OK, DiscountCoupon.Status.ALLOT)
                .le(DiscountCoupon::getValidStartTime, new Date().getTime())
                .ge(DiscountCoupon::getValidEndTime, new Date().getTime());
        List<DiscountCoupon> availCoupons = discountCouponService.list(wrapper).stream()
                .filter(discountCoupon -> discountCouponService.matchCondition(order, discountCoupon.getSn())).toList();

        for (DiscountCoupon coupon : availCoupons) {
            coupon.setRule(discountCouponBatchService.getById(coupon.getBatchId()).getDiscountCouponRule());
        }

        save(order);
        User user = userService.getById(userId);
        return OrderDraftCreateResultVO.success(order, availCoupons, user.getIntegral());

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PrepayResultWithSignVO submitOrder(Long userId, SubmitOrderVO orderSubmitVO) throws DadaDeliveryCallError {
        // 检查订单合法性
        Order order = getById(orderSubmitVO.getOrderId());
        if (order == null) {
            return PrepayResultWithSignVO.fail(PrepayResultWithSignVO.PayStatus.NOT_EXIST);
        }
        //打烊了 下单失败
        if (!shopService.getById(order.getShopId()).isRunning()) {
            return PrepayResultWithSignVO.fail(PrepayResultWithSignVO.PayStatus.SHOP_NOT_OPEN);
        }

        if (order.getStatus() != Order.Status.DRAFT && order.getStatus() != Order.Status.UNPAY) {
            return PrepayResultWithSignVO.fail(PrepayResultWithSignVO.PayStatus.ORDER_STATUS_ERROR);
        }

        // 检查用户积分是否足够
        if (userService.getById(userId).getIntegral() < orderSubmitVO.getUseIntegral()) {
            return PrepayResultWithSignVO.fail(PrepayResultWithSignVO.PayStatus.INTEGRAL_NOT_ENOUGH);
        }

        // 如果关联了优惠券，检查优惠券是否可用
        if (orderSubmitVO.getUseDiscountCouponId() != null && !discountCouponService.canUse(orderSubmitVO, userId)) {
            return PrepayResultWithSignVO.fail(PrepayResultWithSignVO.PayStatus.DISCOUNT_COUPON_NOT_AVAILABLE);
        }

        // 订单通过检查，开始支付
        order.setPayType(orderSubmitVO.getPayType())
                .setDiscountCouponSn(orderSubmitVO.getUseDiscountCouponId())
                .setStatus(Order.Status.UNPAY)
                .setRemark(orderSubmitVO.getRemark())
                .setUseIntegral(orderSubmitVO.getUseIntegral());

        fillOrder(order);

        // 如果使用余额支付，检查余额是否足够
        if (orderSubmitVO.getPayType() == Order.PayType.BALANCE) {
            if (userService.getById(userId).getBalance() < order.getPayAmount()) {
                return PrepayResultWithSignVO.fail(PrepayResultWithSignVO.PayStatus.BALANCE_NOT_ENOUGH);
            } else {
                // 余额充足，直接扣除余额，支付成功
                order.setStatus(Order.Status.PAID);
                executePayedPostOrder(order);
                deliveryOrderSubmit(order,false);
                updateById(order);
                return PrepayResultWithSignVO.orderSuccess();
            }
        }
        updateById(order);
        // 使用微信支付
        User user = userService.getById(userId);
        log.info("请求用户发起微信支付，订单号: {}, 金额: {}", order.getId(), order.getPayAmount());
        return PrepayResultWithSignVO.preOrderSuccess(
                wechatPayService.prepayAndSign(order.getPayAmount(), user.getWxOpenId(), order.getId(), order.getId()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PrepayResultWithSignVO payOrder(Long userId, PayOrderVO vo) throws DadaDeliveryCallError {
        Order order = getById(vo.getOrderId());
        if (order == null) {
            return PrepayResultWithSignVO.fail(PrepayResultWithSignVO.PayStatus.NOT_EXIST);
        }
        if (order.getStatus() != Order.Status.UNPAY) { //只有待支付并且有效的可以支付
            return PrepayResultWithSignVO.fail(PrepayResultWithSignVO.PayStatus.ORDER_STATUS_ERROR);
        }
        User user = userService.getById(userId);
        if (order.getPayType() == Order.PayType.BALANCE) {
            if (user.getBalance() < order.getPayAmount()) {
                return PrepayResultWithSignVO.fail(PrepayResultWithSignVO.PayStatus.BALANCE_NOT_ENOUGH);
            } else {
                // 余额充足，直接扣除余额，支付成功
                order.setStatus(Order.Status.PAID);
                executePayedPostOrder(order);
                deliveryOrderSubmit(order,false);
                updateById(order);
                return PrepayResultWithSignVO.orderSuccess();
            }
        }

        // 使用微信支付
        return PrepayResultWithSignVO.preOrderSuccess(
                wechatPayService.prepayAndSign(order.getPayAmount(), user.getWxOpenId(), order.getId(), order.getId()));


    }

    @Override
    public String getQRCode(String couponSn, Long itemId, String sign) {
        GetWXCodeUnLimitDTO dto = new GetWXCodeUnLimitDTO();
        dto.setScene("i=" + couponSn + ",j=" + itemId + ",k=" + sign);
        dto.setCheckPath(true);
        dto.setEnvVersion(profileVariableConfig.getQrcodeType());
        byte[] rawData;
        try {
            rawData = Objects.requireNonNull(wxApiRemoteService.getTokenService().getWXACodeUnLimit(dto).execute().body()).bytes();
        } catch (IOException e) {
            log.error("获取小程序码失败", e);
            return "";
        }
        // covert to base64
        return Base64.getEncoder().encodeToString(rawData);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean reportMtOrder(MTExternalOrderVO vo) {
        // 检查是否已经存在
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Order::getExtOrderId, vo.getWmOrderIdView());
        if (count(wrapper) > 0) {
            return false;
        }
        // 查询关联店铺
        QueryWrapper<Shop> shopWrapper = new QueryWrapper<>();
        shopWrapper.lambda().eq(Shop::getMtPoiId, vo.getWmPoiId());
        Shop shop = shopService.getOne(shopWrapper);
        if (shop == null) {
            return false;
        }


        Order order = new Order();
        order.setExtOrderId(vo.getWmOrderIdView())
                .setId(vo.getWmOrderIdView())
                .setPickupNumber("MT" + vo.getWmDaySeq())
                .setPayAmount((int) (vo.getTotalAfter() * 100))
                .setTotalAmount((int) (vo.getTotalBefore() * 100))
                .setStatus(Order.Status.PAID)
                .setDeliveryPlatform(Order.DeliveryPlatform.MEITUAN)
                .setDeliveryType(Order.DeliveryType.IMMEDIATELY)
                .setShopId(shop.getId())
                .setSource(Order.Source.MT)
                .setActualDeliveryFee((int) (vo.getShippingFee() * 100));
        save(order);
        // 将美团货品转换为平台货品
        List<OrderItem> orderItems = new ArrayList<>();
        for (MTExternalOrderCartDetailVO detailVO : vo.getCartDetailVos()) {
            for (MTExternalOrderCartItemDetailVO item : detailVO.getDetails()) {
                List<Long> itemIds = skuMappingService.getItemIdsByExternalSku(item.getWmFoodId(), shop.getId(), SkuMapping.MapType.MT);
                if(itemIds.size()==0) {
                    log.warn("美团订单{}中的货品{}(sku:{})不存在映射关系", vo.getWmOrderIdView(), item.getFoodName(), item.getWmFoodId());
                    continue;
                }
                for(Long mappedItemId : itemIds){
                    QueryWrapper<Product> productWrapper = new QueryWrapper<>();
                    productWrapper.lambda().eq(Product::getId, mappedItemId);
                    Product product = productService.getOne(productWrapper);
                    if (product == null) {
                        log.warn("美团订单{}中的货品{}不存在", vo.getWmOrderIdView(), item.getWmFoodId());
                        continue;
                    }
                    OrderItem orderItem = new OrderItem();
                    orderItem.setProductId(product.getId())
                            .setProductActualPrice((int) (item.getFoodPrice() * 100))
                            .setOrderId(order.getId())
                            .setSpec(item.getFoodName()).setProductI18nSpec(item.getFoodName())
                            .setProductName(product.getName())
                            .setProductI18nName(product.getI18nName())
                            .setProductImageUrl(product.getImageUrl())
                            .setProductUnit(product.getUnit())
                            .setProductOriginalPrice(product.getOriginalPrice())
                            .setProductQuantity(item.getCount())
                            .setProdStatus(0)
                            .setCost(product.getCost())
                            .setStatus(0);
                    orderItems.add(orderItem);
                }


            }
        }

        orderItemService.saveBatch(orderItems);
        executePayedPostOrder(order);
        updateById(order);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public QRCodeResultVO receiveQRCodeData(Long userId, QRCodeDataVO data) {
        if (!data.validateSign()) {
            return QRCodeResultVO.fail();
        }

        QRCodeResultVO resultVO = new QRCodeResultVO();
        // 检查订单、订单物品是否都存在
        OrderItem orderItem = orderItemService.getById(data.getOrderItemId());
        if (orderItem == null) {
            return QRCodeResultVO.fail();
        }

        Order order = getById(orderItem.getOrderId());
        if (order == null) {
            return QRCodeResultVO.fail();
        }
        // 检查积分兑换券的状态
        IntegralCoupon coupon = integralCouponService.getById(data.getCouponSn());
        if (coupon == null) {
            resultVO.setStatus(QRCodeResultVO.CouponExchangeStatus.NOT_FOUND);
        } else if (coupon.getStatus() == IntegralCoupon.Status.USED) {
            resultVO.setStatus(QRCodeResultVO.CouponExchangeStatus.ALREADY_USED);
        } else if (coupon.getEndTime() < new Date().getTime()) {
            resultVO.setStatus(QRCodeResultVO.CouponExchangeStatus.EXPIRED);
        } else {
            // 兑换成功，增加用户积分
            User user = userService.getById(userId);
            LambdaUpdateWrapper<User> userWrapper = new LambdaUpdateWrapper<>();
            userWrapper.eq(User::getId, userId).set(User::getIntegral, user.getIntegral() + coupon.getIntegral());
            userService.update(userWrapper);
            integralCouponService.deductCoupon(data.getCouponSn());
            resultVO.setStatus(QRCodeResultVO.CouponExchangeStatus.SUCCESS);
            resultVO.setGainIntegral(coupon.getIntegral());
            // 美团过来的单需要设置userId
            if (order.getUserId() == null) {
                order.setUserId(userId);
                updateById(order);
            }
        }
        resultVO.setSuccess(true);
        // 计算订单全单的营养信息
        List<OrderItem> items = orderItemService.list(new LambdaQueryWrapper<OrderItem>().eq(OrderItem::getOrderId, order.getId()));
        List<NutritionDataWithItemVO> itemsNutrition = new ArrayList<>();
        User user = userService.getById(userId);
        Integer energyRef = user.getEnergyRef();
        NutritionPlan plan = Optional.ofNullable(nutritionPlanService.getById(user.getNutritionPlanId())).orElse(nutritionPlanService.getById(1));
        int planEnergy = Double.valueOf(plan.getEnergy() * 0.01 * energyRef).intValue();
        int targetProtein = Double.valueOf(plan.getProtein() * 0.01 * energyRef / 4.0).intValue();
        int targetFat = Double.valueOf(plan.getFat() * 0.01 * energyRef / 9.0).intValue();
        int targetCarbohydrate = Double.valueOf(plan.getCarbohydrate() * 0.01 * energyRef / 4.0).intValue();
        for (OrderItem orderSubItem : items) {
            Product product = productService.getById(orderSubItem.getProductId());
            if (product == null) {
                continue;
            }
            NutritionDataWithItemVO nData = new NutritionDataWithItemVO();
            nData.setMaterial(product.getMaterial());
            nData.setName(orderSubItem.getProductName()).setCount(orderSubItem.getProductQuantity()).setFat(product.getFat()).setCarbohydrate(product.getCarbohydrate()).setProtein(product.getProtein()).setEnergy(product.getEnergy()).setFiber(product.getFiber()).setNonSaturatedFat(product.getNonSaturatedFat()).setSaturatedFat(product.getSaturatedFat());
            nData.setPercentCarbohydrate(Double.valueOf(nData.getCarbohydrate() * 100.0 / targetCarbohydrate).intValue());
            nData.setI18nName(orderSubItem.getProductI18nName()).setI18nSpec(orderSubItem.getProductI18nSpec());
            nData.setPercentFat(Double.valueOf(nData.getFat() * 100.0 / targetFat).intValue());
            nData.setPercentProtein(Double.valueOf(nData.getProtein() * 100.0 / targetProtein).intValue());
            nData.setPercentEnergy(Double.valueOf(nData.getEnergy() * 100.0 / planEnergy).intValue());
            nData.setPercentFiber(Double.valueOf(nData.getFiber() * 100.0 / 40).intValue());
            nData.setPrice(orderSubItem.getProductOriginalPrice());
            nData.setImgUrl(orderSubItem.getProductImageUrl());
            nData.setSpec(orderSubItem.getSpec());
            itemsNutrition.add(nData);
            if (orderSubItem.getId().equals(orderItem.getId())) {
                resultVO.setCurrentItemNutrition(nData);
                resultVO.setQrShow(product.getQrShow());
                orderItem.setScanedQr(1);
                orderItemService.updateById(orderItem);
            }

        }
        resultVO.setOrderItemNutrition(itemsNutrition);
        // 合并所有商品的营养信息
        int totalEnergy = 0;
        int totalProtein = 0;
        int totalFat = 0;
        int totalCarbohydrate = 0;
        int totalFiber = 0;
        int totalSaturatedFat = 0;
        int totalNonSaturatedFat = 0;
        for (NutritionDataWithItemVO nData : itemsNutrition) {
            totalEnergy += nData.getEnergy();
            totalProtein += nData.getProtein();
            totalFat += nData.getFat();
            totalCarbohydrate += nData.getCarbohydrate();
            totalFiber += nData.getFiber();
            totalSaturatedFat += nData.getSaturatedFat();
            totalNonSaturatedFat += nData.getNonSaturatedFat();
        }


        // 全单营养信息
        NutritionDataVO orderNutrition = new NutritionDataVO()
                .setCarbohydrate(totalCarbohydrate).setFat(totalFat).setProtein(totalProtein)
                .setEnergy(totalEnergy).setFiber(totalFiber).setSaturatedFat(totalSaturatedFat)
                .setNonSaturatedFat(totalNonSaturatedFat);
        orderNutrition.setPercentCarbohydrate(Double.valueOf(orderNutrition.getCarbohydrate() * 100.0 / targetCarbohydrate).intValue());
        orderNutrition.setPercentFat(Double.valueOf(orderNutrition.getFat() * 100.0 / targetFat).intValue());
        orderNutrition.setPercentProtein(Double.valueOf(orderNutrition.getProtein() * 100.0 / targetProtein).intValue());
        orderNutrition.setPercentEnergy(Double.valueOf(orderNutrition.getEnergy() * 100.0 / planEnergy).intValue());
        orderNutrition.setPercentFiber(Double.valueOf(orderNutrition.getFiber() * 100.0 / 40).intValue());
        resultVO.setOrderNutrition(orderNutrition);
        return resultVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean dropOrder() {
        UpdateWrapper<Order> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda()
                .eq(Order::getStatus, Order.Status.UNPAY)
                .le(Order::getCreateTime, new Date().getTime() - 30 * 60 * 1000)
                .set(Order::getStatus, Order.Status.CANCEL);
        return update(updateWrapper);
    }


    @NotNull
    private OrderResultVO extractOrderVO(Order order) {
        OrderResultVO vo = new OrderResultVO();
        vo.setUserAddrName(order.getAddress() + " " + order.getRoomNumber())
                .setContactName(order.getContactName())
                .setContactPhone(order.getContactPhone())
                .setOrderTime(order.getCreateTime())
                .setOrderStatus(order.getStatus().getCode())
                .setOrderId(order.getId())
                .setDeliveryType(order.getDeliveryType().getCode())
                .setPayType(order.getPayType())
                .setDiscountAmount(order.getIntegralAmount() + order.getCouponAmount())
                .setPayTime(order.getPayTime())
                .setFinishTime(order.getFinishTime())
                .setPickUpNumber(order.getPickupNumber())
                .setDeliveryFee(order.getDeliveryFeeFromUser())
                .setTotalPrice(order.getPayAmount());
        List<OrderItem> orderItems = orderItemService.list(new LambdaQueryWrapper<OrderItem>().eq(OrderItem::getOrderId, order.getId()));
        List<OrderItemResultVO> orderItemResultVOList = new ArrayList<>();
        // map orderItem -> orderItemResultVO
        for (OrderItem orderItem : orderItems) {
            OrderItemResultVO itemVO = new OrderItemResultVO();
            itemVO.setPrice(orderItem.getProductActualPrice());
            itemVO.setFoodNum(orderItem.getProductQuantity());
            itemVO.setSpec(orderItem.getSpec());
            itemVO.setI18nSpec(orderItem.getProductI18nSpec());
            itemVO.setImgUrl(orderItem.getProductImageUrl());
            itemVO.setFoodName(orderItem.getProductName());
            itemVO.setFoodI18nName(orderItem.getProductI18nName());
            orderItemResultVOList.add(itemVO);
        }
        vo.setOrderItemResultVOList(orderItemResultVOList);
        return vo;
    }

    public OrderResultVO getOrderByUserOrderId(Long userId, String orderId) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<Order>()
                .eq(Order::getUserId, userId).eq(Order::getId, orderId)
                .notIn(Order::getStatus, Order.Status.DRAFT, Order.Status.AUTO_DELETE);
        Order order = getOne(wrapper, false);
        if (order == null) {
            return new OrderResultVO();
        }
        return extractOrderVO(order);
    }

    public OrderStatusVO getOrderStatusByOrderId(Long userId, String orderId) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<Order>()
                .eq(Order::getUserId, userId).eq(Order::getId, orderId)
                .notIn(Order::getStatus, Order.Status.DRAFT, Order.Status.AUTO_DELETE);
        Order order = getOne(wrapper, false);
        if (order == null) {
            return new OrderStatusVO();
        }
        OrderStatusVO vo = new OrderStatusVO();
        vo.setOrderStatus(order.getStatus().getCode()).setPreparedTime(order.getPreparedTime()).setFinishTime(order.getFinishTime());
        return vo;
    }

    public PaginationResp<OrderResultVO> getOrdersByUserId(Long userId, Integer page, Integer pageSize) {

        // 获取排除了已删除/草稿态的订单列表
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<Order>()
                .eq(Order::getUserId, userId)
                .notIn(Order::getStatus, Order.Status.DRAFT, Order.Status.AUTO_DELETE)
                .orderBy(true, false, Order::getCreateTime);
        Page<Order> orderPage = new Page<>(page, pageSize);
        this.getBaseMapper().selectPage(orderPage, wrapper);
        PaginationResp<OrderResultVO> paginationResp = new PaginationResp<>();
        paginationResp.setCount(orderPage.getTotal());
        paginationResp.setPageSize(orderPage.getSize());
        paginationResp.setTotalPage(orderPage.getPages());

        List<OrderResultVO> orderResultVOList = new ArrayList<>();
        orderPage.getRecords().forEach(order -> {
            OrderResultVO vo = extractOrderVO(order);
            orderResultVOList.add(vo);
        });
        paginationResp.setData(orderResultVOList);
        return paginationResp;
    }

    public Order getOrderById(String orderId, Long userId) {
        Order order = getById(orderId);
        if (order == null || !order.getUserId().equals(userId)) {
            return null;
        }
        return order;
    }

    /**
     * 计算订单原始总价，真实总价
     *
     * @param orderItems 订单项
     */
    public int calculateOrderPrice(List<OrderItem> orderItems) {
        return orderItems.stream().mapToInt(item -> item.getProductQuantity() * item.getProductActualPrice()).sum();
    }

    /**
     * 根据用户配送地址和当下门店状态 选举服务门店的算法,返回的是的最佳门店ID
     */
    private Long getBestShop(Order order) {
        Long availableShopId = 1L;
        //一系列运算
        //最后运算得出的值进行是否营业校验
        return availableShopId;
    }

    /**
     * 生成预配送订单，并填充运费
     */
    private Boolean requestDeliveryPreOrder(Order order) throws DadaDeliveryCallError {
        // 创建询价单，计算运费
        String dadaShopNo = shopService.getById(order.getShopId()).getDadaShopNo();
        QueryDeliveryFee queryDeliveryFee = new QueryDeliveryFee();
        queryDeliveryFee.setShopNo(dadaShopNo).setOriginId(order.getId()).setCityCode("0755").setCargoPrice(order.getTotalAmount() / 100.0).setIsPrepay(0).setReceiverName(order.getContactName()).setReceiverAddress(order.getAddress() + order.getRoomNumber()).setReceiverLng(order.getAddressLongitude()).setReceiverLat(order.getAddressLatitude()).setCargoWeight(1.0).setCallback(profileVariableConfig.getDeliveryCallbackURL()).setReceiverPhone(order.getContactPhone());
        DaDaCommonReq<QueryDeliveryFee> req = new DaDaCommonReq<>();
        req.setBody(queryDeliveryFee);
        req.setSourceId(profileVariableConfig.getDeliverySourceId());
        req.prepareBody();
        DaDaCommonResp<QueryDeliveryFeeResult> resp;
        try {
            resp = daDaDeliveryRemoteService.getService().queryDeliverFee(req).execute().body();
            if (resp == null || resp.getCode() != 0) {
                throw new DadaDeliveryCallError("查询配送费失败");
            }
        } catch (Exception e) {
            log.error("查询达达运费失败", e);
            throw new DadaDeliveryCallError("查询配送费失败");
        }
        Integer deliveryFee = Double.valueOf(resp.getResult().getFee() * 100).intValue();
        order.setDeliveryPreOrderId(resp.getResult().getDeliveryNo());
        order.setDeliveryTime(-1L);
        order.setDeliveryPlatform(Order.DeliveryPlatform.DADA);
        // 40元免运费
        if (order.getTotalAmount() > 4000) {
            order.setDeliveryFeeFromUser(0);
        } else {
            order.setDeliveryFeeFromUser(deliveryFee / 2);
        }
        order.setActualDeliveryFee(deliveryFee);

        if (!profileVariableConfig.isProd()) {
            return true;
        }
        //超过十五块钱就算了
        if (deliveryFee > 1500) {
            return false;
        }
        return true;
    }

    public void deliveryOrderSubmit(Order order,Boolean reAdd) throws DadaDeliveryCallError {
        // 判断是否需要配送，无需配送则直接完成
        if (order.getDeliveryType() == Order.DeliveryType.SELF) {
            return;
        }
        String dadaShopNo = shopService.getById(order.getShopId()).getDadaShopNo();
        QueryDeliveryFee queryDeliveryFee = new QueryDeliveryFee();
        queryDeliveryFee.setShopNo(dadaShopNo).setOriginId(order.getId()).setCityCode("0755").setCargoPrice(order.getTotalAmount() / 100.0).setIsPrepay(0).setReceiverName(order.getContactName()).setReceiverAddress(order.getAddress() + order.getRoomNumber()).setReceiverLng(order.getAddressLongitude()).setReceiverLat(order.getAddressLatitude()).setCargoWeight(1.0).setCallback(profileVariableConfig.getDeliveryCallbackURL()).setReceiverPhone(order.getContactPhone());
        DaDaCommonReq<QueryDeliveryFee> req = new DaDaCommonReq<>();
        req.setBody(queryDeliveryFee);
        req.setSourceId(profileVariableConfig.getDeliverySourceId());
        req.prepareBody();
        DaDaCommonResp<QueryDeliveryFeeResult> resp;
        try {
            if(reAdd){
                resp = daDaDeliveryRemoteService.getService().addOrder(req).execute().body();
            }else{
                resp = daDaDeliveryRemoteService.getService().reAddOrder(req).execute().body();
            }

            log.info("达达下单返回结果：{}", resp);
            if (resp == null || resp.getCode() != 0) {
                throw new DadaDeliveryCallError("提交配送单失败");
            }
            order.setActualDeliveryFee(Double.valueOf(resp.getResult().getFee() * 100).intValue());
            order.setDeliveryStatus(Order.DeliveryStatus.WAIT);
        } catch (Exception e) {
            log.error("提交达达配送单失败", e);
            throw new DadaDeliveryCallError("提交配送单失败");
        }
    }


    /**
     * 填充订单的各类金额，包括商品总价，优惠券抵扣，积分抵扣，运费，实际支付金额等
     */
    private void fillOrder(Order order) {
        // 商品总价
        Integer totalPrice = order.getTotalAmount();
        // 计算优惠券抵扣
        if (order.getDiscountCouponSn() != null) {
            DiscountCoupon coupon = discountCouponService.getById(order.getDiscountCouponSn());
            Long batchId = coupon.getBatchId();
            DiscountCouponBatch batch = discountCouponBatchService.getById(batchId);

            if (batch.getCouponType() == DiscountCouponBatch.CouponType.FULL_REDUCTION) {
                order.setCouponAmount(batch.getDiscountCouponRule().getValue());
            } else {
                order.setCouponAmount(batch.getDiscountCouponRule().getValue() * totalPrice / 100);
            }

        } else {
            order.setCouponAmount(0);
        }

        // 积分抵扣
        if (order.getUseIntegral() != null) {
            order.setIntegralAmount(order.getUseIntegral() * 2);
        } else {
            order.setIntegralAmount(0);
        }

        // 计算订单金额
        Integer orderPrice = totalPrice - order.getCouponAmount() - order.getIntegralAmount();

        order.setPayAmount(orderPrice + order.getDeliveryFeeFromUser());

        if (order.getPayType() == Order.PayType.BALANCE) {
            // 用余额结算
            order.setBalanceAmount(order.getPayAmount());
            order.setPayAmount(order.getPayAmount());
        } else {
            order.setBalanceAmount(0);
            order.setPayAmount(order.getPayAmount());
        }

    }


    private void executePayedPostOrder(Order order) {
        // 扣除优惠券
        if (order.getDiscountCouponSn() != null) {
            discountCouponService.deductCoupon(order.getDiscountCouponSn());
        }
        // 扣除积分
        if (order.getUseIntegral() != null) {
            userService.deductIntegral(order.getUserId(), order.getUseIntegral());
        }
        // 扣除余额
        if (order.getBalanceAmount() != null && order.getBalanceAmount() > 0) {
            userService.deductBalance(order.getUserId(), order.getBalanceAmount());
        }
        // 如果没有填充过支付时间，可以按照当前时间填入
        if (order.getPayTime() == null) {
            order.setPayTime(new Date().getTime());
        }
        // 为了统一厨房操作次序，无论是堂食还是外卖，都需要合并pickUpNumber用于识别次序
        // 但是如果是从美团外卖/饿了么外部下单，并且已经写如果对应的pickUpNumber，那么就不需要再次生成
        if (order.getPickupNumber() == null) {
            order.setPickupNumber(pickupSerialUtils.generate(order.getShopId()));
        }


        // 生成关联积分券
        String couponSn = "-1";
        if (order.getPayAmount() > 0) {
            // 生成积分券
            IntegralCoupon coupon = new IntegralCoupon();
            // 积分向100取整
            coupon.setSn(SerialUtils.generateIntegralSN());
            int integral = order.getPayAmount() / 100;
            if (integral != 0) {
                coupon.setIntegral(integral);
                // 开始时间为当天0点
                coupon.setStartTime(LocalDateTime.of(LocalDate.now(), LocalTime.MIN).toEpochSecond(ZoneOffset.of("+8")) * 1000);
                // 结束时间为14天后的23:59:59
                coupon.setEndTime(LocalDateTime.of(LocalDate.now().plusDays(14), LocalTime.MAX).toEpochSecond(ZoneOffset.of("+8")) * 1000);
                coupon.setStatus(IntegralCoupon.Status.OK);
                if (order.getSource() == Order.Source.SELF) {
                    coupon.setCouponSource(IntegralCoupon.Source.SELF);
                    // 自营获得1.2倍积分
                    coupon.setIntegral(coupon.getIntegral() * 12 / 10);
                } else if (order.getSource() == Order.Source.MT) {
                    coupon.setCouponSource(IntegralCoupon.Source.MT);
                } else if (order.getSource() == Order.Source.ELE) {
                    coupon.setCouponSource(IntegralCoupon.Source.ELE);
                } else {
                    coupon.setCouponSource(IntegralCoupon.Source.SELF);
                }
                integralCouponService.save(coupon);
                couponSn = coupon.getSn();
            }
        }
        // 生成二维码数据
        for (OrderItem item : orderItemService.list(new LambdaQueryWrapper<OrderItem>().eq(OrderItem::getOrderId, order.getId()))) {
            // 生成二维码
            String seq = "QSJ_BOUND" + item.getId() + "QSJ_BOUND" + couponSn;
            String sign = Objects.requireNonNull(DigestUtils.md5(seq)).substring(0, 2);
            String qrCode = getQRCode(couponSn, item.getId(), sign);
            String qrCodeUrl = FileUtil.saveQRCode(item.getOrderId(), item.getId(), qrCode);
            orderItemService.update(new LambdaUpdateWrapper<OrderItem>().set(OrderItem::getQrCode, qrCodeUrl).eq(OrderItem::getId, item.getId()));
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void payCallback(String encryptedBody, String signature, String nonce, String timestamp, String serial, String signType) throws DadaDeliveryCallError, OrderStatusException {
        RequestParam requestParam = new RequestParam.Builder().serialNumber(serial).nonce(nonce).signature(signature).timestamp(timestamp).signType(signType).body(encryptedBody).build();
        JsonObject decrypted = wechatPayService.getNotificationParser().parse(requestParam, JsonObject.class);
        String orderId = decrypted.get("out_trade_no").getAsString();
        String payOrderNo = decrypted.get("transaction_id").getAsString();
        log.info("支付回调解密后的数据:{}", gson.toJson(decrypted));
        if (orderId.startsWith(SerialUtils.PREFIX_ORDER)) {
            Date paySuccessTime = new Date();
            Order order = getById(orderId);
            if (order == null) {
                throw new OrderStatusException("订单不存在");
            }

            if (order.getStatus() == Order.Status.PAID) {
                return;
            }
            if (order.getStatus() != Order.Status.UNPAY) {
                throw new OrderStatusException("订单状态不正确");
            }
            // 更改拉新状态
            QueryWrapper<ShareReward> shareRewardQueryWrapper = new QueryWrapper<>();
            shareRewardQueryWrapper.lambda().eq(ShareReward::getSharedUserId, order.getUserId());
            ShareReward shareReward = shareRewardService.getOne(shareRewardQueryWrapper, false);
            if (shareReward!=null){
                if (shareReward.getStatus() == ShareReward.Status.Invite) {
                    shareReward.setStatus(ShareReward.Status.First_Order);
                }
                Integer curAmount = shareReward.getTotalAmount() + order.getTotalAmount();
                if (shareReward.getStatus() != ShareReward.Status.Rewarded && curAmount > 3000) {
                    // 大于三十满足返现条件
                    shareReward.setStatus(ShareReward.Status.Reward_OK);
                }
                shareReward.setTotalAmount(curAmount);
                shareRewardService.updateById(shareReward);
            }
            // 更改订单状态
            order.setPayOrderNo(payOrderNo);
            order.setPayTime(paySuccessTime.getTime());
            order.setStatus(Order.Status.PAID);
            executePayedPostOrder(order);
            deliveryOrderSubmit(order,false);
            updateById(order);
        } else if (orderId.startsWith(SerialUtils.PREFIX_RECHARGE)) { //充值
            RechargeOrder rechargeOrder = rechargeOrderService.getById(orderId);
            if (rechargeOrder.getOrderStatus() != RechargeOrder.OrderStatus.UNPAY) {
                throw new OrderStatusException("订单状态不正确");
            }
            User user = userService.getById(rechargeOrder.getUserId());
            user.setBalance(user.getBalance() + rechargeOrder.getAmountCharge());
            userService.updateById(user);
            rechargeOrder.setOrderStatus(RechargeOrder.OrderStatus.PAID);
            rechargeOrderService.updateById(rechargeOrder);
        } else if (orderId.startsWith(SerialUtils.PREFIX_VIP_RECHARGE)) {
            RechargeOrder rechargeOrder = rechargeOrderService.getById(orderId);
            if (rechargeOrder.getOrderStatus() != RechargeOrder.OrderStatus.UNPAY) {
                throw new OrderStatusException("订单状态不正确");
            }
            User user = userService.getById(rechargeOrder.getUserId());

            // 如果用户当前不是vip, vip到期时间为当前时间加上一个月
            if (!user.getIsVip()) {
                user.setVipEndTime(LocalDateTime.now().plusMonths(1).toEpochSecond(ZoneOffset.of("+8")) * 1000);
            } else {
                // 如果用户当前是vip, vip到期时间为当前时间加上一个月
                user.setVipEndTime(LocalDateTime.ofEpochSecond(user.getVipEndTime() / 1000, 0, ZoneOffset.of("+8")).plusMonths(1).toEpochSecond(ZoneOffset.of("+8")) * 1000);
            }
            user.setIsVip(true);
            // 为用户分配4张自由卡无门槛优惠券
            List<DiscountCoupon> discountCoupons = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                DiscountCoupon coupon = new DiscountCoupon();
                coupon.setUserId(user.getId());
                coupon.setBatchId(100L); // 100 固定为自由卡无门槛优惠券
                coupon.setSn(SerialUtils.generateSN());
                coupon.setStatus(DiscountCoupon.Status.OK);
                coupon.setValidStartTime(LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8")) * 1000);
                // 有效期为一个月
                coupon.setValidEndTime(LocalDateTime.now().plusMonths(1).toEpochSecond(ZoneOffset.of("+8")) * 1000);
                discountCoupons.add(coupon);
            }
            discountCouponService.saveBatch(discountCoupons);
            rechargeOrder.setOrderStatus(RechargeOrder.OrderStatus.PAID);
            rechargeOrderService.updateById(rechargeOrder);
            userService.updateById(user);
        } else if (orderId.startsWith(SerialUtils.PREFIX_VIP_INCREASE_RECHARGE)) {
            RechargeOrder rechargeOrder = rechargeOrderService.getById(orderId);
            if (rechargeOrder.getOrderStatus() != RechargeOrder.OrderStatus.UNPAY) {
                throw new OrderStatusException("订单状态不正确");
            }
            User user = userService.getById(rechargeOrder.getUserId());


            if (!user.getIsVip()) {
                throw new OrderStatusException("用户不是vip,无法加量");
            }
            // 为用户分配4张自由卡无门槛优惠券
            List<DiscountCoupon> discountCoupons = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                DiscountCoupon coupon = new DiscountCoupon();
                coupon.setUserId(user.getId());
                coupon.setBatchId(100L); // 100 固定为自由卡无门槛优惠券
                coupon.setSn(SerialUtils.generateSN());
                coupon.setStatus(DiscountCoupon.Status.OK);
                coupon.setValidStartTime(LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8")) * 1000);
                // 有效期为一个月
                coupon.setValidEndTime(LocalDateTime.now().plusMonths(1).toEpochSecond(ZoneOffset.of("+8")) * 1000);
                discountCoupons.add(coupon);
            }
            discountCouponService.saveBatch(discountCoupons);
            rechargeOrder.setOrderStatus(RechargeOrder.OrderStatus.PAID);
            rechargeOrderService.updateById(rechargeOrder);
        }
    }

    public void deliveryCallback(DadaDeliveryCallbackVO vo) throws DadaDeliveryCallError {
        if (!vo.verify()) {
            throw new DadaDeliveryCallError("回调签名错误");
        }
        Order order = getById(vo.getOrderId());
        if (order == null) {
            throw new DadaDeliveryCallError("订单不存在");
        }
        if (order.getStatus() == Order.Status.FINISH || order.getDeliveryStatus() == Order.DeliveryStatus.FINISH) {
            return;
        }
        log.info("达达回调订单状态: {}", vo);
        switch (vo.getOrderStatus()) {
            case 1 -> order.setDeliveryStatus(Order.DeliveryStatus.WAIT);
            case 2 -> order.setDeliveryStatus(Order.DeliveryStatus.WAIT_PICKUP);
            case 3 -> order.setDeliveryStatus(Order.DeliveryStatus.DELIVERING);
            case 4 -> {
                order.setDeliveryStatus(Order.DeliveryStatus.FINISH);
                order.setStatus(Order.Status.FINISH);
            }
            case 5 -> {
                deliveryOrderSubmit(order,true);
                order.setDeliveryStatus(Order.DeliveryStatus.CANCEL);
            }
            case 9 -> order.setDeliveryStatus(Order.DeliveryStatus.USER_REFUSE);
            case 10 -> {
                order.setDeliveryStatus(Order.DeliveryStatus.USER_REFUSE_FINISH);
                order.setStatus(Order.Status.FINISH);
            }
            case 100 -> order.setDeliveryStatus(Order.DeliveryStatus.COURIER_ARRIVED);
            default -> throw new DadaDeliveryCallError("订单状态错误");
        }
        this.updateById(order);

    }

    public Boolean resetPickupNumber() {
        Set<String> keys = redisTemplate.keys("shop_pickup_" + "*");
        redisTemplate.delete(keys);
        return null;
    }

    @Override
    public Boolean checkDeliveryStatus() throws IOException,DadaDeliveryCallError {
        // 距离paytime超过十分钟的单+配送状态还是wait的拉出来告警
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Order::getDeliveryType, 1).
                eq(Order::getStatus, Order.Status.PAID).
                eq(Order::getDeliveryStatus, Order.DeliveryStatus.WAIT).
                le(Order::getPayTime, new Date().getTime() - 1000 * 60 * 10);
        List<Order> orders = list(queryWrapper);
        List<String> exceptionOrders = new ArrayList<>();
        // 加小费
        for (Order order : orders) {
            exceptionOrders.add(order.getId());
//            DaDaCommonReq<AddTipDTO> req = new DaDaCommonReq<>();
//            AddTipDTO addTipDTO = new AddTipDTO();
//            addTipDTO.setOrderId(order.getId());
//            Long timeDiffInMilliseconds = new Date().getTime() - (1000 * 60 * 10) - order.getPayTime();
//            Float timeDiffInMinutes = (float) (timeDiffInMilliseconds / 60000.0f);
//            // 四舍五入到最近的分钟数 避免出现59s的情况
//            Float roundedMinutes = (float) Math.round(timeDiffInMinutes * 10) / 10.0f;
//            // 如果小费加的额度小于5,则继续加小费
//            if (timeDiffInMinutes < 5.0) {
//                try {
//                    addTipDTO.setTips(roundedMinutes);
//                    req.setBody(addTipDTO);
//                    log.info("达达订单: {}, 加小费: {}", order.getId(), roundedMinutes);
//                    req.setSourceId(profileVariableConfig.getDeliverySourceId());
//                    req.prepareBody();
//                    daDaDeliveryRemoteService.getService().addTip(req).execute();
//                }
//                catch (Exception e){
//                    log.error("达达配送单加小费失败,订单id: {}, 原因: {}", order.getId(),e);
//                    throw new DadaDeliveryCallError("达达配送单加小费失败");
//                }
//            }
        }
        if (orders.size() != 0) {
            SendEWXRobotMessageDTO dto = new SendEWXRobotMessageDTO();
            dto.setMsgType("text");
            dto.setText(new SendEWXRobotMessageText("存在距离支付时间十分钟但未有骑手接单的订单,这些订单编号:\n" + String.join("\n", exceptionOrders))
            );
            ewxRobotRemoteService.getService().sendMessage(dto).execute();
        }
        return true;
    }

    @Override
    public HeatRecordResultVO getHeatRecord(Long userId, HeatRecordVO vo) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Order::getUserId, userId).
                eq(Order::getStatus, Order.Status.FINISH).
                orderByAsc(Order::getCreateTime);
        List<Order> orders = list(queryWrapper);
        HeatRecordResultVO recordResultVO = new HeatRecordResultVO();
        List<Integer> weekHeat = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 0));
        LocalDate now = LocalDate.now();
        // 获取本周起始日期和结束日期
        LocalDate startOfWeek = now.with(DayOfWeek.MONDAY);
        LocalDate endOfWeek = now.with(DayOfWeek.SUNDAY);
        if (vo.getWeek() != 0) {
            startOfWeek = startOfWeek.minus(vo.getWeek(), ChronoUnit.WEEKS);
            endOfWeek = endOfWeek.minus(vo.getWeek(), ChronoUnit.WEEKS);
        }
        // 遍历本周的每一天
        LocalDate date = startOfWeek;
        Integer i = 0;
        while (!date.isAfter(endOfWeek)) {
            // 获取当天的起始时间和结束时间
            Instant startInstant = date.atStartOfDay().toInstant(ZoneOffset.UTC);
            Long startTimestamp = startInstant.toEpochMilli();
            Instant endInstant = date.atTime(LocalTime.MAX).toInstant(ZoneOffset.UTC);
            Long endTimestamp = endInstant.toEpochMilli();

            for (Order order : orders) {
                // 符合条件的订单计算热量
                if (order.getCreateTime() >= startTimestamp && order.getCreateTime() <= endTimestamp) {
                    List<OrderItem> items = orderItemService.list(new LambdaQueryWrapper<OrderItem>().eq(OrderItem::getOrderId, order.getId()));
                    Integer totalEnergy = 0;
                    for (OrderItem item : items) {
                        Product product = productService.getById(item.getProductId());
                        totalEnergy += product.getEnergy();
                    }
                    weekHeat.set(i, totalEnergy);
                }
            }

            // 跳转到下一天
            i++;
            date = date.plusDays(1);
        }
        recordResultVO.setWeekHeat(weekHeat);
        Double average = weekHeat.stream().filter(k -> k != 0).mapToInt(k -> k).average().orElse(0);

        // 使用Stream API求最大值
        int max = weekHeat.stream().mapToInt(k -> k).max().orElse(0);
        recordResultVO.setMaxHeat(max);
        recordResultVO.setAverageHeat(average);
        return recordResultVO;
    }
}



