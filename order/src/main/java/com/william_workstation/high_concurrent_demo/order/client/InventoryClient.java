package com.william_workstation.high_concurrent_demo.order.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("inventory")
public interface InventoryClient {
    @RequestMapping(method = RequestMethod.POST, value = "/inventory/testConn")
    boolean tryCreateInventory(@RequestParam(name = "failed", defaultValue = "false") boolean failed);
}
