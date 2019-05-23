package com.example.a64635.grandwordremember;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    private Context mContext;
    private boolean[] checkItems;
    Uri uri,uri2;
    ContentResolver resolver;
    private AlertDialog alertDialog = null;
    private AlertDialog.Builder dialogBuilder = null;
    CheckedTextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("学霸背单词");
        toolbar.setSubtitle("快速记忆法");
        resolver = getContentResolver();
        uri = Uri.parse("content://com.example.providers.firstprovider1/");
        mContext = this;


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void ifupdate(ContentResolver resolver, Context mContext, ContentValues contentValues, int i, EditText newword) {
        if (i == 0) {
            Toast.makeText(mContext, "单词已存在", Toast.LENGTH_SHORT).show();
        } else {
            resolver.update(uri, contentValues, "word=?", new String[]{String.valueOf(newword.getText())});
            Toast.makeText(mContext, "覆盖成功", Toast.LENGTH_SHORT).show();
        }
    }

    public void customView() {
        TableLayout wordadd = (TableLayout) getLayoutInflater()
                .inflate(R.layout.addword, null);
        dialogBuilder = new AlertDialog.Builder(mContext);
        alertDialog = dialogBuilder
                // 设置对话框标题
                .setTitle("添加单词")
                // 设置对话框显示的View对象
                .setView(wordadd)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(mContext, "取消添加", Toast.LENGTH_SHORT).show();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText newword = (EditText) alertDialog.findViewById(R.id.new_word);
                        EditText wordex = (EditText) alertDialog.findViewById(R.id.word_exp);
                        EditText level = (EditText) alertDialog.findViewById(R.id.level);
                        Cursor cursor = resolver.query(uri, new String[]{"word as _id"}, " _id=?", new String[]{String.valueOf(newword.getText())}, null);
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("word", String.valueOf(newword.getText()));
                        contentValues.put("explanation", String.valueOf(wordex.getText()));
                        contentValues.put("level", Integer.parseInt(String.valueOf(level.getText())));
                        if (cursor.getCount() == 0) {
                            resolver.insert(uri, contentValues);
                            Toast.makeText(mContext, "添加成功", Toast.LENGTH_SHORT).show();
                        } else {
                            textView = (CheckedTextView) alertDialog.findViewById(R.id.cb);
                            if (textView.isChecked()) {
                                resolver.update(uri, contentValues, "word=?", new String[]{String.valueOf(newword.getText())});
                                Toast.makeText(mContext, "覆盖成功", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(mContext, "单词已存在", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                })
                .create();             // 创建AlertDialog对象
        if (!isFinishing()) {
            alertDialog.show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.entertest) {
            Intent intent = new Intent(this, SecondActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.add_word) {
            customView();
        }
        return super.onOptionsItemSelected(item);
    }

    public void clicker(View v) {
        CheckedTextView checkedTextView = (CheckedTextView) v;
        checkedTextView.toggle();
    }
}
