package com.anshi.lease.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;

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

    private Imageutils mImageutils;
    private Bitmap frontBitmap;
    private Bitmap backBitmap;
    private Bitmap groupBitmap;
    private String frontFileName;
    private String backFileName;
    private String groupFileName;

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

    @OnClick({R.id.tv_upload, R.id.iv_front, R.id.iv_back, R.id.iv_group})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_upload:
                handleCommit();
                break;
            case R.id.iv_front:
                mImageutils.imagepicker(1);
                break;
            case R.id.iv_back:
                mImageutils.imagepicker(2);
                break;
            case R.id.iv_group:
                mImageutils.imagepicker(3);
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
                if (msgCode.equals("0")) {
                    startAnimActivity(AuthCompleteActivity.class);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void image_attachment(int from, String filename, Bitmap file, Uri uri) {
        if (from == 1) {
            this.frontBitmap = file;
            this.frontFileName = filename;
            iv_front.setImageBitmap(file);
            UserInfo.getInstance().getAuthDataVo().setUserIcFront(mImageutils.BitMapToString(frontBitmap));
        } else if (from == 2) {
            this.backBitmap = file;
            this.backFileName = filename;
            iv_back.setImageBitmap(file);
            UserInfo.getInstance().getAuthDataVo().setUserIcBack(mImageutils.BitMapToString(backBitmap));
        } else if (from == 3) {
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
