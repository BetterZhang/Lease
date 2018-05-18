package com.anshi.lease.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import com.anshi.lease.R;
import com.anshi.lease.service.UserAuthService;
import com.anshi.lease.ui.base.LeaseBaseActivity;
import com.jme.common.network.DTRequest;
import java.util.HashMap;
import butterknife.BindView;

/**
 * Created by Android Studio.
 * Author : zhangzhongqiang
 * Email  : betterzhang.dev@gmail.com
 * Time   : 2018/05/15 下午 12:10
 * Desc   : App主页面
 */
public class MainActivity extends LeaseBaseActivity {

    @BindView(R.id.layout_drawer)
    DrawerLayout layout_drawer;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.navigation_view)
    NavigationView navigation_view;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        super.initView();
        initToolbar("小哥乐途", false);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, layout_drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        layout_drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
    }

    private void logout() {
        HashMap<String, String> params = new HashMap<>();
        sendRequest(UserAuthService.getInstance().logout, params, true);
    }

    @Override
    protected void DataReturn(DTRequest request, String msgCode, Object response) {
        super.DataReturn(request, msgCode, response);
        switch (request.getApi().getName()) {
            case "logout":
                if (msgCode.equals("200")) {
                    showShortToast("登出成功");
                    startAnimActivity(LoginActivity.class);
                    this.finish();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void initListener() {
        super.initListener();
        navigation_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_bike:
                        gotoBikeActivity();
                        break;
                    case R.id.item_personal:
                        gotoPersonalActivity();
                        break;
                    case R.id.item_editpwd:
                        gotoEditPwdActivity();
                        break;
                    case R.id.item_logout:
                        logout();
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    private void gotoBikeActivity() {

    }

    private void gotoPersonalActivity() {
        startAnimActivity(PersonalActivity.class);
    }

    private void gotoEditPwdActivity() {
        startAnimActivity(EditPwdActivity.class);
    }

}
