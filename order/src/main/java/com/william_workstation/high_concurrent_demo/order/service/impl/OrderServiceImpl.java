package com.william_workstation.high_concurrent_demo.order.service.impl;

import com.william_workstation.high_concurrent_demo.order.entity.Order;
import com.william_workstation.high_concurrent_demo.order.mapper.OrderMapper;
import com.william_workstation.high_concurrent_demo.order.service.IOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author william
 * @since 2020-11-04
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

}
