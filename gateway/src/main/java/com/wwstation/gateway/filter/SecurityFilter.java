package com.wwstation.gateway.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.wwstation.gateway.components.ErrorMono;
import com.wwstation.gateway.components.ReturnData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

@Slf4j
@Component
public class SecurityFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("进入鉴权过滤器");
        if (!exchange.getRequest().getHeaders().containsKey("mytoken")) {
//            return ErrorMono.ErrorMono(exchange.getResponse(), "请输入token");
        return authErro(exchange.getResponse(),"");
        } else {
            log.info("校验成功");
            return chain.filter(exchange);
        }
    }

    @Override
    public int getOrder() {
        //鉴权过滤器顶在跨域过滤器之后一位
        return 1;
    }

    private Mono<Void> authErro(ServerHttpResponse resp, String mess) {
        resp.setStatusCode(HttpStatus.UNAUTHORIZED);
        resp.getHeaders().add("Content-Type","application/json;charset=UTF-8");
        ReturnData<String> returnData = new ReturnData<>(org.apache.http.HttpStatus.SC_UNAUTHORIZED, mess, mess);
        String returnStr = "";
            returnStr = "登录失败";

        DataBuffer buffer = resp.bufferFactory().wrap(returnStr.getBytes(StandardCharsets.UTF_8));
        return resp.writeWith(Flux.just(buffer));
    }
}
