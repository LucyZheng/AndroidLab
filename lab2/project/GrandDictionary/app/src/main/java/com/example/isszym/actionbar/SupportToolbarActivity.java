package com.example.isszym.actionbar;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;

public class SupportToolbarActivity extends AppCompatActivity {
    ListView listview;
    private Database dbHelper;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toolbar);

        //We have to tell the activity where the toolbar is
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        //Display home with the "up" arrow indicator
        actionBar.setTitle("简明英汉词典");
        actionBar.setSubtitle("中山大学");
        dbHelper=new Database(this,"Dictionary.db",null,1);
        dbHelper.getWritableDatabase();
        listview=(ListView) findViewById(R.id.listview);
        Cursor cursor=dbHelper.getReadableDatabase().rawQuery("select word as _id from dictionary;",null);
        ListAdapter adapter=new SimpleCursorAdapter(this,R.layout.item,cursor,new String[]{"_id"},new int[]{R.id.word}, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        listview.setAdapter(adapter);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.support, menu);
        return true;
    }
    public static WordRec parserJSON(String str){
        JSONObject jsonObject=null;
        try{
            jsonObject=new JSONObject(str);

        }catch (Exception ex){
            ex.printStackTrace();
        }
        WordRec wordRec=new WordRec();
        wordRec.setWord(jsonObject.optString("word"));
        wordRec.setExplanation(jsonObject.optString("explanation"));
        wordRec.setLevel(Integer.parseInt(jsonObject.optString("level")));
        return  wordRec;
    }
    public void getdata(){
        new Thread() {
            @Override
            public void run() {
                try {
                    String tmp="http://172.18.187.9:8080/dict/";
                    URL url=new URL(tmp);
                    URLConnection connection=url.openConnection();
                    InputStream inputStream=connection.getInputStream();
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader=new BufferedReader(inputStreamReader);
                    StringBuilder stringBuilder= new StringBuilder();
                    String line;
                    while((line=bufferedReader.readLine())!=null){
                        stringBuilder.append(line);
                    }
                    String str=stringBuilder.toString();
                    JSONArray jsonArray=new JSONArray(str);
                    for (int i=0;i<jsonArray.length();++i){
                        WordRec wordRec=parserJSON(jsonArray.get(i).toString());
                        SQLiteDatabase tmp2=dbHelper.getWritableDatabase();
                        tmp2.execSQL("insert into dictionary(word,explanation,level) values (?,?,?)",new Object[]{wordRec.getWord(),wordRec.getExplanation(),wordRec.getLevel()});
                        tmp2.close();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }.start();
        Toast.makeText(this, "下载完成", Toast.LENGTH_SHORT).show();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action2:
                Toast.makeText(this, "action2", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu1:
                getdata();
                break;
            case R.id.menu2:
                Toast.makeText(this, "menu2", Toast.LENGTH_SHORT).show();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}
