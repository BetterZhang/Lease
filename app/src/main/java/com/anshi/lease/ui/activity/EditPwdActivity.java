package com.anshi.lease.ui.activity;

import android.view.View;
import android.widget.EditText;
import com.anshi.lease.R;
import com.anshi.lease.common.UserInfo;
import com.anshi.lease.service.UserDeviceService;
import com.anshi.lease.ui.base.LeaseBaseActivity;
import com.anshi.lease.util.TextUtils;
import com.jme.common.network.DTRequest;
import com.jme.common.util.SecurityUtils;
import com.jme.common.util.StringUtils;
import java.util.HashMap;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Android Studio.
 * Author : zhangzhongqiang
 * Email  : betterzhang.dev@gmail.com
 * Time   : 2018/05/16 上午 9:29
 * Desc   : 修改密码页面
 */
public class EditPwdActivity extends LeaseBaseActivity {

    @BindView(R.id.et_old_pwd)
    EditText et_old_pwd;
    @BindView(R.id.et_new_pwd)
    EditText et_new_pwd;
    @BindView(R.id.et_new_pwd_confirm)
    EditText et_new_pwd_confirm;

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
        handleEditPwd();
    }

    private void handleEditPwd() {
        if (TextUtils.isEmpty(et_old_pwd)) {
            showShortToast("请输入原始密码");
            return;
        }
        if (!StringUtils.isPasswordRight(TextUtils.getText(et_new_pwd))) {
            showShortToast("请输入6-10位字母数字混合的密码");
            return;
        }
        if (!TextUtils.getText(et_new_pwd).equals(TextUtils.getText(et_new_pwd_confirm))) {
            showShortToast("新密码两次输入不一致");
            return;
        }
        modifyPassword();
    }

    private void modifyPassword() {
        String loginName = UserInfo.getInstance().getCurrentUser().getKey_user_info().getLoginName();
        long currentTime = System.currentTimeMillis();
        String loginAuthStr = SecurityUtils.getMD5(loginName +
                SecurityUtils.getMD5(TextUtils.getText(et_old_pwd), true) + currentTime, true);
        HashMap<String, String> params = new HashMap<>();
        params.put("oldAuthStr", loginAuthStr);
        params.put("authTime", "" + currentTime);
        params.put("newPassword", SecurityUtils.getMD5(TextUtils.getText(et_new_pwd), true));
        sendRequest(UserDeviceService.getInstance().modifypassword, params, true);
    }

    @Override
    protected void DataReturn(DTRequest request, String msgCode, Object response) {
        super.DataReturn(request, msgCode, response);
        switch (request.getApi().getName()) {
            case "modifypassword":
                if (msgCode.equals("200")) {
                    showShortToast("修改密码成功");
                    this.finish();
                }
                break;
            default:
                break;
        }
    }
}
