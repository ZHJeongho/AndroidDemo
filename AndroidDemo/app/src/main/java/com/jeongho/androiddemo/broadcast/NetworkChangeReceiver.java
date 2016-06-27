package com.jeongho.androiddemo.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.jeongho.androiddemo.utils.ToastUtil;

/**
 * Created by Jeongho on 2016/6/27.
 */
public class NetworkChangeReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {

        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isAvailable()){
            ToastUtil.showShort(context, "network is available");
        }else {
            ToastUtil.showShort(context, "network is unavailable");
        }
    }
}
