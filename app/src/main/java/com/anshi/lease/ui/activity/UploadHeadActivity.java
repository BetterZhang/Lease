package com.anshi.lease.ui.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import com.anshi.lease.R;
import com.anshi.lease.common.Constants;
import com.anshi.lease.common.UserInfo;
import com.anshi.lease.domain.UserVo;
import com.anshi.lease.service.UserAuthService;
import com.anshi.lease.ui.base.LeaseBaseActivity;
import com.anshi.lease.util.Imageutils;
import com.jme.common.network.DTRequest;
import com.squareup.picasso.Picasso;
import java.io.File;
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

    private static final int RESULT_CAPTURE = 100;
    private static final int RESULT_PICK = 101;
    private static final int CROP_PHOTO = 102;

    public final int TYPE_TAKE_PHOTO = 1;//Uri获取类型判断

    @BindView(R.id.iv_head_upload)
    ImageView iv_head_upload;

    private boolean uploadFlag = false;
    private UserVo mUserVo;
    private Imageutils mImageutils;

    private File tempFile;
    private Uri photoURI;

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

        String userIconUrl = "";
        if (mUserVo.getKey_user_info().getUserIcon().contains(Constants.HttpConst.URL_BASE_IMG))
            userIconUrl = mUserVo.getKey_user_info().getUserIcon();
        else
            userIconUrl = Constants.HttpConst.URL_BASE_IMG + mUserVo.getKey_user_info().getUserIcon();
        Picasso.get()
                .load(userIconUrl)
                .placeholder(R.mipmap.ic_head_default)
                .into(iv_head_upload);
    }

    /**
     * @param dirPath
     * @return
     */
    private static String checkDirPath(String dirPath) {
        if (TextUtils.isEmpty(dirPath)) {
            return "";
        }

        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dirPath;
    }

    @OnClick({R.id.tv_upload, R.id.tv_commit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_upload:
//                CropImage.activity().start(this);
                showChooseDialog();
                break;
            case R.id.tv_commit:
                if (uploadFlag)
                    uplodeUserIcon();
                break;
            default:
                break;
        }
    }

    private void showChooseDialog() {
        new AlertDialog.Builder(this)
                .setCancelable(true)
                .setItems(new String[]{"拍照", "相册"}, (dialog, which) -> {
                    if (which == 0) {
                        applyCameraPermission();
                    } else {
                        applyWritePermission();
                    }
                }).show();
    }

    private void applyCameraPermission() {
        String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (Build.VERSION.SDK_INT >= 23) {
            int check0 = ContextCompat.checkSelfPermission(this, permissions[0]);
            int check1 = ContextCompat.checkSelfPermission(this, permissions[1]);
            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
            if (check0 == PackageManager.PERMISSION_GRANTED && check1 == PackageManager.PERMISSION_GRANTED) {
                //调用相机
                useCamera();
            } else {
                requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        } else {
            useCamera();
        }
    }

    private void applyWritePermission() {
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (Build.VERSION.SDK_INT >= 23) {
            int check = ContextCompat.checkSelfPermission(this, permissions[0]);
            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
            if (check == PackageManager.PERMISSION_GRANTED) {
                //调用相机
                selectGallery();
            } else {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
            }
        } else {
            selectGallery();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)
                useCamera();
            else
                showShortToast("需要相机和存储权限");
        }
        if (requestCode == 2) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                selectGallery();
            else
                showShortToast("需要存储权限");
        }
    }

    private void useCamera() {
        Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "xglt");
        if (!file.exists()) {
            file.mkdir();
        }
        /**
         * 这里将时间作为不同照片的名称
         */
        tempFile = new File(file, System.currentTimeMillis() + ".jpg");

        try {
            if (tempFile.exists()) {
                tempFile.delete();
            }
            tempFile.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }


//        tempFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
//                + "/test/" + System.currentTimeMillis() + ".jpg");
//        tempFile.getParentFile().mkdirs();

        if (tempFile != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                //如果是7.0及以上的系统使用FileProvider的方式创建一个Uri
                //改变Uri  com.xykj.customview.fileprovider注意和xml中的一致
                photoURI = FileProvider.getUriForFile(UploadHeadActivity.this, "com.anshi.lease.fileprovider", tempFile);
                takeIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                takeIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            } else {
                //7.0以下使用这种方式创建一个Uri
                photoURI = Uri.fromFile(tempFile);
            }
            takeIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(takeIntent, RESULT_CAPTURE);
        }
    }

    private void selectGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "请选择图片"), RESULT_PICK);
    }

    private void uplodeUserIcon() {
        if (mUserVo == null)
            return;
        Bitmap bitmap = ((BitmapDrawable) iv_head_upload.getDrawable()).getBitmap();
        HashMap<String, String> params = new HashMap<>();
        params.put("id", mUserVo.getKey_user_info().getId());
        params.put("userIcon", mImageutils.BitMapToString(bitmap));
        params.put("updateUser", mUserVo.getKey_user_info().getLoginName());
        sendRequest(UserAuthService.getInstance().uplodeUserIcon, params, true);
    }

    @Override
    protected void DataReturn(DTRequest request, String msgCode, Object response) {
        super.DataReturn(request, msgCode, response);
        if (msgCode.equals("200")) {
            String userIconUrl = (String) response;
            if (!TextUtils.isEmpty(userIconUrl))
                UserInfo.getInstance().getCurrentUser().getKey_user_info().setUserIcon(userIconUrl);

            this.finish();
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//            CropImage.ActivityResult result = CropImage.getActivityResult(data);
//            if (resultCode == RESULT_OK) {
//                uploadFlag = true;
//                Uri resultUri = result.getUri();
//                iv_head.setImageURI(resultUri);
//            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
//                uploadFlag = false;
//                Exception error = result.getError();
//            }
//        }
//    }

    /**
     * 打开截图界面
     *
     * @param uri 原图的Uri
     */
    public void starCropPhoto(Uri uri) {

        if (uri == null) {
            return;
        }
        Intent intent = new Intent();
        intent.setClass(this, ClipHeaderActivity.class);
        intent.setData(uri);
        intent.putExtra("side_length", 200);//裁剪图片宽高
        startActivityForResult(intent, CROP_PHOTO);
    }

    private void setPicToView(Intent picdata) {
        Uri uri = picdata.getData();
        if (uri == null) {
            return;
        }
        iv_head_upload.setImageURI(uri);
        uploadFlag = true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case RESULT_CAPTURE:
                if (resultCode == RESULT_OK) {
                    starCropPhoto(Uri.fromFile(tempFile));
                }
                break;
            case RESULT_PICK:
                if (resultCode == RESULT_OK) {
                    Uri uri = intent.getData();
                    starCropPhoto(uri);
                }
                break;
            case CROP_PHOTO:
                if (resultCode == RESULT_OK) {
                    if (intent != null) {
                        setPicToView(intent);
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void image_attachment(int from, String filename, Bitmap file, Uri uri) {

    }
}
