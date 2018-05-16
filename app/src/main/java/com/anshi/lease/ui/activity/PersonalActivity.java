package com.anshi.lease.ui.activity;

import com.anshi.lease.R;
import com.anshi.lease.ui.base.LeaseBaseActivity;

/**
 * Created by Android Studio.
 * Author : zhangzhongqiang
 * Email  : betterzhang.dev@gmail.com
 * Time   : 2018/05/16 上午 9:27
 * Desc   : 个人资料页面
 */
public class PersonalActivity extends LeaseBaseActivity {

    @Override
    protected int getContentViewId() {
        return R.layout.activity_personal;
    }

    @Override
    protected void initView() {
        super.initView();
        initToolbar("个人资料", true);
    }
}
