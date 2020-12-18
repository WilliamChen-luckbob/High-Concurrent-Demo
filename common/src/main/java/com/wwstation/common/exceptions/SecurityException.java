package com.wwstation.common.exceptions;

import com.wwstation.common.components.ResultEnum;
import lombok.Data;

/**
 * @author william
 * @description
 * @Date: 2020-12-15 22:49
 */
@Data
public class SecurityException extends RuntimeException {
    private Integer code;

    public SecurityException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public SecurityException(ResultEnum errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

    public SecurityException(String message) {
        super(message);
        this.code = ResultEnum.UNKNOWN_ERROR.getCode();
    }
}
