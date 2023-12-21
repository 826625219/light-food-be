package com.qsj.qsjMain.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qsj.qsjMain.model.entity.WorkerAlive;

public interface WorkerAliveService extends IService<WorkerAlive> {

    boolean heartbeat(String workerId, String workerMode);

    void workersAlive(String workerMode);
}
