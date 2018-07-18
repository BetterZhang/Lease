package com.anshi.lease.service;

import com.jme.common.network.DTResponse;
import java.util.HashMap;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
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
    @POST("mobile/v1/auth/getcaptcha")
    Call<DTResponse> getcaptcha();


    /**
     * 登录
     * @return
     */
    @POST("mobile/v1/auth/login")
    Call<DTResponse> login(@Body HashMap<String, String> params);


    /**
     * 发送短信验证码
     * @param params
     * @return
     */
    @POST("mobile/v1/auth/sendsms")
    Call<DTResponse> sendsms(@Body HashMap<String, String> params);


    /**
     * 用户注册
     * @param params
     * @return
     */
    @POST("mobile/v1/auth/register")
    Call<DTResponse> register(@Body HashMap<String, String> params);


    /**
     * 重置密码
     * @param params
     * @return
     */
    @POST("mobile/v1/auth/resetpassword")
    Call<DTResponse> resetpassword(@Body HashMap<String, String> params);


    /**
     * 登出
     * @param params
     * @return
     */
    @POST("mobile/v1/auth/logout")
    Call<DTResponse> logout(@Body HashMap<String, String> params);


    /**
     * 补全用户信息
     * @param params
     * @return
     */
    @POST("mobile/v1/auth/updateuserinfo")
    Call<DTResponse> updateUserInfo(@Body HashMap<String, String> params);


    /**
     * 上传用户头像
     * @param params
     * @return
     */
    @POST("mobile/v1/auth/uplodeusericon")
    Call<DTResponse> uplodeUserIcon(@Body HashMap<String, String> params);


    /**
     * 手机端用户注册时，需要看的所有企业
     * @return
     */
    @GET("mobile/v1/auth/userBindOrg")
    Call<DTResponse> userBindOrg();


    /**
     * 用户实名认证
     * @param params
     * @return
     */
    @POST("mobile/v1/auth/userrealnameauth")
    Call<DTResponse> userRealNameAuth(@Body HashMap<String, String> params);


    /**
     * 用户状态查询
     * @return
     */
    @GET("mobile/v1/auth/userState")
    Call<DTResponse> userState();

}
