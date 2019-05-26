package com.example.a64635.grandwordremember;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;

import static com.example.a64635.grandwordremember.R.id.listview;


/**
 * Created by 64635 on 2019/5/15.
 */

public class ReviewActivity extends AppCompatActivity {
    Toolbar toolbar;
    public Database dbHelper;
    ContentResolver resolver;
    Myadapter adapter;
    ListView listview;
    Uri uri_user;
    private Context mContext;
    int flag = 0;
    public int color = 0;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("学霸背单词");
        toolbar.setSubtitle("开始复习");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//左侧添加一个默认的返回图标
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        mContext = this;
        dbHelper = new Database(this, "rememberword.db", null, 1);
        dbHelper.getWritableDatabase();
        listview = (ListView) findViewById(R.id.listview);
        final ArrayList mylist = new ArrayList<WordRec>();
        db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("rememberwords", null, null, null, null, null, null);
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            WordRec tmp = new WordRec();
            tmp.setWord(cursor.getString(cursor.getColumnIndex("word")));
            tmp.setExplanation(cursor.getString(cursor.getColumnIndex("explanation")));
            tmp.setLevel(cursor.getColumnIndex("level"));
            mylist.add(tmp);

        }


        adapter = new Myadapter(mylist, this);
        adapter.color = settings.getString("list_key", "");
        listview.setAdapter(adapter);

        final Button submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.judge = 1;
                adapter.notifyDataSetChanged();
                submit.setVisibility(View.INVISIBLE);
            }

        });


    }


    public void clickHandler(View source) {
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}
