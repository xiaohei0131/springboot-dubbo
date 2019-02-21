package com.sike.user.dao;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.sike.request.user.UserPageReq;
import org.apache.ibatis.annotations.Param;

public class UserDaoProvider {

    public String findUsers(@Param("userPageReq") UserPageReq userPageReq) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("select id,username,nick_name,portrait,phone,email,register_time,modify_time,user_state from sys_user ");
        sqlBuilder.append("where 1=1 ");
        if (userPageReq != null) {
            if (StringUtils.isNotEmpty(userPageReq.getUsername())) {
                sqlBuilder.append(" and username like '%");
                sqlBuilder.append(userPageReq.getUsername());
                sqlBuilder.append("%'");
            }
            if (StringUtils.isNotEmpty(userPageReq.getEmail())) {
                sqlBuilder.append(" and email like '%");
                sqlBuilder.append(userPageReq.getEmail());
                sqlBuilder.append("%'");
            }
            if (StringUtils.isNotEmpty(userPageReq.getPhone())) {
                sqlBuilder.append(" and phone like '%");
                sqlBuilder.append(userPageReq.getPhone());
                sqlBuilder.append("%'");
            }
            if (StringUtils.isNotEmpty(userPageReq.getRegisterTimeStart())) {
                sqlBuilder.append(" and register_time >='");
                sqlBuilder.append(userPageReq.getRegisterTimeStart());
                sqlBuilder.append("'");
            }
            if (StringUtils.isNotEmpty(userPageReq.getRegisterTimeEnd())) {
                sqlBuilder.append(" and register_time <='");
                sqlBuilder.append(userPageReq.getRegisterTimeEnd());
                sqlBuilder.append("'");
            }
        }
        return sqlBuilder.toString();
    }
}
