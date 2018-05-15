package com.jme.common.network;

import android.content.Context;
import com.google.gson.Gson;
import com.jme.common.R;
import java.net.ConnectException;
import java.util.HashMap;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Yanmin on 2016/3/12.
 */
public class AsynCommon {
    private Context mContext;
    private OnResultListener mListener;
    private Call restResponse;
    private DTRequest mRequest;

    public static AsynCommon SendRequest(API api, HashMap<String, String> params, boolean silent, boolean showErrorMsg, OnResultListener listener, Context context) {
        DTRequest request = new DTRequest(api, params, silent, showErrorMsg);
        AsynCommon task = new AsynCommon(context, listener, request);
        task.execute();

        return task;
    }

    public AsynCommon(Context context, OnResultListener listener, DTRequest request) {
        this.mRequest = request;
        this.mContext = context;
        this.mListener = listener;
    }

    public boolean execute() {
        if (mRequest == null)
            return false;

        restResponse = mRequest.getApi().request(mRequest.getParams());

        restResponse.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                Head head = new Head();
                Object body = "";
                if (response.raw().code() != 200) {
                    head.setCode("" + response.raw().code());
                    head.setMsg("服务器异常");
                } else {
                    if (!mRequest.getApi().isResponseJson()) {
                        body = response.body();
                        head.setCode("0");
                        head.setMsg("成功");
                    } else {
                        DTResponse dtResponse = (DTResponse) response.body();

                        head = dtResponse.getHead();

                        try {
                            body = new Gson().fromJson(dtResponse.getBodyToString(),
                                    mRequest.getApi().getEntryType());
                        } catch (Exception e) {
                            body = dtResponse.getBodyToString();
                        }

                    }
                }
                mListener.OnResult(mRequest, head, body);
                restResponse = null;
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Head head = new Head();
                final Throwable cause = t.getCause() != null ? t.getCause() : t;
                if (cause != null) {
                    if (cause instanceof ConnectException) {
                        head.setCode("500");
                        head.setMsg(mContext.getResources().getString(R.string.text_error_server));
                    } else {
                        head.setCode("408");
                        head.setMsg(mContext.getResources().getString(R.string.text_error_timeout));
                    }
                }
                mListener.OnResult(mRequest, head, null);
                restResponse = null;
            }
        });
        return true;
    }

    public void cancel() {
        if (restResponse != null)
            restResponse.cancel();
    }
}
