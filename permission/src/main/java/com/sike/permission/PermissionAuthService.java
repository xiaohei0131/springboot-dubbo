package com.sike.permission;

import com.sike.permission.bean.PermissionBean;

import javax.servlet.http.HttpServletRequest;

public interface PermissionAuthService {
    /**
     * 鉴权
     * @param request HttpServletRequest对象
     * @param args 目标方法参数
     * @param permissionBean 权限信息
     */
    void authentication(HttpServletRequest request, Object[] args, PermissionBean permissionBean);
}
