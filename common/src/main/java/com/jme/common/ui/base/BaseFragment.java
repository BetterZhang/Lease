package com.jme.common.ui.base;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.MenuRes;
import android.support.annotation.StyleRes;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jme.common.network.API;
import com.jme.common.network.AsynCommon;
import com.jme.common.network.DTRequest;
import com.jme.common.network.Head;
import com.jme.common.network.OnResultListener;
import com.jme.common.ui.view.LoadingDialog;
import com.orhanobut.logger.Logger;

import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Fragment基类
 * Created by zhangzhongqiang on 2016/3/14.
 */
public abstract class BaseFragment extends Fragment implements View.OnTouchListener, OnResultListener {
    //Toast对象
    protected Toast mToast;
    //LoadingDialog对象
    protected LoadingDialog mLoadingDialog;
    //Context对象
    protected Context mContext;
    //Activity对象
    protected AppCompatActivity mActivity;
    //Toolbar Helper 对象
    protected ToolbarHelper mToolbarHelper;

    protected Unbinder unbinder;

    protected boolean mHide = true;

    protected boolean mUseBinding = false;

    protected ViewDataBinding mBindingUtil;

    protected AlertDialog.Builder mPhoneDialog;  //统一帐户登录框

    protected boolean mVisible = false;

    private static final int MSG_ERROR_MSG_REPEAT = 1000;
    private String mPrevErrorCode = "";
    private Handler mErrorHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_ERROR_MSG_REPEAT:
                    mErrorHandler.removeMessages(MSG_ERROR_MSG_REPEAT);
                    mPrevErrorCode = "";
                    break;
            }
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (AppCompatActivity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setRetainInstance(true);
//        mActivity = (AppCompatActivity) getActivity();
        mContext = getContext();
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        mVisible = isVisibleToUser;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        int id = getContentViewId();
        View view;
        if (mUseBinding) {
            mBindingUtil = DataBindingUtil.inflate(inflater, id, container, false);
            if (mBindingUtil == null) {
                view = inflater.inflate(id, container, false);
                Logger.e("DataBindingUtil is failed ...");
            } else {
                view = mBindingUtil.getRoot();
                Logger.i("DataBindingUtil is success ...");
            }

        } else {
            view = inflater.inflate(id, container, false);
        }
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initData(savedInstanceState);
        initListener();
    }

    @Override
    public void onStart() {
        super.onStart();
//        if (!isHidden()) {
//            onHiddenChanged(false);
//        }
    }

    @Override
    public void onStop() {
        super.onStop();
//        if (!isHidden()) {
//            onHiddenChanged(true);
//        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        mHide = hidden;
    }

    //-----------Toolbar Setting-----------
    public void initToolbar(View view, boolean hasBack) {
        mToolbarHelper = new ToolbarHelper(mActivity, getView());

        mToolbarHelper.initToolbar(view);
        setBackNavigation(hasBack);
    }

    public void initToolbar(String title, boolean hasBack) {
        mToolbarHelper = new ToolbarHelper(mActivity, getView());

        mToolbarHelper.initToolbar(title);
        setBackNavigation(hasBack);
    }

    public void initToolbar(String title, boolean hasBack, @ColorInt int resId) {
        mToolbarHelper = new ToolbarHelper(mActivity, getView());

        mToolbarHelper.initToolbar(title, resId);
        setBackNavigation(hasBack);
    }

    public void initToolbar(int resId, boolean hasBack) {
        initToolbar(getString(resId), hasBack);
    }

    public void initToolbar(int resId, boolean hasBack, @ColorInt int colorResID) {
        initToolbar(getString(resId), hasBack, colorResID);
    }

    public void initToolbar(String title, boolean hasBack, @ColorInt int resId, @ColorInt int layoutColorId) {
        mToolbarHelper = new ToolbarHelper(mActivity, getView());

        mToolbarHelper.initToolbar(title, resId, layoutColorId);
        setBackNavigation(hasBack);
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
                    mActivity.onBackPressed();
                }
            });
    }

    public void setBackNavigation(boolean hasBack, @DrawableRes int drawableResId) {
        if (mToolbarHelper != null)
            mToolbarHelper.setBackNavigation(hasBack, drawableResId, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mActivity.onBackPressed();
                }
            });
    }

    public void setRightNavigation(String str, @DrawableRes int resId, ToolbarHelper.OnSingleMenuItemClickListener listener) {
        if (mToolbarHelper != null)
            mToolbarHelper.setSingleMenu(str, resId, listener);
    }

    public void setRightNavigation(String str, @DrawableRes int resId, @StyleRes int _resId, ToolbarHelper.OnSingleMenuItemClickListener listener) {
        if (mToolbarHelper != null)
            mToolbarHelper.setSingleMenu(str, resId, _resId, listener);
    }

    public void setRightMultiNavigation(@MenuRes int resId, final Toolbar.OnMenuItemClickListener listener) {
        if (mToolbarHelper != null)
            mToolbarHelper.setMenu(resId, listener);
    }

    public void setRightMultiNavigation(@MenuRes int resId, @StyleRes int _resId, final Toolbar.OnMenuItemClickListener listener) {
        if (mToolbarHelper != null)
            mToolbarHelper.setMenu(resId, _resId, listener);
    }

    public void clearRightNavigation() {
        if (mToolbarHelper != null)
            mToolbarHelper.clearMenu();
    }
    //-----------Toolbar Setting End-----------

    /**
     * 短暂显示Toast提示(来自res)
     *
     * @param resId
     */
    protected void showShortToast(final int resId) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mToast == null) {
                    mToast = Toast.makeText(mActivity, getResources().getString(resId), Toast.LENGTH_SHORT);
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
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mToast == null) {
                        mToast = Toast.makeText(mActivity, text, Toast.LENGTH_SHORT);
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
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mToast == null) {
                    mToast = Toast.makeText(mActivity, getResources().getString(resId), Toast.LENGTH_SHORT);
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
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mToast == null) {
                        mToast = Toast.makeText(mActivity, text, Toast.LENGTH_SHORT);
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
        intent.setClass(mActivity, cls);
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
        intent.setClass(mActivity, cls);
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
            mLoadingDialog = new LoadingDialog(mActivity);
        }
        mLoadingDialog.setLoadingText(text);
        if (!mActivity.isFinishing() && !mLoadingDialog.isShowing()) {
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
        if (head.getCode() != null && !head.getCode().equals("200")) {
            if (!request.isShowErrorMsg()) {
                return;
            }
            if (request.isSilent()) {
                if (!head.getCode().equals(mPrevErrorCode)) {
                    mPrevErrorCode = head.getCode();
                    mErrorHandler.removeMessages(MSG_ERROR_MSG_REPEAT);
                    mErrorHandler.sendEmptyMessageDelayed(MSG_ERROR_MSG_REPEAT, 120 * 1000);
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

    /**
     * 查找子控件的方法
     *
     * @param resId
     * @return
     */
    protected View findViewById(int resId) {
        return getView().findViewById(resId);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // 拦截触摸事件，防止传递到下一层的View
        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
