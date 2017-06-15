package com.kuyuntech.rtsp.rtspvideo.bean;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by hehe on 2017/6/7.
 */

public class ResponseBean {
    private int code;
    private String message;
    private String data;
    private String type;

    public ResponseBean(String str) throws JSONException {
        JSONObject jsonObject = new JSONObject(str);
        code = jsonObject.optInt("code");
        message = jsonObject.optString("message");
        data = jsonObject.optString("data");
        type = jsonObject.optString("type");
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ResponseBean{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data='" + data + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
