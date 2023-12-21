package com.qsj.qsjMain.model.vo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true)
@ApiModel(value = "订单列表每一项VO")
public class OrderStatusVO {
    @ApiModelProperty(value = "订单状态")
    private Integer orderStatus;

    @ApiModelProperty(value = "菜品准备好的时间")
    private Long preparedTime;

    @ApiModelProperty(value = "完成时间")
    private Long finishTime;

}
