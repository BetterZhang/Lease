package com.anshi.lease.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;
import com.anshi.lease.R;
import com.anshi.lease.domain.UserVo;
import com.anshi.lease.service.UserDeviceService;
import com.anshi.lease.ui.base.LeaseBaseActivity;
import com.jme.common.network.DTRequest;
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

    @BindView(R.id.tv_parts_type)
    TextView tv_parts_type;
    @BindView(R.id.tv_parts_code)
    TextView tv_parts_code;
    @BindView(R.id.tv_parts_name)
    TextView tv_parts_name;
    @BindView(R.id.tv_parts_brand)
    TextView tv_parts_brand;
    @BindView(R.id.tv_parts_pn)
    TextView tv_parts_pn;
    @BindView(R.id.tv_parts_parameters)
    TextView tv_parts_parameters;
    @BindView(R.id.tv_mfrsName)
    TextView tv_mfrsName;
    @BindView(R.id.tv_parts_status)
    TextView tv_parts_status;

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

    @Override
    protected void DataReturn(DTRequest request, String msgCode, Object response) {
        super.DataReturn(request, msgCode, response);
        switch (request.getApi().getName()) {
            case "getByPR":
                if (msgCode.equals("200")) {
                    List<UserVo.KeyVehicleInfoBean.BizPartssBean> bizPartssBeanList = (List<UserVo.KeyVehicleInfoBean.BizPartssBean>) response;
                    if (bizPartssBeanList == null || bizPartssBeanList.size() == 0)
                        return;
                    setBizPartssInfo(bizPartssBeanList.get(0));
                }
                break;
            default:
                break;
        }
    }

    private void setBizPartssInfo(UserVo.KeyVehicleInfoBean.BizPartssBean bizPartssBean) {
        tv_parts_type.setText(getPartsTypeStr(bizPartssBean.getPartsType()));
        tv_parts_code.setText(bizPartssBean.getPartsCode());
        tv_parts_name.setText(bizPartssBean.getPartsName());
        tv_parts_brand.setText(bizPartssBean.getPartsBrand());
        tv_parts_pn.setText(bizPartssBean.getPartsPn());
        tv_parts_parameters.setText(bizPartssBean.getPartsParameters());
        tv_mfrsName.setText(bizPartssBean.getMfrsName());
        tv_parts_status.setText(getPartsStatusStr(bizPartssBean.getPartsStatus()));
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
