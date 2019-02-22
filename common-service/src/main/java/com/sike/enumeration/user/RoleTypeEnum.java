package com.sike.enumeration.user;

import com.sike.enumeration.BaseEnum;

public enum RoleTypeEnum implements BaseEnum {
    SYSTEM(0, "系统内置角色"),
    BUSINESS(1, "业务角色");

    private int code;
    private String msg;

    RoleTypeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }

    @Override
    public String toString() {
        return "RoleTypeEnum{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}
