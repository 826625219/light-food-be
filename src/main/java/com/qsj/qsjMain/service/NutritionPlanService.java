package com.qsj.qsjMain.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qsj.qsjMain.model.entity.NutritionPlan;

import java.util.List;

/**
 *
 */
public interface NutritionPlanService extends IService<NutritionPlan> {

    List<NutritionPlan> getPlans(String planName);
}
