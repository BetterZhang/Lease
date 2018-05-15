package com.anshi.lease.app;

import android.content.Context;
import com.jme.common.app.BaseApplication;

/**
 * Created by Android Studio.
 * Author : zhangzhongqiang
 * Email  : betterzhang.dev@gmail.com
 * Time   : 2018/05/15 下午 1:03
 * Desc   : description
 */
public class LeaseApplication extends BaseApplication {

    private static LeaseApplication instance;
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        context = this.getApplicationContext();
    }

    public static LeaseApplication getInstance() {
        return instance;
    }

    public static Context getContext() {
        return context;
    }

}
