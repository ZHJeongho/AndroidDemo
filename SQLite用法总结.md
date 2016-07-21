# SQLite基本用法
---

SQLite是Android系统上的轻量级数据库。（当然在.NET中也是可以引用的）

SQlite与SQL Server、MySQL一样，都是可以根据SQL语句来执行最基本的增删改查的。但是并不是每个开发Android的攻城狮都会使用SQL语句执行数据库操作，所以Google也专门为SQLite定制了API供不熟悉SQL语句的程序员使用。

## 新建数据库

### 新建数据库步骤：

- **实现SQLiteOpenHelper，实现OnCreat()，OnUpdate()方法**

>顾名思义OnCreat()是创建数据库，OnUpdate()是更新数据库。

不多说，上代码~

```
public class FeedReaderDBHelper extends SQLiteOpenHelper {

    private static final String TAG = "FeedReaderDBHelper";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "FeedReader.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FeedReaderContract.FeedEntry.TABLE_NAME + " (" +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_ENTRY_ID + " INTEGER PRIMARY KEY" + COMMA_SEP +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE + TEXT_TYPE + ")";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedReaderContract.FeedEntry.TABLE_NAME;

    public FeedReaderDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
        Log.d(TAG, "onCreate");
    }

    /**
     * 升级DB版本
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    /**
     * 降低DB版本
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}

```
虽然现实了这两个方法，得调用OnCreate()方法，才能新建Table~ 这时就用到第二个步骤。

- **获取SQLiteOpenHelper实例，调用它的getReadableDatabase()或getWritableDatabase()方法**

>当磁盘空间已经满的时候，数据库也就无法写入。此时，调用getWritableDatabase()来获取DataBase对象，就会抛出异常。
>
>而同样调用getReadableDatabase()方法来获取只读的DataBase对象

数据库存放的位置/data/data/<package name>/databases/目录下

```
mDBHelper = new FeedReaderDBHelper(this);
mDatabase = mDBHelper.getWritableDatabase();
```

## 升级数据库

升级数据库需要执行OnUpdate()方法，如何执行该方法呢？

SQLiteOpenHelper的构造函数

```
    public FeedReaderDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
```
只需要传入的第四个参数的值大于现在数据库的版本值，就是执行OnUpdate()方法。

升级数据库一般都是删除掉原有的表，再执行OnCreate()方法新建升级之后的表。

```
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
        db.execSQL(SQL_CREATE_TABLE);
        ...
        Log.d(TAG, "onCreate");
    }

    /**
     * 升级DB版本
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
```

>所有新建的表单都需要在OnCreate()方法中重新建表。

## 添加数据

1. 实例化一个ContentValue对象，使用其putXXX()方法对每个字段赋值
2. 调用database的insert()方法将初始化好的ContentValue对象插入到相应的表中

先看一个insert()方法：

```
	//第一个参数：表名
	//第二个参数：未指定添加数据的并且可以为空的列自动赋值为NULL，一般都填null
	//第三个参数：一个ContentValue对象
    public long insert(String table, String nullColumnHack, ContentValues values) {
        try {
            return insertWithOnConflict(table, nullColumnHack, values, CONFLICT_NONE);
        } catch (SQLException e) {
            Log.e(TAG, "Error inserting " + values, e);
            return -1;
        }
    }
```
>insert方法返回的新插入行的id，发生错误的时候返回-1

熟悉SQL语句的，也可以使用database的execSQL(String sql)直接将要执行的SQL语句传入

```
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

```

## 更新数据

调用database的update()方法

```
	//第一个参数：表名
	//第二个参数：ContentValue对象，设置
	//第三个参数：相对于sql语句中的where条件，例如id = ？
	//第四个参数：where条件中的值，例如id = 5中的5
	//更新id=5的数据
    public int update(String table, ContentValues values, String whereClause, String[] whereArgs) {
        return updateWithOnConflict(table, values, whereClause, whereArgs, CONFLICT_NONE);
    }
```
例子：

```
    private void updateData() {
        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE, "Fade");
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE, "歪果仁");
        mDatabase.update(FeedReaderContract.FeedEntry.TABLE_NAME, values,
                FeedReaderContract.FeedEntry.COLUMN_NAME_ENTRY_ID + " = ?",
                new String[]{"1"});
    }
```

## 删除数据

与插入、更新是大同小异，调用database的delete()方法

```
	//第一个参数：表名
	//第二个参数：相对于sql语句中的where条件，例如id > ？
	//第三个参数：where条件中的值，例如id > 5中的5
	//删除id为5的数据
    public int delete(String table, String whereClause, String[] whereArgs) {
        acquireReference();
        try {
            SQLiteStatement statement =  new SQLiteStatement(this, "DELETE FROM " + table +
                    (!TextUtils.isEmpty(whereClause) ? " WHERE " + whereClause : ""), whereArgs);
            try {
                return statement.executeUpdateDelete();
            } finally {
                statement.close();
            }
        } finally {
            releaseReference();
        }
    }
```
例子，删除id = 1的数据。

```
    private void deleteData(String selection, String[] selectionArgs) {
        mDatabase.delete(FeedReaderContract.FeedEntry.TABLE_NAME,
                FeedReaderContract.FeedEntry.COLUMN_NAME_ENTRY_ID + " = ?",
                new String[]{"1"});
    }
```

## 查询数据

```
	//表名
	//列名
	//约束条件 where条件
	//where中具体字段对应的值
	//指定需要groupby的列名
	//groupby的进一步约束
	//排序方式
	//
    public Cursor query(String table, String[] columns, String selection,
            String[] selectionArgs, String groupBy, String having,
            String orderBy) {

        return query(false, table, columns, selection, selectionArgs, groupBy,
                having, orderBy, null /* limit */);
    }
```

query方法返回的是一个Cursor对象（游标，光标），使用这个光标一行一行的读取数据

1. 先调用moveToFirst()来使他指向第一行
2. 再调用cursor.getColumnIndex（String columnName)来得到你想找的列的index
3. 再调用cursor.getXXX（int columnIndex）把上面得到的列的index传进来，就可以获取这个列的值
4. 循环调用moveToNext()来重复2 3步骤，依次取出结果集的所有数据
5. 最后cursor.close()，关闭Cursor。

```
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
```

## 事务

数据库的一些操作要保证是一起完成的，就好比银行转账。例如：A转给B，A的余额减少的同时，B增加。如果A减少完，B再增加，可能因为一些异常，导致A的钱转出了，而B并没有增加。。。

这时就需要事务，开启一个事务，可以执行增删改查一系列操作，最后关闭事务。

```
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
            //表示事务执行成功
            mDatabase.setTransactionSuccessful();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
        	//结束事务
            mDatabase.endTransaction();
        }
    }

```
