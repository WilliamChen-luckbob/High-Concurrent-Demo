package com.wwstation.common.components;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author william
 * @description
 * @Date: 2020-12-15 22:32
 */
@Data
@AllArgsConstructor
public class CommonResult implements Serializable {
    private static final long serialVersionUID = -5335497266125077976L;

    private Integer code;
    private String message;
    private Object data;

    public CommonResult(ResultEnum resultEnum) {
        this.code = resultEnum.getCode();
        this.message = resultEnum.getMessage();
    }

    public static CommonResult succeed() {
        return new CommonResult(ResultEnum.OK);
    }

    public static CommonResult succeed(Object data) {
        return new CommonResult(ResultEnum.OK.getCode(), ResultEnum.OK.getMessage(), data);
    }

    public static CommonResult fail(Integer code, String message) {
        return new CommonResult(code, message, null);
    }

    public static CommonResult fail(ResultEnum resultEnum) {
        return new CommonResult(resultEnum.getCode(), resultEnum.getMessage(), null);
    }

    public static CommonResult fail() {
        return fail(ResultEnum.UNKNOWN_ERROR);
    }
}
