package com.wwstation.inventory.service.impl;

import com.wwstation.inventory.entity.Inventory;
import com.wwstation.inventory.mapper.InventoryMapper;
import com.wwstation.inventory.service.IInventoryService;
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
