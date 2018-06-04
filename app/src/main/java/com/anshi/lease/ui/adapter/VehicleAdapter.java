package com.anshi.lease.ui.adapter;

import android.support.annotation.Nullable;
import com.anshi.lease.R;
import com.anshi.lease.common.UserInfo;
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
    private String defaultVehicleCode;

    public VehicleAdapter(@Nullable List<UserVo.KeyVehicleInfoBean> data) {
        super(R.layout.item_vehicle, data);
        defaultVehicleCode = UserInfo.getInstance().getDefaultVehicleCode();
    }

    @Override
    protected void convert(BaseViewHolder helper, UserVo.KeyVehicleInfoBean item) {
        position = helper.getLayoutPosition() + 1;
        helper.setText(R.id.tv_vehicle, "我的车辆" + position);
        if (defaultVehicleCode != null && defaultVehicleCode.equals(item.getVehicleCode()))
            helper.setText(R.id.tv_default_flag, "默认车辆");
    }
}
