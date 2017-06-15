package com.kuyuntech.rtsp.rtspvideo.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.jaeger.library.StatusBarUtil;
import com.kuyuntech.rtsp.rtspvideo.R;

public class VlcPlayerActivity extends AppCompatActivity {
    String url ="";
    String title = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        url = bundle.getString("url");
        title = bundle.getString("title");

        setContentView(R.layout.activity_vlc_player);
        StatusBarUtil.setTransparent(this);


    }

    public String getUrl(){
        return url;
    }

}


