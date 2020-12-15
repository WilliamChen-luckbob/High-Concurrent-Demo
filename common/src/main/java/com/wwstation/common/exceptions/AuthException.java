package com.wwstation.common.exceptions;

import com.wwstation.common.components.ResultEnum;
import lombok.Data;

/**
 * @author william
 * @description
 * @Date: 2020-12-15 22:49
 */
@Data
public class AuthException extends RuntimeException {
    private Integer code;

    public AuthException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public AuthException(ResultEnum errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

    public AuthException(String message) {
        super(message);
        this.code = ResultEnum.UNKNOWN_ERROR.getCode();
    }
}
