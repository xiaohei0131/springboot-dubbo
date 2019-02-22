package com.sike.controller.user;

import com.alibaba.dubbo.config.annotation.Reference;
import com.sike.entity.user.UserEntity;
import com.sike.exception.BusinessException;
import com.sike.exception.ExceptionCodeEnum;
import com.sike.permission.annotation.Permission;
import com.sike.request.user.LoginReq;
import com.sike.request.user.ModifyInfoReq;
import com.sike.request.user.RegisterReq;
import com.sike.request.user.UserPageReq;
import com.sike.response.PageResult;
import com.sike.response.Result;
import com.sike.service.user.UserService;
import com.sike.utils.SessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/user")
public class UserController {

    @Reference(version = "1.0.0")
    UserService userService;

    @Autowired
    SessionUtil sessionUtil;

    @PostMapping("/login")
    public Result login(LoginReq loginReq, HttpServletResponse httpRsp) {
        UserEntity userEntity = userService.login(loginReq);
        sessionUtil.setSession(httpRsp, userEntity);
        return Result.success();
    }

    @PostMapping("/register")
    public Result register(RegisterReq registerReq) {
        userService.register(registerReq);
        return Result.success();
    }

    /**
     * 退出登录（移除缓存session）
     *
     * @param httpReq
     * @return
     */
    @PostMapping("/logout")
    @Permission(code = "user.logout", name = "退出登录")
    public Result logout(HttpServletRequest httpReq) {
        sessionUtil.removeSession(httpReq);
        return Result.success();
    }

    @PostMapping("/list")
    @Permission(code = "user.list", name = "分页查询用户")
    public PageResult getUserList(UserPageReq userPageReq) {
        return userService.queryUsers(userPageReq);
    }

    @PostMapping("/enable")
    @Permission(code = "user.enable", name = "启用用户")
    public Result enableUser(@RequestParam("userId") String userId) {
        if (StringUtils.isEmpty(userId)) {
            throw new BusinessException(ExceptionCodeEnum.PARAM_NULL);
        }
        userService.enableUser(userId);
        return Result.success();
    }

    @PostMapping("/disable")
    @Permission(code = "user.disable", name = "禁用用户")
    public Result disableUser(@RequestParam("userId") String userId) {
        if (StringUtils.isEmpty(userId)) {
            throw new BusinessException(ExceptionCodeEnum.PARAM_NULL);
        }
        userService.disableUser(userId);
        return Result.success();
    }

    @PostMapping("/modifySelfPwd")
    @Permission(code = "user.self.modifyPwd", name = "修改自己的密码")
    public Result modifySelfPwd(HttpServletRequest httpReq, @RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword) {
        UserEntity userEntity = sessionUtil.getUser(httpReq);
        if (userEntity == null) {
            throw new BusinessException(ExceptionCodeEnum.MISSION_LOGIN);
        }
        if (StringUtils.isEmpty(newPassword)) {
            throw new BusinessException(ExceptionCodeEnum.PASSWD_NULL);
        }
        userService.modifyPwd(userEntity.getId(), oldPassword, newPassword);
        return Result.success();
    }

    @PostMapping("/modifySelfInfo")
    @Permission(code = "user.self.modifyInfo", name = "修改用户自己的信息")
    public Result modifySelfInfo(HttpServletRequest httpReq,ModifyInfoReq modifyInfoReq) {
        UserEntity userEntity = sessionUtil.getUser(httpReq);
        if (userEntity == null) {
            throw new BusinessException(ExceptionCodeEnum.MISSION_LOGIN);
        }
        modifyInfoReq.setUserId(userEntity.getId());
        userService.modifyUser(modifyInfoReq);
        return Result.success();
    }

    @PostMapping("/modifyUserInfo")
    @Permission(code = "user.modifyInfo", name = "修改用户信息")
    public Result modifyUserInfo(ModifyInfoReq modifyInfoReq) {
        userService.modifyUser(modifyInfoReq);
        return Result.success();
    }
}
