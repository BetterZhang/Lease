package com.anshi.lease.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import com.anshi.lease.R;
import com.anshi.lease.common.UserInfo;
import com.anshi.lease.domain.UserVo;
import com.anshi.lease.service.UserAuthService;
import com.anshi.lease.ui.base.LeaseBaseActivity;
import com.anshi.lease.util.Imageutils;
import com.jme.common.network.DTRequest;
import com.theartofdev.edmodo.cropper.CropImage;
import java.util.HashMap;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Android Studio.
 * Author : zhangzhongqiang
 * Email  : betterzhang.dev@gmail.com
 * Time   : 2018/06/10 下午6:54
 * Desc   : 上传头像页面
 */
public class UploadHeadActivity extends LeaseBaseActivity implements Imageutils.ImageAttachmentListener {

    @BindView(R.id.iv_head)
    ImageView iv_head;

    private boolean uploadFlag = false;
    private UserVo mUserVo;
    private Imageutils mImageutils;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_upload_head;
    }

    @Override
    protected void initView() {
        super.initView();
        initToolbar("修改头像", true);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        mUserVo = UserInfo.getInstance().getCurrentUser();
        mImageutils = new Imageutils(this);
    }

    @OnClick({R.id.tv_upload, R.id.tv_commit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_upload:
                CropImage.activity()
//                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(this);
                break;
            case R.id.tv_commit:
                if (uploadFlag)
                    uplodeUserIcon();
                break;
            default:
                break;
        }
    }

    private void uplodeUserIcon() {
        if (mUserVo == null)
            return;
        Bitmap bitmap = ((BitmapDrawable) iv_head.getDrawable()).getBitmap();
        HashMap<String, String> params = new HashMap<>();
        params.put("id", mUserVo.getKey_user_info().getId());
        params.put("userIcon", mImageutils.BitMapToString(bitmap));
        params.put("updateUser", mUserVo.getKey_user_info().getUserName());
        sendRequest(UserAuthService.getInstance().uplodeUserIcon, params, true);
    }

    @Override
    protected void DataReturn(DTRequest request, String msgCode, Object response) {
        super.DataReturn(request, msgCode, response);
        if (msgCode.equals("200")) {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                uploadFlag = true;
                Uri resultUri = result.getUri();
                iv_head.setImageURI(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                uploadFlag = false;
                Exception error = result.getError();
            }
        }
    }

    @Override
    public void image_attachment(int from, String filename, Bitmap file, Uri uri) {

    }
}
