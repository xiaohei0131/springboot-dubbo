package com.sike.service.user;

import com.sike.request.user.RolePageReq;
import com.sike.request.user.RolePermissionReq;
import com.sike.response.PageResult;

public interface RoleService {
    /**
     * 新增角色
     * @param roleName
     * @param roleDesc
     */
    void addRole(String roleName,String roleDesc);

    /**
     * 根据角色id删除角色，同步删除角色绑定的权限以及用户和角色绑定关系
     * @param roleId
     */
    void deleteRoleById(String roleId);

    /**
     * 分页查询角色列表
     * @param rolePageReq
     * @return
     */
    PageResult queryRoles(RolePageReq rolePageReq);

    /**
     * 角色绑定权限
     * @param rolePermissionReq
     */
    void roleBindPermission(RolePermissionReq rolePermissionReq);

}
