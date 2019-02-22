package com.sike.request.user;

import com.sike.request.PageReq;

public class UserPageReq extends PageReq {
    private String username;
    private String phone;
    private String email;
    private String password;
    /**
     * 注册时间(起始)
     */
    private String registerTimeStart;
    /**
     * 注册时间(结束)
     */
    private String registerTimeEnd;

    private Integer userState = 1;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRegisterTimeStart() {
        return registerTimeStart;
    }

    public void setRegisterTimeStart(String registerTimeStart) {
        this.registerTimeStart = registerTimeStart;
    }

    public String getRegisterTimeEnd() {
        return registerTimeEnd;
    }

    public void setRegisterTimeEnd(String registerTimeEnd) {
        this.registerTimeEnd = registerTimeEnd;
    }

    public Integer getUserState() {
        return userState;
    }

    public void setUserState(Integer userState) {
        this.userState = userState;
    }

    @Override
    public String toString() {
        return "UserPageReq{" +
                "username='" + username + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", registerTimeStart='" + registerTimeStart + '\'' +
                ", registerTimeEnd='" + registerTimeEnd + '\'' +
                ", userState=" + userState +
                '}';
    }
}
