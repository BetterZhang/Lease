package com.anshi.lease.ui.activity;

import android.os.Bundle;
import android.view.View;
import com.anshi.lease.R;
import com.anshi.lease.ui.base.LeaseBaseActivity;
import butterknife.OnClick;

/**
 * Created by Android Studio.
 * Author : zhangzhongqiang
 * Email  : betterzhang.dev@gmail.com
 * Time   : 2018/06/10 下午6:54
 * Desc   : 上传头像页面
 */
public class UploadHeadActivity extends LeaseBaseActivity {

    @Override
    protected int getContentViewId() {
        return R.layout.activity_upload_head;
    }

    @Override
    protected void initView() {
        super.initView();
        initToolbar("修改头像", true);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
    }

    @OnClick({R.id.tv_upload, R.id.tv_commit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_upload:
                break;
            case R.id.tv_commit:
                break;
            default:
                break;
        }
    }

}
