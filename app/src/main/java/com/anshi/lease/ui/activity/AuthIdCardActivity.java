package com.anshi.lease.ui.activity;

import android.widget.EditText;
import com.anshi.lease.R;
import com.anshi.lease.ui.base.LeaseBaseActivity;
import com.anshi.lease.util.TextUtils;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Android Studio.
 * Author : zhangzhongqiang
 * Email  : betterzhang.dev@gmail.com
 * Time   : 2018/06/05 下午11:18
 * Desc   : description
 */
public class AuthIdCardActivity extends LeaseBaseActivity {

    @BindView(R.id.et_idcard)
    EditText et_idcard;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_auth_idcard;
    }

    @Override
    protected void initView() {
        super.initView();
        initToolbar("实名认证", true);
    }

    @OnClick(R.id.tv_next_step)
    public void onClick() {
        if (TextUtils.isEmpty(et_idcard)) {
            showShortToast("请输入身份证号");
            return;
        }
        startAnimActivity(UploadIdCardctivity.class);
    }

}
