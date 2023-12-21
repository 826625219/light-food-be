package com.qsj.qsjMain.controller;

import com.qsj.qsjMain.model.Message;
import com.qsj.qsjMain.model.ResultCode;
import com.qsj.qsjMain.model.vo.MTExternalOrderVO;
import com.qsj.qsjMain.service.OrderService;
import com.qsj.qsjMain.service.WorkerAliveService;
import com.qsj.qsjMain.utils.CredentialUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 采集者控制器
 */
@RestController
@RequestMapping("api/v1/worker")
@Api(tags = "从属工作者上报，包括门店和采集侧的上报")
public class CollectorController {

    private final OrderService orderService;
    private final WorkerAliveService workerAliveService;


    public CollectorController(OrderService orderService, WorkerAliveService workerAliveService) {
        this.orderService = orderService;
        this.workerAliveService = workerAliveService;
    }

    @RequestMapping(value = "ping", method = RequestMethod.GET)
    @ApiOperation("工作者heartbeat")
    public Message<Boolean> heartbeat(@RequestAttribute @ApiIgnore Long userId, @RequestParam String workerId, @RequestParam String workMode) {
        if(!CredentialUtils.isAdminRequest(userId)) {
            return Message.error(ResultCode.BAD_REQUEST);
        }
        return Message.ok(workerAliveService.heartbeat(workerId, workMode));
    }

    @RequestMapping(value = "mt_report", method = RequestMethod.POST)
    @ApiOperation("上报美团订单")
    public Message<Boolean> mtReport(@RequestAttribute @ApiIgnore Long userId, @RequestBody MTExternalOrderVO vo) {
        if(!CredentialUtils.isAdminRequest(userId)) {
            return Message.error(ResultCode.BAD_REQUEST);
        }
        return Message.ok(orderService.reportMtOrder(vo));
    }

    @RequestMapping(value = "worker_alive", method = RequestMethod.POST)
    @ApiOperation("工作者是否存活")
    public Message<Boolean> workerAlive(@RequestAttribute @ApiIgnore Long userId) {
        if(!CredentialUtils.isAdminRequest(userId)) {
            return Message.error(ResultCode.BAD_REQUEST);
        }
        workerAliveService.workersAlive("listen_order");
//        workerAliveService.workersAlive("confirm_order");
        return Message.ok(true);
    }
}
