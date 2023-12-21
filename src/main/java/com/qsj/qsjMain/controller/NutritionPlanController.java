package com.qsj.qsjMain.controller;


import com.qsj.qsjMain.model.Message;
import com.qsj.qsjMain.model.entity.NutritionPlan;
import com.qsj.qsjMain.service.impl.NutritionPlanServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/nutrition")
@Api(tags = "营养计划接口")
public class NutritionPlanController {
    private final NutritionPlanServiceImpl nutritionPlanService;

    public NutritionPlanController(NutritionPlanServiceImpl nutritionPlanService) {
        this.nutritionPlanService = nutritionPlanService;
    }

    @RequestMapping(value = "plans", method = RequestMethod.GET)
    @ApiOperation("获取营养计划列表")
    public Message<List<NutritionPlan>> getPlans(@RequestParam(defaultValue = "") String planName) {
        return Message.ok(nutritionPlanService.getPlans(planName));
    }
}

