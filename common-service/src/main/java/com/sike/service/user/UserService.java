package com.sike.service.user;

import com.sike.entity.user.UserEntity;
import com.sike.request.user.LoginReq;
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
}
