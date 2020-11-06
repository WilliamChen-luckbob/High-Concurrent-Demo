package com.william_workstation.high_concurrent_demo.order.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;

@FeignClient("finance")
public interface FinanceClient {
    @RequestMapping(method = RequestMethod.POST, value = "/finance/testConn")
    boolean tryCreateBalance(@RequestParam(name = "failed", defaultValue = "false") boolean failed);
}
