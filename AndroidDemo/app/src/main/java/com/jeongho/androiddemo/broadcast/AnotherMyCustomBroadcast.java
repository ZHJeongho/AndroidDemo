package com.jeongho.androiddemo.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.jeongho.androiddemo.utils.ToastUtil;

/**
 * Created by Jeongho on 2016/6/27.
 */
public class AnotherMyCustomBroadcast extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        ToastUtil.showShort(context, "AnotherMyCustomBroadcast");
        abortBroadcast();
    }
}
