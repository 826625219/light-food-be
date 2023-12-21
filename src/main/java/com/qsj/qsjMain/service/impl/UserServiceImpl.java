package com.qsj.qsjMain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qsj.qsjMain.config.ProfileVariableConfig;
import com.qsj.qsjMain.exception.SignatureError;
import com.qsj.qsjMain.model.entity.*;
import com.qsj.qsjMain.model.mapper.UserMapper;
import com.qsj.qsjMain.model.vo.*;
import com.qsj.qsjMain.remote.service.GPTProxyRemoteService;
import com.qsj.qsjMain.remote.service.WXApiRemoteService;
import com.qsj.qsjMain.remote.service.model.dto.ChatMessageDTO;
import com.qsj.qsjMain.remote.service.model.dto.SendCSMessageDTO;
import com.qsj.qsjMain.remote.service.model.dto.SendCSMessageDetailDTO;
import com.qsj.qsjMain.remote.service.model.vo.GptMessageResp;
import com.qsj.qsjMain.remote.service.model.vo.WXJSCode2SessionResp;
import com.qsj.qsjMain.service.IntegralCouponService;
import com.qsj.qsjMain.service.NutritionPlanService;
import com.qsj.qsjMain.service.RechargeOrderService;
import com.qsj.qsjMain.service.UserService;
import com.qsj.qsjMain.utils.CredentialUtils;
import com.qsj.qsjMain.utils.FileUtil;
import com.qsj.qsjMain.utils.NickNameUtils;
import com.qsj.qsjMain.utils.SerialUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 用户服务实现类
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {
    private final WXApiRemoteService wxApiRemoteService;
    private final IntegralCouponService integralCouponService;
    private final NutritionPlanService nutritionPlanService;

    private final WechatPayService wechatPayService;

    private final RechargeOrderService rechargeOrderService;

    private final DiscountCouponServiceImpl discountCouponService;

    private final ShareRewardServiceImpl shareRewardService;

    private final GPTProxyRemoteService gptProxyRemoteService;

    private final ProfileVariableConfig profileVariableConfig;

    @Resource
    private RedisTemplate<String, Long> redisTemplate;

    public UserServiceImpl(WXApiRemoteService wxApiRemoteService,
                           IntegralCouponService integralCouponService, NutritionPlanService nutritionPlanService, WechatPayService wechatPayService, RechargeOrderService rechargeOrderService,
                           DiscountCouponServiceImpl discountCouponService, ShareRewardServiceImpl shareRewardService, GPTProxyRemoteService gptProxyRemoteService, ProfileVariableConfig profileVariableConfig) {

        this.wxApiRemoteService = wxApiRemoteService;
        this.integralCouponService = integralCouponService;
        this.nutritionPlanService = nutritionPlanService;
        this.wechatPayService = wechatPayService;
        this.rechargeOrderService = rechargeOrderService;
        this.discountCouponService = discountCouponService;
        this.shareRewardService = shareRewardService;
        this.gptProxyRemoteService = gptProxyRemoteService;
        this.profileVariableConfig = profileVariableConfig;
    }

    public String login(LoginRequestVO vo) throws IOException, SignatureError {
        WXJSCode2SessionResp resp = wxApiRemoteService.getSecretService().loginByCode(vo.getCode()).execute().body();
        if (Objects.isNull(resp)) {
            throw new SignatureError("wx login failed");
        }
        String openId = resp.getOpenId();
        String unionId = resp.getUnionId();
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getWxOpenId, openId);
        User user = getOne(queryWrapper);
        if (user == null) {
            user = new User();
            user.setWxOpenId(openId).setWxUnionId(unionId).setWxSessionKey(resp.getSessionKey()).
                    setCredentialSalt(CredentialUtils.generateRandomSalt()).
                    setBalance(0).setIntegral(10).
                    setWxAvatarUrl("https://static.lightmeal-service.com/static/img/default_avatar.png").
                    setWxNickName(NickNameUtils.getName()).
                    setIsVip(false).setNutritionPlanId(1L).setStatus(0).setLastLoginTime(new Date().getTime());
            save(user);

            // 拉新逻辑处理
            if (vo.getShareFrom() != null) {
                ShareReward shareReward = new ShareReward();
                shareReward.setStatus(ShareReward.Status.Invite);
                shareReward.setSharedUserId(user.getId());
                shareReward.setShareUserId(vo.getShareFrom());
                shareReward.setTotalAmount(0);
                shareRewardService.save(shareReward);
            }
            //发券
                DiscountCoupon coupon = new DiscountCoupon();
                coupon.setUserId(user.getId());
                coupon.setBatchId(101L); // 101 固定为新客优惠券
                coupon.setSn(SerialUtils.generateSN());
                coupon.setStatus(DiscountCoupon.Status.OK);
                coupon.setValidStartTime(LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8")) * 1000);
                // 有效期为一个月
                coupon.setValidEndTime(LocalDateTime.now().plusMonths(1).toEpochSecond(ZoneOffset.of("+8")) * 1000);
                discountCouponService.save(coupon);


        } else {
            user.setWxSessionKey(resp.getSessionKey()).setCredentialSalt(CredentialUtils.generateRandomSalt())
                    .setLastLoginTime(new Date().getTime());
            updateById(user);
        }
        return CredentialUtils.encrypt(String.join("|BOUND|",
                user.getId().toString(),
                user.getCredentialSalt(),
                Long.valueOf(new Date().getTime()).toString()));
    }

    @Override
    public void deductBalance(Long userId, Integer amount) {
        User user = getById(userId);
        user.setBalance(Math.max(user.getBalance() - amount, 0));
        updateById(user);
    }

    @Override
    public void deductIntegral(Long userId, Integer amount) {
        User user = this.getById(userId);
        user.setIntegral(Math.max(user.getIntegral() - amount, 0));
        updateById(user);
    }

    @Transactional(rollbackFor = Exception.class)
    public AddIntegralResultVO useIntegralCoupon(Long userId, String sn) {
        User user = getById(userId);

        QueryWrapper<IntegralCoupon> wrapper = new QueryWrapper<>();
//        构造查询条件
        wrapper.lambda().eq(IntegralCoupon::getSn, sn);
        IntegralCoupon coupon = integralCouponService.getOne(wrapper);
        if (coupon == null) {
            return AddIntegralResultVO.fail(AddIntegralResultVO.Status.NOT_EXIST);
        }

        // already used
        if (coupon.getStatus() == IntegralCoupon.Status.USED) {
            return AddIntegralResultVO.fail(AddIntegralResultVO.Status.USED);
        }

        // expired
        if (coupon.getEndTime() < new Date().getTime()) {
            return AddIntegralResultVO.fail(AddIntegralResultVO.Status.EXPIRED);
        }

        coupon.setStatus(IntegralCoupon.Status.USED);
        user.setIntegral(user.getIntegral() + coupon.getIntegral());
        updateById(user);
        integralCouponService.updateById(coupon);
        return new AddIntegralResultVO(AddIntegralResultVO.Status.OK, coupon.getIntegral());
    }


    public Long verifyAndExtract(String encryptedCredential) {
        if (!StringUtils.hasText(encryptedCredential)) {
            return null;
        }
        String decryptedCredential = CredentialUtils.decrypt(encryptedCredential);
        if (decryptedCredential == null) {
            return null;
        }
        String[] splitCredentials = decryptedCredential.split("\\|BOUND\\|");
        if (splitCredentials.length != 3) {
            return null;
        }
        Integer userId = Integer.parseInt(splitCredentials[0]);
        String randomSalt = splitCredentials[1];
        Long timestamp = Long.parseLong(splitCredentials[2]);
        User user = getById(userId);
        if (user == null) {
            return null;
        }
        if (!Objects.equals(user.getCredentialSalt(), randomSalt)) {
            return null;
        }
//        if (new Date().getTime() - timestamp > 1000 * 60 * 60 * 24 * 7) {
//            return false;
//        }
        return user.getId();
    }

    @Transactional(rollbackFor = Exception.class)
    public SwitchPlanResultVO switchNutritionPlan(Long userId, SwitchNutritionPlanVO vo) {
        // 检查计划是否存在
        NutritionPlan nutritionPlan = nutritionPlanService.getById(vo.getId());
        if (nutritionPlan == null) {
            return new SwitchPlanResultVO(false, 0, false);
        }

        // 用户是否第一次填写完善信息
        User user = getById(userId);
        user.setNutritionPlanId(vo.getId()).setGender(vo.getGender()).
                setHeight(vo.getHeight()).setWeight(vo.getWeight()).
                setBirthday(vo.getBirthday()).setPractise(vo.getPractise());
        // 用户是否第一次填写完善信息
        if (!user.getHasFilledHealthInfo()) {
            user.setHasFilledHealthInfo(true);
            // 为用户增加积分
            user.setIntegral(user.getIntegral() + 100);
            updateById(user);
            return new SwitchPlanResultVO(true, 100, true);
        } else {
            updateById(user);
            return new SwitchPlanResultVO(true, 0, false);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean updateProfile(Long userId, UpdateProfileVO vo) {
        LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(User::getId, userId);
        Optional.ofNullable(vo.getBirthday()).ifPresent(v -> wrapper.set(User::getBirthday, v));
        Optional.ofNullable(vo.getGender()).ifPresent(v -> wrapper.set(User::getGender, v));
        Optional.ofNullable(vo.getHeight()).ifPresent(v -> wrapper.set(User::getHeight, v));
        Optional.ofNullable(vo.getWeight()).ifPresent(v -> wrapper.set(User::getWeight, v));
        Optional.ofNullable(vo.getPractise()).ifPresent(v -> wrapper.set(User::getPractise, v));
        Optional.ofNullable(vo.getNickName()).ifPresent(v -> wrapper.set(User::getWxNickName, v));
        if (StringUtils.hasText(vo.getAvatarUrl()) && !vo.getAvatarUrl().startsWith("http")) {
            // we should save avatar to local file system
            // TODO: for distributed system, we should use distributed file system in the future
            String avatarUrl = FileUtil.saveAvatar(userId, vo.getAvatarUrl());
            wrapper.set(User::getWxAvatarUrl, avatarUrl);
        }
        return update(wrapper);
    }

    @Override
    public ProfileResultVO getProfile(Long userId) {
        User user = getById(userId);
        ProfileResultVO resultVO = new ProfileResultVO();
        resultVO.setAvatarUrl(user.getWxAvatarUrl()).setNickName(user.getWxNickName()).
                setGender(user.getGender()).setBirthday(user.getBirthday()).
                setBalance(user.getBalance()).setIntegral(user.getIntegral()).
                setNutritionPlanId(user.getNutritionPlanId()).setHeight(user.getHeight()).
                setIsVip(user.getIsVip()).
                setVipExpireTime(user.getVipEndTime()).
                setEnergyRef(user.getEnergyRef()).
                setCouponNum(discountCouponService.getCouponsAmount(userId)).
                setWeight(user.getWeight()).setPractise(user.getPractise()).setUserId(userId);
        return resultVO;
    }

    public Integer getIntegral(Long userId) {
        return getById(userId).getIntegral();
    }

    @Override
    public PrepayResultWithSignVO recharge(Long userId, Integer amount) {
        // 判断充值金额是否符合档位
        if (!RechargeOrder.AMOUNTS.contains(amount)) {
            return PrepayResultWithSignVO.fail(PrepayResultWithSignVO.PayStatus.INVALID_AMOUNT);
        }
        User user = getById(userId);
        RechargeOrder order = new RechargeOrder();
        order.setOrderId(SerialUtils.generateRecharge()).setOrderStatus(RechargeOrder.OrderStatus.UNPAY)
                .setAmountPay(amount).setAmountCharge(RechargeOrder.mapAmountToCharge(amount)).setUserId(userId)
                .setRechargeType(RechargeOrder.OrderType.BALANCE);
        rechargeOrderService.save(order);
        //调起微信支付
        return PrepayResultWithSignVO.
                preOrderSuccess(wechatPayService.prepayAndSign(
                        amount,
                        user.getWxOpenId(),
                        order.getOrderId(),
                        order.getOrderId()));
    }

    @Override
    public PrepayResultWithSignVO buyVip(Long userId, Boolean increaseFlag) {
        User user = getById(userId);
        int amount;
        RechargeOrder order = new RechargeOrder();
        String payOrderDesc;
        if (increaseFlag) {
            order.setOrderId(SerialUtils.generateVipIncrease()).setRechargeType(RechargeOrder.OrderType.VIP_INCREASE);
            payOrderDesc = "4张5元抵扣红包";
            amount = 880;
        } else {
            order.setOrderId(SerialUtils.generateVipRecharge()).setRechargeType(RechargeOrder.OrderType.VIP);
            payOrderDesc = "SAAKAD会员-30天";
            amount = 990;
        }
        if (!profileVariableConfig.isProd()) {
            amount = 1;
        }
        order.setOrderStatus(RechargeOrder.OrderStatus.UNPAY)
                .setAmountPay(amount).setAmountCharge(0).setUserId(userId);

        rechargeOrderService.save(order);
        return PrepayResultWithSignVO.
                preOrderSuccess(wechatPayService.prepayAndSign(
                        amount,
                        user.getWxOpenId(),
                        order.getOrderId(),
                        payOrderDesc));
    }

    public boolean csChatBiz(CSCallbackVO vo) {
        String fromUser = vo.getFromUserName();
        String event = vo.getEvent();
        String msgType = vo.getMsgType();
        boolean delegateByGpt = false;
        String replyContent = "";
        if ("event".equals(msgType) && "kf_close_session".equals(event)) {
            replyContent = "当前客服不在线";
        } else if ("event".equals(msgType) && "user_enter_tempsession".equals(event)) {
            String userEnterKey = "user_enter_first_" + fromUser;
            // 注意还得是半小时内第一次进
            if (!redisTemplate.hasKey(userEnterKey)) {
                replyContent = "您好，我是基于ChatGPT驱动的，专属于您的SAAKAD健康与菜品管理师，有什么可以帮到您的吗？";
                redisTemplate.opsForValue().set(userEnterKey, 1L, 30, TimeUnit.MINUTES);
            } else {
                replyContent = "欢迎回来! 有什么可以帮到您的吗？";
            }
        } else if ("text".equals(msgType)) {
            delegateByGpt = true;
        }


        // 在新线程中发送消息，并直接返回
        String finalReplyContent = replyContent;
        Boolean finalDelegateByGpt = delegateByGpt;
        new Thread(() -> {
            try {
                sendChatMessage(fromUser, finalReplyContent, vo, finalDelegateByGpt);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        return true;
    }

    public void sendChatMessage(String toUser, String content, CSCallbackVO vo, Boolean delegateByGPT) throws IOException {
        String replyContent = content;
        if (delegateByGPT) {
            boolean intervalThrottle = false;
            String userIntervalThrottleKey = "user_throttle_" + toUser;
            if (Boolean.TRUE.equals(redisTemplate.hasKey(userIntervalThrottleKey))) {
                intervalThrottle = true;
            }
            // 查看积分是否足够,足够通过并且积分减一,不够的话进行提示
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(User::getWxOpenId, toUser);
            User user = getOne(queryWrapper);
            if (user.getIntegral() <= 0) {
                replyContent = "您的积分不足,可通过下单本店主食菜品,扫描餐盒上热量信息二维码获取积分";
            } else if (intervalThrottle) {
                replyContent = "您说的太快了，我还没反应过来呢。请10秒后再试试。";
            } else {
                // 减少积分
                user.setIntegral(user.getIntegral() - 1);
                updateById(user);
                redisTemplate.opsForValue().set(userIntervalThrottleKey, 1L, 10, TimeUnit.SECONDS);
                ChatMessageDTO proxyDTO = new ChatMessageDTO();
                proxyDTO.setContent(vo.getContent());
                proxyDTO.setContext("");
                proxyDTO.setUid(vo.getFromUserName());
                GptMessageResp resp = gptProxyRemoteService.getGptService().chat(proxyDTO).execute().body();
                replyContent = resp.getRes();
                log.info("user {} send message {}, got gpt reply {}", toUser, vo.getContent(), replyContent);
            }

        }
        SendCSMessageDTO dto = SendCSMessageDTO.builder().toUser(toUser).msgType("text").text(
                SendCSMessageDetailDTO.builder().content(replyContent).build()
        ).build();
        wxApiRemoteService.getTokenService().sendCSMessage(dto).execute();
    }

    public Boolean resetGpt() {
        //清除所有用户昨天的记录
        Set<String> keys = redisTemplate.keys("user_daily_throttle_*");
        log.info("keys拿到的数据" + keys.toString());
        redisTemplate.delete(keys);
        return null;
    }
}

