package com.kuyuntech.rtsp.rtspvideo;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kuyuntech.rtsp.rtspvideo.bean.VideoBean;

import java.util.ArrayList;

/**
 * Created by jianghejie on 15/11/26.
 */
public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> implements View.OnClickListener {

    private OnItemClickListener mOnItemClickListener = null;
    public ArrayList<VideoBean> datas = null;


    public VideoAdapter(ArrayList<VideoBean> datas) {
        this.datas = datas;
    }

    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.name.setText(datas.get(position).getName());
        viewHolder.brief.setText(datas.get(position).getBrief());
        viewHolder.itemView.setTag(position);
    }

    //获取数据的数量
    @Override
    public int getItemCount() {
        return datas.size();
    }

    @Override
    public void onClick(View view) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取position
            mOnItemClickListener.onItemClick(view,(int)view.getTag());
        }
    }
    //自定义的ViewHolder，持有每个Item的的所有界面元素


    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        public TextView name;
        public TextView brief;

        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view.findViewById(R.id.cardView);
            name = (TextView) view.findViewById(R.id.name);
            brief = (TextView) view.findViewById(R.id.brief);

        }
    }

    //define interface
    public static interface OnItemClickListener {
        void onItemClick(View view , int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}

