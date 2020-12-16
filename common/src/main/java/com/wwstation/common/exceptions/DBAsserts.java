package com.wwstation.common.exceptions;

import com.wwstation.common.components.ResultEnum;

/**
 * @author william
 * @description
 * @Date: 2020-12-15 22:59
 */
public class DBAsserts {
    public static void fail(String message) {
        throw new DBExeption(message);
    }

    public static void fail(ResultEnum errorCode) {
        throw new DBExeption(errorCode);
    }

    public static void fail(ResultEnum errorCode, String message) {
        throw new DBExeption(errorCode.getCode(), message);
    }
}
