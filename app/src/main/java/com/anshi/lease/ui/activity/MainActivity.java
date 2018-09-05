package com.anshi.lease.ui.activity;

import android.Manifest;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.anshi.lease.R;
import com.anshi.lease.common.Constants;
import com.anshi.lease.common.UserInfo;
import com.anshi.lease.domain.UserVo;
import com.anshi.lease.domain.VehicleInfoVo;
import com.anshi.lease.domain.VehiclePowerVo;
import com.anshi.lease.service.UserAuthService;
import com.anshi.lease.service.UserDeviceService;
import com.anshi.lease.ui.base.LeaseBaseActivity;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jme.common.network.DTRequest;
import com.jme.common.ui.config.RxBusConfig;
import com.jme.common.util.SharedPreUtils;
import com.squareup.picasso.Picasso;
import com.tbruyelle.rxpermissions2.RxPermissions;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import butterknife.BindView;

/**
 * Created by Android Studio.
 * Author : zhangzhongqiang
 * Email  : betterzhang.dev@gmail.com
 * Time   : 2018/05/15 下午 12:10
 * Desc   : App主页面
 */
public class MainActivity extends LeaseBaseActivity implements View.OnClickListener, OnGetGeoCoderResultListener {

    private static final int accuracyCircleFillColor = 0xAAFFFF88;
    private static final int accuracyCircleStrokeColor = 0xAA00FF00;

