package com.kuyuntech.rtsp.rtspvideo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;
import com.jaeger.library.StatusBarUtil;
import com.kuyuntech.rtsp.rtspvideo.R;
import com.kuyuntech.rtsp.rtspvideo.RequestUrlConstants;
import com.kuyuntech.rtsp.rtspvideo.bean.ResponseBean;
import com.kuyuntech.rtsp.rtspvideo.util.DigestUtils;
import com.kuyuntech.rtsp.rtspvideo.util.PreferencesUtils;
import com.kuyuntech.rtsp.rtspvideo.util.ToastUtils;
import com.kyleduo.switchbutton.SwitchButton;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;


import org.json.JSONException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.edt_username)
    EditText edtUsername;
    @BindView(R.id.edt_password)
    EditText edtPassword;
    @BindView(R.id.swb_remeberPassw)
    SwitchButton swbtnRemeberPassw;
    @BindView(R.id.btn_login)
    Button btnLogin;

    private String username;
    private String password;

    MaterialDialog materialDialog = null;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        StatusBarUtil.setTransparent(this);
        materialDialog = new MaterialDialog.Builder(this)
                .title("登录中")
                .content("请稍后")
                .progress(true, 0)
                .progressIndeterminateStyle(false).build();


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                materialDialog.show();
                http2login();
            }
        });
        swbtnRemeberPassw.setChecked(true);

        edtUsername.setText(PreferencesUtils.getString(MainActivity.this, "username"));
        edtPassword.setText(PreferencesUtils.getString(MainActivity.this, "password"));


    }

    private boolean validateInput() {
        username = edtUsername.getText().toString();
        password = edtPassword.getText().toString();
        if (username == null || username.equals("") || password == null || password.equals("")) {
            return false;
        }
        return true;
    }

    private void http2login() {
        if (validateInput()) {
            OkHttpUtils.post()
                    .url(RequestUrlConstants.URL_LOGIN)
                    .addParams("username", username)
                    .addParams("password", DigestUtils.md5(password).toLowerCase())
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            materialDialog.dismiss();
                            ToastUtils.showToast(MainActivity.this, "请求失败");
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            materialDialog.dismiss();
                            ResponseBean responseBean = null;
                            try {
                                responseBean = new ResponseBean(response);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                ToastUtils.showToast(MainActivity.this, "数据格式错误");
                            }
                            if (responseBean == null) {
                                return;
                            }
                            if (responseBean.getCode() == 2000) {

                                PreferencesUtils.putString(MainActivity.this, "username", username);
                                if (swbtnRemeberPassw.isChecked()) {
                                    PreferencesUtils.putString(MainActivity.this, "password", password);
                                }

                                startActivity(new Intent(MainActivity.this, XRecyclerviewActivity.class));
                                finish();

                            } else {
                                ToastUtils.showToast(MainActivity.this, responseBean.getMessage());
                            }

                        }
                    });

        } else {
            ToastUtils.showToast(this, "账号密或码不能为空");
        }
    }


}
