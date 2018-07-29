package com.anshi.lease.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.anshi.lease.R;
import com.anshi.lease.common.UserInfo;
import com.anshi.lease.domain.TrackVo;
import com.anshi.lease.domain.UserVo;
import com.anshi.lease.service.UserDeviceService;
import com.anshi.lease.ui.base.LeaseBaseActivity;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.jme.common.network.DTRequest;
import com.jme.common.ui.config.RxBusConfig;
import com.jme.common.util.BigDecimalUtil;
import com.jme.common.util.SharedPreUtils;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Android Studio.
 * Author : zhangzhongqiang
 * Email  : betterzhang.dev@gmail.com
 * Time   : 2018/06/07 下午9:37
 * Desc   : 行车轨迹页面
 */
public class TrackActivity extends LeaseBaseActivity implements OnDateSetListener {

    @BindView(R.id.mapView)
    MapView mapView;
    @BindView(R.id.tv_start_time)
    TextView tv_start_time;
    @BindView(R.id.tv_end_time)
    TextView tv_end_time;
    @BindView(R.id.tv_confirm)
    TextView tv_confirm;

    private BaiduMap mBaiduMap;
    public LocationClient mLocationClient;
    public BDLocationListener myListener = new MyLocationListener();
    private LatLng latLng;
    private boolean isFirstLoc = true; // 是否首次定位

    private int mCurrentDirection = 0;
    private double mCurrentLat = 0.0;
    private double mCurrentLon = 0.0;
    private float mCurrentAccracy;

    private MyLocationData locData;

    private TimePickerDialog mDialogAll;
    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private String startTime;
    private String endTime;

    private List<TrackVo> mTrackVoList = new ArrayList<>();
    List<LatLng> polylines = new ArrayList<>();
    private Polyline mPolyline;

    private UserVo mUserVo;

