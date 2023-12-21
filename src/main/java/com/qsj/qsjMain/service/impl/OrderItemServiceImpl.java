package com.qsj.qsjMain.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qsj.qsjMain.model.entity.OrderItem;
import com.qsj.qsjMain.model.mapper.OrderItemMapper;
import com.qsj.qsjMain.service.OrderItemService;
import org.springframework.stereotype.Service;


/**
 * 用户服务实现类
 */
@Service
public class OrderItemServiceImpl extends ServiceImpl<OrderItemMapper, OrderItem>
        implements OrderItemService {

}
