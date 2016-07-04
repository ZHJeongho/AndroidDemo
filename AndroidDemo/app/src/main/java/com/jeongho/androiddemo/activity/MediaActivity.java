package com.jeongho.androiddemo.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.jeongho.androiddemo.R;
import com.jeongho.androiddemo.adapter.AudioAdapter;
import com.jeongho.androiddemo.base.BaseActivity;
import com.jeongho.androiddemo.bean.AudioBean;
import com.jeongho.androiddemo.utils.ToastUtil;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Jeongho on 2016/7/4.
 */
public class MediaActivity extends BaseActivity implements AudioAdapter.OnItemClickListener {

    private Button mPlayBtn;
    private Button mPauseBtn;
    private Button mStopBtn;

    private RecyclerView mAudioRv;
    private RecyclerView.LayoutManager mLayoutManager;
    private AudioAdapter mAudioAdapter;

    private List<AudioBean> mAudioBeanList;

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
        mAudioBeanList = new LinkedList<>();

        Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
        if (cursor.moveToFirst()){
            do{
                AudioBean bean = new AudioBean();
                int id = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                bean.setId(id);
                bean.setTitle(title);
                bean.setAlbum(album);
                bean.setDuration(duration);
                mAudioBeanList.add(bean);
            }while (cursor.moveToNext());
            cursor.close();
        }

        Log.d("list length", mAudioBeanList.size() + "");
        mLayoutManager = new LinearLayoutManager(this);
        mAudioRv.setLayoutManager(mLayoutManager);
        mAudioAdapter = new AudioAdapter(this, mAudioBeanList);
        mAudioRv.setAdapter(mAudioAdapter);
        mAudioAdapter.setOnItemClickListener(this);
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

    @Override
    public void onItemClick(View view, int position) {
        ToastUtil.showShort(this, mAudioBeanList.get(position).getTitle());
    }
}
