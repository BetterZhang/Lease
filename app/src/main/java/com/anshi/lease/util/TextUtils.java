package com.anshi.lease.util;

import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Android Studio.
 * Author : zhangzhongqiang
 * Email  : betterzhang.dev@gmail.com
 * Time   : 2018/05/17 下午 2:36
 * Desc   : description
 */
public class TextUtils {

    public static boolean isEmpty(EditText editText) {
        return android.text.TextUtils.isEmpty(editText.getText().toString().trim());
    }

    public static boolean isEmpty(TextView textView) {
        return android.text.TextUtils.isEmpty(textView.getText().toString().trim());
    }

    public static String getText(EditText editText) {
        return editText.getText().toString().trim();
    }

}
