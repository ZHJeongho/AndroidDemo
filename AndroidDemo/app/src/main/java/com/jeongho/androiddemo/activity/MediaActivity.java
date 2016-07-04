package com.jeongho.androiddemo.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.jeongho.androiddemo.R;
import com.jeongho.androiddemo.base.BaseActivity;

/**
 * Created by Jeongho on 2016/7/4.
 */
public class MediaActivity extends BaseActivity{

    private Button mPlayBtn;
    private Button mPauseBtn;
    private Button mStopBtn;

    private RecyclerView mAudioRv;

    @Override
    public void initLayout() {
        setContentView(R.layout.activity_media);
    }

    @Override
    public void initView() {
        mPlayBtn = (Button) findViewById(R.id.btn_play_media);
        mPlayBtn.setOnClickListener(this);
        mPauseBtn = (Button) findViewById(R.id.btn_pause_media);
        mPauseBtn.setOnClickListener(this);
        mStopBtn = (Button) findViewById(R.id.btn_stop_media);
        mStopBtn.setOnClickListener(this);

        mAudioRv = (RecyclerView) findViewById(R.id.rv_audio);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_play_media:
                break;
            case R.id.btn_pause_media:
                break;
            case R.id.btn_stop_media:
                break;
        }
    }

    public static void startAction(Context context) {
        Intent intent = new Intent(context, MediaActivity.class);
        context.startActivity(intent);
    }
}
