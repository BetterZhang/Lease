package com.anshi.lease.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import com.anshi.lease.R;
import com.anshi.lease.common.UserInfo;
import com.anshi.lease.domain.UserVo;
import com.anshi.lease.service.UserDeviceService;
import com.anshi.lease.ui.base.LeaseBaseActivity;
import com.jme.common.network.DTRequest;
import java.util.HashMap;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Android Studio.
 * Author : zhangzhongqiang
 * Email  : betterzhang.dev@gmail.com
 * Time   : 2018/06/04 下午9:00
 * Desc   : description
 */
public class VehicleDetailActivity extends LeaseBaseActivity {

    @BindView(R.id.tv_vehicle_code)
    TextView tv_vehicle_code;
    @BindView(R.id.tv_vehicle_pn)
    TextView tv_vehicle_pn;
    @BindView(R.id.tv_vehicle_brand)
    TextView tv_vehicle_brand;
    @BindView(R.id.tv_vehicle_madein)
    TextView tv_vehicle_madein;
    @BindView(R.id.tv_mfrsName)
    TextView tv_mfrsName;
    @BindView(R.id.tv_vehicle_status)
    TextView tv_vehicle_status;
    @BindView(R.id.tv_battery_code)
    TextView tv_battery_code;
    @BindView(R.id.tv_battery_name)
    TextView tv_battery_name;
    @BindView(R.id.tv_battery_brand)
    TextView tv_battery_brand;
    @BindView(R.id.tv_battery_pn)
    TextView tv_battery_pn;
    @BindView(R.id.tv_parameters)
    TextView tv_parameters;
    @BindView(R.id.tv_battery_mfrsName)
    TextView tv_battery_mfrsName;
    @BindView(R.id.tv_battery_status)
    TextView tv_battery_status;

    private String id;
    private UserVo.KeyVehicleInfoBean vehicleInfo;
    private UserVo.KeyVehicleInfoBean.BizBatteriesBean batteriesBean;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_vehicle_detail;
    }

    @Override
    protected void initView() {
        super.initView();
        initToolbar("我的车辆", true);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        if (bundle == null)
            return;
        id = bundle.getString("id");
        vehicleInfo = (UserVo.KeyVehicleInfoBean) bundle.getSerializable("vehicleInfo");

        if (vehicleInfo != null)
            setVehicleInfo();

        getByPK();
    }

    private void setVehicleInfo() {
        tv_vehicle_code.setText(vehicleInfo.getVehicleCode());
        tv_vehicle_pn.setText(vehicleInfo.getVehiclePn());
        tv_vehicle_brand.setText(vehicleInfo.getVehicleBrand());
        tv_vehicle_madein.setText(vehicleInfo.getVehicleMadeIn());
        tv_mfrsName.setText(vehicleInfo.getMfrsName());
        tv_vehicle_status.setText(getVehicleStatusStr(vehicleInfo.getVehicleStatus()));
    }

    private String getVehicleStatusStr(String status) {
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

    private void setBatteryInfo() {
        tv_battery_code.setText(batteriesBean.getBatteryCode());
        tv_battery_name.setText(batteriesBean.getBatteryName());
        tv_battery_brand.setText(batteriesBean.getBatteryBrand());
        tv_battery_pn.setText(batteriesBean.getBatteryPn());
        tv_parameters.setText(batteriesBean.getBatteryParameters());
        tv_battery_mfrsName.setText(batteriesBean.getMfrsName());
        tv_battery_status.setText(getVehicleStatusStr(batteriesBean.getBatteryStatus()));
    }

    // 根据车辆ID列表获取车辆信息列表
//    private void getLocByVehiclePK() {
//        HashMap<String, String> params = new HashMap<>();
//        params.put("ids", id);
//        sendRequest(UserDeviceService.getInstance().getLocByVehiclePK, params, true);
//    }

    // 根据车辆ID列表获取车辆信息列表
    private void getByPK() {
        if (TextUtils.isEmpty(id))
            return;
        HashMap<String, String> params = new HashMap<>();
        params.put("id", id);
        params.put("flag", "true");
        sendRequest(UserDeviceService.getInstance().getByPK, params, true);
    }

    @Override
    protected void DataReturn(DTRequest request, String msgCode, Object response) {
        super.DataReturn(request, msgCode, response);
        switch (request.getApi().getName()) {
            case "getByPK":
                if (msgCode.equals("200")) {
                    UserVo.KeyVehicleInfoBean vehicleInfoBean = (UserVo.KeyVehicleInfoBean) response;
                    if (vehicleInfoBean == null)
                        return;
                    batteriesBean = vehicleInfoBean.getBizBatteries().get(0);
                    setBatteryInfo();
                }
                break;
            default:
                break;
        }
    }

    @OnClick({R.id.tv_parts_info, R.id.tv_set_default})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_parts_info:
                gotoPartsInfoActivity();
                break;
            case R.id.tv_set_default:
                setDefaultVehicle();
                break;
            default:
                break;
        }
    }

    private void gotoPartsInfoActivity() {
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        startAnimActivity(PartsInfoActivity.class, bundle);
    }

    private void setDefaultVehicle() {
        UserInfo.getInstance().setDefaultVehicleCode(vehicleInfo.getVehicleCode());
        UserInfo.getInstance().setDefaultVehicleId(vehicleInfo.getId());
        setResult(RESULT_OK);
        this.finish();
    }

}
