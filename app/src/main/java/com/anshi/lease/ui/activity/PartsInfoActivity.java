package com.anshi.lease.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;

import com.anshi.lease.R;
import com.anshi.lease.service.UserDeviceService;
import com.anshi.lease.ui.base.LeaseBaseActivity;

import java.util.HashMap;

/**
 * Created by Android Studio.
 * Author : zhangzhongqiang
 * Email  : betterzhang.dev@gmail.com
 * Time   : 2018/06/04 下午10:35
 * Desc   : 车辆配件信息页面
 */
public class PartsInfoActivity extends LeaseBaseActivity {

    private String id;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_parts_info;
    }

    @Override
    protected void initView() {
        super.initView();
        initToolbar("车辆配件信息", true);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        if (bundle == null)
            return;
        id = bundle.getString("id");

        getByPR();
    }

    private void getByPR() {
        if (TextUtils.isEmpty(id))
            return;
        HashMap<String, String> params = new HashMap<>();
        params.put("id", id);
        sendRequest(UserDeviceService.getInstance().getByPR, params, true);
    }

}
