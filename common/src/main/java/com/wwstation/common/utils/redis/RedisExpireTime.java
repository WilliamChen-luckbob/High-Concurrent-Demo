package com.wwstation.common.utils.redis;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * redis的过期时间
 * @author william
 * @description
 * @Date: 2020-12-02 15:58
 */
@Getter
@AllArgsConstructor
public enum RedisExpireTime {
    NEVER_EXPIRE(-1),
    SECOND_30(30),
    SECOND_60(60),
    MIN_30(1800),
    MIN_60(3600),
    HOUR_2(7200),
    HOUR_12(3600*12),
    HOUR_24(3600*24)
    ;

private long expireTime;
}
