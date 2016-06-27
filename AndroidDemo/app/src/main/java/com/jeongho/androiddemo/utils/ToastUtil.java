package com.jeongho.androiddemo.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Jeongho on 2016/6/27.
 */
public class ToastUtil {
    public static void showShort(Context context, String value){
        Toast.makeText(context, value, Toast.LENGTH_SHORT).show();
    }

    public static void showLong(Context context, String value){
        Toast.makeText(context, value, Toast.LENGTH_LONG).show();
    }
}
