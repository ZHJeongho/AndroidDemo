package com.jeongho.androiddemo.activity;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.jeongho.androiddemo.R;
import com.jeongho.androiddemo.base.BaseActivity;
import com.jeongho.androiddemo.fragment.FirstFragment;
import com.jeongho.androiddemo.fragment.SecondFragment;
import com.jeongho.androiddemo.utils.SharedPreferencesUtil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends BaseActivity {

    private static final String FILE_KEY = "com.jeongho.androiddemo.PREFERENCE_FILE_KEY";

    private FragmentManager fm;
    private FirstFragment ff;

    private SharedPreferencesUtil mPreferencesUtil;

    private Button mChangeFragmentBtn;
    private Button mOutputBtn;
    private Button mInputBtn;

    private static final String TAG = "MainActivity";

    @Override
    public void initLayout() {
        setContentView(R.layout.activity_main);
    }

    @Override
    public void initView() {
        initFragment();

        mChangeFragmentBtn = (Button) findViewById(R.id.btn_change_fragment);
        mChangeFragmentBtn.setOnClickListener(this);
        mOutputBtn = (Button) findViewById(R.id.btn_output);
        mOutputBtn.setOnClickListener(this);
        mInputBtn = (Button) findViewById(R.id.btn_input);
        mInputBtn.setOnClickListener(this);

    }

    private void initFragment() {
        ff = new FirstFragment();
        fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.add(R.id.fl_content, ff);
        transaction.commit();
    }

    @Override
    public void initData() {
        mPreferencesUtil = new SharedPreferencesUtil(this, FILE_KEY);
        mPreferencesUtil.putInt("age", 18);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_change_fragment:
                FragmentTransaction transaction = fm.beginTransaction();
                SecondFragment sf = new SecondFragment();
                //        transaction.replace(R.id.fl_content, sf);
                //        transaction.commit();
                if (ff.isAdded()){
                    transaction.hide(ff).add(R.id.fl_content, sf).commit();
                }

//                Toast.makeText(this, mPreferencesUtil.getInt("age", 0) + "", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_output:
                saveFile();
                break;
            case R.id.btn_input:
                readFromFile();
        }
    }

    private void readFromFile() {
        FileInputStream inputStream = null;
        StringBuilder content = new StringBuilder();
        BufferedReader br = null;
        try {
            inputStream = openFileInput("myfile");
            br = new BufferedReader(new InputStreamReader(inputStream));
            String line ="";
            while ((line = br.readLine()) != null){
                content.append(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (br != null){
                Log.d("reader", "is not null");
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Toast.makeText(this, content.toString(), Toast.LENGTH_SHORT).show();
    }

    private void saveFile() {
        FileOutputStream out = null;
        String fileName = "myfile";
        String value = "this is content";
        BufferedWriter writer = null;

        try {
            out = openFileOutput(fileName, Context.MODE_PRIVATE);
//            out.write(value.getBytes());
//            out.close();

            //同上一样调用FileOutput的write
            //最终调用IoBridge
            writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.write(value);
            Toast.makeText(this, "save file success", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (writer != null){
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
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
}
