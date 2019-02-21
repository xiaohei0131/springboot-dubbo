package com.sike;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.alibaba.dubbo.spring.boot.annotation.EnableDubboConfiguration;
import com.sike.permission.annotation.EnablePermissionConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, DruidDataSourceAutoConfigure.class})
@EnableDubboConfiguration
@EnablePermissionConfiguration
public class ControllerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ControllerApplication.class, args);
    }
}
