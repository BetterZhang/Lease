package com.jme.common.ui.base;

/**
 * Created by gengda on 16/9/18.
 */

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.widget.Button;

import com.jme.common.R;

public class CountDownTimer extends android.os.CountDownTimer {

    private Context mContext;
    private Button mCountdownButton;
    private String mButtonText;
    private CountDownEndListener mListener;
    private int mTickColor = 0;
    private int mFinishColor = 0;

    public interface CountDownEndListener {
        void onEndMethod();
    }

    public CountDownTimer(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
    }

    //设置监听器 也就是实例化接口
    public void setCountDownEndListener(CountDownEndListener listener) {
        mListener = listener;
    }

    /**
     * @param millisInFuture    总的时间（毫秒）
     * @param countDownInterval 间隔时间（毫秒）
     * @param button            倒计时的按钮
     * @param buttonText        倒计时按钮的初始文字
     */
    public CountDownTimer(Context context, long millisInFuture, long countDownInterval, Button button, String buttonText) {
        this(millisInFuture, countDownInterval);
        this.mContext = context;
        this.mCountdownButton = button;
        this.mButtonText = buttonText;
        this.mFinishColor =  R.color.white;
    }

    public CountDownTimer(Context context, long millisInFuture, long countDownInterval, Button button, String buttonText, int tickColor) {
        this(millisInFuture, countDownInterval);
        this.mContext = context;
        this.mCountdownButton = button;
        this.mButtonText = buttonText;
        this.mTickColor = tickColor;
        this.mFinishColor = R.color.white;
    }

    public CountDownTimer(Context context, long millisInFuture, long countDownInterval, Button button, String buttonText, int tickColor, int finishColor) {
        this(millisInFuture, countDownInterval);
        this.mContext = context;
        this.mCountdownButton = button;
        this.mButtonText = buttonText;
        this.mTickColor = tickColor;
        this.mFinishColor = finishColor;
    }

    @Override
    public void onFinish() {
        mCountdownButton.setEnabled(true);
        mCountdownButton.setText(mButtonText);
        mCountdownButton.setTextColor(ContextCompat.getColor(mContext, mFinishColor));
        mListener.onEndMethod();
    }

    @Override
    public void onTick(long millisUntilFinished) {
        mCountdownButton.setEnabled(false);
        mCountdownButton.setText("重新获取" + millisUntilFinished / 1000 + "秒");
        if (mTickColor == 0)
            mCountdownButton.setTextColor(ContextCompat.getColor(mContext, R.color.common_font_gray));
        else
            mCountdownButton.setTextColor(ContextCompat.getColor(mContext, mTickColor));
    }
}