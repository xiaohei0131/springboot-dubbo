package com.sike.user.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.sike.entity.user.RoleEntity;
import com.sike.enumeration.user.RoleTypeEnum;
import com.sike.exception.BusinessException;
import com.sike.exception.ExceptionCodeEnum;
import com.sike.request.user.RolePageReq;
import com.sike.request.user.RolePermissionReq;
import com.sike.response.PageResult;
import com.sike.service.user.RoleService;
import com.sike.user.bean.RoleResult;
import com.sike.user.dao.RoleDao;
import com.sike.utils.KeyGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service(version = "1.0.0")
@Component
@Transactional
public class RoleServiceImpl implements RoleService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private RoleDao roleDao;

    @Override
    public void addRole(String roleName, String roleDesc) {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setDescription(roleDesc);
        roleEntity.setName(roleName);
        roleEntity.setId(KeyGenerator.getKey());
        roleEntity.setRoleType(RoleTypeEnum.BUSINESS.getCode());//默认新增的为业务角色
        int addNum = roleDao.createRole(roleEntity);
        if (addNum == 0) {
            throw new BusinessException(ExceptionCodeEnum.OPERATION_FAIL);
        }
    }

    @Override
    public void deleteRoleById(String roleId) {
        //删除角色，只允许删除业务角色
        int delNum = roleDao.deleteRoleByRoleId(roleId, RoleTypeEnum.BUSINESS.getCode());
        if (delNum == 0) {
            throw new BusinessException(ExceptionCodeEnum.OPERATION_FAIL);
        }
        //删除角色权限
        roleDao.deleteRolePermissionByRoleId(roleId);
        //删除角色用户关系
        roleDao.deleteUserRoleByRoleId(roleId);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResult queryRoles(RolePageReq rolePageReq) {
        PageHelper.startPage(rolePageReq.getPageNum(), rolePageReq.getPageSize());
        List<RoleResult> roles = roleDao.findRoles(rolePageReq);
        return PageResult.make(roles);
    }

    @Override
    public void roleBindPermission(RolePermissionReq rolePermissionReq) {
        RoleEntity role = roleDao.findRoleById(rolePermissionReq.getRoleId());
        if (role == null) {
            logger.error("角色不存在！");
            throw new BusinessException(ExceptionCodeEnum.OPERATION_FAIL);
        }
        //全部删除在新增
        roleDao.deleteRolePermissionByRoleId(rolePermissionReq.getRoleId());
        if (CollectionUtils.isEmpty(rolePermissionReq.getPermissions())) {
            return;
        }
        int addNum = roleDao.addRolePermission(rolePermissionReq);
        if (addNum == 0) {
            throw new BusinessException(ExceptionCodeEnum.OPERATION_FAIL);
        }
    }
}
