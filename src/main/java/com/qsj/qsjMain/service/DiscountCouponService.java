package com.qsj.qsjMain.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qsj.qsjMain.model.entity.DiscountCoupon;
import com.qsj.qsjMain.model.entity.Order;
import com.qsj.qsjMain.model.vo.ReceiveCouponVO;
import com.qsj.qsjMain.model.vo.SubmitOrderVO;

import java.util.List;


/**
 *
 */
public interface DiscountCouponService extends IService<DiscountCoupon> {

    boolean canUse(SubmitOrderVO submitOrderVO, Long userId);

    /**
     * 检查优惠券是否满足使用条件，也可以是未领取的券，引导用户领取
     * @param order 订单
     * @param couponSn 优惠券sn
     * @return true: 可以使用，false: 不可以使用
     */
    boolean matchCondition(Order order, String couponSn);

    void deductCoupon(String couponId);

    /**
     * 获取用户的优惠券列表
     * @param userId 用户id
     * @return 优惠券列表
     */
    List<DiscountCoupon> getCoupons(Long userId);

    /**
     * 用户领取优惠券
     * @param userId 用户id
     * @param vo 领取券vo
     * @return 领取结果
     */
    boolean receiveCoupon(Long userId,ReceiveCouponVO vo);

    public Long getCouponsAmount(Long userId);

}
