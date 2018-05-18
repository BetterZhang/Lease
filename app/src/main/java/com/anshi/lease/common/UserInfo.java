package com.anshi.lease.common;

import com.anshi.lease.domain.UserVo;

/**
 * Created by Android Studio.
 * Author : zhangzhongqiang
 * Email  : betterzhang.dev@gmail.com
 * Time   : 2018/05/18 下午 2:49
 * Desc   : 保存账户信息
 */
public class UserInfo {

    private UserVo userVo;

    private static UserInfo userInfo = null;

    private UserInfo() {

    }

    public static UserInfo getInstance() {
        if (userInfo == null) {
            userInfo = new UserInfo();
        }
        return userInfo;
    }

    public UserVo getCurrentUser() {
        return userVo;
    }

    public void login(UserVo userVo) {
        this.userVo = userVo;
    }

    public void logout() {
        userVo = null;
    }

    public boolean isLogin() {
        return userVo != null;
    }

}
