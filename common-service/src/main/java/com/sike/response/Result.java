package com.sike.response;

import com.sike.bean.BaseBean;
import com.sike.exception.BusinessException;
import com.sike.exception.ExceptionCodeEnum;

public class Result extends BaseBean {
    /**
     * 执行结果
     */
    private boolean isSuccess;

    /**
     * 错误码
     */
    private String errorCode;

    /**
     * 错误原因
     */
    private String message;

    /**
     * 返回数据
     */
    private Object data;

    /**
     * 返回成功结果
     *
     * @return
     */
    public static Result success() {
        Result result = new Result();
        result.setSuccess(true);
        return result;
    }

    /**
     * 返回带数据的成功结果
     *
     * @param data
     * @return
     */
    public static Result success(Object data) {
        Result result = new Result();
        result.setSuccess(true);
        result.setData(data);
        return result;
    }

    /**
     * 返回【未知异常】的失败结果
     *
     * @return
     */
    public static Result failure() {
        Result result = new Result();
        result.setSuccess(false);
        result.setErrorCode(ExceptionCodeEnum.UNKNOW_ERROR.getCode());
        result.setMessage(ExceptionCodeEnum.UNKNOW_ERROR.getMessage());
        return result;
    }

    /**
     * 返回业务异常的失败结果
     * @param e
     * @return
     */
    public static Result failure(BusinessException e) {
        Result result = new Result();
        result.setSuccess(false);
        result.setErrorCode(e.getCodeEnum().getCode());
        result.setMessage(e.getCodeEnum().getMessage());
        return result;
    }

    /**
     * 返回带数据的业务异常的失败结果
     * @param e
     * @param data
     * @return
     */
    public static Result failure(BusinessException e,Object data) {
        Result result = new Result();
        result.setSuccess(false);
        result.setErrorCode(e.getCodeEnum().getCode());
        result.setMessage(e.getCodeEnum().getMessage());
        result.setData(data);
        return result;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
