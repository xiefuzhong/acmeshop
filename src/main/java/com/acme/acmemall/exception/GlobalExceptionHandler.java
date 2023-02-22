package com.acme.acmemall.exception;

import com.acme.acmemall.common.ResultMap;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2023/2/22 10:59
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 拦截JSON参数校验
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(ApiCusException.class)
    public ResultMap bindException(ApiCusException e) {
        return ResultMap.error(e.getErrno(), e.getErrmsg());
    }
}
