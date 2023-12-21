package com.qsj.qsjMain.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 营养计划表
 */
@TableName(value = "worker_alive")
@Accessors(chain = true)
@Data
public class WorkerAlive implements Serializable {

    @TableId(type = IdType.INPUT)
    private String workerId; // 工作者名字

    private String workerType; // 工作者类型

    private Long updateTime; // 最后一次心跳时间

}
