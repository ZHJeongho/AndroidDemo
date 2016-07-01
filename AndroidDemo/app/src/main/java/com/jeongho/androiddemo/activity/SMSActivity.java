package com.jeongho.androiddemo.activity;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.view.View;
import android.widget.TextView;

import com.jeongho.androiddemo.R;
import com.jeongho.androiddemo.base.BaseActivity;

/**
 * Created by Jeongho on 16/7/1.
 */
public class SMSActivity extends BaseActivity{
    private TextView mSenderTv;
    private TextView mContentTv;

    private IntentFilter mReceiveFilter;
    private SMSReceiver mReceiver;
    @Override
    public void initLayout() {
        setContentView(R.layout.activity_sms);
    }

    @Override
    public void initView() {
        mSenderTv = (TextView) findViewById(R.id.tv_sender);
        mContentTv = (TextView) findViewById(R.id.tv_content);
    }

    @Override
    public void initData() {
        mReceiveFilter = new IntentFilter();
        mReceiveFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        mReceiver = new SMSReceiver();
        registerReceiver(mReceiver, mReceiveFilter);
    }

    @Override
    public void onClick(View v) {

    }

    public static void startAction(Context context) {
        Intent intent = new Intent(context, SMSActivity.class);
        context.startActivity(intent);
    }


    class SMSReceiver extends BroadcastReceiver {
        @TargetApi(Build.VERSION_CODES.M)
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            Object[] pdus = (Object[]) bundle.get("pdus");
            SmsMessage[] messages = new SmsMessage[pdus.length];
            String format = intent.getStringExtra("format");
            for (int i = 0; i < messages.length; i++){
                messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i], format);
            }
            //获取号码
            String address = messages[0].getOriginatingAddress();
            String fullMessage = "";
            for(SmsMessage message : messages){
                fullMessage += message.getMessageBody();
            }

            mSenderTv.setText(address);
            mContentTv.setText(fullMessage);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }
}


