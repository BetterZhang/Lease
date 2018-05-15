package com.anshi.lease.ui.base;

import android.content.Intent;
import android.os.Bundle;
import com.jme.common.ui.base.BaseActivity;

/**
 * Created by Android Studio.
 * Author : zhangzhongqiang
 * Email  : betterzhang.dev@gmail.com
 * Time   : 2018/05/15 下午 12:10
 * Desc   : LeaseBaseActivity
 */
public abstract class LeaseBaseActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            Intent intent = getPackageManager().getLaunchIntentForPackage(getApplication().getPackageName());
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            System.exit(0);
            return;
        }
    }
}
