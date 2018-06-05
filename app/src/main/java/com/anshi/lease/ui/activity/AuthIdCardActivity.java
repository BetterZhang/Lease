package com.anshi.lease.ui.activity;

import com.anshi.lease.R;
import com.anshi.lease.ui.base.LeaseBaseActivity;

/**
 * Created by Android Studio.
 * Author : zhangzhongqiang
 * Email  : betterzhang.dev@gmail.com
 * Time   : 2018/06/05 下午11:18
 * Desc   : description
 */
public class AuthIdCardActivity extends LeaseBaseActivity {

    @Override
    protected int getContentViewId() {
        return R.layout.activity_auth_idcard;
    }

    @Override
    protected void initView() {
        super.initView();
        initToolbar("实名认证", true);
    }
}
