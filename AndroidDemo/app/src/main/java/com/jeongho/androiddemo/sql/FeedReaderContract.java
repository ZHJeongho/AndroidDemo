package com.jeongho.androiddemo.sql;

import android.provider.BaseColumns;

/**
 * Created by Jeongho on 2016/6/24.
 */
public class FeedReaderContract {
    public FeedReaderContract() {
    }

    public static abstract class FeedEntry implements BaseColumns{
        public static final String TABLE_NAME = "entry";
        public static final String COLUMN_NAME_ENTRY_ID = "entryid";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_SUBTITLE = "subtitle";
    }
}
