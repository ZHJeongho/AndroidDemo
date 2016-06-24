package com.jeongho.androiddemo.activity;

import android.view.View;
import android.widget.Button;

import com.jeongho.androiddemo.R;
import com.jeongho.androiddemo.base.BaseActivity;

/**
 * Created by Jeongho on 2016/6/24.
 */
public class DataBaseActivity extends BaseActivity{

    private Button mNewDatabaseBtn;
    @Override
    public void initLayout() {
        setContentView(R.layout.activity_database);
    }

    @Override
    public void initView() {
        mNewDatabaseBtn = (Button) findViewById(R.id.btn_new_db);
        mNewDatabaseBtn.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_new_db:
                break;
        }
    }
}
