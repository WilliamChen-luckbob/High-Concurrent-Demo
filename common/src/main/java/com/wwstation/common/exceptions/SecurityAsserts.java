package com.wwstation.common.exceptions;

import com.wwstation.common.components.ResultEnum;

/**
 * @author william
 * @description
 * @Date: 2020-12-15 22:49
 */
public class SecurityAsserts {
    public static void fail(String message) {
        throw new SecurityException(message);
    }

    public static void fail(ResultEnum errorCode) {
        throw new SecurityException(errorCode);
    }

    public static void fail(ResultEnum errorCode, String message) {
        throw new SecurityException(errorCode.getCode(), message);
    }
}
