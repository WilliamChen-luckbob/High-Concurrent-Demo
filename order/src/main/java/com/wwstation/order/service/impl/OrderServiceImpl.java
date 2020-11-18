package com.wwstation.order.service.impl;

import com.wwstation.order.entity.Order;
import com.wwstation.order.mapper.OrderMapper;
import com.wwstation.order.service.IOrderService;
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
