package com.anshi.lease.service;

import com.anshi.lease.common.Constants;
import com.jme.common.network.API;
import com.jme.common.network.DTResponse;
import com.jme.common.network.IService;
import java.io.IOException;
import java.util.HashMap;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;

/**
 * Created by Android Studio.
 * Author : zhangzhongqiang
 * Email  : betterzhang.dev@gmail.com
 * Time   : 2018/05/17 下午 2:04
 * Desc   : description
 */
public class UserAuthService extends IService<UserAuthApi> {

    public UserAuthService() {
        super(Constants.HttpConst.URL_BASE, true);
    }

    public static UserAuthService getInstance() {
        return (UserAuthService) getInstance(UserAuthService.class);
    }

    @Override
    protected Interceptor addHeader() {
        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                return chain.proceed(request);
            }
        };
        return interceptor;
    }

    public API getcaptcha = new API<String>("getcaptcha") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            return mApi.getcaptcha();
        }
    };

}