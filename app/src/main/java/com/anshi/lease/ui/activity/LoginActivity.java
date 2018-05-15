package com.anshi.lease.ui.activity;

import android.view.View;
import com.anshi.lease.R;
import com.anshi.lease.ui.base.LeaseBaseActivity;
import butterknife.OnClick;

/**
 * Created by Android Studio.
 * Author : zhangzhongqiang
 * Email  : betterzhang.dev@gmail.com
 * Time   : 2018/05/15 下午 12:13
 * Desc   : 登录页面
 */
public class LoginActivity extends LeaseBaseActivity {

    @Override
    protected int getContentViewId() {
        return R.layout.activity_login;
    }

    @OnClick({R.id.btn_login, R.id.tv_register, R.id.tv_forget_pwd})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                startAnimActivity(MainActivity.class);
                break;
            case R.id.tv_register:
                startAnimActivity(RegisterActivity.class);
                break;
            case R.id.tv_forget_pwd:
                startAnimActivity(ForgetPwdActivity.class);
                break;
            default:
                break;
        }
    }

}
