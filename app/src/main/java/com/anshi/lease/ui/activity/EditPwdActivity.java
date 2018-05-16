package com.anshi.lease.ui.activity;

import android.view.View;
import com.anshi.lease.R;
import com.anshi.lease.ui.base.LeaseBaseActivity;
import butterknife.OnClick;

/**
 * Created by Android Studio.
 * Author : zhangzhongqiang
 * Email  : betterzhang.dev@gmail.com
 * Time   : 2018/05/16 上午 9:29
 * Desc   : 修改密码页面
 */
public class EditPwdActivity extends LeaseBaseActivity {

    @Override
    protected int getContentViewId() {
        return R.layout.activity_edit_pwd;
    }

    @Override
    protected void initView() {
        super.initView();
        initToolbar("修改密码", true);
    }

    @OnClick(R.id.tv_confirm)
    public void onClick(View view) {
        editPwd();
    }

    private void editPwd() {

    }

}
