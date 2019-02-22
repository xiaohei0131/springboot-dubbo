package com.sike.controller.user;

import com.alibaba.dubbo.config.annotation.Reference;
import com.sike.permission.annotation.Permission;
import com.sike.request.user.RolePageReq;
import com.sike.request.user.RolePermissionReq;
import com.sike.response.PageResult;
import com.sike.response.Result;
import com.sike.service.user.RoleService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/role")
public class RoleController {
    @Reference(version = "1.0.0")
    RoleService roleService;

    @PostMapping("/list")
    @Permission(code = "role.list", name = "分页查询角色")
    public PageResult getRoleList(RolePageReq rolePageReq) {
        return roleService.queryRoles(rolePageReq);
    }

    @PostMapping("/add")
    @Permission(code = "role.add", name = "创建角色")
    public Result addRole(@RequestParam("roleName") String roleName, @RequestParam("roleDesc") String roleDesc) {
        roleService.addRole(roleName, roleDesc);
        return Result.success();
    }

    @PostMapping("/permission/bind")
    @Permission(code = "role.bind.permission", name = "角色分配权限")
    public Result roleBindPermission(@RequestBody RolePermissionReq rolePermissionReq) {
        roleService.roleBindPermission(rolePermissionReq);
        return Result.success();
    }

    @DeleteMapping("/delete/{roleId}")
    @Permission(code = "role.delete", name = "角色删除")
    public Result deleteRoleById(@PathVariable("roleId") String roleId) {
        roleService.deleteRoleById(roleId);
        return Result.success();
    }

}
