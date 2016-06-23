package com.jeongho.androiddemo.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Jeongho on 16/6/23.
 */
public class SharedPreferencesUtil {

    private SharedPreferences mSharedPreferences;

    private SharedPreferences.Editor mEditor;

    public SharedPreferencesUtil(Context context, String name){
        mSharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
    }

    public SharedPreferences getSharedPreferences(){
        return mSharedPreferences;
    }

    public SharedPreferences.Editor getEditor(){
        return mEditor;
    }

    /**
     * 存储数据（int）
     * @param key
     * @param value
     */
    public void putInt(String key, int value){
        mEditor.putInt(key, value).commit();
    }

    /**
     * 存储数据（String）
     * @param key
     * @param value
     */
    public void putString(String key, String value){
        mEditor.putString(key, value).commit();
    }
    /**
     * 存储数据（Boolean）
     * @param key
     * @param value
     */
    public void putBoolean(String key, boolean value){
        mEditor.putBoolean(key, value).commit();
    }
    /**
     * 存储数据（Float）
     * @param key
     * @param value
     */
    public void putFloat(String key, Float value){
        mEditor.putFloat(key, value).commit();
    }
    /**
     * 存储数据（Long）
     * @param key
     * @param value
     */
    public void putLong(String key, Long value){
        mEditor.putLong(key, value).commit();
    }

    /**
     * 获取数据（int）
     * @param key
     * @param defValue
     * @return
     */
    public int getInt(String key, int defValue){
        return mSharedPreferences.getInt(key, defValue);
    }

    /**
     * 获取数据（String）
     * @param key
     * @param defValue
     * @return
     */
    public String getString(String key, String defValue){
        return mSharedPreferences.getString(key, defValue);
    }

    /**
     * 获取数据（Boolean）
     * @param key
     * @param defValue
     * @return
     */
    public boolean getBoolean(String key, boolean defValue){
        return mSharedPreferences.getBoolean(key, defValue);
    }

    /**
     * 获取数据（Float）
     * @param key
     * @param defValue
     * @return
     */
    public float getFloat(String key, float defValue){
        return mSharedPreferences.getFloat(key, defValue);
    }

    /**
     * 获取数据（Long）
     * @param key
     * @param defValue
     * @return
     */
    public long getLong(String key, long defValue){
        return mSharedPreferences.getLong(key, defValue);
    }

    /**
     * 全部清除
     */
    public void clear(){
        mEditor.clear().commit();
    }

    /**
     * 移除某个key对应的数据
     * @param key
     */
    public void remove(String key){
        mEditor.remove(key).commit();
    }
}
