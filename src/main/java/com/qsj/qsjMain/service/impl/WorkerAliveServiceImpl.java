package com.qsj.qsjMain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qsj.qsjMain.model.entity.WorkerAlive;
import com.qsj.qsjMain.model.mapper.WorkerAliveMapper;
import com.qsj.qsjMain.remote.service.EWXRobotRemoteService;
import com.qsj.qsjMain.remote.service.model.dto.SendEWXRobotMessageDTO;
import com.qsj.qsjMain.remote.service.model.dto.SendEWXRobotMessageText;
import com.qsj.qsjMain.service.WorkerAliveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


/**
 * 用户服务实现类
 */
@Slf4j
@Service
public class WorkerAliveServiceImpl extends ServiceImpl<WorkerAliveMapper, WorkerAlive>
        implements WorkerAliveService {

    final EWXRobotRemoteService ewxRobotRemoteService;

    public WorkerAliveServiceImpl(EWXRobotRemoteService ewxRobotRemoteService) {
        this.ewxRobotRemoteService = ewxRobotRemoteService;
    }

    @Override
    public boolean heartbeat(String workerId, String workerMode) {
        LambdaQueryWrapper<WorkerAlive> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WorkerAlive::getWorkerId, workerId);
        queryWrapper.eq(WorkerAlive::getWorkerType, workerMode);
        WorkerAlive worker = getOne(queryWrapper, false);
        if (worker == null) {
            worker = new WorkerAlive();
            worker.setWorkerId(workerId);
            worker.setWorkerType(workerMode);
            worker.setUpdateTime(new Date().getTime());
            return save(worker);
        } else {
            worker.setUpdateTime(new Date().getTime());
            return updateById(worker);
        }
    }

    @Override
    public void workersAlive(String workerMode) {
        LambdaQueryWrapper<WorkerAlive> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WorkerAlive::getWorkerType, workerMode);
        List<WorkerAlive> workers = list(queryWrapper);
        for (WorkerAlive worker : workers) {
            if (worker.getUpdateTime() + 1000 * 60 > new Date().getTime()) {
                return;
            }
        }
        try {
            SendEWXRobotMessageDTO dto = new SendEWXRobotMessageDTO();
            dto.setMsgType("text");
            dto.setText(new SendEWXRobotMessageText("工作者离线超过一分钟，请检查！workerMode：" + workerMode + ""));
            ewxRobotRemoteService.getService().sendMessage(dto).execute();
        }
        catch (Exception e) {
            log.error("发送企业微信消息失败", e);
        }
    }


}
