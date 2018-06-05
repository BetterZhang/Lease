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
    private String defaultVehicleCode;

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

    public String getDefaultVehicleCode() {
        return defaultVehicleCode;
    }

    public void setDefaultVehicleCode(String vehicleCode) {
        this.defaultVehicleCode = vehicleCode;
    }

    public void login(UserVo userVo) {
        this.userVo = userVo;
        if (userVo.getKey_vehicle_info() != null && userVo.getKey_vehicle_info().size() > 0)
            defaultVehicleCode = userVo.getKey_vehicle_info().get(0).getVehicleCode();
    }

    public void logout() {
        userVo = null;
        defaultVehicleCode = null;
    }

    public boolean isLogin() {
        return userVo != null;
    }

}
