package com.wwstation.common.exceptions;

import com.wwstation.common.components.ResultEnum;
import lombok.Data;

/**
 * 自定义异常的顶级父类
 *
 * @author william
 * @description
 * @Date: 2020-12-15 22:15
 */
@Data
public class GlobalException extends RuntimeException {
    private Integer code;

    public GlobalException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public GlobalException(ResultEnum errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

    public GlobalException(String message) {
        super(message);
        this.code = ResultEnum.UNKNOWN_ERROR.getCode();
    }
}
