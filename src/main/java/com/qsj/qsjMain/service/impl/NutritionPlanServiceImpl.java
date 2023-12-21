package com.qsj.qsjMain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qsj.qsjMain.model.entity.NutritionPlan;
import com.qsj.qsjMain.model.mapper.NutritionPlanMapper;
import com.qsj.qsjMain.service.NutritionPlanService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;


/**
 * 用户服务实现类
 */
@Service
public class NutritionPlanServiceImpl extends ServiceImpl<NutritionPlanMapper, NutritionPlan>
        implements NutritionPlanService {

    @Override
    public List<NutritionPlan> getPlans(String planName){
        QueryWrapper<NutritionPlan> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(NutritionPlan::getStatus, 1);
        if(StringUtils.hasText(planName)){
            wrapper.lambda().like(NutritionPlan::getName, planName);
        }
        return list(wrapper);
    }
}
