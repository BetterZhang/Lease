package com.anshi.lease.ui.activity;

import android.view.View;
import android.widget.EditText;
import com.anshi.lease.R;
import com.anshi.lease.common.UserInfo;
import com.anshi.lease.domain.UserVo;
import com.anshi.lease.service.UserAuthService;
import com.anshi.lease.ui.base.LeaseBaseActivity;
import com.anshi.lease.util.TextUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jme.common.network.DTRequest;
import com.jme.common.ui.config.RxBusConfig;
import com.jme.common.util.SecurityUtils;
import com.jme.common.util.SharedPreUtils;
import java.lang.reflect.Type;
import java.util.HashMap;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Android Studio.
 * Author : zhangzhongqiang
 * Email  : betterzhang.dev@gmail.com
 * Time   : 2018/05/15 下午 12:13
 * Desc   : 登录页面
 */
public class LoginActivity extends LeaseBaseActivity {

    @BindView(R.id.et_account)
    EditText et_account;
    @BindView(R.id.et_password)
    EditText et_password;

    private Gson gson = new Gson();
//    private UserVo mUserVo;
    private Type type;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        super.initView();
        type = new TypeToken<UserVo>() {}.getType();
//        String loginUserInfoJson = SharedPreUtils.getString(this, RxBusConfig.LOGIN_USER_INFO);
//        if (!android.text.TextUtils.isEmpty(loginUserInfoJson)) {
//            mUserVo = gson.fromJson(loginUserInfoJson, type);
//        }
//
//        if (mUserVo != null) {
//            UserInfo.getInstance().login(mUserVo, false);
//            startAnimActivity(MainActivity.class);
//            this.finish();
//        }
    }

    @OnClick({R.id.btn_login, R.id.tv_register, R.id.tv_forget_pwd})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                handleLogin();
                break;
            case R.id.tv_register:
                startAnimActivity(RegisterActivity.class);
                break;
            case R.id.tv_forget_pwd:
                startAnimActivity(ForgetPwdActivity.class);
                break;
            default:
                break;
        }
    }

    private void handleLogin() {
        if (TextUtils.isEmpty(et_account)) {
            showShortToast("请输入账号");
            return;
        }
        if (TextUtils.isEmpty(et_password)) {
            showShortToast("请输入密码");
            return;
        }
        userLogin();
    }

    private void userLogin() {
        long currentTime = System.currentTimeMillis();
        String loginAuthStr = SecurityUtils.getMD5(TextUtils.getText(et_account) +
                SecurityUtils.getMD5(TextUtils.getText(et_password), true) + currentTime, true);
        HashMap<String, String> params = new HashMap<>();
        params.put("loginName", TextUtils.getText(et_account));
        params.put("loginAuthStr", loginAuthStr);
        params.put("loginTime", "" + currentTime);
        params.put("needCaptcha", "false");
        sendRequest(UserAuthService.getInstance().login, params, true);
    }

    @Override
    protected void DataReturn(DTRequest request, String msgCode, Object response) {
        super.DataReturn(request, msgCode, response);
        switch (request.getApi().getName()) {
            case "login":
                if (msgCode.equals("200")) {
                    UserVo userVo = (UserVo) response;
                    if (userVo == null)
                        return;
                    UserInfo.getInstance().login(userVo, true);
                    showShortToast("登录成功");

                    String loginUserInfoJson = gson.toJson(userVo, type);
                    SharedPreUtils.setString(this, RxBusConfig.HEADER_LOGIN_TOKEN, userVo.getKey_login_token());
                    SharedPreUtils.setString(this, RxBusConfig.LOGIN_USER_INFO, loginUserInfoJson);
                    startAnimActivity(MainActivity.class);
                    this.finish();
                }
                break;
            default:
                break;
        }
    }
}
