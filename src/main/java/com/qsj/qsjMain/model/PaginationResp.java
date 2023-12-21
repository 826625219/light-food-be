package com.qsj.qsjMain.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 分页响应
 */
@Data
@ApiModel("分页响应")
public class PaginationResp<T> {
    @ApiModelProperty("总条数")
    private Long count;
    @ApiModelProperty("总页数")
    private Long totalPage;
    @ApiModelProperty("每页条数")
    private Long pageSize;
    @ApiModelProperty("数据内容")
    private List<T> data;
}
