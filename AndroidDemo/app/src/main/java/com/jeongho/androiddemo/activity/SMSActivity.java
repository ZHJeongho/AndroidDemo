package com.jeongho.androiddemo.activity;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jeongho.androiddemo.R;
import com.jeongho.androiddemo.base.BaseActivity;
import com.jeongho.androiddemo.utils.ToastUtil;

/**
 * Created by Jeongho on 16/7/1.
 */
public class SMSActivity extends BaseActivity{
    private TextView mSenderTv;
    private TextView mContentTv;

    private EditText mToEdt;
    private EditText mContentEdt;
    private Button mSendMessageBtn;

    private IntentFilter mReceiveFilter;
    private SMSReceiver mReceiver;

    private IntentFilter mSendFilter;
    private SendStatusReceiver mSendStatusReceiver;

    @Override
    public void initLayout() {
        setContentView(R.layout.activity_sms);
    }

    @Override
    public void initView() {
        mSenderTv = (TextView) findViewById(R.id.tv_sender);
        mContentTv = (TextView) findViewById(R.id.tv_content);

        mToEdt = (EditText) findViewById(R.id.edt_to);
        mContentEdt = (EditText) findViewById(R.id.edt_content);

        mSendMessageBtn = (Button) findViewById(R.id.btn_send_message);
        mSendMessageBtn.setOnClickListener(this);
    }

    @Override
    public void initData() {
        mReceiveFilter = new IntentFilter();
        mReceiveFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        mReceiver = new SMSReceiver();
        registerReceiver(mReceiver, mReceiveFilter);

        mSendFilter = new IntentFilter();
        mSendFilter.addAction("SENT_SMS_ACTION");
        mSendStatusReceiver = new SendStatusReceiver();
        registerReceiver(mSendStatusReceiver, mSendFilter);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_send_message){
            sendMessage();
        }
    }

    private void sendMessage() {
        String receiver = mToEdt.getText().toString();
        String content = mContentEdt.getText().toString();

        Intent intent = new Intent("SENT_SMS_ACTION");
        PendingIntent sendIntent = PendingIntent.getBroadcast(SMSActivity.this, 0, intent, 0);
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(receiver, null, content, sendIntent, null);
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


    class SendStatusReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if (getResultCode() == RESULT_OK){
                ToastUtil.showShort(context, "Send succeeded!");
            }else {
                ToastUtil.showShort(context, "Send failed!");
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
        unregisterReceiver(mSendStatusReceiver);
    }

    public static void startAction(Context context) {
        Intent intent = new Intent(context, SMSActivity.class);
        context.startActivity(intent);
    }
}


