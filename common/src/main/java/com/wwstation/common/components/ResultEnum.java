package com.wwstation.common.components;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author william
 * @description
 * @Date: 2020-12-15 22:26
 */
@Getter
@AllArgsConstructor
public enum ResultEnum {
    OK(200, "成功"),
    UNKNOWN_ERROR(500, "未知错误"),
    ;
    private Integer code;
    private String message;
}
