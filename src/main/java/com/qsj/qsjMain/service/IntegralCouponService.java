package com.qsj.qsjMain.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qsj.qsjMain.model.entity.IntegralCoupon;

/**
 *
 */
public interface IntegralCouponService extends IService<IntegralCoupon> {
    void deductCoupon(String couponId);
}
