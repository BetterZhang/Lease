package com.anshi.lease.ui.activity;

import android.content.Intent;
import com.anshi.lease.R;
import com.anshi.lease.ui.base.LeaseBaseActivity;
import butterknife.OnClick;

/**
 * Created by Android Studio.
 * Author : zhangzhongqiang
 * Email  : betterzhang.dev@gmail.com
 * Time   : 2018/06/07 下午11:07
 * Desc   : 完成实名认证页面
 */
public class AuthCompleteActivity extends LeaseBaseActivity {

    @Override
    protected int getContentViewId() {
        return R.layout.activity_auth_complete;
    }

    @Override
    protected void initView() {
        super.initView();
        initToolbar("实名认证", true);
    }

    @OnClick(R.id.tv_complete)
    public void onClick() {
        this.finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        super.onBackPressed();
    }

}
