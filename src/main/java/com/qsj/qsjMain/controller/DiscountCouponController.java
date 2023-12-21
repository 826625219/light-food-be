package com.qsj.qsjMain.controller;

import com.qsj.qsjMain.model.Message;
import com.qsj.qsjMain.model.entity.DiscountCoupon;
import com.qsj.qsjMain.model.vo.ReceiveCouponVO;
import com.qsj.qsjMain.service.impl.DiscountCouponServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@RestController
@RequestMapping("api/v1/discountCoupon")
@Api(tags = "优惠券接口")
public class DiscountCouponController {
    private final DiscountCouponServiceImpl discountCouponService;

    public DiscountCouponController(DiscountCouponServiceImpl discountCouponService) {
        this.discountCouponService = discountCouponService;
    }


    @RequestMapping(value = "coupons", method = RequestMethod.GET)
    @ApiOperation("获取优惠券列表,包括未使用和未领取的")
    public Message<List<DiscountCoupon>> getCoupons(@RequestAttribute @ApiIgnore Long userId) {
        return Message.ok(discountCouponService.getCoupons(userId));
    }

    @RequestMapping(value = "receiveCoupon", method = RequestMethod.POST)
    @ApiOperation("领取优惠券")
    public Message<Boolean> receiveCoupon(@RequestAttribute @ApiIgnore Long userId,@RequestBody ReceiveCouponVO vo) {
        return Message.ok(discountCouponService.receiveCoupon(userId,vo));
    }
}
