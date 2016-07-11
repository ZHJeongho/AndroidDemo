package com.jeongho.androiddemo.utils;

import android.util.Log;

/**
 * Created by Jeongho on 16/7/11.
 */
public class LogUtil {

    private static final int VERBOSE = 1;
    private static final int DEBUG = 2;
    private static final int INFO = 1;
    private static final int WARN = 1;
    private static final int ERROR = 1;
    private static final int NOTHING = 1;

    private static final int LEVEL = VERBOSE;

    public static void v(String tag, String value){
        if (LEVEL <= VERBOSE){
            Log.v(tag, value);
        }
    }

    public static void d(String tag, String value){
        if (LEVEL <= DEBUG){
            Log.d(tag, value);
        }
    }

    public static void i(String tag, String value){
        if (LEVEL <= INFO){
            Log.i(tag, value);
        }
    }

    public static void w(String tag, String value){
        if (LEVEL <= WARN){
            Log.w(tag, value);
        }
    }

    public static void e(String tag, String value){
        if (LEVEL <= ERROR){
            Log.e(tag, value);
        }
    }
}
