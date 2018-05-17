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
 * Time   : 2018/05/17 下午 2:02
 * Desc   : description
 */
public interface UserAuthApi {

    /**
     * 获得图片验证码
     * @return
     */
    @POST("/mobile/v1/auth/getcaptcha")
    Call<DTResponse> getcaptcha();


    /**
     * 登录
     * @return
     */
    @POST("/mobile/v1/auth/login")
    Call<DTResponse> login(@Body HashMap<String, String> params);


    /**
     * 发送短信验证码
     * @param params
     * @return
     */
    @POST("/mobile/v1/auth/sendsms")
    Call<DTResponse> sendsms(@Body HashMap<String, String> params);


    /**
     * 用户注册
     * @param params
     * @return
     */
    @POST("/mobile/v1/auth/register")
    Call<DTResponse> register(@Body HashMap<String, String> params);

}
