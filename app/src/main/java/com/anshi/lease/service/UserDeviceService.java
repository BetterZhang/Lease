package com.anshi.lease.service;

import com.anshi.lease.common.Constants;
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
public class UserDeviceService extends IService<UserDeviceApi> {

    public UserDeviceService() {
        super(Constants.HttpConst.URL_BASE, true);
    }

    public static UserDeviceService getInstance() {
        return (UserDeviceService) getInstance(UserDeviceService.class);
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

    public API modifypassword = new API<String>("modifypassword") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            return mApi.modifypassword(params);
        }
    };

    public API getByPK = new API<String>("getByPK") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            return mApi.getByPK();
        }
    };

    public API getByPR = new API<String>("getByPR") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            return mApi.getByPR();
        }
    };

    public API getLocByVehiclePK = new API<String>("getLocByVehiclePK") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            return mApi.getLocByVehiclePK();
        }
    };

    public API getPowerByVehiclePK = new API<String>("getPowerByVehiclePK") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            return mApi.getPowerByVehiclePK();
        }
    };

    public API getTrackByTime = new API<String>("getTrackByTime") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            return mApi.getTrackByTime();
        }
    };

    public API getVehicleByUserId = new API<List<UserVo.KeyVehicleInfoBean>>("getVehicleByUserId") {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            return mApi.getVehicleByUserId(params);
        }
    };

}
