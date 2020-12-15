package com.wwstation.common.exceptions;

import com.wwstation.common.components.ResultEnum;

/**
 * @author william
 * @description
 * @Date: 2020-12-15 22:49
 */
public class AuthAsserts {
    public static void fail(String message) {
        throw new AuthException(message);
    }

    public static void fail(ResultEnum errorCode) {
        throw new AuthException(errorCode);
    }

    public static void fail(ResultEnum errorCode, String message) {
        throw new AuthException(errorCode.getCode(), message);
    }
}
