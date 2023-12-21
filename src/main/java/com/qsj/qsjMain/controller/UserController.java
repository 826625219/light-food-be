package com.qsj.qsjMain.controller;

import com.qsj.qsjMain.exception.SignatureError;
import com.qsj.qsjMain.model.Message;
import com.qsj.qsjMain.model.ResultCode;
import com.qsj.qsjMain.model.entity.UserAddressBook;
import com.qsj.qsjMain.model.vo.*;
import com.qsj.qsjMain.service.impl.BalanceHistoryService;
import com.qsj.qsjMain.service.impl.ShopServiceImpl;
import com.qsj.qsjMain.service.impl.UserAddressBookServiceImpl;
import com.qsj.qsjMain.service.impl.UserServiceImpl;
import com.qsj.qsjMain.utils.CredentialUtils;
import com.qsj.qsjMain.utils.WXUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.io.IOException;
import java.util.List;

/**
 * @author wuweiliang
 * order controller
 */
@RestController
@RequestMapping("api/v1/user")
@Api(tags = "用户接口")
public class UserController {

    private final UserServiceImpl userService;

    private final UserAddressBookServiceImpl userAddressBookService;

    private final BalanceHistoryService balanceHistoryService;
    private final ShopServiceImpl shopService;

    public UserController(UserServiceImpl userService, UserAddressBookServiceImpl userAddressBookService, BalanceHistoryService balanceHistoryService, ShopServiceImpl shopService) {
        this.userService = userService;
        this.userAddressBookService = userAddressBookService;
        this.balanceHistoryService = balanceHistoryService;
        this.shopService = shopService;
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ApiOperation("用户登录")
    public Message<String> login(@RequestBody LoginRequestVO vo) throws IOException, SignatureError {
        return Message.ok(userService.login(vo));
    }

    @RequestMapping(value = "address", method = RequestMethod.POST)
    @ApiOperation("添加或者更新地址")
    public Message<Boolean> updateAddress(@RequestAttribute @ApiIgnore Long userId, @RequestBody CreateUpdateAddressVO vo) {
        return Message.ok(userAddressBookService.createOrUpdateAddressBook(userId, vo));
    }

    @RequestMapping(value = "removeAddress", method = RequestMethod.POST)
    @ApiOperation("删除地址")
    public Message<Boolean> updateAddress(@RequestAttribute @ApiIgnore Long userId, @RequestBody RemoveAddressVO vo) {
        return Message.ok(userAddressBookService.removeAddressBook(userId, vo.getId()));
    }

    @RequestMapping(value = "getAddress", method = RequestMethod.GET)
    @ApiOperation("获得用户的地址列表")
    public Message<List<UserAddressBook>> updateAddress(@RequestAttribute @ApiIgnore Long userId) {
        return Message.ok(userAddressBookService.getAddressBookList(userId));
    }

    @RequestMapping(value = "useIntegralCoupon", method = RequestMethod.POST)
    @ApiOperation("使用积分券获得积分")
    public Message<AddIntegralResultVO> useCoupon(@RequestAttribute @ApiIgnore Long userId, @RequestBody UserAddIntegralVO vo) {
        return Message.ok(userService.useIntegralCoupon(userId, vo.getCouponSn()));
    }

    @RequestMapping(value = "switchNutritionPlan", method = RequestMethod.POST)
    @ApiOperation("切换营养计划")
    public Message<SwitchPlanResultVO> switchNutritionPlan(@RequestAttribute @ApiIgnore Long userId, @RequestBody SwitchNutritionPlanVO vo) {
        return Message.ok(userService.switchNutritionPlan(userId, vo));
    }

    @RequestMapping(value = "getIntegral", method = RequestMethod.GET)
    @ApiOperation("获取用户积分")
    public Message<Integer> getIntegral(@RequestAttribute @ApiIgnore Long userId) {
        return Message.ok(userService.getIntegral(userId));
    }

    @RequestMapping(value = "updateProfile", method = RequestMethod.POST)
    @ApiOperation("更新用户信息")
    public Message<Boolean> updateProfile(@RequestAttribute @ApiIgnore Long userId, @RequestBody UpdateProfileVO vo) {
        return Message.ok(userService.updateProfile(userId, vo));
    }

    @RequestMapping(value = "getProfile", method = RequestMethod.GET)
    @ApiOperation("获取用户信息")
    public Message<ProfileResultVO> getProfile(@RequestAttribute @ApiIgnore Long userId) {
        return Message.ok(userService.getProfile(userId));
    }

    @RequestMapping(value = "recharge", method = RequestMethod.POST)
    @ApiOperation("用户充值")
    public Message<PrepayResultWithSignVO> recharge(@RequestAttribute @ApiIgnore Long userId, @RequestBody RechargeVO vo) {
        return Message.ok(userService.recharge(userId, vo.getAmount()));
    }

    @RequestMapping(value = "buyVip", method = RequestMethod.POST)
    @ApiOperation("购买vip服务/vip增值服务")
    public Message<PrepayResultWithSignVO> buyVip(@RequestAttribute @ApiIgnore Long userId, @RequestBody RechargeVO vo) {
        return Message.ok(userService.buyVip(userId, vo.getIncreaseFlag()));
    }

    @RequestMapping(value = "getBalanceRecord", method = RequestMethod.GET)
    @ApiOperation("获取用户余额流水")
    public Message<List<BalanceRecordVO>> getBalanceRecord(@RequestAttribute @ApiIgnore Long userId) {
        return Message.ok(balanceHistoryService.getBalanceRecord(userId));
    }

    @RequestMapping(value = "msgNotify", method = RequestMethod.GET)
    @ApiOperation("微信消息回调监测")
    public String payNotifyCheck(@RequestParam String signature, @RequestParam Long timestamp,
                                 @RequestParam String nonce, @RequestParam(value = "echostr") String echoStr) {
        {
            if (!WXUtils.msgSignatureCheck(signature, timestamp.toString(), nonce)) {
                return "bad signature";
            }
            return echoStr;
        }
    }

    @RequestMapping(value = "msgNotify", method = RequestMethod.POST)
    @ApiOperation("微信消息回调")
    public String payNotify(@RequestParam String signature, @RequestParam Long timestamp,
                            @RequestParam String nonce,
                            @RequestBody CSCallbackVO vo){
        {
            if (!WXUtils.msgSignatureCheck(signature, timestamp.toString(), nonce)) {
                return "invalid signature";
            }
            userService.csChatBiz(vo);
            return "success";
        }
    }

    @RequestMapping(value = "resetGpt", method = RequestMethod.POST)
    @ApiOperation("每天用户的GPT聊天条数清空")
    public Message<Boolean> resetGpt(@RequestAttribute @ApiIgnore Long userId) {
        if(!CredentialUtils.isAdminRequest(userId)) {
            return Message.error(ResultCode.BAD_REQUEST);
        }
        return Message.ok(userService.resetGpt());
    }

    @RequestMapping(value = "getShop", method = RequestMethod.GET)
    @ApiOperation("获得用户周围门店列表")
    public Message<List<ShopListVO>> getShop(@RequestAttribute @ApiIgnore Long userId,
                                             Double latitude, Double longitude) {
        return Message.ok(shopService.getShop(latitude, longitude));
    }


//    @RequestMapping(value = "changeShopStatus", method = RequestMethod.POST)
//    @ApiOperation("修改门店营业状态")
//    public Message<Boolean> changeShopStatus(@RequestBody ChangeShopStatusVO vo) {
//        return Message.ok(userService.changeShopStatus(vo));
//    }

    @RequestMapping(value = "autoCloseShops", method = RequestMethod.POST)
    @ApiOperation("自动打烊")
    public Message<Boolean> autoShopClose(@RequestAttribute @ApiIgnore Long userId) {
        if(!CredentialUtils.isAdminRequest(userId)) {
            return Message.error(ResultCode.BAD_REQUEST);
        }
        return Message.ok(shopService.autoCloseShops());
    }

    @RequestMapping(value = "autoStartShops", method = RequestMethod.POST)
    @ApiOperation("自动开业")
    public Message<Boolean> autoShopStart(@RequestAttribute @ApiIgnore Long userId) {
        if(!CredentialUtils.isAdminRequest(userId)) {
            return Message.error(ResultCode.BAD_REQUEST);
        }
        return Message.ok(shopService.autoStartShops());
    }

}
