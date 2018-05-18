package com.anshi.lease.ui.activity;

import com.anshi.lease.R;
import com.anshi.lease.ui.base.LeaseBaseActivity;

/**
 * Created by Android Studio.
 * Author : zhangzhongqiang
 * Email  : betterzhang.dev@gmail.com
 * Time   : 2018/05/18 下午 5:22
 * Desc   : AuthTipActivity
 */
public class AuthTipActivity extends LeaseBaseActivity {

    @Override
    protected int getContentViewId() {
        return R.layout.activity_auth_tip;
    }

    @Override
    protected void initView() {
        super.initView();
        initToolbar("我的车辆", true);
    }
}
