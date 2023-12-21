package com.qsj.qsjMain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qsj.qsjMain.model.entity.DiscountCoupon;
import com.qsj.qsjMain.model.entity.StepRank;
import com.qsj.qsjMain.model.mapper.StepRankMapper;
import com.qsj.qsjMain.service.StepRankService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class StepRankServiceImpl extends ServiceImpl<StepRankMapper, StepRank>implements StepRankService {

}
