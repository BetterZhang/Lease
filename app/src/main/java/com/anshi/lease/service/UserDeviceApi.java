package com.anshi.lease.service;

import com.jme.common.network.DTResponse;
import java.util.HashMap;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Android Studio.
 * Author : zhangzhongqiang
 * Email  : betterzhang.dev@gmail.com
 * Time   : 2018/05/17 下午 5:09
 * Desc   : description
 */
public interface UserDeviceApi {

    /**
     * 修改密码
     * @return
     */
    @POST("/mobile/v1/device/modifypassword")
    Call<DTResponse> modifypassword(@Body HashMap<String, String> params);


    /**
     * 根据ID获取车辆电池信息
     * @return
     */
    @POST("/mobile/v1/device/getbypk")
    Call<DTResponse> getByPK();


    /**
     * 根据ID获取车辆信息返回
     * @return
     */
    @POST("/mobile/v1/device/getbypr")
    Call<DTResponse> getByPR();


    /**
     * 根据车辆ID列表获取车辆信息列表
     * @return
     */
    @POST("/mobile/v1/device/getlocbyvehiclepk")
    Call<DTResponse> getLocByVehiclePK();


    /**
     * 根据车辆ID列表获取设备电量信息列表
     * @return
     */
    @POST("/mobile/v1/device/getpowerbyvehiclepk")
    Call<DTResponse> getPowerByVehiclePK();


    /**
     * 根据车辆ID以及时间区间获取车辆轨迹信息
     * @return
     */
    @POST("/mobile/v1/device/gettrackbytime")
    Call<DTResponse> getTrackByTime();


    /**
     * 根据当前登录用户查询当前用户下的所有车辆
     * @return
     */
    @POST("/mobile/v1/device/getVehicleByUserId")
    Call<DTResponse> getVehicleByUserId(@Body HashMap<String, String> params);

}
