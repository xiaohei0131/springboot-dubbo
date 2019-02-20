package com.sike.user.dao;

import com.sike.entity.user.RoleEntity;
import com.sike.entity.user.RolePermissionEntity;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface RoleDao {

    @Select("select sr.*  from sys_role sr join sys_user_role sur on sr.id=sur.role_id where sur.user_id=#{userId}")
    @Results({
            @Result(column = "id", property = "permissions", many = @Many(select = "com.sike.user.dao.RoleDao.findPermissionsByRoleId"))
    })
    List<RoleEntity> findRolesByUserId(@Param("userId") String userId);

    @Select("select permission,application from sys_role_permission where role_id=#{roleId}")
    List<RolePermissionEntity> findPermissionsByRoleId(@Param("roleId") String roleId);

    @Select("select id from sys_role where name=#{name}")
    String findRoleIdByName(@Param("name") String name);

    @Insert("insert into sys_user_role (user_id,role_id) values(#{userId},#{roleId})")
    int addUserRole(@Param("userId") String userId,@Param("roleId") String roleId);
}
