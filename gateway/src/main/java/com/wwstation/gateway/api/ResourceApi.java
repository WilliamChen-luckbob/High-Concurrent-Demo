package com.wwstation.gateway.api;

import org.springframework.cloud.openfeign.FeignClient;

/**
 * 调用资源服务的内部通信端口
 * @author william
 * @description
 * @Date: 2020-12-18 23:11
 */
@FeignClient("resource-center")
public interface ResourceApi {

}
