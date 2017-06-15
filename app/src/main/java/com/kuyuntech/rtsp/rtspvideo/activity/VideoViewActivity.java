package com.kuyuntech.rtsp.rtspvideo.activity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.kuyuntech.rtsp.rtspvideo.R;
import com.kuyuntech.rtsp.rtspvideo.util.L;
import com.universalvideoview.UniversalMediaController;
import com.universalvideoview.UniversalVideoView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoViewActivity extends AppCompatActivity {

    @BindView(R.id.videoView)
    UniversalVideoView videoView;
    @BindView(R.id.media_controller)
    UniversalMediaController mediaController;
    @BindView(R.id.video_layout)
    FrameLayout videoLayout;
    @BindView(R.id.rl_vv)
    RelativeLayout rlVv;

    private String url;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        url = bundle.getString("url");

        videoView.setMediaController(mediaController);
        videoView.setVideoViewCallback(new UniversalVideoView.VideoViewCallback() {
            @Override
            public void onScaleChange(boolean isFullscreen) {
//                this.isFullscreen = isFullscreen;
//                if (isFullscreen) {
//                    ViewGroup.LayoutParams layoutParams = videoLayout.getLayoutParams();
//                    layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
//                    layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
//                    videoLayout.setLayoutParams(layoutParams);
//                    //设置全屏时,无关的View消失,以便为视频控件和控制器控件留出最大化的位置
//                    videoLayout.setVisibility(View.GONE);
//                } else {
//                    ViewGroup.LayoutParams layoutParams = videoLayout.getLayoutParams();
//                    layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
//                    layoutParams.height = this.cachedHeight;
//                    videoLayout.setLayoutParams(layoutParams);
//                    mBottomLayout.setVisibility(View.VISIBLE);
//                }
            }


            @Override
            public void onPause(MediaPlayer mediaPlayer) { // 视频暂停
                L.d( "onPause UniversalVideoView callback");
            }

            @Override
            public void onStart(MediaPlayer mediaPlayer) { // 视频开始播放或恢复播放
                L.d("onStart UniversalVideoView callback");
            }

            @Override
            public void onBufferingStart(MediaPlayer mediaPlayer) {// 视频开始缓冲
                L.d("onBufferingStart UniversalVideoView callback");
            }

            @Override
            public void onBufferingEnd(MediaPlayer mediaPlayer) {// 视频结束缓冲
                L.d( "onBufferingEnd UniversalVideoView callback");
            }

        });
        videoView.setVideoURI(Uri.parse(url));
        videoView.start();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


}
