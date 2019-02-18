package com.sike.controller.user;

import com.alibaba.dubbo.config.annotation.Reference;
import com.sike.entity.user.UserEntity;
import com.sike.request.user.LoginReq;
import com.sike.request.user.RegisterReq;
import com.sike.request.user.UserPageReq;
import com.sike.response.PageResult;
import com.sike.response.Result;
import com.sike.service.redis.RedisService;
import com.sike.service.user.UserService;
import com.sike.utils.KeyGenerator;
import com.sike.utils.SessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Reference(version = "1.0.0")
    UserService userService;

    @Autowired
    SessionUtil sessionUtil;

    public static Map<String, UserEntity> userMap = new HashMap<>();

    @GetMapping("/list")
    public PageResult getUserList(UserPageReq userPageReq) {
        return userService.queryUsers(userPageReq);
    }

    @PostMapping("/login")
    public Result login(LoginReq loginReq, HttpServletResponse httpRsp) {
        UserEntity userEntity = userService.login(loginReq);
        sessionUtil.setSession(httpRsp,userEntity);
        return Result.success();
    }

    @PostMapping("/register")
    public Result register(RegisterReq registerReq) {
        userService.register(registerReq);
        return Result.success();
    }

    /**
     * 退出登录（移除缓存session）
     * @param httpReq
     * @return
     */
    @PostMapping("/logout")
    public Result logout(HttpServletRequest httpReq) {
        sessionUtil.removeSession(httpReq);
        return Result.success();
    }
}
