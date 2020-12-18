package com.wwstation.gateway.exceptions;

import com.alibaba.fastjson.JSONObject;
import com.wwstation.common.components.CommonResult;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * @author william
 * @description
 * @Date: 2020-12-18 22:49
 */
public class GatewayAsserts {

    public static Mono<Void> fail(ServerHttpResponse resp, String mess) {
        resp.setStatusCode(HttpStatus.UNAUTHORIZED);
        resp.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        CommonResult fail = CommonResult.fail(HttpStatus.UNAUTHORIZED.value(), mess, mess);
        String returnStr = JSONObject.toJSONString(fail);
        DataBuffer buffer = resp.bufferFactory().wrap(returnStr.getBytes(StandardCharsets.UTF_8));
        return resp.writeWith(Flux.just(buffer));
    }
}
