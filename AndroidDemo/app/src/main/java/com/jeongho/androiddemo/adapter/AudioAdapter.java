package com.jeongho.androiddemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jeongho.androiddemo.R;
import com.jeongho.androiddemo.bean.AudioBean;

import java.util.List;

/**
 * Created by Jeongho on 2016/7/4.
 */
public class AudioAdapter extends RecyclerView.Adapter<AudioAdapter.AudioHolder>{
    private Context mContext;
    private List<AudioBean> mAudioBeans;

    private OnItemClickListener mOnItemClickListener;

    public AudioAdapter(Context context, List<AudioBean> audioBeans) {
        mContext = context;
        mAudioBeans = audioBeans;
    }

    @Override
    public AudioHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_audio, parent, false);
        return new AudioHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final AudioHolder holder, int position) {
        //绑定数据
        //holder.mAudioIDTv.setText(mAudioBeans.get(position).getId());
        holder.mAudioNameTv.setText(mAudioBeans.get(position).getTitle());

        if (mOnItemClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(v, position);
                }
            });
        }
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

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }
}
