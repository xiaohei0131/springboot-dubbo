package com.sike.permission;

import com.sike.permission.bean.ApiProperties;
import com.sike.permission.bean.ApplicationProperties;
import com.sike.permission.bean.Constants;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(
        prefix = "sike.permission"
)
public class PermissionProperties {

    /**
     * 使用redis上报权限，redis配置基于springboot
     **/
    private String server;

    /**
     * 扫描路径，多个用英文分号隔开
     **/
    private String scan;

    private ApiProperties api;
    private ApplicationProperties application;

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getScan() {
        return scan;
    }

    public void setScan(String scan) {
        this.scan = scan;
    }

    public ApiProperties getApi() {
        return api;
    }

    public void setApi(ApiProperties api) {
        this.api = api;
    }

    public ApplicationProperties getApplication() {
        return application;
    }

    public void setApplication(ApplicationProperties application) {
        this.application = application;
    }
}
