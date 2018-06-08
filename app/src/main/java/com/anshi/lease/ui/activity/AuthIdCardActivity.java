package com.anshi.lease.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import com.anshi.lease.R;
import com.anshi.lease.common.UserInfo;
import com.anshi.lease.domain.AuthDataVo;
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

    private AuthDataVo mAuthDataVo;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_auth_idcard;
    }

    @Override
    protected void initView() {
        super.initView();
        initToolbar("实名认证", true);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        mAuthDataVo = new AuthDataVo();
        mAuthDataVo.setId(UserInfo.getInstance().getCurrentUser().getKey_user_info().getId());
        mAuthDataVo.setUpdateUser(UserInfo.getInstance().getCurrentUser().getKey_user_info().getUserName());
        UserInfo.getInstance().setAuthDataVo(mAuthDataVo);
    }

    @OnClick(R.id.tv_next_step)
    public void onClick() {
        if (TextUtils.isEmpty(et_idcard)) {
            showShortToast("请输入身份证号");
            return;
        }
        UserInfo.getInstance().getAuthDataVo().setUserPid(TextUtils.getText(et_idcard));
        startAnimActivity(UploadIdCardctivity.class);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        super.onBackPressed();
    }
}
