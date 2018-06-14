package com.anshi.lease.ui.adapter;

import android.support.annotation.Nullable;
import com.anshi.lease.R;
import com.anshi.lease.domain.UserVo;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jme.common.ui.config.RxBusConfig;
import com.jme.common.util.SharedPreUtils;
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
    private String defaultVehicleCode;

    public VehicleAdapter(@Nullable List<UserVo.KeyVehicleInfoBean> data) {
        super(R.layout.item_vehicle, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, UserVo.KeyVehicleInfoBean item) {
        defaultVehicleCode = SharedPreUtils.getString(mContext, RxBusConfig.DEFAULT_VEHICLE_CODE);
        position = helper.getLayoutPosition() + 1;
        helper.setText(R.id.tv_vehicle, "我的车辆" + position);
        if (defaultVehicleCode != null && defaultVehicleCode.equals(item.getVehicleCode()))
            helper.setText(R.id.tv_default_flag, "默认车辆");
        else
            helper.setText(R.id.tv_default_flag, "");
    }
}
