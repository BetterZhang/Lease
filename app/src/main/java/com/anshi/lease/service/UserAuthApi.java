package com.anshi.lease.service;

import com.jme.common.network.DTResponse;

import retrofit2.Call;
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

}
