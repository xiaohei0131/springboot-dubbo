package com.sike.service.user;

import com.sike.entity.user.UserEntity;
import com.sike.request.user.LoginReq;
import com.sike.request.user.ModifyInfoReq;
import com.sike.request.user.RegisterReq;
import com.sike.request.user.UserPageReq;
import com.sike.response.PageResult;

public interface UserService {
    /**
     * 登录
     *
     * @param loginReq
     * @return
     */
    UserEntity login(LoginReq loginReq);

    /**
     * 注册
     *
     * @param registerReq
     * @return
     */
    UserEntity register(RegisterReq registerReq);

    /**
     * 分页查询用户
     *
     * @param userPageReq
     * @return
     */
    PageResult queryUsers(UserPageReq userPageReq);

    /**
     * 启用用户
     *
     * @param userId
     * @return
     */
    void enableUser(String userId);

    /**
     * 禁用用用户
     *
     * @param userId
     * @return
     */
    void disableUser(String userId);

    /**
     * 修改密码
     *
     * @param userId      用户id
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     */
    void modifyPwd(String userId, String oldPassword, String newPassword);

    /**
     * 修改用户信息
     *
     * @param modifyInfoReq
     */
    void modifyUser(ModifyInfoReq modifyInfoReq);
}
