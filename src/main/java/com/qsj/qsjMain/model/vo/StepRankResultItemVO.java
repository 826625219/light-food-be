package com.qsj.qsjMain.model.vo;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@ApiModel(value = "步数排行榜单项信息VO")
public class StepRankResultItemVO {
    private Integer rank;
    private String name;
    private Integer step;
    private String avatarUrl;
    private Boolean selfFlag;
}
