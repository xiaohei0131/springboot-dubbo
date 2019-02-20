package com.sike.permission;

import com.sike.permission.annotation.EnablePermissionConfiguration;
import com.sike.permission.annotation.Permission;
import com.sike.permission.bean.PermissionBean;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.aop.framework.ReflectiveMethodInvocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Aspect
@Component
@ConditionalOnBean(annotation = {EnablePermissionConfiguration.class})
@EnableConfigurationProperties({PermissionProperties.class})
public class PermissionHandler {

    @Autowired
    PermissionProperties permissionProperties;
    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    @ConditionalOnMissingBean(PermissionAuthService.class)
    public PermissionAuthService permissionAuthService() {
        return new PermissionAuthServiceImpl();
    }

    @Autowired
    PermissionAuthService permissionAuthService;

    @Pointcut("@annotation(com.sike.permission.annotation.Permission)")
    public void permissionMethods() {
    }

    @Pointcut("@target(org.springframework.web.bind.annotation.RequestMapping)")
    public void requestMethods() {
    }

    @Pointcut("permissionMethods() && requestMethods()")
    public void authMethods() {
    }

    /**
     * 拦截拥有@Permission和@RequestMapping的请求
     */
    @Before("authMethods()")
    public void doBefore(JoinPoint joinPoint) throws NoSuchFieldException, IllegalAccessException {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String className = joinPoint.getSignature().getDeclaringTypeName();

        List<String> packages = getAuthPackages();
        boolean include = false;
        if (StringUtils.isEmpty(className) && CollectionUtils.isEmpty(packages)) {
            include = true;
        } else {
            for (String authPackage : packages) {
                if (className != null && className.startsWith(authPackage)) {
                    include = true;
                    break;
                }
            }
        }
        if (include) {

            MethodInvocationProceedingJoinPoint methodPoint = (MethodInvocationProceedingJoinPoint) joinPoint;

            Field proxy = methodPoint.getClass().getDeclaredField("methodInvocation");

            proxy.setAccessible(true);

            ReflectiveMethodInvocation j = (ReflectiveMethodInvocation) proxy.get(methodPoint);

            Method method = j.getMethod();

            Permission permissionAnnotation = method.getAnnotation(Permission.class);
            PermissionBean permissionBean = new PermissionBean();
            permissionBean.setApplicationName(permissionProperties.getApplication().getName());
            permissionBean.setCode(permissionAnnotation.code());
            permissionBean.setName(permissionAnnotation.name());

            // 访问鉴权
            permissionAuthService.authentication(request, joinPoint.getArgs(),permissionBean);
        }
    }

    private List<String> getAuthPackages() {
        List<String> packages = new ArrayList<>();
        if (!StringUtils.isEmpty(permissionProperties.getScan())) {
            packages = Arrays.asList(permissionProperties.getScan().split(";"));
        } else {
            Map<String, Object> beansWithAnnotationMap = applicationContext.getBeansWithAnnotation(EnablePermissionConfiguration.class);
            Class<? extends Object> clazz;
            for (Map.Entry<String, Object> entry : beansWithAnnotationMap.entrySet()) {
                clazz = entry.getValue().getClass();//获取到实例对象的class信息
                packages.add(clazz.getPackage().getName());
            }
        }
        return packages;
    }
}
