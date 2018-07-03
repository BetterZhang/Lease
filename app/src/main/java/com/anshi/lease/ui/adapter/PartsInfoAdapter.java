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
 * Time   : 2018/07/03 下午10:05
 * Desc   : 配件Adapter
 */
public class PartsInfoAdapter extends BaseQuickAdapter<UserVo.KeyVehicleInfoBean.BizPartssBean, BaseViewHolder> {

    public PartsInfoAdapter(@Nullable List<UserVo.KeyVehicleInfoBean.BizPartssBean> data) {
        super(R.layout.item_parts_info, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, UserVo.KeyVehicleInfoBean.BizPartssBean item) {
        if (item == null)
            return;

        helper.setText(R.id.tv_parts_type, getPartsTypeStr(item.getPartsType()))
                .setText(R.id.tv_parts_code, item.getPartsCode())
                .setText(R.id.tv_parts_name, item.getPartsName())
                .setText(R.id.tv_parts_brand, item.getPartsBrand())
                .setText(R.id.tv_parts_pn, item.getPartsPn())
                .setText(R.id.tv_parts_parameters, item.getPartsParameters())
                .setText(R.id.tv_mfrsName, item.getMfrsName())
                .setText(R.id.tv_parts_status, getPartsStatusStr(item.getPartsStatus()));
    }

    private String getPartsTypeStr(String type) {
        String partsTypeStr = "";
        switch (type) {
            case "SEATS":
                partsTypeStr = "车座";
                break;
            case "FRAME":
                partsTypeStr = "车架";
                break;
            case "HANDLEBAR":
                partsTypeStr = "车把";
                break;
            case "BELL":
                partsTypeStr = "车铃";
                break;
            case "TYRE":
                partsTypeStr = "轮胎";
                break;
            case "PEDAL":
                partsTypeStr = "脚蹬";
                break;
            case "DASHBOARD":
                partsTypeStr = "仪表盘";
                break;
            default:
                partsTypeStr = "";
                break;
        }
        return partsTypeStr;
    }

    private String getPartsStatusStr(String status) {
        String statusStr = "";
        switch (status) {
            case "INVALID":
                statusStr = "作废";
                break;
            case "FREEZE":
                statusStr = "冻结/维保";
                break;
            case "NORMAL":
                statusStr = "正常";
                break;
            default:
                break;
        }
        return statusStr;
    }

}
