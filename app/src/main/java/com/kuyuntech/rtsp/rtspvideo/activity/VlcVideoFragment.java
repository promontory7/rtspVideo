package com.kuyuntech.rtsp.rtspvideo.activity;


import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.kuyuntech.rtsp.rtspvideo.R;
import com.kuyuntech.rtsp.rtspvideo.util.L;

import org.videolan.vlc.VlcVideoView;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 */
public class VlcVideoFragment extends Fragment implements View.OnClickListener {

    //    public static String path = "http://v.tiaooo.com/ljbmVx2MrnY2NiNk-cWsUh08480y";
    public static String path = "http://v.tiaooo.com/ljbmVx2MrnY2NiNk-cWsUh08480y";
    String title = "";
    public boolean isFullscreen;


    @BindView(R.id.player)
    VlcVideoView vlcVideoView;
    @BindView(R.id.info)
    TextView logInfo;
    @BindView(R.id.name)
    TextView name;

    String tag = "VlcVideoFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video, container, false);
        //为啥这么多人用不会用butterknife 要我注掉
        ButterKnife.bind(this, view);
        path = ((VlcPlayerActivity) getActivity()).url;
        title = ((VlcPlayerActivity) getActivity()).title;
        name.setText(title);
        L.e(path);

        if (getActivity().getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE) {
            isFullscreen = true;
        } else {
            isFullscreen = false;
        }


        view.findViewById(R.id.change).setOnClickListener(this);
//        Media media = new Media(VLCInstance.get(getContext()), Uri.parse(path));
//        media.setHWDecoderEnabled(false, false);
//        vlcVideoView.setMedia(media);

        vlcVideoView.setMediaListenerEvent(new MediaControl(vlcVideoView, logInfo));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.i(tag, "---------   start   ----------------");
                vlcVideoView.startPlay(path);
            }
        }, 100);

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        vlcVideoView.clearFocus();
        vlcVideoView.onStop();
        vlcVideoView.onDestory();

    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.change:
                isFullscreen = !isFullscreen;
                if (isFullscreen) {
                    getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

                } else {
                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                }
                break;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            isFullscreen = false;
        }
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            isFullscreen = true;
        }
    }
}
