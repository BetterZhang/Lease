package com.anshi.lease.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.anshi.lease.R;
import com.anshi.lease.common.Constants;
import com.anshi.lease.common.UserInfo;
import com.anshi.lease.domain.UserVo;
import com.anshi.lease.domain.VehicleInfoVo;
import com.anshi.lease.service.UserAuthService;
import com.anshi.lease.service.UserDeviceService;
import com.anshi.lease.ui.base.LeaseBaseActivity;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.jme.common.network.DTRequest;
import com.jme.common.ui.base.ToolbarHelper;
import com.squareup.picasso.Picasso;

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
public class MainActivity extends LeaseBaseActivity implements View.OnClickListener {

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
    @BindView(R.id.layout_vehicle)
    LinearLayout layout_vehicle;

    ImageView iv_head;
    TextView tv_name;
    TextView tv_status;

    private UserVo mUserVo;
    private List<VehicleInfoVo> mVehicleInfoVoList = new ArrayList<>();
    private VehicleInfoVo mVehicleInfoVo;

    private BaiduMap mBaiduMap;
    public LocationClient mLocationClient;
    public BDLocationListener myListener = new MyLocationListener();
    private LatLng latLng;
    private boolean isFirstLoc = true; // 是否首次定位

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        super.initView();
        initToolbar("小哥乐途", false);

        setRightNavigation("", R.mipmap.ic_track, new ToolbarHelper.OnSingleMenuItemClickListener() {
            @Override
            public void onSingleMenuItemClick() {
                startAnimActivity(TrackActivity.class);
            }
        });

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
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        mUserVo = UserInfo.getInstance().getCurrentUser();
        if (mUserVo != null)
            setUserData();
    }

    private void setUserData() {
        iv_head_back.setVisibility(View.VISIBLE);
        Picasso.with(this)
                .load(Constants.HttpConst.URL_BASE_IMG + mUserVo.getKey_user_info().getUserIcon())
                .placeholder(R.mipmap.ic_head_default)
                .into(iv_head_back);
        Picasso.with(this)
                .load(Constants.HttpConst.URL_BASE_IMG + mUserVo.getKey_user_info().getUserIcon())
                .placeholder(R.mipmap.ic_head_default)
                .into(iv_head);
        tv_name.setText(mUserVo.getKey_user_info().getNickName());
        tv_status.setText(mUserVo.getKey_user_info().getUserRealNameAuthFlag().equals("AUTHORIZED") ? "已实名" : "未实名");
    }

    private void logout() {
        HashMap<String, String> params = new HashMap<>();
        sendRequest(UserAuthService.getInstance().logout, params, true);
    }

    @Override
    protected void DataReturn(DTRequest request, String msgCode, Object response) {
        super.DataReturn(request, msgCode, response);
        switch (request.getApi().getName()) {
            case "logout":
                if (msgCode.equals("200")) {
                    showShortToast("登出成功");
                    startAnimActivity(LoginActivity.class);
                    this.finish();
                }
                break;
            case "getLocByVehiclePK":
                if (msgCode.equals("200")) {
                    mVehicleInfoVoList = (List<VehicleInfoVo>) response;
                    if (mVehicleInfoVoList == null || mVehicleInfoVoList.size() == 0) {
                        showShortToast("未查询到本车辆的定位信息");
                        return;
                    }
                    mVehicleInfoVo = mVehicleInfoVoList.get(0);

                    LatLng ll = new LatLng(mVehicleInfoVo.getLAT(), mVehicleInfoVo.getLON());
                    MapStatus.Builder builder = new MapStatus.Builder();
                    builder.target(ll).zoom(18.0f);
                    mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                } else {
                    showShortToast("未查询到本车辆的定位信息");
                }
                break;
            case "getPowerByVehiclePK":
                if (msgCode.equals("200")) {

                }
                break;
            default:
                break;
        }
    }

    private void gotoAuthIdCardActivity() {
        startAnimActivity(AuthIdCardActivity.class);
    }

    @Override
    protected void initListener() {
        super.initListener();
        iv_head_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });
        navigation_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_bike:
                        if (mUserVo.getKey_user_info().getUserRealNameAuthFlag().equals("AUTHORIZED"))
                            gotoMyVehicleActivity();
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
            }
        });
        tv_status.setOnClickListener(this);
        layout_vehicle.setOnClickListener(this);
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
                } else {
                    gotoAuthIdCardActivity();
                }
                break;
            case R.id.layout_vehicle:
                if (!mUserVo.getKey_user_info().getUserRealNameAuthFlag().equals("AUTHORIZED") ||
                        mUserVo.getKey_vehicle_info().size() == 0) {
                    showShortToast("请先进行实名认证并从企业申领车辆后才能使用本功能");
                } else {
                    getLocByVehiclePK();
                }
                break;
        }
    }

    private void getLocByVehiclePK() {
        if (TextUtils.isEmpty(UserInfo.getInstance().getDefaultVehicleId()))
            return;
        HashMap<String, String> params = new HashMap<>();
        params.put("id", UserInfo.getInstance().getDefaultVehicleId());
        sendRequest(UserDeviceService.getInstance().getLocByVehiclePK, params, true);
    }

    private void getPowerByVehiclePK() {
        if (TextUtils.isEmpty(UserInfo.getInstance().getDefaultVehicleId()))
            return;
        HashMap<String, String> params = new HashMap<>();
        params.put("id", UserInfo.getInstance().getDefaultVehicleId());
        sendRequest(UserDeviceService.getInstance().getPowerByVehiclePK, params, true);
    }

    private void initMap() {
        //获取地图控件引用
        mBaiduMap = mapView.getMap();
        //普通地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mBaiduMap.setMyLocationEnabled(true);

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
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation
        // .getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);
        option.setOpenGps(true); // 打开gps

        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }

    //实现BDLocationListener接口,BDLocationListener为结果监听接口，异步获取定位结果
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            latLng = new LatLng(location.getLatitude(), location.getLongitude());
            // 构造定位数据
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            // 设置定位数据
            mBaiduMap.setMyLocationData(locData);
            // 当不需要定位图层时关闭定位图层
            //mBaiduMap.setMyLocationEnabled(false);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

                if (location.getLocType() == BDLocation.TypeGpsLocation) {
                    // GPS定位结果
                    Toast.makeText(MainActivity.this, location.getAddrStr(), Toast.LENGTH_SHORT).show();
                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                    // 网络定位结果
                    Toast.makeText(MainActivity.this, location.getAddrStr(), Toast.LENGTH_SHORT).show();

                } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {
                    // 离线定位结果
                    Toast.makeText(MainActivity.this, location.getAddrStr(), Toast.LENGTH_SHORT).show();

                } else if (location.getLocType() == BDLocation.TypeServerError) {
                    Toast.makeText(MainActivity.this, "服务器错误，请检查", Toast.LENGTH_SHORT).show();
                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                    Toast.makeText(MainActivity.this, "网络错误，请检查", Toast.LENGTH_SHORT).show();
                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                    Toast.makeText(MainActivity.this, "手机模式错误，请检查是否飞行", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mapView.onPause();
    }
}
