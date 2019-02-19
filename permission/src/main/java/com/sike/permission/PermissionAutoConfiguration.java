package com.sike.permission;

import com.alibaba.fastjson.JSONObject;
import com.sike.permission.annotation.EnablePermissionConfiguration;
import com.sike.permission.annotation.Permission;
import com.sike.permission.bean.PermissionBean;
import org.springframework.beans.BeanInstantiationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.NameValueExpression;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.*;

@Configuration
@ConditionalOnBean(
        annotation = {EnablePermissionConfiguration.class}
)
@EnableConfigurationProperties({PermissionProperties.class})
public class PermissionAutoConfiguration implements CommandLineRunner {
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private PermissionProperties properties;
    @Autowired
    StringRedisTemplate redisTemplate;


    private Set<String> permissionCodeSet = new HashSet<>();
    private Set<String> permissionNameSet = new HashSet<>();

    @Override
    public void run(String... args) throws Exception {
        if (properties == null) {
            throw new IllegalArgumentException("Not found configuration whith prefix is \"sike.permission\".Please check your configuration.");
        }
        if (properties.getApplication() == null || StringUtils.isEmpty(properties.getApplication().getName())) {
            throw new IllegalArgumentException("Not found configuration about \"sike.permission.application.name\".Please check your configuration.");
        }
        String[] packages;
        if (!StringUtils.isEmpty(properties.getScan())) {
            packages = properties.getScan().split(";");
        } else {
            List<String> pgs = new ArrayList<>();
            Map<String, Object> beansWithAnnotationMap = applicationContext.getBeansWithAnnotation(EnablePermissionConfiguration.class);
            Class<? extends Object> clazz;
            for (Map.Entry<String, Object> entry : beansWithAnnotationMap.entrySet()) {
                clazz = entry.getValue().getClass();//获取到实例对象的class信息
                pgs.add(clazz.getPackage().getName());
            }
            packages = pgs.toArray(new String[pgs.size()]);
        }
        getPermissions(packages);
    }

    private void getPermissions(String[] packages) {
        if (packages == null || packages.length == 0) {
            return;
        }
        StringBuilder key = new StringBuilder();
        key.append("permission:");
        key.append(properties.getApplication().getName());
        key.append(":");
        key.append(properties.getApplication().getVersion());
        //删除缓存
        redisTemplate.delete(key.toString());

        RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        // 取得对应Annotation映射，BeanName -- 实例
        RequestMappingInfo requestMappingInfo;
        Method method;
        Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();
        List<String> permissionBeanList = new ArrayList<>();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> m : map.entrySet()) {
            requestMappingInfo = m.getKey();
            method = m.getValue().getMethod();
            if (subPackage(method.getDeclaringClass().getPackage().getName(),packages)) {
                PermissionBean permissionBean = getPermission(requestMappingInfo, method);
                if (permissionBean != null) {
                    redisTemplate.opsForHash().put(key.toString(),permissionBean.getCode(),JSONObject.toJSONString(permissionBean));
//                    permissionBeanList.add(JSONObject.toJSONString(permissionBean));
                }
            }
        }

       /* if(CollectionUtils.isEmpty(permissionBeanList)){
            return;
        }
        StringBuilder key = new StringBuilder();
        key.append("permission:");
        key.append(properties.getApplication().getName());
        key.append(":");
        key.append(properties.getApplication().getVersion());
        redisTemplate.delete(key.toString());
        redisTemplate.opsForSet().add(key.toString(), permissionBeanList.toArray(new String[permissionBeanList.size()]));*/
    }

    private boolean subPackage(String pgName,String[] packages){
        if(StringUtils.isEmpty(pgName)){
            return false;
        }
        for(int i = 0;i<packages.length;i++){
            if(pgName.startsWith(packages[i])){
                return true;
            }
        }
        return false;
    }

    private PermissionBean getPermission(RequestMappingInfo requestMappingInfo, Method method) {
        PermissionBean permissionBean = new PermissionBean();
        Permission permission = AnnotationUtils.findAnnotation(method, Permission.class);
        if (permission == null) {
            return null;
        }
        if (permissionCodeSet.contains(permission.code())) {
            throw new BeanInstantiationException(method.getClass(), "Duplicate permission code [" + permission.code() + "]");
        }
        permissionCodeSet.add(permission.code());
        permissionBean.setCode(permission.code());
        if (permissionNameSet.contains(permission.name())) {
            throw new BeanInstantiationException(method.getClass(), "Duplicate permission name [" + permission.name() + "]");
        }
        permissionNameSet.add(permission.name());
        permissionBean.setName(permission.name());
        PatternsRequestCondition patternsRequestCondition = requestMappingInfo.getPatternsCondition();

        List<String> urls = new ArrayList<>();
        for (String url : patternsRequestCondition.getPatterns()) {
            urls.add(url);
        }
        permissionBean.setUrls(urls);
        List<String> methods = new ArrayList<>();
        for (RequestMethod requestMethod : requestMappingInfo.getMethodsCondition().getMethods()) {
            methods.add(requestMethod.toString());
        }
        permissionBean.setMethods(methods);

        List<String> consumers = new ArrayList<>();
        for (MediaType mediaType : requestMappingInfo.getConsumesCondition().getConsumableMediaTypes()) {
            consumers.add(mediaType.toString());
        }
        permissionBean.setConsumers(consumers);

        List<String> produces = new ArrayList<>();
        for (MediaType mediaType : requestMappingInfo.getProducesCondition().getProducibleMediaTypes()) {
            produces.add(mediaType.toString());
        }
        permissionBean.setProduces(produces);


        List<String> headers = new ArrayList<>();
        for (NameValueExpression<String> nve : requestMappingInfo.getHeadersCondition().getExpressions()) {
            headers.add(nve.toString());
        }
        permissionBean.setHeaders(headers);

        List<String> params = new ArrayList<>();
        for (NameValueExpression<String> nve : requestMappingInfo.getParamsCondition().getExpressions()) {
            params.add(nve.toString());
        }
        permissionBean.setParams(params);

        return permissionBean;
    }
}
