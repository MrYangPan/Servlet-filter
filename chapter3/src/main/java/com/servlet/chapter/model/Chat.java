package com.servlet.chapter.model;

import java.util.Set;

/**
 * Created by Mr.PanYang on 2018/5/16.
 */
public class Chat {

    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    private Set<String> loginUserNames;

    public Set<String> getLoginUserNames() {
        return loginUserNames;
    }

    public void setLoginUserNames(Set<String> loginUserNames) {
        this.loginUserNames = loginUserNames;
    }

}
