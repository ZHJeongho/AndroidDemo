package com.jeongho.androiddemo.activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jeongho.androiddemo.R;
import com.jeongho.androiddemo.base.BaseActivity;
import com.jeongho.androiddemo.sql.FeedReaderContract;
import com.jeongho.androiddemo.sql.FeedReaderDBHelper;

/**
 * Created by Jeongho on 2016/6/24.
 */
public class DataBaseActivity extends BaseActivity {

    private static final String TAG = "DataBaseActivity";

    private Button mNewDatabaseBtn;
    private Button mInsertDataBtn;
    private Button mDeleteDataBtn;
    private Button mUpdateDataBtn;
    private Button mQueryDataBtn;
    private TextView mContentTv;

    private FeedReaderDBHelper mDBHelper;
    private SQLiteDatabase mDatabase;

    @Override
    public void initLayout() {
        setContentView(R.layout.activity_database);
    }

    @Override
    public void initView() {
        mNewDatabaseBtn = (Button) findViewById(R.id.btn_new_db);
        mNewDatabaseBtn.setOnClickListener(this);
        mInsertDataBtn = (Button) findViewById(R.id.btn_insert_data);
        mInsertDataBtn.setOnClickListener(this);
        mDeleteDataBtn = (Button) findViewById(R.id.btn_delete_data);
        mDeleteDataBtn.setOnClickListener(this);
        mUpdateDataBtn = (Button) findViewById(R.id.btn_update_data);
        mUpdateDataBtn.setOnClickListener(this);
        mQueryDataBtn = (Button) findViewById(R.id.btn_query_data);
        mQueryDataBtn.setOnClickListener(this);

        mContentTv = (TextView) findViewById(R.id.tv_content);
    }

    @Override
    public void initData() {
        mDBHelper = new FeedReaderDBHelper(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_new_db:
                mDatabase = mDBHelper.getWritableDatabase();
                break;
            case R.id.btn_insert_data:
                if (mDatabase == null) {
                    Log.d(TAG, "mDatabase null");
                    mDatabase = mDBHelper.getWritableDatabase();
                }
                String id = "89757";
                String title = "编号89757";
                String subTitle = "JJ林俊杰";
                insertData(id, title, subTitle);
                break;
            case R.id.btn_delete_data:
                if (mDatabase == null) {
                    mDatabase = mDBHelper.getWritableDatabase();
                }
                deleteData(FeedReaderContract.FeedEntry.COLUMN_NAME_ENTRY_ID + " = ?",
                        new String[]{"89757"});
                break;
            case R.id.btn_update_data:
                if (mDatabase == null) {
                    mDatabase = mDBHelper.getWritableDatabase();
                }
                updateData();
                break;
            case R.id.btn_query_data:
                if (mDatabase == null) {
                    mDatabase = mDBHelper.getWritableDatabase();
                }
                queryData();
                break;
        }
    }

    private void updateData() {
        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE, "Fade");
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE, "歪果仁");
        mDatabase.update(FeedReaderContract.FeedEntry.TABLE_NAME, values,
                FeedReaderContract.FeedEntry.COLUMN_NAME_ENTRY_ID + " = ?",
                new String[]{"1"});
    }

    private void queryData() {
        Cursor cursor = mDatabase.query(FeedReaderContract.FeedEntry.TABLE_NAME,
                null, null, null, null, null, null);
        StringBuilder stringBuilder = new StringBuilder();
        if (cursor.moveToFirst()){
            do {
                String id = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_ENTRY_ID));
                String title = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE));
                String subTitle = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE));
                stringBuilder.append("id:" + id + "\n");
                stringBuilder.append("title:" + title + "\n");
                stringBuilder.append("subTitle:" + subTitle + "\n");

            }while (cursor.moveToNext());
        }
        cursor.close();
        mContentTv.setText(stringBuilder);
    }

    private void deleteData(String selection, String[] selectionArgs) {
        mDatabase.delete(FeedReaderContract.FeedEntry.TABLE_NAME,
                FeedReaderContract.FeedEntry.COLUMN_NAME_ENTRY_ID + " = ?",
                new String[]{"1"});
    }

    private void insertData(String id, String title, String subTitle) {
        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_ENTRY_ID, id);
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE, id);
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE, subTitle);
        long newRow = 0;
        newRow = mDatabase.insert(FeedReaderContract.FeedEntry.TABLE_NAME, null, values);
        Log.d(TAG, "insert in " + newRow + " row");
        //使用sql语句插入
        mDatabase.execSQL("INSERT INTO " + FeedReaderContract.FeedEntry.TABLE_NAME + " VALUES(" +
                "'1', '绅士', '薛之谦')");
    }

    private void transaction(){
        mDatabase.beginTransaction();
        try{
            String id = "89757";
            String title = "编号89757";
            String subTitle = "JJ林俊杰";
            insertData(id, title, subTitle);
            deleteData(FeedReaderContract.FeedEntry.COLUMN_NAME_ENTRY_ID + " = ?",
                    new String[]{"89757"});
            queryData();
            mDatabase.setTransactionSuccessful();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            mDatabase.endTransaction();
        }
    }

    public static void startAction(Context context){
        Intent intent = new Intent(context, DataBaseActivity.class);
        context.startActivity(intent);
    }
}