    private Marker mMarker;
    private InfoWindow mInfoWindow;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_track;
    }

    @Override
    protected void initView() {
        super.initView();
        initToolbar("行车轨迹", true);

        long tenYears = 10L * 365 * 1000 * 60 * 60 * 24L;

        mDialogAll = new TimePickerDialog.Builder()
                .setCallBack(this)
                .setCancelStringId("取消")
                .setSureStringId("确定")
                .setTitleStringId("选择时间")
                .setYearText("年")
                .setMonthText("月")
                .setDayText("日")
                .setHourText("时")
                .setMinuteText("分")
                .setCyclic(false)
                .setMinMillseconds(System.currentTimeMillis() - tenYears)
                .setMaxMillseconds(System.currentTimeMillis() + tenYears)
                .setCurrentMillseconds(System.currentTimeMillis())
                .setThemeColor(getResources().getColor(R.color.colorPrimary))
                .setType(Type.ALL)
                .setWheelItemTextNormalColor(getResources().getColor(R.color.timetimepicker_default_text_color))
                .setWheelItemTextSelectorColor(getResources().getColor(R.color.timepicker_toolbar_bg))
                .setWheelItemTextSize(12)
                .build();

        initMap();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        mUserVo = UserInfo.getInstance().getCurrentUser();
    }

    private void getTrackByTime() {
        if (TextUtils.isEmpty(SharedPreUtils.getString(this, RxBusConfig.DEFAULT_VEHICLE_ID))) {
            showShortToast("请从企业申领车辆后再使用本功能");
            return;
        }
        if (TextUtils.isEmpty(startTime) || TextUtils.isEmpty(endTime)) {
            showShortToast("时间选择有误");
            return;
        }
        HashMap<String, String> params = new HashMap<>();
        params.put("id", SharedPreUtils.getString(this, RxBusConfig.DEFAULT_VEHICLE_ID));
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        sendRequest(UserDeviceService.getInstance().getTrackByTime, params, true);
    }

    @Override
    protected void DataReturn(DTRequest request, String msgCode, Object response) {
        super.DataReturn(request, msgCode, response);
        switch (request.getApi().getName()) {
            case "getTrackByTime":
                if (msgCode.equals("200")) {
                    mTrackVoList.clear();
                    polylines.clear();
                    mBaiduMap.clear();
                    mTrackVoList = (List<TrackVo>) response;
                    if (mTrackVoList == null || mTrackVoList.size() == 0) {
                        showShortToast("该时间段内暂无行车轨迹记录");
                        return;
                    }
                    for (int i = 0; i < mTrackVoList.size(); i++) {
                        polylines.add(new LatLng(mTrackVoList.get(i).getLAT(), mTrackVoList.get(i).getLON()));
                    }

                    PolylineOptions polylineOptions = new PolylineOptions().points(polylines).width(5).color(Color.RED);
                    mPolyline = (Polyline) mBaiduMap.addOverlay(polylineOptions);

                    LatLng ll = new LatLng(mTrackVoList.get(0).getLAT(), mTrackVoList.get(0).getLON());
                    MapStatus.Builder builder = new MapStatus.Builder();
                    builder.target(ll).zoom(18.0f);
                    MarkerOptions markerOptions = new MarkerOptions().position(ll).
                            icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_locate_vehicle));
                    mMarker = (Marker) mBaiduMap.addOverlay(markerOptions);
                    mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(ll));

                    getRunDistanceAndTime();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void initListener() {
        super.initListener();
        mBaiduMap.setOnMarkerClickListener(marker -> {
            if (marker == mMarker) {
                getRunDistanceAndTime();
            }
            return true;
        });
    }

    private void getRunDistanceAndTime() {
        BigDecimal distance = new BigDecimal(0);
        long time = 0;
        for (int i = 0; i < mTrackVoList.size(); i++) {
            distance = distance.add(new BigDecimal(mTrackVoList.get(i).getRunDistance()));
            time = time + mTrackVoList.get(i).getStayTime();
        }

        int minutes = (int) (time / (1000 * 60));
        String distanceStr = BigDecimalUtil.format2Number(distance.divide(new BigDecimal(1000)).toPlainString(), 3);
        Button button = new Button(getApplicationContext());
        button.setBackgroundResource(R.drawable.bg_popup);
        button.setText("行驶距离：" + distanceStr + "千米\n行驶时长：" + minutes + "分钟");
        button.setTextColor(Color.BLACK);
        button.setWidth(600);
        button.setHeight(250);

        InfoWindow.OnInfoWindowClickListener listener = () -> mBaiduMap.hideInfoWindow();

        LatLng ll = mMarker.getPosition();
        mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(button), ll, -100, listener);
        mBaiduMap.showInfoWindow(mInfoWindow);
    }

    @OnClick({R.id.tv_start_time, R.id.tv_end_time, R.id.tv_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_start_time:
                mDialogAll.show(getSupportFragmentManager(), "StartTime");
                break;
            case R.id.tv_end_time:
                mDialogAll.show(getSupportFragmentManager(), "EndTime");
                break;
            case R.id.tv_confirm:
                if (!mUserVo.getKey_user_info().getUserRealNameAuthFlag().equals("AUTHORIZED")) {
                    showShortToast("请先进行实名认证并从企业申领车辆后才能使用本功能");
                    return;
                } else if (mUserVo.getKey_vehicle_info().size() == 0) {
                    showShortToast("从企业申领车辆后才能使用本功能");
                    return;
                }
                getTrackByTime();
                break;
        }
    }

    private void initMap() {
        //获取地图控件引用
        mBaiduMap = mapView.getMap();

        //默认显示普通地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        //开启交通图
        //mBaiduMap.setTrafficEnabled(true);
        //开启热力图
        //mBaiduMap.setBaiduHeatMapEnabled(true);
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
        //配置定位SDK参数
        initLocation();
        mLocationClient.registerLocationListener(myListener);    //注册监听函数
        //开启定位
        mLocationClient.start();
        //图片点击事件，回到定位点
        mLocationClient.requestLocation();
    }

    //配置定位SDK参数
    private void initLocation() {
//        LocationClientOption option = new LocationClientOption();
//        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
//        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
//        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
//        int span = 1000;
//        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
//        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
//        option.setOpenGps(true);//可选，默认false,设置是否使用gps
//        option.setLocationNotify(true);//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
//        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation
//        // .getLocationDescribe里得到，结果类似于“在北京天安门附近”
//        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
//        option.setIgnoreKillProcess(false);
//        option.setOpenGps(true); // 打开gps
//
//        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
//        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
//        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
//        mLocationClient.setLocOption(option);

        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocationClient.setLocOption(option);
    }

    @Override
    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
        String text = getDateToString(millseconds);
        switch (timePickerView.getTag()) {
            case "StartTime":
                startTime = text;
                tv_start_time.setText("起始时间  "+ text.substring(0, text.length() - 3));
                break;
            case "EndTime":
                endTime = text;
                tv_end_time.setText("终止时间  "+ text.substring(0, text.length() - 3));
                tv_confirm.setVisibility(View.VISIBLE);
                break;
        }
    }

    //实现BDLocationListener接口,BDLocationListener为结果监听接口，异步获取定位结果
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mapView == null) {
                return;
            }
            mCurrentLat = location.getLatitude();
            mCurrentLon = location.getLongitude();
            mCurrentAccracy = location.getRadius();
            locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(mCurrentDirection).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {

        }
    }

    @Override
    protected void onResume() {
        // 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mapView.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        // 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        // 退出时销毁定位
        mLocationClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        // 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mapView.onDestroy();
        mapView = null;
        super.onDestroy();
    }

    public String getDateToString(long time) {
        Date d = new Date(time);
        return sf.format(d);
    }

}
