package xyz.idaoteng.myblog.login.entity.po;

import xyz.idaoteng.auth.subject.UserInfo;

public class User extends UserInfo {
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
