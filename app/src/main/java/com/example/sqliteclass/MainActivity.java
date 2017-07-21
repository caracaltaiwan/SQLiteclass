package com.example.sqliteclass;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView viewAll;
    Button btn;
    SQLiteOH dbOH;
    SQLite db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewAll = (TextView) findViewById(R.id.tv);
        btn = (Button) findViewById(R.id.button);

        //SQLite的宣告
        db = new SQLite(this, dbOH);//SQLiteOH的DB

        init();
        viewAll.setText(db.QueryTable("AA")+"\n"+ db.QueryTable("BB"));//upDataText改成Cursorfat
        btn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                init();
                viewAll.setText(db.QueryTable("AA")+"\n"+ db.QueryTable("BB"));
                return false;
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.dropTable("AA");
                db.dropTable("BB");
                init();
                viewAll.setText(db.QueryTable("BB")+"\n"+ db.QueryTable("BB"));
            }
        });
    }

    private void init() {
        db.createTable("AA","BB");
        db.insertTable("AA","Hello");
        db.insertTable("BB","你好");
    }
}
