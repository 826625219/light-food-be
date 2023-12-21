package com.qsj.qsjMain.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qsj.qsjMain.model.entity.RechargeOrder;
import com.qsj.qsjMain.model.mapper.RechargeOrderMapper;
import com.qsj.qsjMain.service.RechargeOrderService;
import org.springframework.stereotype.Service;


@Service
public class RechargeOrderServiceImpl extends ServiceImpl<RechargeOrderMapper, RechargeOrder> implements
        RechargeOrderService {
}
