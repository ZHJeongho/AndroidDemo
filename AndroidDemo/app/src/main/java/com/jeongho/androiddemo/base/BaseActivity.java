package com.jeongho.androiddemo.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by Jeongho on 16/6/23.
 */
public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initLayout();
        initView();
        initData();
    }

    public abstract void initLayout();
    public abstract void initView();
    public abstract void initData();

}
