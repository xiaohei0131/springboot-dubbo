package com.sike.utils;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.sike.entity.user.UserEntity;
import com.sike.exception.BusinessException;
import com.sike.exception.ExceptionCodeEnum;
import com.sike.service.redis.RedisService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class SessionUtil {

    @Reference(version = "1.0.0")
    private RedisService redisService;

    @Value("${sike.session.name:'USER_TOKEN'}")
    private String sessionName;

    @Value("${sike.session.timeout:300}")
    private Long tokenExpire;

    /**
     * 获取SessionID
     * @param request 当前的请求对象
     * @return SessionID的值
     */
    private String getSessionId(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        // 遍历所有cookie
        String sessionId = null;
        if (cookies!=null && cookies.length>0) {
            for (Cookie cookie : cookies) {
                if (sessionName.equals(cookie.getName())) {
                    sessionId = cookie.getValue();
                    break;
                }
            }
        }

        return sessionId;
    }

    /**
     * 存储session
     * @param httpRsp
     * @param userEntity
     * @return
     */
    public void setSession(HttpServletResponse httpRsp,UserEntity userEntity){
        String tokenId = KeyGenerator.getKey();
        Cookie cookie = new Cookie(sessionName, tokenId);
        cookie.setPath("/");
        httpRsp.addCookie(cookie);
        //redis存储
        boolean re = redisService.set(tokenId, userEntity, tokenExpire);
        if(!re){
            throw new BusinessException(ExceptionCodeEnum.SESSION_NO_STORE);
        }
    }

    /**
     * 移除session
     * @param httpServletReq
     * @return
     */
    public void removeSession(HttpServletRequest httpServletReq){
        // sessionId
        String sessionId = getSessionId(httpServletReq);
        if (StringUtils.isEmpty(sessionId)) {
            throw new BusinessException(ExceptionCodeEnum.UNLOGIN);
        }

        redisService.remove(sessionId);
    }

    /**
     * 获取用户信息
     * @param httpServletReq HTTP请求
     * @return 用户信息
     */
    public UserEntity getUser(HttpServletRequest httpServletReq) {
        // sessionId
        String sessionId = getSessionId(httpServletReq);
        if (StringUtils.isEmpty(sessionId)) {
            throw new BusinessException(ExceptionCodeEnum.UNLOGIN);
        }

        // 获取UserEntity
        Object userEntity = redisService.get(sessionId);
        if (userEntity == null) {
            return null;
        }
        return (UserEntity) userEntity;

    }
}
