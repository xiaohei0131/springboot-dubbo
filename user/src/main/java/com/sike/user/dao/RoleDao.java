package com.sike.user.dao;

import com.sike.entity.user.RoleEntity;
import com.sike.entity.user.RolePermissionEntity;
import com.sike.request.user.RolePageReq;
import com.sike.request.user.RolePermissionReq;
import com.sike.user.bean.RoleResult;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface RoleDao {

    @Select("select sr.*  from sys_role sr join sys_user_role sur on sr.id=sur.role_id where sur.user_id=#{userId}")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "id", property = "permissions", many = @Many(select = "com.sike.user.dao.RoleDao.findPermissionsByRoleId"))
    })
    List<RoleEntity> findRolesByUserId(@Param("userId") String userId);

    @Select("select permission,application from sys_role_permission where role_id=#{roleId}")
    List<RolePermissionEntity> findPermissionsByRoleId(@Param("roleId") String roleId);

    @Select("select id from sys_role where name=#{name}")
    String findRoleIdByName(@Param("name") String name);

    @Select("select * from sys_role where id=#{roleId}")
    RoleEntity findRoleById(@Param("roleId") String roleId);

    @Insert("insert into sys_user_role (user_id,role_id) values(#{userId},#{roleId})")
    int addUserRole(@Param("userId") String userId, @Param("roleId") String roleId);

    @Insert("INSERT INTO sys_role (id,name,description,role_type) " +
            "        VALUES (#{id},#{name},#{description},#{roleType})")
    int createRole(RoleEntity roleEntity);

    @Delete("delete from sys_role where id=#{roleId} and role_type=#{roleType}")
    int deleteRoleByRoleId(@Param("roleId") String roleId, @Param("roleType") int roleType);

    @Delete("delete from sys_user_role where role_id=#{roleId}")
    int deleteUserRoleByRoleId(@Param("roleId") String roleId);

    @Delete("delete from sys_role_permission where role_id=#{roleId}")
    int deleteRolePermissionByRoleId(@Param("roleId") String roleId);

    @SelectProvider(type = RoleDaoProvider.class, method = "findRoles")
    @Results({
            @Result(column = "id", property = "roleId"),
            @Result(column = "name", property = "roleName"),
            @Result(column = "description", property = "description"),
            @Result(column = "id", property = "userNum", one = @One(select = "com.sike.user.dao.RoleDao.countUsersByRoleId"))
    })
    List<RoleResult> findRoles(@Param("rolePageReq") RolePageReq rolePageReq);

    @Select("select COUNT(distinct user_id) from sys_user_role where role_id=#{roleId}")
    int countUsersByRoleId(@Param("roleId") String roleId);

    @InsertProvider(type = RoleDaoProvider.class,method = "addRolePermission")
    int addRolePermission(@Param("rolePermissionReq")RolePermissionReq rolePermissionReq);
}
