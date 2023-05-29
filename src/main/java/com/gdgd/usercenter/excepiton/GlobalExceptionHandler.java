package com.gdgd.usercenter.excepiton;

import com.gdgd.usercenter.common.BaseResponse;
import com.gdgd.usercenter.common.ErrorCode;
import com.gdgd.usercenter.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 *
 * @author 86731
 */

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    //这个方法捕获指定异常
    @ExceptionHandler(BusinessException.class)
    public BaseResponse businessExceptionHandler(BusinessException e) {
        log.error("businessException: " + e.getMessage(),e);
        return ResultUtils.error(e.getCode(),e.getMessage(),e.getDescription());

    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse runtimeExceptionHandler(RuntimeException e) {
        log.error("runtimeException",e);
        return ResultUtils.error(ErrorCode.SYS_ERROR,e.getMessage(),"");
    }
}
