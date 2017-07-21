package com.example.sqliteclass;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    final String DB_NAME = "test.db";
    SQLiteDatabase db;
    TextView viewAll;
    Button btn;
    SQLite dab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewAll = (TextView) findViewById(R.id.tv);
        btn = (Button) findViewById(R.id.button);

        db = openOrCreateDatabase(DB_NAME,MODE_PRIVATE,null);
        init();
        upDataText();
        btn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                init();
                upDataText();
                return false;
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dropTable("t_user");
                init();
                upDataText();
            }
        });
    }

    private void init() {
        createTable();
        insertTable("test");
        insertTable("eddy");
    }

    private void upDataText() {
        StringBuffer sf = new StringBuffer();
        Cursor cursor = loadAll();
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            sf.append(cursor.getInt(0)).append(":").append(cursor.getString(1)).append("\n");
            cursor.moveToNext();
        }
        viewAll.setText(sf.toString());
    }

    private void createTable() {
        try{
            db.execSQL("CREATE TABLE t_user(_ID INTEGER PRIMARY KEY autoincrement,NAME TEXT);");
        } catch (Exception e){

        }
    }

    public Cursor loadAll(){
        Cursor cursor = db.query("t_user",new String[]{"_ID","NAME"},null,null,null,null,null);
        cursor = db.rawQuery("SELECT * from t_user",null);
        return cursor;
    }

    private boolean insertTable(String database_name) {
        String sql = "";
        try{
            sql = "insert into t_user values(null,'"+database_name+"')";
            db.execSQL(sql);
            return true;
        } catch (Exception e){
            return false;
        }
    }

    private boolean dropTable(String tablename){
        try {
            String sql = "DROP TABLE "+tablename;
            db.execSQL(sql);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }


}
