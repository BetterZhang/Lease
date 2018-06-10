package com.anshi.lease.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.anshi.lease.R;
import com.anshi.lease.common.UserInfo;
import com.anshi.lease.domain.UserVo;
import com.anshi.lease.ui.base.LeaseBaseActivity;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Android Studio.
 * Author : zhangzhongqiang
 * Email  : betterzhang.dev@gmail.com
 * Time   : 2018/05/16 上午 9:27
 * Desc   : 个人资料页面
 */
public class PersonalActivity extends LeaseBaseActivity {

    @BindView(R.id.iv_head)
    ImageView iv_head;
    @BindView(R.id.tv_user_type)
    TextView tv_user_type;
    @BindView(R.id.tv_user_name)
    TextView tv_user_name;
    @BindView(R.id.tv_nick_name)
    TextView tv_nick_name;
    @BindView(R.id.tv_idcard)
    TextView tv_idcard;
    @BindView(R.id.tv_phone)
    TextView tv_phone;
    @BindView(R.id.tv_company)
    TextView tv_company;
    @BindView(R.id.tv_user_status)
    TextView tv_user_status;

    private UserVo mUserVo;
    private UserVo.KeyUserInfoBean mKeyUserInfoBean;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_personal;
    }

    @Override
    protected void initView() {
        super.initView();
        initToolbar("个人资料", true);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        mUserVo = UserInfo.getInstance().getCurrentUser();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mUserVo != null)
            setUserData();
    }

    private void setUserData() {
        mKeyUserInfoBean = mUserVo.getKey_user_info();
        if (mKeyUserInfoBean == null)
            return;
        tv_user_name.setText(TextUtils.isEmpty(mKeyUserInfoBean.getLoginName()) ? "" : mKeyUserInfoBean.getLoginName());
        tv_nick_name.setText(TextUtils.isEmpty(mKeyUserInfoBean.getNickName()) ? "" : mKeyUserInfoBean.getNickName());
        tv_idcard.setText(TextUtils.isEmpty(mKeyUserInfoBean.getUserPid()) ? "" : mKeyUserInfoBean.getUserPid());
        tv_phone.setText(TextUtils.isEmpty(mKeyUserInfoBean.getUserMobile()) ? "" : mKeyUserInfoBean.getUserMobile());
        tv_company.setText(TextUtils.isEmpty(mKeyUserInfoBean.getOrgName()) ? "" : mKeyUserInfoBean.getOrgName());
        tv_user_type.setText(TextUtils.isEmpty(mKeyUserInfoBean.getUserType()) ? "" : getUserType(mKeyUserInfoBean.getUserType()));
        tv_user_status.setText(TextUtils.isEmpty(mKeyUserInfoBean.getUserStatus()) ? "" : getUserStatus(mKeyUserInfoBean.getUserStatus()));
    }

    @OnClick({R.id.tv_nick_name, R.id.layout_head})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_nick_name:
                startAnimActivity(EditNickNameActivity.class);
                break;
            case R.id.layout_head:
                startAnimActivity(UploadHeadActivity.class);
                break;
            default:
                break;
        }
    }

    private String getUserType(String userType) {
        String userTypeStr = "";
        switch (userType) {
            case "PLATFORM":
                userTypeStr = "平台";
                break;
            case "ENTERPRISE":
                userTypeStr = "企业";
                break;
            case "INDIVIDUAL":
                userTypeStr = "个人";
                break;
            default:
                break;
        }
        return userTypeStr;
    }

    private String getUserStatus(String userStatus) {
        String userStatusStr = "";
        switch (userStatus) {
            case "NORMAL":
                userStatusStr = "正常";
                break;
            case "FREEZE":
                userStatusStr = "冻结/维保";
                break;
            case "INVALID":
                userStatusStr = "作废";
                break;
            default:
                break;
        }
        return userStatusStr;
    }

}
