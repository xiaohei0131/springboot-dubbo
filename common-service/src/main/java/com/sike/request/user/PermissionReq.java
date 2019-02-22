package com.sike.request.user;


import com.sike.bean.BaseBean;

public class PermissionReq extends BaseBean {
    private String permission;
    private String application;

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }
}
