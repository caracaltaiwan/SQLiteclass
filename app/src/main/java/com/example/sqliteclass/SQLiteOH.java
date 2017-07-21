package com.example.sqliteclass;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 重夢 on 2017/7/21.
 */

public class SQLiteOH extends SQLiteOpenHelper {

    //<editor-fold desc="變數名稱">
    //資料庫名稱
    public static final String DATABASE_NAME = "mydata.db";
    // 資料庫版本，資料結構改變的時候要更改這個數字，通常是加一
    public static final int VERSION = 1;
    // 資料庫物件，固定的欄位變數
    public static SQLiteDatabase database;
    //資料庫表單
    String[]
            TABLE_NAME = {"Student_Info","Contact"};
    //</editor-fold>

    public SQLiteOH(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    // 需要資料庫的元件呼叫這個方法，這個方法在一般的應用都不需要修改
    public static SQLiteDatabase getDatabase(Context context) {
        if (database == null || !database.isOpen()) {//db = openOrCreateDatabase(DB_NAME,MODE_PRIVATE,null);//另一種宣告方式
            database = new SQLiteOH(context, DATABASE_NAME, null, VERSION).getWritableDatabase();
        }
        return database;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 建立應用程式需要的表格/*SQLiteOH是用getDatabase的方式 呼叫建構子的 又因為database的建構子寫在getDatabase 導致onCreate不能寫*/
        //database.execSQL(SQLite.CREATE_TABLE);
        //database.execSQL("Insert Into Student_Info values('''Hello,''');");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 刪除原有的表格
        // 待會再回來完成它
        for (int i = 0; i < TABLE_NAME.length; i++) {
            database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME[i]);
        }
        // 呼叫onCreate建立新版的表格
        onCreate(db);
    }


}
