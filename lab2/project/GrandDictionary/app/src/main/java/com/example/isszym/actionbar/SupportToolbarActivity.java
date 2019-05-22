package com.example.isszym.actionbar;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
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
    private Toolbar mToolbar;
    ContentResolver resolver;
    ListAdapter adapter;
    Uri uri;
    TextView textViewbottom;
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
        uri = Uri.parse("content://com.example.providers.firstprovider1/");
        resolver = getContentResolver();
        listview=(ListView) findViewById(R.id.listview);
//        Cursor cursor=dbHelper.getReadableDatabase().rawQuery("select word as _id from dictionary;",null);
        Cursor cursor = resolver.query(uri, new String[]{"word as _id"}, null,null,null);
        adapter=new SimpleCursorAdapter(this,R.layout.item,cursor,new String[]{"_id"},new int[]{R.id.word}, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        listview.setAdapter(adapter);
        textViewbottom=(TextView)findViewById(R.id.textView);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView nowclick=(TextView) view.findViewById(R.id.word);
                String tmpword=nowclick.getText().toString();
                Cursor cursor = resolver.query(uri, new String[]{"word as _id,explanation,level"}, " _id=?", new String[]{tmpword}, null);
                cursor.moveToFirst();
                textViewbottom.setText(tmpword+"\n"+cursor.getString(cursor.getColumnIndex("explanation")));
            }
        });

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
                    Log.d("out:", "downloading...");
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
                        Log.v("out:", wordRec.toString());
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("word", wordRec.getWord());
                        contentValues.put("explanation", wordRec.getExplanation());
                        contentValues.put("level", wordRec.getLevel());
                        resolver.insert(uri, contentValues);
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
