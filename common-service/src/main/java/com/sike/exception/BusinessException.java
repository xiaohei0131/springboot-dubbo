package com.sike.exception;

import java.io.Serializable;

/**
 * 业务异常
 */
public class BusinessException extends RuntimeException implements Serializable {
    private ExceptionCodeEnum codeEnum;

    public BusinessException(ExceptionCodeEnum codeEnum){
        super(codeEnum.getMessage());
        this.codeEnum = codeEnum;
    }

    public BusinessException(){}

    public ExceptionCodeEnum getCodeEnum() {
        return codeEnum;
    }
}
