package com.wwstation.common.exceptions;

import com.wwstation.common.components.ResultEnum;
import lombok.Data;

/**
 * @author william
 * @description
 * @Date: 2020-12-15 22:58
 */
@Data
public class DBExeption extends RuntimeException {
    private Integer code;

    public DBExeption(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public DBExeption(ResultEnum errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

    public DBExeption(String message) {
        super(message);
        this.code = ResultEnum.UNKNOWN_ERROR.getCode();
    }

}
