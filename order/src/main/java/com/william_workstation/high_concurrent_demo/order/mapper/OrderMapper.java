package com.william_workstation.high_concurrent_demo.order.mapper;

import com.william_workstation.high_concurrent_demo.order.entity.Order;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.mapstruct.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author william
 * @since 2020-11-04
 */
@Mapper
public interface OrderMapper extends BaseMapper<Order> {

}
