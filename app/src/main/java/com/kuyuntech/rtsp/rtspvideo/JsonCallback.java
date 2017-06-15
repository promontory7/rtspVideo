package com.kuyuntech.rtsp.rtspvideo;

import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONObject;


import okhttp3.Response;

/**
 * Created by hehe on 2017/6/6.
 */

public abstract class JsonCallback extends Callback {

    @Override
    public JSONObject parseNetworkResponse(Response response, int id) throws Exception {
        JSONObject jsonObject = null;
        try {
            if (response != null) {
                jsonObject = new JSONObject(response.body().toString());
            }
        } finally {
            if (jsonObject == null) {
                jsonObject = new JSONObject();
            }
            return jsonObject;
        }

    }

}
