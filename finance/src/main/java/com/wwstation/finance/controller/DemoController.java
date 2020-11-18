package com.wwstation.finance.controller;

import com.wwstation.finance.entity.Balance;
import com.wwstation.finance.service.IBalanceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@Api(tags = "财务模拟")
@RequestMapping("/finance")
public class DemoController {
    @Autowired
    IBalanceService balanceService;

    @PostMapping(value = "/testConn")
    @ApiOperation(value = "测试")

    public boolean tryCreateBalance(@RequestParam(name = "failed", defaultValue = "false") boolean failed) {

        if (failed){
            throw new RuntimeException("哎呀，出错啦");
        }
        Balance balance = new Balance();
        balance.setUserName("william");
        balance.setBalance(new BigDecimal("10000"));

        try {
//            int i=1/0;
            balanceService.save(balance);
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
        return true;
    }
}
