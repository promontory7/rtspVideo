package com.kuyuntech.rtsp.rtspvideo.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;


import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jaeger.library.StatusBarUtil;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import com.kuyuntech.rtsp.rtspvideo.VideoAdapter;
import com.kuyuntech.rtsp.rtspvideo.R;
import com.kuyuntech.rtsp.rtspvideo.RequestUrlConstants;
import com.kuyuntech.rtsp.rtspvideo.bean.ResponseBean;
import com.kuyuntech.rtsp.rtspvideo.bean.VideoBean;
import com.kuyuntech.rtsp.rtspvideo.util.L;
import com.kuyuntech.rtsp.rtspvideo.util.PreferencesUtils;
import com.kuyuntech.rtsp.rtspvideo.util.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.Call;


/**
 * Created by Administrator on 2016/6/12.
 */
public class XRecyclerviewActivity extends AppCompatActivity {

    private XRecyclerView mRecyclerView;
    private VideoAdapter mAdapter;
    private ArrayList<VideoBean> listData = new ArrayList<VideoBean>();
    Gson gson = new Gson();
    int pageNum = 1;
    private long exitTime = 0;

    MaterialDialog logoutDialog = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xrecyclerview);
        StatusBarUtil.setTransparent(this);
        initActionBar();
        initRecyclerView();

        PreferencesUtils.putInt(XRecyclerviewActivity.this, "video", 0);

        logoutDialog = new MaterialDialog.Builder(this)
                .title("退出登录中")
                .content("请稍后")
                .progress(true, 0)
                .progressIndeterminateStyle(false).build();

    }

    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setLogo(R.mipmap.ic_policy);
        actionBar.setDisplayUseLogoEnabled(true);
    }

    private void initRecyclerView() {
        mRecyclerView = (XRecyclerView) this.findViewById(R.id.xrecyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setLoadingMoreEnabled(false);

        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
//        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallZigZagDeflect);
        mRecyclerView.setArrowImageView(R.drawable.xrecyclerview_pull_arrow2);

//        View header1 = LayoutInflater.from(this).inflate(R.layout.recyclerview_header, (ViewGroup) findViewById(android.R.id.content), false);
//        header1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ToastUtils.showToast(XRecyclerviewActivity.this, "点击了HEADER");
//            }
//        });
//        mRecyclerView.addHeaderView(header1);

        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                http2getVideoList(1, 50);
            }

            @Override
            public void onLoadMore() {

            }
        });

        mAdapter = new VideoAdapter(listData);

        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new VideoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putString("url", listData.get(position).getUrl());
                bundle.putString("title", listData.get(position).getName());

                int videoType = PreferencesUtils.getInt(XRecyclerviewActivity.this, "video");
                Intent intent = null;
                if (videoType != 0) {
                    intent = new Intent(XRecyclerviewActivity.this, VideoViewActivity.class);
                } else {
                    intent = new Intent(XRecyclerviewActivity.this, VlcPlayerActivity.class);
                }

                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        mRecyclerView.refresh();


    }

    //获取列表请求
    private void http2getVideoList(int pageNum, int pageSize) {
        OkHttpUtils.get()
                .url(RequestUrlConstants.URL_LIST_ITEM)
                .addParams("pageNum", pageNum + "")
                .addParams("pageSize", pageSize + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        L.e("请求错误:  " + e.toString());
                        ToastUtils.showToast(XRecyclerviewActivity.this, "请求错误");
                        mRecyclerView.refreshComplete();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        JSONObject jsonObject = null;
                        L.e(response);
                        try {
                            jsonObject = new JSONObject(response);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            ToastUtils.showToast(XRecyclerviewActivity.this, "数据格式错误");
                            L.e("数据格式错误:  " + response);
                        } finally {
                            mRecyclerView.refreshComplete();
                        }

                        int responseCode = jsonObject.optInt("code");
                        if (responseCode == 2000) {
                            String itemListString = jsonObject.optJSONObject("data").optString("list");
                            listData.clear();
                            ArrayList<VideoBean> list = gson.fromJson(itemListString, new TypeToken<ArrayList<VideoBean>>() {
                            }.getType());
                            listData.addAll(list);
                            mAdapter.notifyDataSetChanged();
                        } else {
                            ToastUtils.showToast(XRecyclerviewActivity.this, jsonObject.optString("message"));
                        }
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
//            case R.id.search:
//                break;
            case R.id.logout:
                new MaterialDialog.Builder(this)
                        .content(" 确认是否退出登录？")
                        .positiveText("确认")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                logoutDialog.show();
                                http2logout();
                            }
                        })
                        .negativeText("取消")
                        .show();
                break;
//            case R.id.setting:
//                new MaterialDialog.Builder(this)
//                        .title("播放器选择")
//                        .items(R.array.video_setting)
//                        .itemsCallbackSingleChoice(PreferencesUtils.getInt(XRecyclerviewActivity.this, "video"), new MaterialDialog.ListCallbackSingleChoice() {
//                            @Override
//                            public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
//
//                                PreferencesUtils.putInt(XRecyclerviewActivity.this, "video", which);
//                                return true;
//                            }
//                        })
//                        .positiveText("选择")
//                        .show();
//
//                break;
            default:
                break;
        }


        return true;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);

    }

    public void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            ToastUtils.showToast(XRecyclerviewActivity.this, "再按一次退出程序");
            exitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }

    public void http2logout() {

        OkHttpUtils.post()
                .url(RequestUrlConstants.URL_LOGOUT)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        logoutDialog.dismiss();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        logoutDialog.dismiss();
                        ResponseBean responseBean = null;
                        try {
                            responseBean = new ResponseBean(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            ToastUtils.showToast(XRecyclerviewActivity.this, "数据格式错误");
                        }
                        if (responseBean == null) {
                            return;
                        }
                        if (responseBean.getCode() == 2000) {

                            startActivity(new Intent(XRecyclerviewActivity.this, MainActivity.class));
                            finish();

                        } else {
                            ToastUtils.showToast(XRecyclerviewActivity.this, responseBean.getMessage());
                        }

                    }
                });

    }
}
