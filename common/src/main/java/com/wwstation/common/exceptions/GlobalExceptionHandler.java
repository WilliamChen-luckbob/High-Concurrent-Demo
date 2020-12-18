package com.wwstation.common.exceptions;

import com.wwstation.common.components.CommonResult;
import com.wwstation.common.components.ResultEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author william
 * @description
 * @Date: 2020-12-11 18:14
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    /**
     * 全局处理：捕获主动抛出的普通异常
     *
     * @param ex
     * @return
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(GlobalException.class)
    public CommonResult handleGlobalException(GlobalException ex) {
        log.error("全局异常控制输出：{}", ex.getMessage());
        return CommonResult.fail(ex.getCode(), ex.getMessage());
    }

    /**
     * 全局处理：捕获主动抛出的安全异常
     *
     * @param ex
     * @return
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(GlobalException.class)
    public CommonResult handleAuthException(SecurityException ex) {
        log.error("认证异常控制输出：{}", ex.getMessage());
        return CommonResult.fail(ex.getCode(), ex.getMessage());
    }

    /**
     * 全局处理：捕获主动抛出的DB异常
     *
     * @param ex
     * @return
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(DBExeption.class)
    public CommonResult handleAuthException(DBExeption ex) {
        log.error("DB异常控制输出：{}", ex.getMessage());
        return CommonResult.fail(ex.getCode(), ex.getMessage());
    }

    /**
     * 全局处理：捕获其他异常
     *
     * @param ex
     * @return
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public CommonResult handleAuthorizationException(Exception ex) {
        log.error("全局异常控制输出：{}", ex.getMessage());
        return CommonResult.fail(ResultEnum.UNKNOWN_ERROR.getCode(), "操作异常");
    }
}
