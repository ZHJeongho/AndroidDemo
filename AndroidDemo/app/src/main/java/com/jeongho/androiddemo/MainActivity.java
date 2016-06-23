package com.jeongho.androiddemo;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.jeongho.androiddemo.fragment.FirstFragment;
import com.jeongho.androiddemo.fragment.SecondFragment;

public class MainActivity extends AppCompatActivity {

    private FragmentManager fm;
    private FirstFragment ff;
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_main);

        ff = new FirstFragment();
        fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.add(R.id.fl_content, ff);
        transaction.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");


    }

    public void changeFragment(View v){
        FragmentTransaction transaction = fm.beginTransaction();
        SecondFragment sf = new SecondFragment();

//        transaction.replace(R.id.fl_content, sf);
//        transaction.commit();

        if (sf.isAdded()){
            transaction.hide(ff).show(sf);
            transaction.addToBackStack(null);
            transaction.commit();
        }else {
            transaction.hide(ff).add(R.id.fl_content, sf);
            transaction.addToBackStack(null);
            transaction.commit();
        }

    }
}
