package com.jeongho.androiddemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jeongho.androiddemo.R;
import com.jeongho.androiddemo.bean.AudioBean;

import java.util.LinkedList;

/**
 * Created by Jeongho on 2016/7/4.
 */
public class AudioAdapter extends RecyclerView.Adapter<AudioAdapter.AudioHolder>{
    private Context mContext;
    private LinkedList<AudioBean> mAudioBeans;
    public AudioAdapter(Context context, LinkedList<AudioBean> audioBeans) {
        mContext = context;
        mAudioBeans = audioBeans;
    }

    @Override
    public AudioHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_audio, parent, false);
        return new AudioHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AudioHolder holder, int position) {
        //绑定数据
//        holder.mAudioIDTv
    }

    @Override
    public int getItemCount() {
        return mAudioBeans.size();
    }

    public class AudioHolder extends RecyclerView.ViewHolder{
        //控件绑定
        private TextView mAudioIDTv;
        private TextView mAudioNameTv;
        public AudioHolder(View itemView) {
            super(itemView);
            mAudioIDTv = (TextView) itemView.findViewById(R.id.tv_audio_id);
            mAudioNameTv = (TextView) itemView.findViewById(R.id.tv_audio_name);
        }
    }
}
