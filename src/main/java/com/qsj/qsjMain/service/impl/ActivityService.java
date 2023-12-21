package com.qsj.qsjMain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.qsj.qsjMain.model.entity.SaleActivity;
import com.qsj.qsjMain.model.entity.StepRank;
import com.qsj.qsjMain.model.entity.User;
import com.qsj.qsjMain.model.mapper.SaleActivityMapper;
import com.qsj.qsjMain.model.vo.StepRankResultItemVO;
import com.qsj.qsjMain.model.vo.StepRankResultVO;
import com.qsj.qsjMain.model.vo.WeRunResultItemVO;
import com.qsj.qsjMain.model.vo.WeRunResultVO;
import com.qsj.qsjMain.service.StepRankService;
import com.qsj.qsjMain.service.UserService;
import com.qsj.qsjMain.utils.WXUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class ActivityService extends ServiceImpl<SaleActivityMapper, SaleActivity> {

    private final Gson gson = new Gson();
    private final StepRankService stepRankService;

    private final UserService userService;

    public ActivityService(StepRankService stepRankService, UserService userService) {
        this.stepRankService = stepRankService;
        this.userService = userService;
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean submitRunData(Long userId, Long activityId, String encryptedData, String iv) {
        // check if activity is valid
        SaleActivity activity = this.getById(activityId);
        if (Objects.isNull(activity)) {
            return false;
        }
        if (!activity.getEnabled()) {
            return false;
        }
        if (activity.getStartTime() > System.currentTimeMillis() || activity.getEndTime() < System.currentTimeMillis()) {
            return false;
        }

        String result = WXUtils.decryptData(encryptedData, userService.getById(userId).getWxSessionKey(), iv);
        WeRunResultVO weRunResultVO = gson.fromJson(result, WeRunResultVO.class);
        // get last run result
        List<WeRunResultItemVO> steps = weRunResultVO.getStepInfoList();
        if (steps.size() == 0) {
            return false;
        }
        WeRunResultItemVO lastStep = steps.get(steps.size() - 1);
        // 检查本期活动是否已经上传过数据
        StepRank rank = stepRankService.getOne(new LambdaQueryWrapper<StepRank>().eq(StepRank::getUserId, userId).eq(StepRank::getActivityId, activityId));
        if (Objects.isNull(rank)) {
            rank = new StepRank();
            rank.setActivityId(activityId);
            rank.setUserId(userId);
            rank.setStep(lastStep.getStep());
            stepRankService.save(rank);
        } else {
            LambdaUpdateWrapper<StepRank> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(StepRank::getId, rank.getId()).set(StepRank::getStep, lastStep.getStep());
            stepRankService.update(updateWrapper);
        }
        return true;
    }

    public StepRankResultVO getStepRank(Long userId, Long activityId) {
        // check if activity is valid
        SaleActivity activity = this.getById(activityId);
        if (Objects.isNull(activity)) {
            return null;
        }
        if (!activity.getEnabled()) {
            return null;
        }

        QueryWrapper<StepRank> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(StepRank::getActivityId, activityId).orderByDesc(StepRank::getStep);
        List<StepRank> ranks = stepRankService.list(queryWrapper);
        StepRankResultVO result = new StepRankResultVO();
        result.setFinished(activity.getEndTime() < System.currentTimeMillis());
        result.setActivityName(activity.getName());
        boolean joined = false;
        List<StepRankResultItemVO> items = new ArrayList<>();
        for(int i = 0; i < ranks.size(); i++) {
            StepRank rank = ranks.get(i);
            StepRankResultItemVO item = new StepRankResultItemVO();
            item.setRank(i + 1);
            item.setStep(rank.getStep());
            item.setSelfFlag(false);
            User user = userService.getById(rank.getUserId());
            if(Objects.nonNull(user)) {
                item.setAvatarUrl(user.getWxAvatarUrl());
                item.setName(user.getMaskedNickName());
            }
            else {
                item.setAvatarUrl("");
                item.setName("");
            }

            if (Objects.equals(rank.getUserId(), userId)) {
                item.setSelfFlag(true);
                joined = true;
                item.setName(user.getWxNickName());
            }
            items.add(item);
        }
        result.setData(items);
        result.setJoined(joined);
        return result;
    }
}
