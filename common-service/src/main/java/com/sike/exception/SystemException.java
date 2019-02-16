package com.sike.exception;

import java.io.Serializable;

/**
 * @description 系统异常
 */
public class SystemException extends RuntimeException implements Serializable {

    private ExceptionCodeEnum codeEnum;

    public SystemException(ExceptionCodeEnum codeEnum) {
        super(codeEnum.getMessage());
        this.codeEnum = codeEnum;
    }

    public SystemException() {

    }
    public ExceptionCodeEnum getCodeEnum() {
        return codeEnum;
    }
}
