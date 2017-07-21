package com.example.sqliteclass;

import android.content.ContentResolver;
import android.content.Context;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;

/**
 * Created by 重夢 on 2017/7/21.
 */

public class SQLite {
    //<editor-fold desc="變數名稱">
    //資料庫表單名稱
    public static String[]
            TABLE_NAME = {"AA"};

    //表單欄位名稱
    public static final String
            TABLE_COLUMN_FIRST = "_ID",
            TABLE_COLUMN_SECOND = "NAME",
            TABLE_COLUMN_THIRD = "PHONE_NUMBER";

    // 使用上面宣告的變數建立表格的SQL指令
    /** SQL語法守則
     * 1.永遠記得頭尾
     * 2.永遠記得類型前後加空格
     * 3.永遠記得分行*/
    public static String
            CREATE_TABLE =
            "CREATE TABLE " +
                    TABLE_NAME[0] + " ( "+//頭
                    TABLE_COLUMN_FIRST +" INTEGER PRIMARY KEY autoincrement, "/*<-我是類型*/+
                    TABLE_COLUMN_SECOND +" CHAR(20), "+
                    TABLE_COLUMN_THIRD +" CHAR(20) "+
                    ");";//尾
    // 資料庫物件
    static SQLiteDatabase db;

    //</editor-fold>

    // 建構子，一般的應用都不需要修改
    public SQLite(Context context, SQLiteOH sqLiteOH){
        db = sqLiteOH.getDatabase(context);
    }

    // 關閉資料庫，一般的應用都不需要修改
    public void close(){
        db.close();
    }

