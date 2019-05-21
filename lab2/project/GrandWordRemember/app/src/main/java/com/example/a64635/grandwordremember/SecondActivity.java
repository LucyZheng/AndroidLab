package com.example.a64635.grandwordremember;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
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
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    ContentResolver resolver;
    Myadapter adapter;
    ListView listview;
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
        Uri uri_user = Uri.parse(urii);
        resolver = getContentResolver();
        listview=(ListView) findViewById(R.id.listview);
        Cursor cursor = resolver.query(uri_user, new String[]{"word as _id,explanation,level"}, null,null,"_id limit 10");
        String[] exp=new String[10];
        int i=0;
        for (cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
            WordRec tmp=new WordRec();
            tmp.setWord(cursor.getString(cursor.getColumnIndex("_id")));
            tmp.setExplanation(cursor.getString(cursor.getColumnIndex("explanation")));
            tmp.setLevel(cursor.getColumnIndex("level"));
            mylist.add(tmp);
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
