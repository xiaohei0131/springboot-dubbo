# springboot-dubbo
> - SpringBoot 2.0.0.RELEASE
> - dubbo-spring-boot-starter 2.0.0
> - jdk 1.8

-----

## permission模块提供权限搜集及拦截作用
> 其他controller模块依赖permission，启动类增加注解@EnablePermissionConfiguration，然后使用@Permission(code = "{权限代码}",name = "{权限名称}")，配合@RequestMapping注解使用。

> 权限permission配置，若要实现权限校验功能，请实现接口 com.sike.permission.PermissionAuthService中authentication方法，
>   authentication方法参数有*
>       @param request HttpServletRequest对象
>       @param args 目标方法参数
>       @param permissionBean 权限信息
  -   sike.permission.server ，权限上报到指定服务
   若不配置，则不上报权限，
   若配置为redis，则上报权限到redis中，redis配置由spring.redis.* 决定，redis中key为 permission:${sike.permission.application.name}，set类型数据
   若配置为api（暂未实现），则上报到sike.permission.api.address地址，sike.permission.api.method默认post方式上报，sike.permission.api.params是map类型，指定上报api时候传参
  -   sike.permission.application.name 权限所属应用，确保唯一
  -   sike.permission.scan 权限扫描路径，同时也是权限拦截路径
