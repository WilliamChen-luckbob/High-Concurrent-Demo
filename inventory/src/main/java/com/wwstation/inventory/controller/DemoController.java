package com.wwstation.inventory.controller;

import com.wwstation.inventory.entity.Inventory;
import com.wwstation.inventory.service.IInventoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "库存模拟")
@RequestMapping("/inventory")
public class DemoController {
    @Autowired
    IInventoryService inventoryService;

    @PostMapping(value = "/testConn")
    @ApiOperation(value = "测试")
    public boolean tryCreateInventory(@RequestParam(name = "failed", defaultValue = "false") boolean failed) {
        if (failed) {
            throw new RuntimeException("哎呀，出错啦");
        }
        Inventory inventory = new Inventory();
        inventory.setSku("test12345");
        inventory.setQuantity(10);
        inventory.setOccupiedQuantity(0);
        try {
            inventoryService.save(inventory);
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
        return true;
    }
}
