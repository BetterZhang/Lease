package com.jme.common.util;

import android.text.InputFilter;
import android.text.Spanned;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by XuJun on 2017/10/17.
 */

public class InputUtils {

    public static InputFilter getInputFilter() {
        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (source.equals(" "))
                    return "";

                String speChat = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";

                Pattern pattern = Pattern.compile(speChat);
                Matcher matcher = pattern.matcher(source.toString());

                if (matcher.find())
                    return "";
                else if (source.equals("-") || source.equals("_"))
                    return "";
                else
                    return null;
            }
        };

        return filter;
    }

}
