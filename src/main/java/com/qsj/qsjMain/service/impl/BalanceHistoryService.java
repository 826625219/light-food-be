package com.qsj.qsjMain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qsj.qsjMain.model.entity.Order;
import com.qsj.qsjMain.model.entity.RechargeOrder;
import com.qsj.qsjMain.model.vo.BalanceRecordVO;
import com.qsj.qsjMain.service.OrderService;
import com.qsj.qsjMain.service.RechargeOrderService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BalanceHistoryService {
    private final OrderService orderService;

    private final RechargeOrderService rechargeOrderService;

    public BalanceHistoryService(OrderService orderService, RechargeOrderService rechargeOrderService) {
        this.orderService = orderService;
        this.rechargeOrderService = rechargeOrderService;
    }

    public List<BalanceRecordVO> getBalanceRecord(Long userId) {
        LambdaQueryWrapper<RechargeOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RechargeOrder::getUserId, userId).eq(RechargeOrder::getOrderStatus,
                RechargeOrder.OrderStatus.PAID).eq(RechargeOrder::getRechargeType, RechargeOrder.OrderType.BALANCE);
        List<RechargeOrder> rechargeOrders = rechargeOrderService.list(wrapper);

        LambdaQueryWrapper<Order> orderWrapper = new LambdaQueryWrapper<>();
        orderWrapper.eq(Order::getUserId, userId).eq(Order::getStatus, Order.Status.FINISH);
        List<Order> orders = orderService.list(orderWrapper);

        List<BalanceRecordVO> result = new ArrayList<>();

        for (RechargeOrder rechargeOrder : rechargeOrders) {
            BalanceRecordVO balanceRecordVO = new BalanceRecordVO();
            balanceRecordVO.setAmount(rechargeOrder.getAmountCharge());
            balanceRecordVO.setStatus(BalanceRecordVO.BalanceRecordStatus.IN);
            balanceRecordVO.setTime(rechargeOrder.getUpdateTime());
            result.add(balanceRecordVO);
        }

        for (Order order : orders) {
            BalanceRecordVO balanceRecordVO = new BalanceRecordVO();
            balanceRecordVO.setAmount(order.getBalanceAmount());
            balanceRecordVO.setStatus(BalanceRecordVO.BalanceRecordStatus.OUT);
            balanceRecordVO.setTime(order.getUpdateTime());
            result.add(balanceRecordVO);
        }

        result.sort((o1, o2) -> o2.getTime().compareTo(o1.getTime()));
        return result;

    }
}
