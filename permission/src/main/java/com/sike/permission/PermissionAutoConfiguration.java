package com.sike.permission;

import com.alibaba.fastjson.JSONObject;
import com.sike.permission.annotation.EnablePermissionConfiguration;
import com.sike.permission.annotation.Permission;
import com.sike.permission.bean.Constants;
import com.sike.permission.bean.PermissionBean;
import com.sike.permission.tools.PermissionTool;
import org.springframework.beans.BeanInstantiationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

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

    private void getPermissions(String[] packages) throws ClassNotFoundException {
        if (packages == null || packages.length == 0) {
            return;
        }
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(beanFactory);
        List<PermissionBean> permissionBeanList = new ArrayList<>();
        List<String> permissionList = new ArrayList<>();
        for (String packageName : packages) {
            Set<BeanDefinition> candidateComponents = scanner.findCandidateComponents(packageName.trim());
            if (CollectionUtils.isEmpty(candidateComponents)) {
                continue;
            }
            for (BeanDefinition beanDefinition : candidateComponents) {
                Class<?> serviceBean = Class.forName(beanDefinition.getBeanClassName());
                Method[] methods = serviceBean.getMethods();
                PermissionBean permissionBean;
                for (Method method : methods) {
                    permissionBean = getPermission(method);
                    if (permissionBean != null) {
                        permissionBeanList.add(permissionBean);
                        permissionList.add(JSONObject.toJSONString(permissionBean));
                    }
                }
            }
        }

        if (CollectionUtils.isEmpty(permissionBeanList) || CollectionUtils.isEmpty(permissionList)) {
            return;
        }
        PermissionTool.setPermissionBeans(permissionBeanList);
        if (StringUtils.isEmpty(properties.getServer())) {
            return;
        }
        if (Constants.SERVER_REDIS.equals(properties.getServer())) {
            String key = redisPermissionKey();
            //删除缓存
            redisTemplate.delete(key);
            redisTemplate.opsForSet().add(key, permissionList.toArray(new String[permissionList.size()]));
        }
    }

    /**
     * 当前application权限集存入redis的key
     *
     * @return
     */
    private String redisPermissionKey() {
        StringBuilder key = new StringBuilder();
        key.append("permission:");
        key.append(properties.getApplication().getName());
        /*key.append(":");
        key.append(properties.getApplication().getVersion());*/
        return key.toString();
    }

    private PermissionBean getPermission(Method method) {
        PermissionBean permissionBean = new PermissionBean();
        permissionBean.setApplicationName(properties.getApplication().getName());
        Permission permission = AnnotationUtils.findAnnotation(method, Permission.class);
        if (permission == null) {
            return null;
        }
        if (permissionCodeSet.contains(permission.code())) {
            throw new BeanInstantiationException(method.getDeclaringClass(), "Duplicate permission code [" + permission.code() + "]");
        }
        permissionCodeSet.add(permission.code());
        permissionBean.setCode(permission.code());
        if (permissionNameSet.contains(permission.name())) {
            throw new BeanInstantiationException(method.getDeclaringClass(), "Duplicate permission name [" + permission.name() + "]");
        }
        permissionNameSet.add(permission.name());
        permissionBean.setName(permission.name());
        return permissionBean;
    }
}
