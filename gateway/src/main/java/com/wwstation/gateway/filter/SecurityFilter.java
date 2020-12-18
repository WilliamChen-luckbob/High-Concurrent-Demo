package com.wwstation.gateway.filter;

import com.wwstation.gateway.exceptions.GatewayAsserts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@ConfigurationProperties(prefix = "customized.white-list")
public class SecurityFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("进入鉴权过滤器");
        //处理白名单
        if (!exchange.getRequest().getHeaders().containsKey("token")) {
            return GatewayAsserts.fail(exchange.getResponse(), "请登录");
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


}
