package com.sike.permission.tools;

import com.sike.permission.bean.PermissionBean;
import org.springframework.http.MediaType;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class PermissionTool {
    private static List<PermissionBean> permissionBeans;

    public static void setPermissionBeans(List<PermissionBean> permissionBeans) {
        PermissionTool.permissionBeans = permissionBeans;
    }

    public static List<PermissionBean> getPermissionBeans() {
        return permissionBeans;
    }
}
