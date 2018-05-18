package com.anshi.lease.ui.activity;

import android.os.Bundle;
import android.widget.EditText;
import com.anshi.lease.R;
import com.anshi.lease.common.UserInfo;
import com.anshi.lease.domain.UserVo;
import com.anshi.lease.service.UserAuthService;
import com.anshi.lease.ui.base.LeaseBaseActivity;
import com.anshi.lease.util.TextUtils;
import com.jme.common.network.DTRequest;
import com.jme.common.ui.base.ToolbarHelper;
import java.util.HashMap;
import butterknife.BindView;

/**
 * Created by Android Studio.
 * Author : zhangzhongqiang
 * Email  : betterzhang.dev@gmail.com
 * Time   : 2018/05/16 上午 10:30
 * Desc   : 修改昵称页面
 */
public class EditNickNameActivity extends LeaseBaseActivity {

    @BindView(R.id.et_nick_name)
    EditText et_nick_name;

    private UserVo mUserVo;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_edit_nick_name;
    }

    @Override
    protected void initView() {
        super.initView();
        initToolbar("修改昵称", true);

        setRightNavigation("确定", 0, new ToolbarHelper.OnSingleMenuItemClickListener() {
            @Override
            public void onSingleMenuItemClick() {
                if (TextUtils.isEmpty(et_nick_name)) {
                    showShortToast("请输入昵称");
                    return;
                }
                editNickName();
            }
        });
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        mUserVo = UserInfo.getInstance().getCurrentUser();
    }

    private void editNickName() {
        if (mUserVo == null)
            return;
        HashMap<String, String> params = new HashMap<>();
        params.put("id", mUserVo.getKey_user_info().getId());
        params.put("loginName", mUserVo.getKey_user_info().getLoginName());
        params.put("nickName", TextUtils.getText(et_nick_name));
        params.put("userName", mUserVo.getKey_user_info().getUserName());
        params.put("userPid", mUserVo.getKey_user_info().getUserPid());
        params.put("updateUser", mUserVo.getKey_user_info().getLoginName());
        sendRequest(UserAuthService.getInstance().updateUserInfo, params, true);
    }

    @Override
    protected void DataReturn(DTRequest request, String msgCode, Object response) {
        super.DataReturn(request, msgCode, response);
        switch (request.getApi().getName()) {
            case "updateUserInfo":
                if (msgCode.equals("200")) {
                    showShortToast("昵称修改成功");
                    mUserVo.getKey_user_info().setNickName(TextUtils.getText(et_nick_name));
                    this.finish();
                }
                break;
            default:
                break;
        }
    }
}