    //創建表單
    public boolean createTable(String... table_name){/*不安全*/
        int table_number = 0;//從零開始計算
        TABLE_NAME = table_name;//將表單名稱匯入--可擴展成檢查有無惡意符號
        String sql;
        try {
            for (table_number = 0; table_number < table_name.length; table_number++) {
                rewrite(TABLE_NAME[table_number]);//呼叫重寫SQL的指令
                sql = CREATE_TABLE;//↑已經將默認的表單指令(CREATE_TALE)改寫
                db.execSQL(sql);//↑執行上述指令
            }
            insertTable("AA",""+TABLE_NAME[0]+","+TABLE_NAME[1]);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //重置表單
    public boolean dropTable(String... strings){//重置特定的table
        int table_number = 0;
        String sql = "";
        try {
            for (int i = 0; i < strings.length; i++) {
                for (table_number = 0; table_number < TABLE_NAME.length; table_number++) {
                    if(strings[i].equals(TABLE_NAME[table_number])){
                        sql = "drop TABLE "+TABLE_NAME[table_number];
                        db.execSQL(sql);
                    }
                }
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean dropTable(){//重置所有的table
        int table_number = 0;
        String sql = "";
        try {
            for (table_number = 0; table_number < TABLE_NAME.length; table_number++) {
                sql = "drop TABLE "+TABLE_NAME[table_number];
                db.execSQL(sql);
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 新增參數指定的物件
    public boolean insertTable(String Table_name,String... database_name) {/*不安全*/
        int table_number = 0;
        String sql = "";
        try {
            for (int i = 0; i < TABLE_NAME.length; i++) {
                if(Table_name.equals(TABLE_NAME[i])){
                    sql = "insert into " + TABLE_NAME[i] + " values(null,'" + database_name[0] + "','TestData')";
                    //break;
                }
            }
            db.execSQL(sql);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public boolean insertTable(int table_number,String database_name,String database_phone) {/*不安全*/
        String sql = "";
        try {
            sql = "insert into " + TABLE_NAME[table_number] + " values(null,'" + database_name + "','" + database_phone +"')";
            db.execSQL(sql);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    //呼叫重寫SQL的指令
    private static void rewrite(String table_name) {//可以設為void 多設一個string 是為了防止出錯 /*不安全*/
        //"Create Table Student_info(_ID INTEGER PRIMARY KEY autoincrement,NAME CHAR(20),PHONE_NUMBER CHAR(20));)"
        try {//防止無法預期的錯誤
            CREATE_TABLE =
                    "Create Table " +//SQL指令 記得空格
                            table_name + "(" +//表單名稱
                            TABLE_COLUMN_FIRST + " INTEGER PRIMARY KEY autoincrement," +
                            TABLE_COLUMN_SECOND + " CHAR(20)," +
                            TABLE_COLUMN_THIRD + " CHAR(20)"+
                            ");";
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //輸出第一個表單(須改輸出格式)
    public String show1(){
        StringBuffer sf = new StringBuffer();
        Cursor cursor = null;//SELECT * from Student_Info
        try {
            cursor = db.rawQuery("SELECT * from "+TABLE_NAME[0], null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //<editor-fold desc="印出手機電話號碼 -- 如要引用記得刪掉">
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            try {
                sf.append(cursor.getInt(0)).append(":").append(cursor.getString(1)).append(":").append(cursor.getString(2));
            } catch (Exception e) {
                e.printStackTrace();
            }
            sf.append("\n");
            cursor.moveToNext();
        }
        //</editor-fold>
        return sf.toString();
    }

    public String QueryTable(String string) {
        StringBuffer sf = new StringBuffer();
        Cursor cursor = Searchfor(string);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            sf.append(cursor.getInt(0)).append(":").append(cursor.getString(1)).append("\n");
            cursor.moveToNext();
        }
        return sf.toString();
    }

    public Cursor Searchfor(String str){
        //Cursor cursor = db.query("t_user",new String[]{"_ID","NAME"},null,null,null,null,null);
        Cursor cursor = new Cursor() {
            @Override
            public int getCount() {
                return 0;
            }

            @Override
            public int getPosition() {
                return 0;
            }

            @Override
            public boolean move(int offset) {
                return false;
            }

            @Override
            public boolean moveToPosition(int position) {
                return false;
            }

            @Override
            public boolean moveToFirst() {
                return false;
            }

            @Override
            public boolean moveToLast() {
                return false;
            }

            @Override
            public boolean moveToNext() {
                return false;
            }

            @Override
            public boolean moveToPrevious() {
                return false;
            }

            @Override
            public boolean isFirst() {
                return false;
            }

            @Override
            public boolean isLast() {
                return false;
            }

            @Override
            public boolean isBeforeFirst() {
                return false;
            }

            @Override
            public boolean isAfterLast() {
                return false;
            }

            @Override
            public int getColumnIndex(String columnName) {
                return 0;
            }

            @Override
            public int getColumnIndexOrThrow(String columnName) throws IllegalArgumentException {
                return 0;
            }

            @Override
            public String getColumnName(int columnIndex) {
                return null;
            }

            @Override
            public String[] getColumnNames() {
                return new String[0];
            }

            @Override
            public int getColumnCount() {
                return 0;
            }

            @Override
            public byte[] getBlob(int columnIndex) {
                return new byte[0];
            }

            @Override
            public String getString(int columnIndex) {
                return null;
            }

            @Override
            public void copyStringToBuffer(int columnIndex, CharArrayBuffer buffer) {

            }

            @Override
            public short getShort(int columnIndex) {
                return 0;
            }

            @Override
            public int getInt(int columnIndex) {
                return 0;
            }

            @Override
            public long getLong(int columnIndex) {
                return 0;
            }

            @Override
            public float getFloat(int columnIndex) {
                return 0;
            }

            @Override
            public double getDouble(int columnIndex) {
                return 0;
            }

            @Override
            public int getType(int columnIndex) {
                return 0;
            }

            @Override
            public boolean isNull(int columnIndex) {
                return false;
            }

            @Override
            public void deactivate() {

            }

            @Override
            public boolean requery() {
                return false;
            }

            @Override
            public void close() {

            }

            @Override
            public boolean isClosed() {
                return false;
            }

            @Override
            public void registerContentObserver(ContentObserver observer) {

            }

            @Override
            public void unregisterContentObserver(ContentObserver observer) {

            }

            @Override
            public void registerDataSetObserver(DataSetObserver observer) {

            }

            @Override
            public void unregisterDataSetObserver(DataSetObserver observer) {

            }

            @Override
            public void setNotificationUri(ContentResolver cr, Uri uri) {

            }

            @Override
            public Uri getNotificationUri() {
                return null;
            }

            @Override
            public boolean getWantsAllOnMoveCalls() {
                return false;
            }

            @Override
            public void setExtras(Bundle extras) {

            }

            @Override
            public Bundle getExtras() {
                return null;
            }

            @Override
            public Bundle respond(Bundle extras) {
                return null;
            }
        };
        for (int i = 0; i < TABLE_NAME.length; i++) {
            if(str.equals(TABLE_NAME[i])){
                cursor = db.rawQuery("SELECT * from "+TABLE_NAME[i],null);
            }
        }
        return cursor;
    }

    public static int sumfunction(int... values){
        int i = 0;
        int j = values.length;
        for(int k = 0; k < j; k++) {
            int l = values[k];
            i += l;
        }
        return i;
    }

}
