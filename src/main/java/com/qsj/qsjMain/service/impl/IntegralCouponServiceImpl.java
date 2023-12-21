package com.qsj.qsjMain.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qsj.qsjMain.model.entity.IntegralCoupon;
import com.qsj.qsjMain.model.mapper.IntegralCouponMapper;
import com.qsj.qsjMain.service.IntegralCouponService;
import org.springframework.stereotype.Service;


/**
 * 用户服务实现类
 */
@Service
public class IntegralCouponServiceImpl extends ServiceImpl<IntegralCouponMapper, IntegralCoupon>
        implements IntegralCouponService {
    @Override
    public void deductCoupon(String couponId) {
        UpdateWrapper<IntegralCoupon> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(IntegralCoupon::getSn, couponId).set(IntegralCoupon::getStatus, IntegralCoupon.Status.USED);
        update(updateWrapper);
    }
}
