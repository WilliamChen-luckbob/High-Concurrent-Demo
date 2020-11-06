package com.william_workstation.high_concurrent_demo.inventory.service.impl;

import com.william_workstation.high_concurrent_demo.inventory.entity.Inventory;
import com.william_workstation.high_concurrent_demo.inventory.mapper.InventoryMapper;
import com.william_workstation.high_concurrent_demo.inventory.service.IInventoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author william
 * @since 2020-11-05
 */
@Service
public class InventoryServiceImpl extends ServiceImpl<InventoryMapper, Inventory> implements IInventoryService {

}
