package com.sike.handler;

import com.sike.entity.user.RoleEntity;
import com.sike.entity.user.RolePermissionEntity;
import com.sike.entity.user.UserEntity;
import com.sike.exception.BusinessException;
import com.sike.exception.ExceptionCodeEnum;
import com.sike.permission.PermissionAuthService;
import com.sike.permission.bean.PermissionBean;
import com.sike.utils.SessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static com.sike.constant.RoleConstants.ROLE_ADMIN;

@Component
public class PermissionAuthHandler implements PermissionAuthService {
    @Autowired
    SessionUtil sessionUtil;

    @Override
    public void authentication(HttpServletRequest request, Object[] args, PermissionBean permissionBean) {
        UserEntity userEntity = sessionUtil.getUser(request);
        if(userEntity == null){
            throw new BusinessException(ExceptionCodeEnum.UNLOGIN);
        }
        List<RolePermissionEntity> permissions = new ArrayList<>();
        for (RoleEntity roleEntity : userEntity.getRoles()) {
            if (ROLE_ADMIN.equals(roleEntity.getName())) {
                return;
            }
            permissions.addAll(roleEntity.getPermissions());
        }
        for (RolePermissionEntity permissionEntity : permissions) {
            if (permissionEntity.getApplication().equals(permissionBean.getApplicationName()) &&
                    permissionEntity.getPermission().equals(permissionBean.getCode())) {
                return;
            }
        }
        throw new BusinessException(ExceptionCodeEnum.NO_PERMISSION);
    }
}
