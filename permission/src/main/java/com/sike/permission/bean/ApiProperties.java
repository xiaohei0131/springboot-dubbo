package com.sike.permission.bean;

import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;
import java.util.Map;

public class ApiProperties {
    //请求地址
    private String address;
    //请求类型
    private String method = RequestMethod.POST.name();
    //请求参数
    private Map<String, Object> params = new HashMap<>();

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }
}
