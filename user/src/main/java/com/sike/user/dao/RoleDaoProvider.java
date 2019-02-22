package com.sike.user.dao;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.sike.request.user.PermissionReq;
import com.sike.request.user.RolePageReq;
import com.sike.request.user.RolePermissionReq;
import org.apache.ibatis.annotations.Param;

public class RoleDaoProvider {

    public String findRoles(@Param("rolePageReq") RolePageReq rolePageReq) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("select id,name,description from sys_role ");
        sqlBuilder.append("where 1=1 ");
        if (rolePageReq != null) {
            if (StringUtils.isNotEmpty(rolePageReq.getRoleName())) {
                sqlBuilder.append(" and name like '%");
                sqlBuilder.append(rolePageReq.getRoleName());
                sqlBuilder.append("%'");
            }
            if (StringUtils.isNotEmpty(rolePageReq.getRoleDesc())) {
                sqlBuilder.append(" and description like '%");
                sqlBuilder.append(rolePageReq.getRoleDesc());
                sqlBuilder.append("%'");
            }
        }
        return sqlBuilder.toString();
    }

    public String addRolePermission(@Param("rolePermissionReq") RolePermissionReq rolePermissionReq) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("insert into sys_role_permission (role_id,permission,application) values ");
        int rowNum = 0;
        for (PermissionReq permissionReq : rolePermissionReq.getPermissions()){
            if(rowNum > 0){
                sqlBuilder.append(",");
            }
            sqlBuilder.append("(");
            sqlBuilder.append("'").append(rolePermissionReq.getRoleId()).append("',");
            sqlBuilder.append("'").append(permissionReq.getPermission()).append("',");
            sqlBuilder.append("'").append(permissionReq.getApplication()).append("'");
            sqlBuilder.append(")");
            rowNum ++;
        }
        return sqlBuilder.toString();
    }
}
