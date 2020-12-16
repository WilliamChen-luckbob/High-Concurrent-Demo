package com.wwstation.common.exceptions;

import com.wwstation.common.components.ResultEnum;

/**
 * 全局断言处理类，抛出指定的全局异常，并交给全局异常处理类进行处理
 *
 * @author william
 * @description
 * @Date: 2020-12-11 18:15
 */
public class GlobalAsserts {
    public static void fail(String message) {
        throw new GlobalException(message);
    }

    public static void fail(ResultEnum errorCode) {
        throw new GlobalException(errorCode);
    }

    public static void fail(ResultEnum errorCode, String message) {
        throw new GlobalException(errorCode.getCode(), message);
    }
}
