package com.qsj.qsjMain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qsj.qsjMain.model.entity.*;
import com.qsj.qsjMain.model.mapper.DiscountCouponMapper;
import com.qsj.qsjMain.model.vo.ReceiveCouponVO;
import com.qsj.qsjMain.model.vo.SubmitOrderVO;
import com.qsj.qsjMain.service.DiscountCouponBatchService;
import com.qsj.qsjMain.service.DiscountCouponService;
import com.qsj.qsjMain.service.OrderItemService;
import com.qsj.qsjMain.utils.SerialUtils;
import org.springframework.stereotype.Service;


import java.util.Date;
import java.util.List;
import java.util.Objects;


/**
 * 用户服务实现类
 */
@Service
public class DiscountCouponServiceImpl extends ServiceImpl<DiscountCouponMapper, DiscountCoupon>
        implements DiscountCouponService {

    private final DiscountCouponBatchService discountCouponBatchService;
    private final OrderItemService orderItemService;

    public DiscountCouponServiceImpl(DiscountCouponBatchService discountCouponBatchService, OrderItemService orderItemService) {
        this.discountCouponBatchService = discountCouponBatchService;
        this.orderItemService = orderItemService;
    }

    @Override
    public boolean canUse(SubmitOrderVO submitOrderVO, Long userId) {
        // 不可以使用不属于自己的优惠券
        return Objects.equals(userId, this.getById(submitOrderVO.getUseDiscountCouponId()).getUserId());
    }

    @Override
    public boolean matchCondition(Order order, String couponSn) {
        DiscountCoupon coupon = getById(couponSn);
        // 优惠券不存在
        if (coupon == null) {
            return false;
        }
        // 优惠券已过期
        if (coupon.getValidEndTime() < new Date().getTime()) {
            return false;
        }

        // 优惠券不满足使用条件
        Long batchId = coupon.getBatchId();
        DiscountCouponBatch batch = discountCouponBatchService.getById(batchId);
        DiscountCouponRule rule = batch.getDiscountCouponRule();
        if (rule.getConditionType() == DiscountCouponRule.ConditionType.FULL_REDUCTION) {
            return order.getTotalAmount() >= rule.getConditionValue();
        }
        if (rule.getConditionType() == DiscountCouponRule.ConditionType.DISCOUNT) {
            return order.getTotalAmount() >= rule.getConditionValue();
        }
        if (rule.getConditionType() == DiscountCouponRule.ConditionType.SPECIFIED_GOODS) {
            List<OrderItem> orderItems = orderItemService.list(new QueryWrapper<OrderItem>().lambda().eq(OrderItem::getOrderId, order.getId()));
            for (OrderItem orderItem : orderItems) {
                if (Objects.equals(rule.getCommodityId(), orderItem.getProductId())) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    @Override
    public void deductCoupon(String couponId) {
        UpdateWrapper<DiscountCoupon> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(DiscountCoupon::getSn, couponId).set(DiscountCoupon::getStatus, DiscountCoupon.Status.USED);
        update(updateWrapper);
    }

    @Override
    public List<DiscountCoupon> getCoupons(Long userId) {
        LambdaQueryWrapper<DiscountCoupon> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DiscountCoupon::getUserId, userId).in(DiscountCoupon::getStatus, DiscountCoupon.Status.OK, DiscountCoupon.Status.ALLOT);

        List<DiscountCoupon> coupons = list(wrapper);
        for (DiscountCoupon coupon : coupons) {
            coupon.setRule(discountCouponBatchService.getById(coupon.getBatchId()).getDiscountCouponRule());
        }
        return coupons;
    }

    @Override
    public Long getCouponsAmount(Long userId) {
        LambdaQueryWrapper<DiscountCoupon> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DiscountCoupon::getUserId, userId).eq(DiscountCoupon::getStatus, DiscountCoupon.Status.OK);
        return  count(wrapper);
    }

    @Override
    public boolean receiveCoupon(Long userId, ReceiveCouponVO vo) {
        DiscountCoupon coupon = getById(vo.getSn());
        if (coupon == null) {
            return false;
        }
        // 需要是已分配，待领取状态
        if (coupon.getStatus() != DiscountCoupon.Status.ALLOT) {
            return false;
        }
        // 需要在有效期之内
        if (coupon.getValidStartTime() > new Date().getTime()) {
            return false;
        }
        if (coupon.getValidEndTime() < new Date().getTime()) {
            return false;
        }
        if (!coupon.getUserId().equals(userId)) {
            return false;
        }
        coupon.setStatus(DiscountCoupon.Status.OK);
        return updateById(coupon);
    }

    public DiscountCoupon dispatchCoupon(Long userId, Long batchId) {
        DiscountCoupon coupon = new DiscountCoupon();
        coupon.setUserId(userId);
        coupon.setBatchId(batchId);
        coupon.setStatus(DiscountCoupon.Status.OK);
        coupon.setReceiveTime(new Date().getTime());
        coupon.setSn(SerialUtils.generateSN());
        coupon.setValidStartTime(new Date().getTime());
        coupon.setValidEndTime(new Date().getTime() + 1000L * 60 * 60 * 24 * 30);
        save(coupon);
        return coupon;
    }
}
