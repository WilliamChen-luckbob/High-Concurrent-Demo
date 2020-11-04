package com.william_workstation.high_concurrent_demo.order.controller;

import com.william_workstation.high_concurrent_demo.order.entity.Order;
import com.william_workstation.high_concurrent_demo.order.service.IOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@Api(tags = "下单模拟")
@RequestMapping("/order")
public class DemoController {
    @Autowired
    IOrderService orderService;

    @PostMapping(value = "/testConn")
    @ApiOperation(value = "测试")
    public boolean tryCreateOrder() {
        //todo order表表名变成了关键字，呸！

        Order order = new Order();
        order.setOrderNo("123456");
        order.setCreateTime(LocalDateTime.now());
        orderService.save(order);
        return true;
    }
}
