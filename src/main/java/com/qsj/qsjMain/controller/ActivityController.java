package com.qsj.qsjMain.controller;

import com.qsj.qsjMain.model.Message;
import com.qsj.qsjMain.model.vo.StepRankResultVO;
import com.qsj.qsjMain.model.vo.UpdateStepVO;
import com.qsj.qsjMain.service.impl.ActivityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 活动控制器
 */
@RestController
@RequestMapping("api/v1/activity")
@Api(tags = "活动接口")
public class ActivityController {
    private final ActivityService activityService;
    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    @RequestMapping(value = "submitRunData", method = RequestMethod.POST)
    @ApiOperation("上传微信运动数据")
    public Message<Boolean> uploadWeRunData(@RequestAttribute @ApiIgnore Long userId, @RequestBody UpdateStepVO vo) {
        return Message.ok(activityService.submitRunData(userId,vo.getActivityId(), vo.getEncryptedData(),vo.getIv()));
    }

    @RequestMapping(value = "getStepRank", method = RequestMethod.GET)
    @ApiOperation("获取步数活动排行榜")
    public Message<StepRankResultVO> getStepRank(@RequestAttribute @ApiIgnore Long userId, @RequestParam Long activityId) {
        return Message.ok(activityService.getStepRank(userId,activityId));
    }
}
