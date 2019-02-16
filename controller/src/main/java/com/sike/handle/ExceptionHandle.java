package com.sike.handle;

import com.sike.exception.BusinessException;
import com.sike.exception.ExceptionCodeEnum;
import com.sike.response.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * REST接口的通用异常处理
 */
@ControllerAdvice
@ResponseBody
public class ExceptionHandle {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 业务异常处理
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(BusinessException.class)
    public Result exceptionHandler(BusinessException exception) {
        return Result.failure(exception);
    }

    /**
     * 请求方法不正确
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result HttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException exception) {
        return Result.failure(new BusinessException(ExceptionCodeEnum.ERROR_404), exception.getMessage());
    }

    /**
     * 系统异常处理
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(Exception.class)
    public Result sysExpHandler(Exception exception) {
        logger.error("系统异常 ", exception);
        return Result.failure();
    }

}
