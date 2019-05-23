package com.example.a64635.grandwordremember;

import android.content.ContentResolver;
import android.content.ContentValues;
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
import android.widget.CursorAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

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
        String urii="content://com.example.providers.firstprovider1/";
        ArrayList mylist=new ArrayList<WordRec>();
        uri_user = Uri.parse(urii);
        resolver = getContentResolver();
        listview=(ListView) findViewById(R.id.listview);
        Cursor cursor = resolver.query(uri_user, new String[]{"word as _id,explanation,level"}, null,null,"_id limit 10");
        String[] exp=new String[10];
        int i=0;
        dbHelper= new Database(this,"rememberword.db",null,1);
        dbHelper.getWritableDatabase();
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        for (cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){

            WordRec tmp=new WordRec();
            tmp.setWord(cursor.getString(cursor.getColumnIndex("_id")));
            tmp.setExplanation(cursor.getString(cursor.getColumnIndex("explanation")));
            tmp.setLevel(cursor.getColumnIndex("level"));
            mylist.add(tmp);
            Cursor cursor3=db.query("rememberwords",new String[]{"word as _id"}, "_id = ?", new String[]{tmp.getWord()},null,null,null);
            if (cursor3.getCount()==0){
                values.put("word", tmp.getWord());
                values.put("explanation",tmp.getExplanation());
                values.put("level",tmp.getLevel());
                db.insert("rememberwords",null,values);
                values.clear();
            }

        }
        cursor.moveToFirst();
        adapter=new Myadapter(mylist,this);
        listview.setAdapter(adapter);


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