    @BindView(R.id.layout_drawer)
    DrawerLayout layout_drawer;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.navigation_view)
    NavigationView navigation_view;
    @BindView(R.id.mapView)
    MapView mapView;
    @BindView(R.id.iv_head)
    ImageView iv_head_back;

    @BindView(R.id.tv_vehicle_code1)
    TextView tv_vehicle_code1;
    @BindView(R.id.tv_vehicle_code2)
    TextView tv_vehicle_code2;
    @BindView(R.id.tv_state1)
    TextView tv_state1;
    @BindView(R.id.tv_state2)
    TextView tv_state2;
    @BindView(R.id.card_down)
    CardView card_down;
    @BindView(R.id.card_up)
    CardView card_up;
    @BindView(R.id.tv_power)
    TextView tv_power;
    @BindView(R.id.tv_distance)
    TextView tv_distance;
    @BindView(R.id.iv_locate)
    ImageView iv_locate;
    @BindView(R.id.tv_location)
    TextView tv_location;

    ImageView iv_head;
    TextView tv_name;
    TextView tv_status;

    private UserVo mUserVo;
    private List<VehicleInfoVo> mVehicleInfoVoList = new ArrayList<>();
    private VehicleInfoVo mVehicleInfoVo;

    private BaiduMap mBaiduMap;
    public LocationClient mLocationClient;
    public BDLocationListener myListener = new MyLocationListenner();
    private boolean isFirstLoc = true; // 是否首次定位

    private int mCurrentDirection = 0;
    private double mCurrentLat = 0.0;
    private double mCurrentLon = 0.0;
    private float mCurrentAccracy;
    private String mAddrStr = "";

    private MyLocationData locData;

    BitmapDescriptor mCurrentMarker;
    private Marker mMarker;
    private InfoWindow mInfoWindow;

    private List<VehiclePowerVo> mVehiclePowerVoList = new ArrayList<>();
    private VehiclePowerVo mVehiclePowerVo;

    private String userIconUrl;
    private Gson gson = new Gson();
    private Type type;

    private String defaultVehicleCode;

    private long exitTime = 0;

    GeoCoder mSearch = null; // 搜索模块，也可去掉地图模块独立使用

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        super.initView();
        type = new TypeToken<UserVo>() {}.getType();
        initToolbar("小哥乐途", false);

        setRightNavigation("", R.mipmap.ic_track, () -> startAnimActivity(TrackActivity.class));

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, layout_drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        layout_drawer.addDrawerListener(toggle);
        toggle.syncState();

        toggle.setDrawerIndicatorEnabled(false);

        View headerLayout = navigation_view.getHeaderView(0);
        iv_head = headerLayout.findViewById(R.id.iv_head);
        tv_name = headerLayout.findViewById(R.id.tv_name);
        tv_status = headerLayout.findViewById(R.id.tv_status);

        initMap();
        RxPermissions permissions = new RxPermissions(this);
        permissions.request(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe(granted -> {
                    if (granted) {
                        mLocationClient.restart();
                    } else {
                        showShortToast("您没有授权定位权限，请在设置中打开授权");
                    }
                });
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
//        getUserState();
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(this);
    }

    private void getUserState() {
        HashMap<String, String> params = new HashMap<>();
        sendRequest(UserAuthService.getInstance().userState, params, true);
    }

    private void setUserData() {
        iv_head_back.setVisibility(View.VISIBLE);
        if (mUserVo.getKey_user_info().getUserIcon().contains(Constants.HttpConst.URL_BASE_IMG))
            userIconUrl = mUserVo.getKey_user_info().getUserIcon();
        else
            userIconUrl = Constants.HttpConst.URL_BASE_IMG + mUserVo.getKey_user_info().getUserIcon();
        Picasso.with(this)
                .load(userIconUrl)
                .placeholder(R.mipmap.ic_head_default)
                .into(iv_head_back);
        Picasso.with(this)
                .load(userIconUrl)
                .placeholder(R.mipmap.ic_head_default)
                .into(iv_head);
        tv_name.setText(mUserVo.getKey_user_info().getNickName());
        tv_status.setText(getUserAuthFlag(mUserVo.getKey_user_info().getUserRealNameAuthFlag()));

        if (!mUserVo.getKey_user_info().getUserRealNameAuthFlag().equals("AUTHORIZED")) {
            setCardTip("请先进行实名认证并从企业申领车辆后才能使用本功能");
        } else if (mUserVo.getKey_vehicle_info().size() == 0) {
            setCardTip("从企业申领车辆后才能使用本功能");
        } else {
            tv_vehicle_code1.setText("车辆" + mUserVo.getKey_vehicle_info().get(0).getVehicleCode());
            tv_vehicle_code2.setText("车辆" + mUserVo.getKey_vehicle_info().get(0).getVehicleCode());
            getLocByVehiclePK();
        }
    }

    private void setCardTip(String tip) {
        card_down.setVisibility(View.GONE);
        card_up.setVisibility(View.VISIBLE);
        tv_vehicle_code2.setText(tip);
        tv_state2.setVisibility(View.GONE);
    }

    private String getUserAuthFlag(String userAuthStr) {
        String userAuthFlag = "";
        switch (userAuthStr) {
            case "AUTHORIZED":
                userAuthFlag = "已实名";
                break;
            case "UNAUTHORIZED":
                userAuthFlag = "未实名 >";
                break;
            case "TOAUTHORIZED":
                userAuthFlag = "待审核";
                break;
            case "REJECTAUTHORIZED":
                userAuthFlag = "已驳回 >";
                break;
            default:
                break;
        }
        return userAuthFlag;
    }

    private void logout() {
        HashMap<String, String> params = new HashMap<>();
        sendRequest(UserAuthService.getInstance().logout, params, false, false, false);
    }

    @Override
    protected void DataReturn(DTRequest request, String msgCode, Object response) {
        super.DataReturn(request, msgCode, response);
        switch (request.getApi().getName()) {
            case "logout":
                UserInfo.getInstance().logout();
                showShortToast("登出成功");
                startAnimActivity(LoginActivity.class);
                this.finish();
                break;
            case "getLocByVehiclePK":
                if (msgCode.equals("200")) {
                    mVehicleInfoVoList = (List<VehicleInfoVo>) response;
                    if (mVehicleInfoVoList == null || mVehicleInfoVoList.size() == 0) {
                        showShortToast("未查询到本车辆的定位信息");
                        return;
                    }
                    mVehicleInfoVo = mVehicleInfoVoList.get(0);

                    mBaiduMap.clear();
                    LatLng ll = new LatLng(mVehicleInfoVo.getLAT(), mVehicleInfoVo.getLON());
                    MapStatus.Builder builder = new MapStatus.Builder();
                    builder.target(ll).zoom(18.0f);
//                    // 修改为自定义marker
//                    mCurrentMarker = BitmapDescriptorFactory
//                            .fromResource(R.drawable.icon_geo);
//                    mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(
//                            MyLocationConfiguration.LocationMode.NORMAL, true, mCurrentMarker,
//                            accuracyCircleFillColor, accuracyCircleStrokeColor));
//                    mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

                    MarkerOptions markerOptions = new MarkerOptions().position(ll).
                            icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_locate_vehicle));
                    mMarker = (Marker) mBaiduMap.addOverlay(markerOptions);
                    mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(ll));

                    mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(ll));

                    getPowerByVehiclePK();
                } else {
                    showShortToast("未查询到本车辆的定位信息");
                }
                break;
            case "getPowerByVehiclePK":
                if (msgCode.equals("200")) {
                    mVehiclePowerVoList = (List<VehiclePowerVo>) response;
                    if (mVehiclePowerVoList == null || mVehiclePowerVoList.size() == 0)
                        return;
                    mVehiclePowerVo = mVehiclePowerVoList.get(0);
                    if (mVehiclePowerVo == null)
                        return;

                    tv_power.setText(mVehiclePowerVo.getRsoc());
                    tv_distance.setText("--");

                    showPopWindow();
                }
                break;
            case "userState":
                if (msgCode.equals("200")) {
                    mUserVo = (UserVo) response;
                }
                if (mUserVo != null) {
                    defaultVehicleCode = SharedPreUtils.getString(mContext, RxBusConfig.DEFAULT_VEHICLE_CODE);
                    UserInfo.getInstance().login(mUserVo, isDefaultVehicleDelete(mUserVo, defaultVehicleCode));
                    String loginUserInfoJson = gson.toJson(mUserVo, type);
                    SharedPreUtils.setString(this, RxBusConfig.HEADER_LOGIN_TOKEN, mUserVo.getKey_login_token());
                    SharedPreUtils.setString(this, RxBusConfig.LOGIN_USER_INFO, loginUserInfoJson);
                } else {
                    mUserVo = UserInfo.getInstance().getCurrentUser();
                }
                if (mUserVo != null)
                    setUserData();
                break;
            default:
                break;
        }
    }

    private boolean isDefaultVehicleDelete(UserVo userVo, String defaultVehicleCode) {
        boolean flag = true;
        if (userVo.getKey_vehicle_info() != null && userVo.getKey_vehicle_info().size() > 0) {
            for (UserVo.KeyVehicleInfoBean vehicleInfoBean : userVo.getKey_vehicle_info()) {
                if (vehicleInfoBean.getVehicleCode().equals(defaultVehicleCode)) {
                    flag = false;
                    break;
                }
            }
        }
        return flag;
    }

    private void showPopWindow() {
        Button button = new Button(getApplicationContext());
        button.setBackgroundResource(R.drawable.bg_popup);
        button.setText("剩余电量" + mVehiclePowerVo.getRsoc() + "%");
        button.setTextColor(Color.BLACK);
        button.setWidth(300);

        InfoWindow.OnInfoWindowClickListener listener = () -> {
//                            LatLng ll = marker.getPosition();
//                            LatLng llNew = new LatLng(ll.latitude + 0.005,
//                                    ll.longitude + 0.005);
//                            marker.setPosition(llNew);
            mBaiduMap.hideInfoWindow();
        };

        LatLng ll = mMarker.getPosition();
        mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(button), ll, -100, listener);
        mBaiduMap.showInfoWindow(mInfoWindow);
    }

    private void gotoAuthIdCardActivity() {
        startAnimActivity(AuthIdCardActivity.class);
    }

    @Override
    protected void initListener() {
        super.initListener();
        iv_head_back.setOnClickListener(view -> toggle());
        mBaiduMap.setOnMarkerClickListener(marker -> {
            if (marker == mMarker) {
                getPowerByVehiclePK();
            }
            return true;
        });
        navigation_view.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.item_bike:
                    if (mUserVo.getKey_user_info().getUserRealNameAuthFlag().equals("AUTHORIZED"))
                        gotoMyVehicleActivity();
                    else if (mUserVo.getKey_user_info().getUserRealNameAuthFlag().equals("TOAUTHORIZED"))
                        showShortToast("您已经提交实名认证，请等待审核通过之后再使用该功能");
                    else
                        gotoAuthTipActivity();
                    break;
                case R.id.item_personal:
                    gotoPersonalActivity();
                    break;
                case R.id.item_editpwd:
                    gotoEditPwdActivity();
                    break;
                case R.id.item_logout:
                    logout();
                    break;
                default:
                    break;
            }
            return false;
        });
        tv_status.setOnClickListener(this);
        tv_state1.setOnClickListener(this);
        tv_state2.setOnClickListener(this);
        iv_locate.setOnClickListener(this);
    }

    private void toggle() {
        int drawerLockMode = layout_drawer.getDrawerLockMode(GravityCompat.START);
        if (layout_drawer.isDrawerVisible(GravityCompat.START)
                && (drawerLockMode != DrawerLayout.LOCK_MODE_LOCKED_OPEN)) {
            layout_drawer.closeDrawer(GravityCompat.START);
        } else if (drawerLockMode != DrawerLayout.LOCK_MODE_LOCKED_CLOSED) {
            layout_drawer.openDrawer(GravityCompat.START);
        }
    }

    private void gotoAuthTipActivity() {
        startAnimActivity(AuthTipActivity.class);
    }

    private void gotoMyVehicleActivity() {
        startAnimActivity(MyVehicleActivity.class);
    }

    private void gotoPersonalActivity() {
        startAnimActivity(PersonalActivity.class);
    }

    private void gotoEditPwdActivity() {
        startAnimActivity(EditPwdActivity.class);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_status:
                if (mUserVo.getKey_user_info().getUserRealNameAuthFlag().equals("AUTHORIZED")) {
                    showShortToast("您已经实名认证，请勿重复提交");
                } else if (mUserVo.getKey_user_info().getUserRealNameAuthFlag().equals("TOAUTHORIZED")) {
                    showShortToast("您已经提交实名认证，请等待审核");
                } else {
                    gotoAuthIdCardActivity();
                }
                break;
            case R.id.tv_state1:
                card_down.setVisibility(View.GONE);
                card_up.setVisibility(View.VISIBLE);
                tv_state2.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_state2:
                card_down.setVisibility(View.VISIBLE);
                card_up.setVisibility(View.GONE);
                tv_state1.setVisibility(View.VISIBLE);
                break;
            case R.id.iv_locate:
                if (!mUserVo.getKey_user_info().getUserRealNameAuthFlag().equals("AUTHORIZED") || mUserVo.getKey_vehicle_info().size() == 0) {
                    LatLng ll = new LatLng(mCurrentLat, mCurrentLon);
                    MapStatus.Builder builder = new MapStatus.Builder();
                    builder.target(ll).zoom(18.0f);
                    mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                } else {
                    tv_vehicle_code1.setText("车辆" + SharedPreUtils.getString(this, RxBusConfig.DEFAULT_VEHICLE_CODE));
                    tv_vehicle_code2.setText("车辆" + SharedPreUtils.getString(this, RxBusConfig.DEFAULT_VEHICLE_CODE));
                    getLocByVehiclePK();
                }
                break;
            default:
                break;
        }
    }

    private void getLocByVehiclePK() {
        if (TextUtils.isEmpty(SharedPreUtils.getString(this, RxBusConfig.DEFAULT_VEHICLE_ID)))
            return;
        HashMap<String, String> params = new HashMap<>();
        params.put("id", SharedPreUtils.getString(this, RxBusConfig.DEFAULT_VEHICLE_ID));
        sendRequest(UserDeviceService.getInstance().getLocByVehiclePK, params, true);
    }

    private void getPowerByVehiclePK() {
        if (TextUtils.isEmpty(SharedPreUtils.getString(this, RxBusConfig.DEFAULT_VEHICLE_ID)))
            return;
        HashMap<String, String> params = new HashMap<>();
        params.put("id", SharedPreUtils.getString(this, RxBusConfig.DEFAULT_VEHICLE_ID));
        sendRequest(UserDeviceService.getInstance().getPowerByVehiclePK, params, true);
    }

    private void initMap() {
        mapView.showZoomControls(false);
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
//        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
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
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        mLocationClient.setLocOption(option);
    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult result) {

    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Log.i("GeoLog", "GeoError");
            return;
        }
        tv_location.setText(result.getAddress());
    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

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

        getUserState();

