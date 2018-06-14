package com.anshi.lease.common;

import com.anshi.lease.app.LeaseApplication;
import com.anshi.lease.domain.AuthDataVo;
import com.anshi.lease.domain.UserVo;
import com.jme.common.ui.config.RxBusConfig;
import com.jme.common.util.SharedPreUtils;

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
    private String defaultVehicleId;
    private AuthDataVo authDataVo;

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

    public String getDefaultVehicleId() {
        return defaultVehicleId;
    }

    public void setDefaultVehicleCode(String vehicleCode) {
        this.defaultVehicleCode = vehicleCode;
    }

    public void setDefaultVehicleId(String vehicleId) {
        this.defaultVehicleId = vehicleId;
    }

    public void login(UserVo userVo) {
        this.userVo = userVo;
        if (userVo.getKey_vehicle_info() != null && userVo.getKey_vehicle_info().size() > 0) {
            defaultVehicleCode = userVo.getKey_vehicle_info().get(0).getVehicleCode();
            defaultVehicleId = userVo.getKey_vehicle_info().get(0).getId();
        }
    }

    public void logout() {
        userVo = null;
        defaultVehicleCode = null;
        defaultVehicleId = null;
        SharedPreUtils.setString(LeaseApplication.getContext(), RxBusConfig.LOGIN_USER_INFO, "");
        SharedPreUtils.setString(LeaseApplication.getContext(), RxBusConfig.HEADER_LOGIN_TOKEN, "");
    }

    public boolean isLogin() {
        return userVo != null;
    }

    public AuthDataVo getAuthDataVo() {
        return authDataVo;
    }

    public void setAuthDataVo(AuthDataVo authDataVo) {
        this.authDataVo = authDataVo;
    }

    public void resetAuthDataVo() {
        this.authDataVo = null;
    }
}
