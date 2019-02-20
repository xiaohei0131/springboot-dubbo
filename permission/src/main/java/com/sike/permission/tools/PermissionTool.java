package com.sike.permission.tools;

import com.sike.permission.bean.PermissionBean;
import org.springframework.http.MediaType;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class PermissionTool {
    private static AntPathMatcher antPathMatcher = new AntPathMatcher();
    private static List<PermissionBean> permissionBeans;

    public static void setPermissionBeans(List<PermissionBean> permissionBeans) {
        PermissionTool.permissionBeans = permissionBeans;
    }

    /**
     * 返回请求所需权限
     * @param request
     * @return
     */
    public static PermissionBean getPathPermission(HttpServletRequest request) {
        String path = request.getRequestURI();
        String method = request.getMethod();
        String consumer = request.getHeader("Content-Type");
        String produce = request.getHeader("Accept");
        if (null == permissionBeans || permissionBeans.size() == 0) {
            return null;
        }
        List<String> urls;
        List<String> methods;
        List<String> consumers;
        List<String> produces;
        for (PermissionBean permission : permissionBeans) {
            methods = permission.getMethods();
            if (!(methods.size() == 0 || methods.contains(method))) {
                continue;
            }
            urls = permission.getUrls();
            boolean matchPath = false;
            for (int j = 0; j < urls.size(); j++) {
                if (antPathMatcher.match(urls.get(j), path)) {
                    matchPath = true;
                    break;
                }
            }
            if (!matchPath) {
                continue;
            }
            consumers = permission.getConsumers();
            if (!(consumers.size() == 0 || consumers.contains(MediaType.ALL_VALUE) || consumers.contains(consumer))) {
                continue;
            }
            produces = permission.getProduces();
            if (!(produces.size() == 0 || produces.contains(MediaType.ALL_VALUE) || produces.contains(produce))) {
                continue;
            }
            return permission;
        }
        return null;
    }
}
