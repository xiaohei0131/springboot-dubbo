package com.sike.user.dao;

import com.sike.bean.user.UserQuery;
import com.sike.entity.user.RoleEntity;
import com.sike.entity.user.UserEntity;
import com.sike.request.user.ModifyInfoReq;
import com.sike.request.user.UserPageReq;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface UserDao {

    @SelectProvider(type = UserDaoProvider.class, method = "findUsers")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "nick_name", property = "nickName"),
            @Result(column = "register_time", property = "registerTime"),
            @Result(column = "user_state", property = "userState"),
            @Result(column = "id", property = "roles", many = @Many(select = "com.sike.user.dao.RoleDao.findRolesByUserId"))
    })
    List<UserEntity> findUsers(@Param("userPageReq") UserPageReq userPageReq);

    @Select("select id,username,nick_name,password,salt,portrait,phone,email,register_time,modify_time,user_state from sys_user where username=#{userQuery.username} or phone=#{userQuery.phone} or email=#{userQuery.email}")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "nick_name", property = "nickName"),
            @Result(column = "register_time", property = "registerTime"),
            @Result(column = "user_state", property = "userState"),
            @Result(column = "id", property = "roles", many = @Many(select = "com.sike.user.dao.RoleDao.findRolesByUserId"))
    })
    UserEntity queyUser(@Param("userQuery") UserQuery userQuery);

    @Insert("INSERT INTO sys_user (id,username,nick_name,password,salt,phone,email,portrait,register_time,user_state) " +
            "        VALUES (#{id},#{username},#{nickName},#{password},#{salt},#{phone},#{email},#{portrait},now(),#{userState})")
    int createUser(UserEntity userEntity);

    @Update("update sys_user set user_state=#{status} where id=#{userId}")
    int updateUserState(@Param("userId") String userId,@Param("status") int status);

    @Select("select id,username,nick_name,password,salt,phone,email,user_state from sys_user where id=#{userId}")
    @Results({
            @Result(column = "nick_name", property = "nickName"),
            @Result(column = "user_state", property = "userState")
    })
    UserEntity queryUserById(@Param("userId") String userId);

    @Update("update sys_user set password=#{pwd} where id=#{userId}")
    int updatePwd(@Param("userId") String userId,@Param("pwd") String pwd);

    @Update("update sys_user set nick_name=#{user.nickName},phone=#{user.phone},email=#{user.email},portrait=#{user.portrait} where id=#{user.userId}")
    int updateUserInfo(@Param("user") ModifyInfoReq modifyInfoReq);
}