//        if (mUserVo != null) {
//            tv_name.setText(TextUtils.isEmpty(UserInfo.getInstance().getCurrentUser().getKey_user_info().getNickName()) ?
//                    "" : UserInfo.getInstance().getCurrentUser().getKey_user_info().getNickName());
//
//            if (mUserVo.getKey_user_info().getUserIcon().contains(Constants.HttpConst.URL_BASE_IMG))
//                userIconUrl = mUserVo.getKey_user_info().getUserIcon();
//            else
//                userIconUrl = Constants.HttpConst.URL_BASE_IMG + mUserVo.getKey_user_info().getUserIcon();
//
//            if (!TextUtils.isEmpty(userIconUrl)) {
//                Picasso.with(this)
//                        .load(userIconUrl)
//                        .placeholder(R.mipmap.ic_head_default)
//                        .into(iv_head_back);
//                Picasso.with(this)
//                        .load(userIconUrl)
//                        .placeholder(R.mipmap.ic_head_default)
//                        .into(iv_head);
//            }
//        }
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
        mSearch.destroy();
        mapView = null;
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            if (System.currentTimeMillis() - exitTime > 2000) {
                Snackbar snackbar = Snackbar.make(mapView, getString(R.string.text_exit_app), Snackbar.LENGTH_SHORT);
                snackbar.setAction(getString(R.string.text_cancel), v -> exitTime = 0)
                        .setActionTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
                View snakebarView = snackbar.getView();
                TextView textView = snakebarView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(getResources().getColor(R.color.common_font_content_white));
                snackbar.show();

                exitTime = System.currentTimeMillis();
            } else {
                finish();
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.exit(0);
                    }
                }.start();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
