package com.qsj.qsjMain.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@ApiModel(value = "步数排行榜信息VO")
public class StepRankResultVO {
    @ApiModelProperty("排行榜具体数据")
    List<StepRankResultItemVO> data;

    @ApiModelProperty("活动名")
    private String activityName;

    @ApiModelProperty("用户是否已参加活动")
    private Boolean joined;

    @ApiModelProperty("活动是否已结束")
    private Boolean finished;

}
