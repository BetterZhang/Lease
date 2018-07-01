package com.anshi.lease.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.anshi.lease.R;
import com.anshi.lease.common.UserInfo;
import com.anshi.lease.domain.AuthDataVo;
import com.anshi.lease.service.UserAuthService;
import com.anshi.lease.ui.base.LeaseBaseActivity;
import com.anshi.lease.util.Imageutils;
import com.jme.common.network.DTRequest;
import java.io.File;
import java.util.HashMap;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Android Studio.
 * Author : zhangzhongqiang
 * Email  : betterzhang.dev@gmail.com
 * Time   : 2018/06/07 下午11:07
 * Desc   : 上传身份证图片页面
 */
public class UploadIdCardctivity extends LeaseBaseActivity implements Imageutils.ImageAttachmentListener {

    @BindView(R.id.iv_front)
    ImageView iv_front;
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.iv_group)
    ImageView iv_group;
    @BindView(R.id.layout_front)
    LinearLayout layout_front;
    @BindView(R.id.layout_back)
    LinearLayout layout_back;
    @BindView(R.id.layout_group)
    LinearLayout layout_group;
    @BindView(R.id.tv_front)
    TextView tv_front;
    @BindView(R.id.tv_back)
    TextView tv_back;
    @BindView(R.id.tv_group)
    TextView tv_group;
    @BindView(R.id.iv_add1)
    ImageView iv_add1;
    @BindView(R.id.iv_add2)
    ImageView iv_add2;
    @BindView(R.id.iv_add3)
    ImageView iv_add3;

    private Imageutils mImageutils;
    private Bitmap frontBitmap;
    private Bitmap backBitmap;
    private Bitmap groupBitmap;
    private String frontFileName;
    private String backFileName;
    private String groupFileName;

    private boolean frontFlag = false;
    private boolean backFlag = false;
    private boolean groupFlag = false;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_upload_idcard;
    }

    @Override
    protected void initView() {
        super.initView();
        initToolbar("实名认证", true);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        mImageutils = new Imageutils(this);
    }

    @OnClick({R.id.tv_upload, R.id.layout_add1, R.id.layout_add2, R.id.layout_add3,
                R.id.tv_front, R.id.tv_back, R.id.tv_group})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_upload:
                handleCommit();
                break;
            case R.id.layout_add1:
                mImageutils.imagepicker(1);
                break;
            case R.id.layout_add2:
                mImageutils.imagepicker(2);
                break;
            case R.id.layout_add3:
                mImageutils.imagepicker(3);
                break;
            case R.id.tv_front:
                frontFlag = !frontFlag;
                setFrontLayoutStatus();
                break;
            case R.id.tv_back:
                backFlag = !backFlag;
                setBackLayoutStatus();
                break;
            case R.id.tv_group:
                groupFlag = !groupFlag;
                setGroupLayoutStatus();
                break;
            default:
                break;
        }
    }

    private void handleCommit() {
        if (frontBitmap == null || backBitmap == null || groupBitmap == null) {
            showShortToast("请完成相关照片上传");
            return;
        }
        userRealNameAuth();
    }

    private void userRealNameAuth() {
        AuthDataVo authDataVo = UserInfo.getInstance().getAuthDataVo();
        HashMap<String, String> params = new HashMap<>();
        params.put("id", authDataVo.getId());
        params.put("userPid", authDataVo.getUserPid());
        params.put("userIcFront", authDataVo.getUserIcFront());
        params.put("userIcBack", authDataVo.getUserIcBack());
        params.put("userIcGroup", authDataVo.getUserIcGroup());
        params.put("updateUser", authDataVo.getUpdateUser());
        sendRequest(UserAuthService.getInstance().userRealNameAuth, params, true);
    }

    @Override
    protected void DataReturn(DTRequest request, String msgCode, Object response) {
        super.DataReturn(request, msgCode, response);
        switch (request.getApi().getName()) {
            case "userRealNameAuth":
                if (msgCode.equals("200")) {
                    startAnimActivity(AuthCompleteActivity.class);
                }
                break;
            default:
                break;
        }
    }

    private void setFrontLayoutStatus() {
        if (frontFlag) {
            layout_front.setVisibility(View.VISIBLE);
            tv_front.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(this, R.mipmap.ic_up), null);
        } else {
            layout_front.setVisibility(View.GONE);
            tv_front.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(this, R.mipmap.ic_down), null);
        }
    }

    private void setBackLayoutStatus() {
        if (backFlag) {
            layout_back.setVisibility(View.VISIBLE);
            tv_back.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(this, R.mipmap.ic_up), null);
        } else {
            layout_back.setVisibility(View.GONE);
            tv_back.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(this, R.mipmap.ic_down), null);
        }
    }

    private void setGroupLayoutStatus() {
        if (groupFlag) {
            layout_group.setVisibility(View.VISIBLE);
            tv_group.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(this, R.mipmap.ic_up), null);
        } else {
            layout_group.setVisibility(View.GONE);
            tv_group.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(this, R.mipmap.ic_down), null);
        }
    }

    @Override
    public void image_attachment(int from, String filename, Bitmap file, Uri uri) {
        if (from == 1) {
            iv_add1.setVisibility(View.GONE);
            iv_front.setVisibility(View.VISIBLE);
            this.frontBitmap = file;
            this.frontFileName = filename;
            iv_front.setImageBitmap(file);
            UserInfo.getInstance().getAuthDataVo().setUserIcFront(mImageutils.BitMapToString(frontBitmap));
        } else if (from == 2) {
            iv_add2.setVisibility(View.GONE);
            iv_back.setVisibility(View.VISIBLE);
            this.backBitmap = file;
            this.backFileName = filename;
            iv_back.setImageBitmap(file);
            UserInfo.getInstance().getAuthDataVo().setUserIcBack(mImageutils.BitMapToString(backBitmap));
        } else if (from == 3) {
            iv_add3.setVisibility(View.GONE);
            iv_group.setVisibility(View.VISIBLE);
            this.groupBitmap = file;
            this.groupFileName = filename;
            iv_group.setImageBitmap(file);
            UserInfo.getInstance().getAuthDataVo().setUserIcGroup(mImageutils.BitMapToString(groupBitmap));
        }

        String path = Environment.getExternalStorageDirectory() + File.separator + "ImageAttach" + File.separator;
        mImageutils.createImage(file, filename, path, false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mImageutils.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mImageutils.request_permission_result(requestCode, permissions, grantResults);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        super.onBackPressed();
    }
}
