package com.sike.permission;

import com.sike.permission.bean.PermissionBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

public class PermissionAuthServiceImpl implements PermissionAuthService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public PermissionAuthServiceImpl() {
        logger.warn("This is default authentication and do nothing.Please implement PermissionAuthService by yourself.");
    }

    @Override
    public void authentication(HttpServletRequest request, Object[] args, PermissionBean permissionBean) {

    }
}
