package com.anshi.lease.ui.activity;

import com.anshi.lease.R;
import com.anshi.lease.ui.base.LeaseBaseActivity;
import com.jme.common.ui.base.ToolbarHelper;

/**
 * Created by Android Studio.
 * Author : zhangzhongqiang
 * Email  : betterzhang.dev@gmail.com
 * Time   : 2018/05/16 上午 10:30
 * Desc   : 修改昵称页面
 */
public class EditNickNameActivity extends LeaseBaseActivity {

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
                editNickName();
            }
        });
    }

    private void editNickName() {

    }
}
