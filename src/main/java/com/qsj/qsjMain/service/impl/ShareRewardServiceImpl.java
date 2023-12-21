package com.qsj.qsjMain.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qsj.qsjMain.model.entity.ShareReward;
import com.qsj.qsjMain.model.mapper.ShareRewardMapper;
import com.qsj.qsjMain.service.ShareRewardService;
import org.springframework.stereotype.Service;

@Service
public class ShareRewardServiceImpl extends ServiceImpl<ShareRewardMapper, ShareReward>implements ShareRewardService {
}
