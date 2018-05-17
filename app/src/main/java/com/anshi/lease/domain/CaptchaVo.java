package com.anshi.lease.domain;

/**
 * Created by Android Studio.
 * Author : zhangzhongqiang
 * Email  : betterzhang.dev@gmail.com
 * Time   : 2018/05/17 下午 2:25
 * Desc   : description
 */
public class CaptchaVo {

    private String key_captcha_token;
    private String key_captcha_base64;

    public String getKey_captcha_token() {
        return key_captcha_token;
    }

    public void setKey_captcha_token(String key_captcha_token) {
        this.key_captcha_token = key_captcha_token;
    }

    public String getKey_captcha_base64() {
        return key_captcha_base64;
    }

    public void setKey_captcha_base64(String key_captcha_base64) {
        this.key_captcha_base64 = key_captcha_base64;
    }
}
