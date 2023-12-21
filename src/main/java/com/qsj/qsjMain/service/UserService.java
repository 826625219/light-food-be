package com.qsj.qsjMain.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qsj.qsjMain.exception.SignatureError;
import com.qsj.qsjMain.model.entity.User;
import com.qsj.qsjMain.model.vo.*;

import java.io.IOException;

/**
 *
 */
public interface UserService extends IService<User> {

    /**
     * login app by wx code, and update user profile information
     * @param vo login request data
     * @return if success, return credential in cookies; otherwise, return null or raise exception
     */
    String login(LoginRequestVO vo) throws IOException, SignatureError;

    /**
     * 减少用户余额
     */
    void deductBalance(Long userId, Integer amount);

    /**
     * 减少用户积分
     */
    void deductIntegral(Long userId, Integer amount);

    /**
     * 用户兑换积分券
     * @param userId 用户id
     * @param sn 积分券SN
     * @return 兑换状态返回码
     */
    AddIntegralResultVO useIntegralCoupon(Long userId, String sn);

    /**
     * 更新用户信息
     * @param userId   用户id
     * @param vo 用户信息
     * @return 更新状态
     */
    Boolean updateProfile(Long userId, UpdateProfileVO vo);

    /**
     * 获取用户信息
     * @param userId 用户id
     * @return 用户信息
     */
    ProfileResultVO getProfile(Long userId);

    /**
     * 获取用户积分
     *
     * @param userId 用户id
     * @return 积分数
     */
    Integer getIntegral(Long userId);

    PrepayResultWithSignVO recharge(Long userId, Integer amount);

    PrepayResultWithSignVO buyVip(Long userId, Boolean increaseFlag);

}
