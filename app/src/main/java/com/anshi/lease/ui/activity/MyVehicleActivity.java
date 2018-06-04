package com.anshi.lease.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.anshi.lease.R;
import com.anshi.lease.common.UserInfo;
import com.anshi.lease.domain.UserVo;
import com.anshi.lease.ui.adapter.VehicleAdapter;
import com.anshi.lease.ui.base.LeaseBaseActivity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;

/**
 * Created by Android Studio.
 * Author : zhangzhongqiang
 * Email  : betterzhang.dev@gmail.com
 * Time   : 2018/06/04 下午2:45
 * Desc   : 我的车辆页面
 */
public class MyVehicleActivity extends LeaseBaseActivity {

    @BindView(R.id.rcv_vehicle)
    RecyclerView rcv_vehicle;

    private VehicleAdapter mAdapter;
    private List<UserVo.KeyVehicleInfoBean> mVehicleInfoBeans = new ArrayList<>();

    @Override
    protected int getContentViewId() {
        return R.layout.activity_my_vehicle;
    }

    @Override
    protected void initView() {
        super.initView();
        initToolbar("我的车辆", true);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rcv_vehicle.setLayoutManager(manager);

        mAdapter = new VehicleAdapter(null);
        rcv_vehicle.setAdapter(mAdapter);

        rcv_vehicle.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rcv_vehicle.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        mVehicleInfoBeans = UserInfo.getInstance().getCurrentUser().getKey_vehicle_info();
        if (mVehicleInfoBeans == null)
            return;
        mAdapter.setNewData(mVehicleInfoBeans);
    }

    @Override
    protected void initListener() {
        super.initListener();
        rcv_vehicle.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                showShortToast("点击了item");
            }
        });
    }
}
