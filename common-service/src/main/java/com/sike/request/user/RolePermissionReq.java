package com.sike.request.user;

import com.sike.bean.BaseBean;

import java.util.List;

public class RolePermissionReq extends BaseBean {
    private String roleId;
    private List<PermissionReq> permissions;

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public List<PermissionReq> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<PermissionReq> permissions) {
        this.permissions = permissions;
    }

}
