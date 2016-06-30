package com.jeongho.androiddemo.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.jeongho.androiddemo.R;
import com.jeongho.androiddemo.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeongho on 2016/6/30.
 */
public class ContactActivity extends BaseActivity{

    private ListView mContactLv;
    private List<String> mContactList;
    private ArrayAdapter<String> mAdapter;

    @Override
    public void initLayout() {
        setContentView(R.layout.activity_contact);
    }

    @Override
    public void initView() {
        mContactLv = (ListView) findViewById(R.id.lv_contact);
    }

    @Override
    public void initData() {
        mContactList = new ArrayList<>();
        readContacts();
    }

    private void readContacts() {
        Cursor cursor = null;
        cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null, null, null, null);
        try{
            while (cursor.moveToNext()){
                String name = cursor.getString(
                        cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String nunber = cursor.getString(
                        cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                mContactList.add("昵称：" + name + "\n号码：" + nunber);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (cursor != null){
                cursor.close();
            }
        }
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mContactList);
        mContactLv.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {

    }

    public static void startAction(Context context) {
        Intent intent = new Intent(context, ContactActivity.class);
        context.startActivity(intent);
    }
}
