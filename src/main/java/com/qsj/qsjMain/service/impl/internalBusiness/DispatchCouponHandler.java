package com.qsj.qsjMain.service.impl.internalBusiness;

import com.qsj.qsjMain.model.entity.DiscountCoupon;
import com.qsj.qsjMain.service.impl.DiscountCouponServiceImpl;
import com.qsj.qsjMain.service.impl.ShopServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class DispatchCouponHandler extends BaseCommandHandler{

    final DiscountCouponServiceImpl discountCouponService;

    final ShopServiceImpl shopService;

    public DispatchCouponHandler(DiscountCouponServiceImpl discountCouponService, ShopServiceImpl shopService) {
        this.discountCouponService = discountCouponService;
        this.shopService = shopService;
    }

    @Override
    public String handle(String msg, String fromUser) {
        if(!shopService.isSuperUser(fromUser)){
            return "您没有权限发券！";
        }
        String[] split = msg.split(" ");
        String userId = split[1];
        String batchId = split[2];
        DiscountCoupon coupon = discountCouponService.dispatchCoupon(Long.parseLong(userId), Long.parseLong(batchId));
        return String.format("发券成功！券码SN为: %s", coupon.getSn());
    }

    @Override
    public String getPattern() {
        return "发券 (\\d+) (\\d+)";
    }
}
