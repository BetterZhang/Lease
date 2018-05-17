package com.jme.common.network;

import com.google.gson.Gson;
import java.io.Serializable;

/**
 * Created by zhangzhongqiang on 2015/7/29.
 */
public class DTResponse<V> implements Serializable {

    private String code;

    private String message;

    private V respData;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public V getRespData() {
        return respData;
    }

    public void setRespData(V respData) {
        this.respData = respData;
    }

    public String getRespDataString(){
        return new Gson().toJson(respData);
    }
}
