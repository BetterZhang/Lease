package com.anshi.lease.ui.activity;

import com.anshi.lease.R;
import com.anshi.lease.ui.base.LeaseBaseActivity;
import butterknife.OnClick;

/**
 * Created by Android Studio.
 * Author : zhangzhongqiang
 * Email  : betterzhang.dev@gmail.com
 * Time   : 2018/06/07 下午11:07
 * Desc   : 上传身份证图片页面
 */
public class UploadIdCardctivity extends LeaseBaseActivity {

    @Override
    protected int getContentViewId() {
        return R.layout.activity_upload_idcard;
    }

    @Override
    protected void initView() {
        super.initView();
        initToolbar("实名认证", true);
    }

    @OnClick(R.id.tv_upload)
    public void onClick() {
        startAnimActivity(AuthCompleteActivity.class);
    }

}
