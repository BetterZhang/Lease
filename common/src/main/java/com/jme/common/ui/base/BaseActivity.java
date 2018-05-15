package com.jme.common.ui.base;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.StyleRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import com.jme.common.network.API;
import com.jme.common.network.AsynCommon;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.network.OnResultListener;
import com.jme.common.ui.view.LoadingDialog;
import com.jme.common.util.AppManager;
import java.util.HashMap;
import butterknife.ButterKnife;

/**
 * Activity基类
 * Created by zhangzhongqiang on 2016/3/14.
 */
public abstract class BaseActivity extends AppCompatActivity implements OnResultListener {
    //Toast对象
    protected Toast mToast;
    //Context对象
    protected Context mContext;
    //LoadingDialog对象
    protected LoadingDialog mLoadingDialog;
    //Toolbar Helper 对象
    protected ToolbarHelper mToolbarHelper;

    protected boolean mUseBinding = false;

    protected ViewDataBinding mBindingUtil;

    private AlertDialog mPhoneDialog;
    private AlertDialog.Builder mPhoneBuilder;  //统一帐户登录框

    private static final int MSG_ERROR_MSG_REPET = 1000;
    private String mPrevErrorCode = "";
    private Handler mErrorHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_ERROR_MSG_REPET:
                    mErrorHandler.removeMessages(MSG_ERROR_MSG_REPET);
                    mPrevErrorCode = "";
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        AppManager.getAppManager().addActivity(this);

//        StatusBarUtil.FlymeSetStatusBarLightMode(getWindow(), true);
//        StatusBarUtil.MIUISetStatusBarLightMode(getWindow(), true);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Window window = getWindow();
//            // Translucent status bar
//            window.setFlags(
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        }

        if (getContentViewId() != 0 && !mUseBinding) {
            setContentView(getContentViewId());
        } else if (mUseBinding) {
            mBindingUtil = DataBindingUtil.setContentView(this, getContentViewId());
        }

        /*
        //设置 paddingTop
        ViewGroup rootView = (ViewGroup) getWindow().getDecorView().findViewById(android.R.id.content);
        rootView.setPadding(0, ScreenUtil.getStatusBarHeight(this), 0, 0);
//        rootView.setFitsSystemWindows(true);
        rootView.setClipToPadding(true);

        //根布局添加占位状态栏
        ViewGroup decorView = (ViewGroup) getWindow().getDecorView();
        View statusBarView = new View(this);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ScreenUtil.getStatusBarHeight(this));
        statusBarView.setBackgroundResource(R.drawable.bg_toolbar);
        decorView.addView(statusBarView, lp);
        */

        ButterKnife.bind(this);
        initView();
        initData(savedInstanceState);
        initListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().finishActivity(this);
    }

    //-----------Toolbar Setting-----------
    public void initToolbar(View view, boolean hasBack) {
        mToolbarHelper = new ToolbarHelper(this);

        mToolbarHelper.initToolbar(view);
        setBackNavigation(hasBack);
    }

    public void initToolbar(String title, boolean hasBack) {
        mToolbarHelper = new ToolbarHelper(this);

        mToolbarHelper.initToolbar(title);
        setBackNavigation(hasBack);
    }

    public void initToolbar(String title, boolean hasBack, @ColorInt int resId) {
        mToolbarHelper = new ToolbarHelper(this);

        mToolbarHelper.initToolbar(title, resId);
        setBackNavigation(hasBack);
    }

    public void initToolbar(String title, boolean hasBack, @ColorInt int resId, @ColorInt int layoutColorId) {
        mToolbarHelper = new ToolbarHelper(this);

        mToolbarHelper.initToolbar(title, resId, layoutColorId);
        setBackNavigation(hasBack);
    }

    public void initToolbar(String[] titils, boolean hasBack) {
        mToolbarHelper = new ToolbarHelper(this);
        setBackNavigation(hasBack);
        mToolbarHelper.initToolbar(titils);
    }

    public void initToolbar(int resId, boolean hasBack) {
        initToolbar(getString(resId), hasBack);
    }

    public void initToolbar(int resId, boolean hasBack, @ColorInt int colorResID) {
        initToolbar(getString(resId), hasBack, colorResID);
    }

    public void setTitle(int resId) {
        if (resId != 0) {
            setTitle(getString(resId));
        }
    }

    public void setTitle(String title) {
        if (mToolbarHelper != null)
            mToolbarHelper.setTitle(title);
    }

    public void setBackNavigation(boolean hasBack) {
        if (mToolbarHelper != null)
            mToolbarHelper.setBackNavigation(hasBack, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
    }

    public void setBackNavigation(boolean hasBack, @DrawableRes int drawableResId) {
        if (mToolbarHelper != null)
            mToolbarHelper.setBackNavigation(hasBack, drawableResId, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
    }

    public void setBackNavigation(@DrawableRes int drawableResId, View.OnClickListener listener) {
        if (mToolbarHelper != null)
            mToolbarHelper.setBackNavigationIcon(drawableResId, listener);
    }

    public void setRightNavigation(String str, @DrawableRes int resId, @StyleRes int _resId, ToolbarHelper.OnSingleMenuItemClickListener listener) {
        if (mToolbarHelper != null)
            mToolbarHelper.setSingleMenu(str, resId, _resId, listener);
    }

    public void setRightNavigation(String str, @DrawableRes int resId, ToolbarHelper.OnSingleMenuItemClickListener listener) {
        if (mToolbarHelper != null)
            mToolbarHelper.setSingleMenu(str, resId, listener);
    }

    //-----------Toolbar Setting End-----------

    /**
     * 短暂显示Toast提示(来自res)
     *
     * @param resId
     */
    protected void showShortToast(final int resId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mToast == null) {
                    mToast = Toast.makeText(mContext, getResources().getString(resId), Toast.LENGTH_SHORT);
                } else {
                    mToast.setText(resId);
                }
                mToast.show();
            }
        });
    }

    /**
     * 短暂显示Toast提示(来自String)
     *
     * @param text
     */
    protected void showShortToast(final String text) {
        if (!TextUtils.isEmpty(text)) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mToast == null) {
                        mToast = Toast.makeText(mContext, text, Toast.LENGTH_SHORT);
                    } else {
                        mToast.setText(text);
                    }
                    mToast.show();
                }
            });
        }
    }

    /**
     * 长时间显示Toast提示(来自res)
     *
     * @param resId
     */
    protected void showLongToast(final int resId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mToast == null) {
                    mToast = Toast.makeText(mContext, getResources().getString(resId), Toast.LENGTH_LONG);
                } else {
                    mToast.setText(resId);
                }
                mToast.show();
            }
        });
    }

    /**
     * 长时间显示Toast提示(来自String)
     *
     * @param text
     */
    protected void showLongToast(final String text) {
        if (!TextUtils.isEmpty(text)) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mToast == null) {
                        mToast = Toast.makeText(mContext, text, Toast.LENGTH_LONG);
                    } else {
                        mToast.setText(text);
                    }
                    mToast.show();
                }
            });
        }
    }

    /**
     * Activity跳转（无参）
     *
     * @param cls
     */
    protected void startAnimActivity(Class<?> cls) {
        this.startAnimActivity(cls, null);
    }

    /**
     * Activity跳转（有参）
     *
     * @param cls
     * @param bundle
     */
    protected void startAnimActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(mContext, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * Activity跳转 ForResult（无参）
     *
     * @param cls
     * @param requestCode
     */
    protected void startAnimActivityForResult(Class<?> cls, int requestCode) {
        this.startAnimActivityForResult(cls, null, requestCode);
    }

    /**
     * Activity跳转 ForResult（有参）
     *
     * @param cls
     * @param bundle
     * @param requestCode
     */
    protected void startAnimActivityForResult(Class<?> cls, Bundle bundle, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(mContext, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    /**
     * 显示loading框
     *
     * @param text
     */
    protected void showLoadingDialog(String text) {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(mContext);
        }
        mLoadingDialog.setLoadingText(text);
        if (!isFinishing() && !mLoadingDialog.isShowing()) {
            mLoadingDialog.show();
        }
    }

    /**
     * 取消loading框
     */
    protected void dismissLoadingDialog() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }

    //-----------网络访问请求-----------
    protected AsynCommon sendRequest(API api, HashMap<String, String> params, boolean showprogressDialog, boolean showErrorMsgOneTime, boolean showErrorMsg) {
        if (showprogressDialog) {
            showLoadingDialog("");
        }

        AsynCommon task = AsynCommon.SendRequest(api, params, showErrorMsgOneTime, showErrorMsg, this, mContext);
        return task;
    }

    protected AsynCommon sendRequest(API api, HashMap<String, String> params, boolean showprogressDialog, boolean showErrorMsgOneTime) {
        return sendRequest(api, params, showprogressDialog, showErrorMsgOneTime, true);
    }

    protected AsynCommon sendRequest(API api, HashMap<String, String> params, boolean showprogressDialog) {
        return sendRequest(api, params, showprogressDialog, false, true);
    }

    @Override
    public void OnResult(DTRequest request, Head head, Object response) {
        dismissLoadingDialog();
        handleErrorInfo(request, head);
        DataReturn(request, head.getCode(), response);
        DataReturn(request, head, response);
    }

    protected void DataReturn(DTRequest request, String msgCode, Object response) {

    }

    protected void DataReturn(DTRequest request, Head head, Object response) {

    }

    private void handleErrorInfo(DTRequest request, Head head) {
        if (head.getCode() != null && !head.getCode().equals("0") && !head.getCode().equals("107")) {
            if (!request.isShowErrorMsg()) {
                return;
            }
            if (request.isSilent()) {
                if (!head.getCode().equals(mPrevErrorCode)) {
                    mPrevErrorCode = head.getCode();
                    mErrorHandler.removeMessages(MSG_ERROR_MSG_REPET);
                    mErrorHandler.sendEmptyMessageDelayed(MSG_ERROR_MSG_REPET, 120 * 1000);
                    showShortToast(head.getMsg());
                }
            } else {
                showShortToast(head.getMsg());
            }
        }
    }
    //-----------网络访问请求 End-----------

    /**
     * 设置界面资源Id
     */
    protected abstract int getContentViewId();

    /**
     * 绑定界面UI
     */
    protected void initView() {

    }

    /**
     * 页面数据初始化
     */
    protected void initData(Bundle savedInstanceState) {

    }

    /**
     * 界面UI事件监听
     */
    protected void initListener() {

    }

    protected void setWindowStatusBarColor(int color) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.setFlags(
                        WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                        WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(ContextCompat.getColor(mContext, color));

                //底部导航栏
                //window.setNavigationBarColor(activity.getResources().getColor(colorResId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
