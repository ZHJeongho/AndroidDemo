package com.jeongho.androiddemo.utils;

import android.os.Environment;

/**
 * Created by Jeongho on 2016/6/24.
 */
public class FileUtil {
    public static boolean isExternalStorageWritable(){
        String state = Environment.getExternalStorageState();
        //读写权限都有
        if (Environment.MEDIA_MOUNTED.equals(state)){
            return true;
        }else {
            return false;
        }
    }

    public static boolean isExternalStorageReadable(){
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)
                ||Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)){
            return true;
        }else {
            return false;
        }
    }
}
