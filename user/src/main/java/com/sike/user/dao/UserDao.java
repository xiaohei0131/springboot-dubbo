package com.sike.user.dao;

import com.sike.bean.user.UserQuery;
import com.sike.entity.user.RoleEntity;
import com.sike.entity.user.UserEntity;
import com.sike.request.user.UserPageReq;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface UserDao {

    @SelectProvider(type = UserDaoProvider.class, method = "findUsers")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "username", property = "username"),
            @Result(column = "nick_name", property = "nickName"),
            @Result(column = "register_time", property = "registerTime"),
            @Result(column = "user_state", property = "userState"),
            @Result(column = "id", property = "roles", many = @Many(select = "com.sike.user.dao.UserDao.findRolesByUserId"))
    })
    List<UserEntity> findUsers(@Param("userPageReq") UserPageReq userPageReq);

    @Select("select sr.*  from sys_role sr join sys_user_role sur on sr.id=sur.role_id where sur.user_id=#{userId}")
    List<RoleEntity> findRolesByUserId(@Param("userId") String userId);

    @Select("select id,username,nick_name,password,salt,portrait,phone,email,register_time,user_state from sys_user where username=#{userQuery.username} or phone=#{userQuery.phone} or email=#{userQuery.email}")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "username", property = "username"),
            @Result(column = "nick_name", property = "nickName"),
            @Result(column = "register_time", property = "registerTime"),
            @Result(column = "user_state", property = "userState"),
            @Result(column = "id", property = "roles", many = @Many(select = "com.sike.user.dao.UserDao.findRolesByUserId"))
    })
    UserEntity loginCheck(@Param("userQuery") UserQuery userQuery);

    @Insert("INSERT INTO sys_user (id,username,nick_name,password,salt,phone,email,portrait,register_time,user_state) " +
            "        VALUES (#{id},#{username},#{nickName},#{password},#{salt},#{phone},#{email},#{portrait},now(),#{userState})")
    int createUser(UserEntity userEntity);
}
