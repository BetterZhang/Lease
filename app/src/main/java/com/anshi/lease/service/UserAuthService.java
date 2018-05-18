package com.anshi.lease.service;

import com.anshi.lease.common.Constants;
import com.anshi.lease.domain.BindOrgVo;
import com.anshi.lease.domain.CaptchaVo;
import com.anshi.lease.domain.SmsVo;
import com.anshi.lease.domain.UserVo;
import com.jme.common.network.API;
import com.jme.common.network.DTResponse;
import com.jme.common.network.IService;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
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

    public API getcaptcha = new API<CaptchaVo>("getcaptcha") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            return mApi.getcaptcha();
        }
    };

    public API login = new API<UserVo>("login") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            return mApi.login(params);
        }
    };

    public API sendsms = new API<SmsVo>("sendsms") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            return mApi.sendsms(params);
        }
    };

    public API register = new API<String>("register") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            return mApi.register(params);
        }
    };

    public API resetpassword = new API<String>("resetpassword") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            return mApi.resetpassword(params);
        }
    };

    public API logout = new API<String>("logout") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            return mApi.logout(params);
        }
    };

    public API updateUserInfo = new API<String>("updateUserInfo") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            return mApi.updateUserInfo(params);
        }
    };

    public API uplodeUserIcon = new API<String>("uplodeUserIcon") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            return mApi.uplodeUserIcon(params);
        }
    };

    public API userBindOrg = new API<List<BindOrgVo>>("userBindOrg") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            return mApi.userBindOrg();
        }
    };

    public API userRealNameAuth = new API<String>("userRealNameAuth") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            return mApi.userRealNameAuth(params);
        }
    };

}
