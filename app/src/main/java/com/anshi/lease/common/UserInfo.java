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

    public void login(UserVo userVo) {
        this.userVo = userVo;
        if (userVo.getKey_vehicle_info() != null && userVo.getKey_vehicle_info().size() > 0) {
            String defaultVehicleCode = userVo.getKey_vehicle_info().get(0).getVehicleCode();
            String defaultVehicleId = userVo.getKey_vehicle_info().get(0).getId();
            SharedPreUtils.setString(LeaseApplication.getContext(), RxBusConfig.DEFAULT_VEHICLE_CODE, defaultVehicleCode);
            SharedPreUtils.setString(LeaseApplication.getContext(), RxBusConfig.DEFAULT_VEHICLE_ID, defaultVehicleId);
        }
    }

    public void logout() {
        userVo = null;
        SharedPreUtils.setString(LeaseApplication.getContext(), RxBusConfig.LOGIN_USER_INFO, "");
        SharedPreUtils.setString(LeaseApplication.getContext(), RxBusConfig.HEADER_LOGIN_TOKEN, "");
        SharedPreUtils.setString(LeaseApplication.getContext(), RxBusConfig.DEFAULT_VEHICLE_CODE, "");
        SharedPreUtils.setString(LeaseApplication.getContext(), RxBusConfig.DEFAULT_VEHICLE_ID, "");
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
