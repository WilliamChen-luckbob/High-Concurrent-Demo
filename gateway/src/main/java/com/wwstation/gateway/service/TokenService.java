package com.wwstation.gateway.service;

import com.sun.org.apache.xpath.internal.operations.Bool;
import com.wwstation.common.utils.redis.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.ws.ServiceMode;

/**
 * @author william
 * @description
 * @Date: 2020-12-18 23:06
 */
@Service
public class TokenService {
    @Autowired
    RedisUtils redisUtils;

    /**
     * 校验token是否有效登录
     *
     * @param token
     * @return
     */
    public Boolean ifTokenAvailable(String token) {
        return false;
    }

    /**
     * 校验用户是否有权限访问此资源
     * 使用url+method+serviceName唯一确定一个借口资源
     * 做到每一个接口均可被细粒度管控
     *
     * @param url
     * @param method
     * @param serviceName
     * @return
     */
    public Boolean ifResourceAccessible(String url, String method, String serviceName) {
        return false;
    }
}
