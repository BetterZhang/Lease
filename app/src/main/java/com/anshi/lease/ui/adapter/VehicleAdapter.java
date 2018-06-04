package com.anshi.lease.ui.adapter;

import android.support.annotation.Nullable;
import com.anshi.lease.R;
import com.anshi.lease.domain.UserVo;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import java.util.List;

/**
 * Created by Android Studio.
 * Author : zhangzhongqiang
 * Email  : betterzhang.dev@gmail.com
 * Time   : 2018/06/04 下午2:57
 * Desc   : 车辆Adapter
 */
public class VehicleAdapter extends BaseQuickAdapter<UserVo.KeyVehicleInfoBean, BaseViewHolder> {

    private int position;

    public VehicleAdapter(@Nullable List<UserVo.KeyVehicleInfoBean> data) {
        super(R.layout.item_vehicle, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, UserVo.KeyVehicleInfoBean item) {
        position = helper.getLayoutPosition() + 1;
        helper.setText(R.id.tv_vehicle, "我的车辆" + position);
        if (position == 1)
            helper.setText(R.id.tv_default_flag, "默认车辆");
    }
}
