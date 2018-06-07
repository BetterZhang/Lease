package com.anshi.lease.ui.activity;

import android.widget.Toast;
import com.anshi.lease.R;
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
import butterknife.BindView;

/**
 * Created by Android Studio.
 * Author : zhangzhongqiang
 * Email  : betterzhang.dev@gmail.com
 * Time   : 2018/06/07 下午9:37
 * Desc   : 行车轨迹页面
 */
public class TrackActivity extends LeaseBaseActivity {

    @BindView(R.id.mapView)
    MapView mapView;

    private BaiduMap mBaiduMap;
    public LocationClient mLocationClient;
    public BDLocationListener myListener = new MyLocationListener();
    private LatLng latLng;
    private boolean isFirstLoc = true; // 是否首次定位

    @Override
    protected int getContentViewId() {
        return R.layout.activity_track;
    }

    @Override
    protected void initView() {
        super.initView();
        initToolbar("行车轨迹", true);

        initMap();
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
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
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
                    Toast.makeText(TrackActivity.this, location.getAddrStr(), Toast.LENGTH_SHORT).show();
                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                    // 网络定位结果
                    Toast.makeText(TrackActivity.this, location.getAddrStr(), Toast.LENGTH_SHORT).show();

                } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {
                    // 离线定位结果
                    Toast.makeText(TrackActivity.this, location.getAddrStr(), Toast.LENGTH_SHORT).show();

                } else if (location.getLocType() == BDLocation.TypeServerError) {
                    Toast.makeText(TrackActivity.this, "服务器错误，请检查", Toast.LENGTH_SHORT).show();
                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                    Toast.makeText(TrackActivity.this, "网络错误，请检查", Toast.LENGTH_SHORT).show();
                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                    Toast.makeText(TrackActivity.this, "手机模式错误，请检查是否飞行", Toast.LENGTH_SHORT).show();
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
