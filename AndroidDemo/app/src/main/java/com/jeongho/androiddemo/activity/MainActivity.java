package com.jeongho.androiddemo.activity;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.jeongho.androiddemo.R;
import com.jeongho.androiddemo.base.BaseActivity;
import com.jeongho.androiddemo.broadcast.LocalReceiver;
import com.jeongho.androiddemo.broadcast.NetworkChangeReceiver;
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
    private Button mDBTestBtn;
    private Button mContactBtn;

    private Button mRegisterBroadcastBtn;
    private Button mNormalBroadcastBtn;
    private Button mOrderedBroadcastBtn;
    private Button mLocalBroadcastBtn;
    private Button mShowNotificationBtn;

    private FloatingActionButton mSMSFab;

    private NetworkChangeReceiver mNetworkChangeReceiver;

    private LocalReceiver mLocalReceiver;
    private LocalBroadcastManager mLocalBroadcastManager;
    private IntentFilter mLocalBroadcastFilter;

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
        mDBTestBtn = (Button) findViewById(R.id.btn_db_test);
        mDBTestBtn.setOnClickListener(this);
        mRegisterBroadcastBtn = (Button) findViewById(R.id.btn_register_broadcast);
        mRegisterBroadcastBtn.setOnClickListener(this);
        mNormalBroadcastBtn = (Button) findViewById(R.id.btn_normal_broadcast);
        mNormalBroadcastBtn.setOnClickListener(this);
        mOrderedBroadcastBtn = (Button) findViewById(R.id.btn_ordered_broadcast);
        mOrderedBroadcastBtn.setOnClickListener(this);
        mLocalBroadcastBtn = (Button) findViewById(R.id.btn_local_broadcast);
        mLocalBroadcastBtn.setOnClickListener(this);
        mContactBtn = (Button) findViewById(R.id.btn_contact);
        mContactBtn.setOnClickListener(this);
        mShowNotificationBtn = (Button) findViewById(R.id.btn_show_notification);
        mShowNotificationBtn.setOnClickListener(this);

        mSMSFab = (FloatingActionButton) findViewById(R.id.fab_sms);
        mSMSFab.setOnClickListener(this);

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

        mLocalReceiver = new LocalReceiver();
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
        mLocalBroadcastFilter = new IntentFilter();
        mLocalBroadcastFilter.addAction("com.jeongho.androiddemo.localBroadcast");
        mLocalBroadcastManager.registerReceiver(mLocalReceiver, mLocalBroadcastFilter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_change_fragment:
                FragmentTransaction transaction = fm.beginTransaction();
                SecondFragment sf = new SecondFragment();
                //        transaction.replace(R.id.fl_content, sf);
                //        transaction.commit();
                if (sf.isAdded()) {
                    transaction.hide(ff).show(sf);
                    transaction.addToBackStack(null);
                    transaction.commit();
                } else {
                    transaction.hide(ff).add(R.id.fl_content, sf);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }

                //                Toast.makeText(this, mPreferencesUtil.getInt("age", 0) + "", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_output:
                saveFile();
                break;
            case R.id.btn_input:
                readFromFile();
                break;
            case R.id.btn_db_test:
                DataBaseActivity.startAction(this);
                break;
            case R.id.btn_register_broadcast:
                registerNetworkBroadcast();
                break;
            case R.id.btn_normal_broadcast:
                sendNormalCustomBroadcast();
                break;
            case R.id.btn_ordered_broadcast:
                sendOrderedlCustomBroadcast();
                break;
            case R.id.btn_local_broadcast:
                sendLocalBroadcast();
                break;
            case R.id.btn_contact:
                ContactActivity.startAction(this);
                break;
            case R.id.btn_show_notification:
                showNotification();
                break;
            case R.id.fab_sms:
                SMSActivity.startAction(this);
                break;
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void showNotification() {
        //设置点击intent
        Intent resultIntent = new Intent(this, ContactActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), resultIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        //设置提示音
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        //设置样式  通知栏抽屉样式设置
        Notification.Style style = new Notification.BigTextStyle();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(this);
        Notification notification = builder.setContentTitle("This is title")
                .setContentText("This is Text")
                .setSubText("this is sub text")
                .setTicker("this is ticker")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .setSound(alarmSound)
                .setStyle(style)
                .build();
//        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.custom_notification);
//        builder.setContent(remoteViews)
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setContentIntent(pendingIntent);
//        Notification notification = builder.build();
        notificationManager.notify(1, notification);
    }

    /**
     * 发送本地广播
     */
    private void sendLocalBroadcast() {
        Intent intent = new Intent("com.jeongho.androiddemo.localBroadcast");
        mLocalBroadcastManager.sendBroadcast(intent);
    }

    /**
     * 发送有序自定义广播
     */
    private void sendOrderedlCustomBroadcast() {
        Intent intent = new Intent("com.jeongho.androiddemo.myCustomBroadcast");
        sendOrderedBroadcast(intent, null);
    }

    /**
     * 发送正常自定义广播
     */
    private void sendNormalCustomBroadcast() {
        Intent intent = new Intent("com.jeongho.androiddemo.myCustomBroadcast");
        sendBroadcast(intent);
    }

    /**
     * 注册网络连接广播
     */
    private void registerNetworkBroadcast() {
        if (mNetworkChangeReceiver == null) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
            mNetworkChangeReceiver = new NetworkChangeReceiver();
            registerReceiver(mNetworkChangeReceiver, intentFilter);
        }
    }

    /**
     * 从文件读数据
     */
    private void readFromFile() {
        FileInputStream inputStream = null;
        StringBuilder content = new StringBuilder();
        BufferedReader br = null;
        try {
            inputStream = openFileInput("myfile");
            br = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while ((line = br.readLine()) != null) {
                content.append(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
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

    /**
     * 存文件
     */
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
        } finally {
            if (writer != null) {
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mNetworkChangeReceiver != null) {
            unregisterReceiver(mNetworkChangeReceiver);
        }

        if (mLocalReceiver != null) {
            mLocalBroadcastManager.unregisterReceiver(mLocalReceiver);
        }
    }
}
