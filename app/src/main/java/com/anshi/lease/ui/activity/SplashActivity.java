package com.anshi.lease.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import com.anshi.lease.R;
import com.anshi.lease.common.UserInfo;
import com.anshi.lease.domain.UserVo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jme.common.ui.config.RxBusConfig;
import com.jme.common.util.SharedPreUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;
import java.lang.reflect.Type;


/**
 * Created by Android Studio.
 * Author : zhangzhongqiang
 * Email  : betterzhang.dev@gmail.com
 * Time   : 2017/08/23 上午 9:22
 * Desc   : App启动页面
 */

public class SplashActivity extends AppCompatActivity {

    private Gson gson = new Gson();
    private UserVo mUserVo;
    private Type type;

    private String defaultVehicleCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 设置无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        final View view = View.inflate(this, R.layout.activity_splash, null);
        setContentView(view);

        RxPermissions permissions = new RxPermissions(this);
        permissions.request(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe(granted -> {
                    initData();

                    //渐变展示启动屛
                    AlphaAnimation animation = new AlphaAnimation(0.5f, 1.0f);
                    animation.setDuration(1500);
                    view.startAnimation(animation);

                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            if (mUserVo != null) {
                                defaultVehicleCode = SharedPreUtils.getString(SplashActivity.this, RxBusConfig.DEFAULT_VEHICLE_CODE);
                                UserInfo.getInstance().login(mUserVo, isDefaultVehicleDelete(mUserVo, defaultVehicleCode));
                                redirectToMain();
                            } else {
                                redirectToLogin();
                            }
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                });

    }

    private void initData() {
        type = new TypeToken<UserVo>() {}.getType();
        String loginUserInfoJson = SharedPreUtils.getString(this, RxBusConfig.LOGIN_USER_INFO);
        if (!android.text.TextUtils.isEmpty(loginUserInfoJson)) {
            mUserVo = gson.fromJson(loginUserInfoJson, type);
        }
    }

    /**
     * 页面跳转
     */
    private void redirectToMain() {
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        SplashActivity.this.finish();
    }

    private void redirectToLogin() {
        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        SplashActivity.this.finish();
    }

    private boolean isDefaultVehicleDelete(UserVo userVo, String defaultVehicleCode) {
        boolean flag = true;
        if (userVo.getKey_vehicle_info() != null && userVo.getKey_vehicle_info().size() > 0) {
            for (UserVo.KeyVehicleInfoBean vehicleInfoBean : userVo.getKey_vehicle_info()) {
                if (vehicleInfoBean.getVehicleCode().equals(defaultVehicleCode)) {
                    flag = false;
                    break;
                }
            }
        }
        return flag;
    }
}
