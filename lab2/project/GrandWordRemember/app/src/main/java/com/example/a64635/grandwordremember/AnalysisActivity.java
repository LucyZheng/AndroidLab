package com.example.a64635.grandwordremember;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.R.id.list;
import static com.example.a64635.grandwordremember.R.id.listview;

public class AnalysisActivity extends AppCompatActivity {
    private ListView listView;
    ArrayList<Map<String, String>> list;
    SimpleAdapter adapter;
    SQLiteDatabase db;
    Database dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//左侧添加一个默认的返回图标
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        toolbar.setTitle("学霸背单词");
        toolbar.setSubtitle("测验统计");
        listView=(ListView) findViewById(R.id.listviewanalysis);
        dbHelper = new Database(this, "rememberword.db", null, 1);
        dbHelper.getWritableDatabase();
        db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("rememberwords", null, null, null, null, null, null);
        list= new ArrayList<Map<String, String>>();
        Map<String, String> map1 = new HashMap<String,String>();
        map1.put("word","单词");
        map1.put("level","级别");
        map1.put("text","测试");
        map1.put("right","正确");
        list.add(map1);
        if(cursor!=null&&cursor.moveToFirst()){
            do{
                Map<String, String> map = new HashMap<String,String>();
                map.put("word",cursor.getString(cursor.getColumnIndex("word")));
                map.put("level",cursor.getString(cursor.getColumnIndex("level")));
                map.put("text",cursor.getString(cursor.getColumnIndex("test_count")));
                map.put("right",cursor.getString(cursor.getColumnIndex("correct_count")));
                list.add(map);
            }while(cursor.moveToNext());
        }
        adapter = new SimpleAdapter(this, list,
                R.layout.ana_item, new String[] { "word", "level", "text","right" },
                new int[] { R.id.anaword, R.id.analevel, R.id.anatext,R.id.anaright });
        listView.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_analysis, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            this.finish();
        }
        else super.onOptionsItemSelected(item);
        return true;
    }
}

