package com.william_workstation.high_concurrent_demo.order.controller;

import com.baomidou.mybatisplus.annotation.TableName;
import com.william_workstation.high_concurrent_demo.order.client.FinanceClient;
import com.william_workstation.high_concurrent_demo.order.client.InventoryClient;
import com.william_workstation.high_concurrent_demo.order.entity.Order;
import com.william_workstation.high_concurrent_demo.order.service.IOrderService;
import io.seata.spring.annotation.GlobalTransactional;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@Api(tags = "下单模拟")
@RequestMapping("/order")
@Slf4j
public class DemoController {
    @Autowired
    IOrderService orderService;
    @Autowired
    FinanceClient financeClient;
    @Autowired
    InventoryClient inventoryClient;

    @PostMapping(value = "/test/single/succeed")
    @ApiOperation(value = "单独测试")
    @GlobalTransactional(rollbackFor = Exception.class)
    public boolean tryCreateOrderSucceed() {
        Order order = new Order();
        order.setOrderNo("123456");
        order.setCreateTime(LocalDateTime.now());
        log.info("正在下单");
        try {
            orderService.save(order);
            log.info("下单成功");
        } catch (Exception exception) {
            exception.printStackTrace();
            log.error("下单环节失败");
            throw new RuntimeException("下单异常");
        }
        return true;
    }

    @PostMapping(value = "/test/single/failed")
    @ApiOperation(value = "单独测试,抛异常回滚")
    @GlobalTransactional(rollbackFor = Exception.class)
    public boolean tryCreateOrderFailed() {
        Order order = new Order();
        order.setOrderNo("123456");
        order.setCreateTime(LocalDateTime.now());
        log.info("正在下单");
        try {
            orderService.save(order);
            int i = 1 / 0;
            log.info("下单成功");
        } catch (Exception exception) {
            exception.printStackTrace();
            log.error("下单环节失败");
            throw new RuntimeException("下单异常");
        }
        return true;
    }

    @PostMapping(value = "/test/multi/succeed")
    @ApiOperation(value = "联合测试，成功")
    @GlobalTransactional(rollbackFor = Exception.class)
    public String tryFeignSuccess() {
        Order order = new Order();
        order.setOrderNo("123456");
        order.setCreateTime(LocalDateTime.now());
        log.info("正在下单");
        try {
            orderService.save(order);
            log.info("下单成功");
        } catch (Exception exception) {
            exception.printStackTrace();
            log.error("下单环节失败");
            throw new RuntimeException("下单异常");
        }
        if (succeedFeign()) {
            log.info("付款和扣库完成");
            return "成功";
        } else {
            throw new RuntimeException("订单异常回滚");
        }
    }

    @PostMapping(value = "/test/multi/failed")
    @ApiOperation(value = "单独测试，抛异常回滚")
    @GlobalTransactional(rollbackFor = Exception.class)
    public String tryFeignFail() {
        Order order = new Order();
        order.setOrderNo("123456");
        order.setCreateTime(LocalDateTime.now());
        log.info("正在下单");
        try {
            boolean save = orderService.save(order);
            log.info("下单成功");
        } catch (Exception exception) {
            exception.printStackTrace();
            log.error("下单环节失败");
            throw new RuntimeException("下单异常");
        }
        if (failedFeign()) {
            log.info("付款和扣库完成");
            return "成功";
        } else {
            throw new RuntimeException("订单异常回滚");
        }
    }


    private boolean succeedFeign() {
        boolean financeSeucceed = false;
        boolean inventorySucceed = false;
        try {
            financeSeucceed = financeClient.tryCreateBalance(false);
            inventorySucceed = inventoryClient.tryCreateInventory(false);
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }

        if (financeSeucceed && inventorySucceed) {
            return true;
        }
        return false;
    }

    private boolean failedFeign() {
        boolean financeSeucceed = false;
        boolean inventorySucceed = false;
        try {
            financeSeucceed = financeClient.tryCreateBalance(false);
            inventorySucceed = inventoryClient.tryCreateInventory(true);
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }

        if (financeSeucceed && inventorySucceed) {
            return true;
        }
        return false;
    }
}




