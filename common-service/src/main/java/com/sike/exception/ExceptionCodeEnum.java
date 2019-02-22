package com.sike.exception;


import java.io.Serializable;

import static com.sike.exception.ExceptionPrefix.*;


/**
 * 全局的异常状态码 和 异常描述
 *
 * PS:异常码一共由5位组成，前两位为固定前缀，请参考{@link com.sike.exception.ExceptionPrefix}
 */
public enum ExceptionCodeEnum implements Serializable {

    /** 通用异常 */
    UNKNOW_ERROR(ComExpPrefix + "000", "未知异常"),
    ERROR_404(ComExpPrefix + "001", "没有该接口"),
    PARAM_NULL(ComExpPrefix + "002", "参数为空"),
    NO_REPEAT(ComExpPrefix + "003", "请勿重复提交"),
    MISSION_LOGIN(ComExpPrefix + "004", "会话已失效"),
    SESSION_NO_STORE(ComExpPrefix + "005", "session存储失败"),
    OPERATION_FAIL(ComExpPrefix + "006", "操作失败"),

    /** User模块异常 */
    USERNAME_NULL(UserExpPrefix + "000", "用户名为空"),
    PASSWD_NULL(UserExpPrefix + "001", "密码为空"),
    ACCOUNT_FORBIDDEN(UserExpPrefix + "002", "账号未启用"),
    LOGIN_FAIL(UserExpPrefix + "003", "登录失败"),
    UNLOGIN(UserExpPrefix + "004", "尚未登录"),
    NO_PERMISSION(UserExpPrefix + "005", "没有权限"),
    PHONE_NULL(UserExpPrefix + "006", "手机号为空"),
    EMAIL_NULL(UserExpPrefix + "007", "电子邮箱为空"),
    EMAIL_USED(UserExpPrefix + "008", "电子邮箱已被注册"),
    PHONE_USED(UserExpPrefix + "009", "手机号已被注册"),
    USERNAME_USED(UserExpPrefix + "010", "用户名已被注册"),
    ROLE_BIND_FAIL(UserExpPrefix + "011", "角色分配失败"),
    REGISTER_FAIL(UserExpPrefix + "012", "注册失败"),
    ENABLE_FAIL(UserExpPrefix + "013", "启用失败"),
    DISABLE_FAIL(UserExpPrefix + "014", "禁用失败"),
    OLD_PWD_ERROR(UserExpPrefix + "015", "旧密码错误"),
    MODIFY_FAIL(UserExpPrefix + "016", "修改失败");


    private String code;
    private String message;

    private ExceptionCodeEnum(String code, String message){
        this.code = code;
        this.message = message;
    }

    ExceptionCodeEnum(){}

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
