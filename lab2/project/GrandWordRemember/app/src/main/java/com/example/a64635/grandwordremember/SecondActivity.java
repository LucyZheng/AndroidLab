package com.example.a64635.grandwordremember;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
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

public class SecondActivity extends AppCompatActivity {
    Toolbar toolbar;
    Database dbHelper;
    ContentResolver resolver;
    Myadapter adapter;
    ListView listview;
    Uri uri_user;
    private Context mContext;
    int flag=0;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("学霸背单词");
        toolbar.setSubtitle("单词测验");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//左侧添加一个默认的返回图标
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        String urii = "content://com.example.providers.firstprovider1/";
        mContext=this;
        dbHelper = new Database(this, "rememberword.db", null, 1);
        dbHelper.getWritableDatabase();
        uri_user = Uri.parse(urii);
        resolver = getContentResolver();
        listview = (ListView) findViewById(R.id.listview);
        Cursor cursor = resolver.query(uri_user, new String[]{"word as _id,explanation,level"}, null, null, "_id limit 500");
        final ArrayList mylist = new ArrayList<WordRec>();
        db = dbHelper.getWritableDatabase();
        Cursor cursor2 = db.query("rememberwords", null, null, null, null, null, null);
        if (cursor2.getCount() != 0) {
            cursor2.moveToLast();
            String nowword = cursor2.getString(cursor2.getColumnIndex("word"));
            Log.v("lll", nowword);
            int i = 0;
            ContentValues values = new ContentValues();
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                if (cursor.getString(cursor.getColumnIndex("_id")).compareTo(nowword) <= 0)
                    continue;
                WordRec tmp = new WordRec();
                tmp.setWord(cursor.getString(cursor.getColumnIndex("_id")));
                tmp.setExplanation(cursor.getString(cursor.getColumnIndex("explanation")));
                tmp.setLevel(cursor.getColumnIndex("level"));
                mylist.add(tmp);
                Cursor cursor3 = db.query("rememberwords", new String[]{"word as _id"}, "_id = ?", new String[]{tmp.getWord()}, null, null, null);
                if (cursor3.getCount() == 0) {
                    values.put("word", tmp.getWord());
                    values.put("explanation", tmp.getExplanation());
                    values.put("level", tmp.getLevel());
                    db.insert("rememberwords", null, values);
                    i+=1;
                    values.clear();
                }
                if(i==10) break;

            }
        }
        else{
            int i=0;
            ContentValues values = new ContentValues();
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                WordRec tmp = new WordRec();
                tmp.setWord(cursor.getString(cursor.getColumnIndex("_id")));
                tmp.setExplanation(cursor.getString(cursor.getColumnIndex("explanation")));
                tmp.setLevel(cursor.getColumnIndex("level"));
                mylist.add(tmp);
                values.put("word", tmp.getWord());
                values.put("explanation", tmp.getExplanation());
                values.put("level", tmp.getLevel());
                db.insert("rememberwords", null, values);
                i+=1;
                if(i==10) break;

            }
        }
        cursor.moveToFirst();
        adapter = new Myadapter(mylist, this);
        listview.setAdapter(adapter);

        final Button submit=(Button)findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.judge=1;
                adapter.notifyDataSetChanged();
                Map<String, String> nowmap=adapter.user_choose;
                for (int j=0;j<mylist.size();j++){
                    WordRec tmp=(WordRec) mylist.get(j);
                    ContentValues values2=new ContentValues();
                    values2.put("test_count",1);
                    db.update("rememberwords",values2,"word=?",new String[]{tmp.getWord()});
                    values2.clear();
                    if (j<nowmap.size()){
                        String tmpexp=nowmap.get(tmp.getWord());
                        String nowexp=tmp.getExplanation();
                        if (tmpexp.equals(nowexp)) {
                            values2.put("correct_count",1);
                            db.update("rememberwords",values2,"word=?",new String[]{tmp.getWord()});
                            values2.clear();
                        }
                    }
                }
                submit.setVisibility(View.INVISIBLE);
            }

        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_second, menu);
        return true;
    }

    public void clickHandler(View source) {
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}
