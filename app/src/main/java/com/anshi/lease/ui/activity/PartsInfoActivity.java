package com.anshi.lease.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import com.anshi.lease.R;
import com.anshi.lease.domain.UserVo;
import com.anshi.lease.service.UserDeviceService;
import com.anshi.lease.ui.adapter.PartsInfoAdapter;
import com.anshi.lease.ui.base.LeaseBaseActivity;
import com.jme.common.network.DTRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import butterknife.BindView;

/**
 * Created by Android Studio.
 * Author : zhangzhongqiang
 * Email  : betterzhang.dev@gmail.com
 * Time   : 2018/06/04 下午10:35
 * Desc   : 车辆配件信息页面
 */
public class PartsInfoActivity extends LeaseBaseActivity {

    @BindView(R.id.rcv_parts_info)
    RecyclerView rcv_parts_info;

    private PartsInfoAdapter mAdapter;

    private String id;
    private List<UserVo.KeyVehicleInfoBean.BizPartssBean> mPartssBeans = new ArrayList<>();

    @Override
    protected int getContentViewId() {
        return R.layout.activity_parts_info;
    }

    @Override
    protected void initView() {
        super.initView();
        initToolbar("车辆配件信息", true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcv_parts_info.setLayoutManager(layoutManager);

        mAdapter = new PartsInfoAdapter(null);
        rcv_parts_info.setAdapter(mAdapter);

        rcv_parts_info.setItemAnimator(new DefaultItemAnimator());
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

    @Override
    protected void DataReturn(DTRequest request, String msgCode, Object response) {
        super.DataReturn(request, msgCode, response);
        switch (request.getApi().getName()) {
            case "getByPR":
                if (msgCode.equals("200")) {
                    mPartssBeans = (List<UserVo.KeyVehicleInfoBean.BizPartssBean>) response;
                    if (mPartssBeans == null || mPartssBeans.size() == 0) {
                        showShortToast("该车辆暂无配件信息");
                        return;
                    }
                    mAdapter.setNewData(mPartssBeans);
                }
                break;
            default:
                break;
        }
    }

}
