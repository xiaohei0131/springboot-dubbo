package com.sike.user.service;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.sike.bean.user.UserQuery;
import com.sike.constant.RoleConstants;
import com.sike.entity.user.UserEntity;
import com.sike.enumeration.user.UserStateEnum;
import com.sike.exception.BusinessException;
import com.sike.exception.ExceptionCodeEnum;
import com.sike.request.user.LoginReq;
import com.sike.request.user.ModifyInfoReq;
import com.sike.request.user.RegisterReq;
import com.sike.request.user.UserPageReq;
import com.sike.response.PageResult;
import com.sike.service.user.UserService;
import com.sike.user.dao.RoleDao;
import com.sike.user.dao.UserDao;
import com.sike.utils.KeyGenerator;
import com.sike.utils.MD5Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(version = "1.0.0")
@Component
@Transactional
public class UserServiceImpl implements UserService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;

    @Override
    public UserEntity login(LoginReq loginReq) {
        UserQuery userQuery = new UserQuery();
        userQuery.setEmail(loginReq.getEmail());
        userQuery.setUsername(loginReq.getUsername());
        userQuery.setPhone(loginReq.getPhone());
        UserEntity userEntity = userDao.queyUser(userQuery);
        if (userEntity != null && loginReq.getPassword() != null
                && userEntity.getPassword().equals(MD5Util.entrypt(userEntity.getUsername(), loginReq.getPassword(), userEntity.getSalt()))) {
            /** 登录成功，清除敏感数据 **/
            userEntity.setPassword("");
            userEntity.setSalt("");
            if (userEntity.getUserState() == UserStateEnum.OFF.getCode()) {
                throw new BusinessException(ExceptionCodeEnum.ACCOUNT_FORBIDDEN);
            }
        } else {
            throw new BusinessException(ExceptionCodeEnum.LOGIN_FAIL);
        }
        return userEntity;
    }

    @Override
    public UserEntity register(RegisterReq registerReq) {
        checkRegisterParam(registerReq);
        UserEntity userEntity = new UserEntity();
        userEntity.setId(KeyGenerator.getKey());
        userEntity.setUsername(registerReq.getUsername());
        userEntity.setEmail(registerReq.getEmail());
        userEntity.setPhone(registerReq.getPhone());
        userEntity.setNickName(registerReq.getNickName());
        userEntity.setUserState(UserStateEnum.ON.getCode());
        userEntity.setSalt(KeyGenerator.getKey());
        userEntity.setPassword(MD5Util.entrypt(registerReq.getUsername(), registerReq.getPassword(), userEntity.getSalt()));
        int num = userDao.createUser(userEntity);
        if (num == 0) {
            throw new BusinessException(ExceptionCodeEnum.REGISTER_FAIL);
        }
        //绑定角色
        String defaultRoleId = roleDao.findRoleIdByName(RoleConstants.ROLE_NORMAL);
        num = roleDao.addUserRole(userEntity.getId(), defaultRoleId);
        if (num == 0) {
            throw new BusinessException(ExceptionCodeEnum.ROLE_BIND_FAIL);
        }
        return userEntity;
    }

    /**
     * 校验用户注册参数
     *
     * @param registerReq
     */
    private void checkRegisterParam(RegisterReq registerReq) {
        if (StringUtils.isEmpty(registerReq.getUsername())) {
            throw new BusinessException(ExceptionCodeEnum.USERNAME_NULL);
        }
        if (StringUtils.isEmpty(registerReq.getPassword())) {
            throw new BusinessException(ExceptionCodeEnum.PASSWD_NULL);
        }
        if (StringUtils.isEmpty(registerReq.getEmail())) {
            throw new BusinessException(ExceptionCodeEnum.EMAIL_NULL);
        }
        if (StringUtils.isEmpty(registerReq.getPhone())) {
            throw new BusinessException(ExceptionCodeEnum.PHONE_NULL);
        }

        UserQuery userQuery = new UserQuery();
        userQuery.setEmail(registerReq.getEmail());
        userQuery.setUsername(registerReq.getUsername());
        userQuery.setPhone(registerReq.getPhone());
        UserEntity userEntity = userDao.queyUser(userQuery);
        if (userEntity != null) {
            if (registerReq.getUsername().equals(userEntity.getUsername())) {
                throw new BusinessException(ExceptionCodeEnum.USERNAME_USED);
            }
            if (registerReq.getEmail().equals(userEntity.getEmail())) {
                throw new BusinessException(ExceptionCodeEnum.EMAIL_USED);
            }
            if (registerReq.getPhone().equals(userEntity.getPhone())) {
                throw new BusinessException(ExceptionCodeEnum.PHONE_USED);
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public PageResult queryUsers(UserPageReq userPageReq) {
        PageHelper.startPage(userPageReq.getPageNum(), userPageReq.getPageSize());
        List<UserEntity> users = userDao.findUsers(userPageReq);
        return PageResult.make(users);
    }

    @Override
    public void enableUser(String userId) {
        int upNum = userDao.updateUserState(userId, UserStateEnum.ON.getCode());
        if (upNum == 0) {
            throw new BusinessException(ExceptionCodeEnum.ENABLE_FAIL);
        }
    }

    @Override
    public void disableUser(String userId) {
        int upNum = userDao.updateUserState(userId, UserStateEnum.OFF.getCode());
        if (upNum == 0) {
            throw new BusinessException(ExceptionCodeEnum.DISABLE_FAIL);
        }
    }

    @Override
    public void modifyPwd(String userId, String oldPassword, String newPassword) {
        if (StringUtils.isEmpty(newPassword)) {
            throw new BusinessException(ExceptionCodeEnum.PASSWD_NULL);
        }
        UserEntity user = userDao.queryUserById(userId);
        if (user == null) {
            throw new BusinessException(ExceptionCodeEnum.MODIFY_FAIL);
        }
        if (user.getUserState() == UserStateEnum.OFF.getCode()) {
            throw new BusinessException(ExceptionCodeEnum.ACCOUNT_FORBIDDEN);
        }
        if (!user.getPassword().equals(MD5Util.entrypt(user.getUsername(), oldPassword, user.getSalt()))) {
            throw new BusinessException(ExceptionCodeEnum.OLD_PWD_ERROR);
        }
        int upNum = userDao.updatePwd(userId, MD5Util.entrypt(user.getUsername(), newPassword, user.getSalt()));
        if (upNum == 0) {
            throw new BusinessException(ExceptionCodeEnum.MODIFY_FAIL);
        }
    }

    @Override
    public void modifyUser(ModifyInfoReq modifyInfoReq) {
        if (StringUtils.isEmpty(modifyInfoReq.getEmail())) {
            throw new BusinessException(ExceptionCodeEnum.EMAIL_NULL);
        }
        if (StringUtils.isEmpty(modifyInfoReq.getPhone())) {
            throw new BusinessException(ExceptionCodeEnum.PHONE_NULL);
        }

        int upNum = userDao.updateUserInfo(modifyInfoReq);
        if (upNum == 0) {
            throw new BusinessException(ExceptionCodeEnum.MODIFY_FAIL);
        }
    }
}
