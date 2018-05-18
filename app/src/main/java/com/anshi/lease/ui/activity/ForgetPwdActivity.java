package com.anshi.lease.ui.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.anshi.lease.R;
import com.anshi.lease.domain.CaptchaVo;
import com.anshi.lease.domain.SmsVo;
import com.anshi.lease.service.UserAuthService;
import com.anshi.lease.ui.base.LeaseBaseActivity;
import com.anshi.lease.util.TextUtils;
import com.jme.common.network.DTRequest;
import com.jme.common.ui.base.CountDownTimer;
import com.jme.common.util.Base64;
import com.jme.common.util.SecurityUtils;
import com.jme.common.util.StringUtils;
import java.util.HashMap;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Android Studio.
 * Author : zhangzhongqiang
 * Email  : betterzhang.dev@gmail.com
 * Time   : 2018/05/15 下午 3:16
 * Desc   : 找回密码页面
 */
public class ForgetPwdActivity extends LeaseBaseActivity {

    @BindView(R.id.et_phone)
    EditText et_phone;
    @BindView(R.id.et_image_code)
    EditText et_image_code;
    @BindView(R.id.et_sms_code)
    EditText et_sms_code;
    @BindView(R.id.iv_code)
    ImageView iv_code;
    @BindView(R.id.tv_send_sms)
    TextView tv_send_sms;
    @BindView(R.id.et_password)
    EditText et_password;

    private CaptchaVo mCaptchaVo;
    private SmsVo mSmsVo;
    private Bitmap mBitmap;
    private byte[] decodedString;

    private String captchaToken = "";
    private String smsToken = "";

    private CountDownTimer timer;
    private boolean hasSendCode = false;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_forget_pwd;
    }

    @Override
    protected void initView() {
        super.initView();
        initToolbar("找回密码", true);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        timer = new CountDownTimer(this, 30000, 1000, tv_send_sms,
                getString(R.string.text_get_verification_code), R.color.white);
        getCaptcha();
    }

    @Override
    protected void initListener() {
        super.initListener();
        timer.setCountDownEndListener(new CountDownTimer.CountDownEndListener() {
            @Override
            public void onEndMethod() {
                getCaptcha();
            }
        });
    }

    private void getCaptcha() {
        HashMap<String, String> params = new HashMap<>();
        sendRequest(UserAuthService.getInstance().getcaptcha, params, true);
    }

    private void sendSms() {
        HashMap<String, String> params = new HashMap<>();
        params.put("mobile", TextUtils.getText(et_phone));
        params.put("captcha", TextUtils.getText(et_image_code));
        params.put("captchaToken", captchaToken);
        params.put("needCaptchaToken", "true");
        sendRequest(UserAuthService.getInstance().sendsms, params, true);
    }

    private void resetPassword() {
        HashMap<String, String> params = new HashMap<>();
        params.put("smsToken", smsToken);
        params.put("smsVCode", TextUtils.getText(et_sms_code));
        params.put("newPassword", SecurityUtils.getMD5(TextUtils.getText(et_password), true));
        sendRequest(UserAuthService.getInstance().resetpassword, params, true);
    }

    @Override
    protected void DataReturn(DTRequest request, String msgCode, Object response) {
        super.DataReturn(request, msgCode, response);
        switch (request.getApi().getName()) {
            case "getcaptcha":
                if (msgCode.equals("200")) {
                    mCaptchaVo = (CaptchaVo) response;
                    if (mCaptchaVo == null)
                        return;
                    captchaToken = mCaptchaVo.getKey_captcha_token();
                    decodedString = Base64.decode(mCaptchaVo.getKey_captcha_base64().getBytes());
                    mBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    iv_code.setImageBitmap(mBitmap);
                }
                break;
            case "sendsms":
                if (msgCode.equals("200")) {
                    mSmsVo = (SmsVo) response;
                    if (mSmsVo == null)
                        return;
                    smsToken = mSmsVo.getKey_sms_vcode_token();
                    hasSendCode = true;
                    showShortToast("短信验证码已发送");
                    if (timer != null) {
                        timer.start();
                    }
                } else {
                    getCaptcha();
                    et_image_code.setText("");
                }
                break;
            case "resetpassword":
                if (msgCode.equals("200")) {
                    showShortToast("重置密码成功");
                    this.finish();
                }
                break;
            default:
                break;
        }
    }

    @OnClick({R.id.iv_code, R.id.tv_send_sms, R.id.btn_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_code:
                getCaptcha();
                break;
            case R.id.tv_send_sms:
                handleSendSms();
                break;
            case R.id.btn_submit:
                handleSubmit();
                break;
            default:
                break;
        }
    }

    private void handleSendSms() {
        if (!StringUtils.isPhoneNumber(et_phone.getText().toString())) {
            showShortToast("请输入正确的手机号");
            return;
        }
        if (TextUtils.isEmpty(et_image_code)) {
            showShortToast("请输入图形验证码");
            return;
        }
        sendSms();
    }

    private void handleSubmit() {
        if (!StringUtils.isPhoneNumber(et_phone.getText().toString())) {
            showShortToast("请输入正确的手机号");
            return;
        }
        if (TextUtils.isEmpty(et_image_code)) {
            showShortToast("请输入图形验证码");
            return;
        }
        if (TextUtils.isEmpty(et_sms_code)) {
            showShortToast("请输入短信验证码");
            return;
        }
        if (!StringUtils.isPasswordRight(TextUtils.getText(et_password))) {
            showShortToast("请输入6-10位字母数字混合的密码");
            return;
        }
        if (!hasSendCode) {
            showShortToast("请获取短信验证码");
            return;
        }
        resetPassword();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null)
            timer.onFinish();
    }

}
