package com.example.isszym.actionbar;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CheckedTextView;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.SimpleCursorAdapter;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.isszym.actionbar.R.id.textView;

public class SupportToolbarActivity extends AppCompatActivity {
    ListView listview;
    private Toolbar mToolbar;
    ContentResolver resolver;
    SimpleCursorAdapter adapter;
    SimpleCursorAdapter adapter2,adapter3;
    Cursor cursor;
    Cursor cursor2,cursor3;
    Uri uri;
    TextView textViewbottom;
    private Context mContext;
    String tmpword;
    private boolean isopen = true;
    private AlertDialog alertDialog = null;
    private AlertDialog.Builder dialogBuilder = null;
    int flag = 0, flag2 = 0;
    int nowheight;
    CheckedTextView textView2;
    List<TextView> listguard;
    private HorizontalScrollView horizontalScrollView;
    private LinearLayout container;
    private String alphabet[] = new String[]{" ", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
    private ArrayList<String> data = new ArrayList<>();
    private TextView testTextViewguard;
    String guardword;
    ProgressDialog pd;
    int ifover;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toolbar);
        mContext = this;
        //We have to tell the activity where the toolbar is
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        //Display home with the "up" arrow indicator
        actionBar.setTitle("简明英汉词典");
        actionBar.setSubtitle("中山大学");
        uri = Uri.parse("content://com.example.providers.firstprovider1/");

        setguard();

        resolver = getContentResolver();
        listview = (ListView) findViewById(R.id.listview);
        cursor = resolver.query(uri, new String[]{"word as _id,explanation"}, null, null, "_id");
        cursor2 = resolver.query(uri, new String[]{"word as _id,explanation"}, null, null, "_id");
        adapter = new SimpleCursorAdapter(this, R.layout.item, cursor, new String[]{"_id"}, new int[]{R.id.word}, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        adapter2 = new SimpleCursorAdapter(this, R.layout.item2, cursor2, new String[]{"_id", "explanation"}, new int[]{R.id.word2, R.id.subex}, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        listview.setAdapter(adapter);
        textViewbottom = (TextView) findViewById(textView);
        ViewGroup.LayoutParams params2 = listview.getLayoutParams();
        final int heighttmp=params2.height;
        Resources resources = this.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        int height2 = dm.heightPixels;
        params2.height = height2 - 20;
        listview.setLayoutParams(params2);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (flag == 0) {
                    ViewGroup.LayoutParams params3 = listview.getLayoutParams();
                    params3.height=heighttmp;
                    listview.setLayoutParams(params3);
                    TextView nowclick = (TextView) view.findViewById(R.id.word);
                    String tmpword = nowclick.getText().toString();
                    Cursor cursor2 = resolver.query(uri, new String[]{"word as _id,explanation,level"}, " _id=?", new String[]{tmpword}, null);
                    cursor2.moveToFirst();
                    textViewbottom.setText(tmpword + "\n" + cursor2.getString(cursor2.getColumnIndex("explanation")));
                }
                ;
            }
        });
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                PopupMenu popup = new PopupMenu(SupportToolbarActivity.this, view);
                popup.getMenuInflater().inflate(R.menu.menu_pop, popup.getMenu());
                TextView nowclick;
                if (flag == 0) {
                    nowclick = (TextView) view.findViewById(R.id.word);
                } else {
                    nowclick = (TextView) view.findViewById(R.id.word2);
                }
                tmpword = nowclick.getText().toString();
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.delete:
                                simpledelete();
                                break;
                            case R.id.correct:
                                simplecorrect();
                                break;
                        }
                        return true;
                    }
                });
                popup.show();
                return true;
            }
        });
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            if (message.what == 0)
            {
                Log.v("lll",String.valueOf(ifover));
                pd.setProgress(ifover);}
            else if (message.what == 1) {
                new RefreshList().execute();
            }
        }
    };
    public void show(int max_length) {
        pd = new ProgressDialog(SupportToolbarActivity.this);
        pd.setMax(max_length);
        pd.setTitle("下载单词");
        pd.setCancelable(false);
        pd.setProgressStyle(pd.STYLE_HORIZONTAL);
        pd.setIndeterminate(false);
        pd.show();

    }
    public void setguard() {
        Collections.addAll(data, alphabet);
        horizontalScrollView = (HorizontalScrollView) findViewById(R.id.horizontalScrollView);
        container = (LinearLayout) findViewById(R.id.horizontalScrollViewItemContainer);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.setMargins(20, 10, 20, 10);
        for (int i = 0; i < data.size(); i++) {
            TextView textView = new TextView(this);
            textView.setText(data.get(i));
            textView.setTextSize(15);
            textView.setWidth(40);
            textView.setGravity(Gravity.CENTER);
            textView.setBackgroundColor(Color.rgb(255, 228, 225));
            textView.setLayoutParams(layoutParams);

            final int tmp = i;
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.setBackgroundColor(Color.rgb(187, 255, 255));
                    for (int j = 0; j < container.getChildCount(); j++) {
                        if (tmp == j) continue;
                        container.getChildAt(j).setBackgroundColor(Color.rgb(255, 228, 225));
                    }
                    guardword = data.get(tmp);
                    if (guardword.equals(" ")) {
                        if (flag == 0)
                            new RefreshList().execute();
                        else
                            new RefreshListexp().execute();
                    }
                    if (!guardword.equals(" ")) {
                        if (flag == 0)
                            new RefreshListguard().execute();
                        else
                            new RefreshListguardexp().execute();
                    }
                }
            });
            container.addView(textView);
            container.invalidate();
        }
    }

    private class RefreshList extends AsyncTask<Void, Void, Cursor> {
        protected Cursor doInBackground(Void... params) {
            Cursor newCursor = resolver.query(uri, new String[]{"word as _id,explanation,level"}, null, null, " _id");
            return newCursor;
        }

        protected void onPostExecute(Cursor newCursor) {
            adapter.changeCursor(newCursor);
            cursor.close();
            cursor = newCursor;
        }
    }

    private class RefreshListguard extends AsyncTask<Void, Void, Cursor> {
        protected Cursor doInBackground(Void... params) {
            Cursor newCursor = resolver.query(uri, new String[]{"word as _id,explanation,level"}, "word like ?", new String[]{guardword + "%"}, " _id");
            return newCursor;
        }

        protected void onPostExecute(Cursor newCursor) {
            adapter.changeCursor(newCursor);
            cursor.close();
            cursor = newCursor;
        }
    }

    private class RefreshListexp extends AsyncTask<Void, Void, Cursor> {
        protected Cursor doInBackground(Void... params) {
            Cursor newCursor = resolver.query(uri, new String[]{"word as _id,explanation,level"}, null, null, " _id");
            return newCursor;
        }

        protected void onPostExecute(Cursor newCursor) {
            adapter2.changeCursor(newCursor);
            cursor2.close();
            cursor2 = newCursor;
        }
    }

    private class RefreshListguardexp extends AsyncTask<Void, Void, Cursor> {
        protected Cursor doInBackground(Void... params) {
            Cursor newCursor = resolver.query(uri, new String[]{"word as _id,explanation,level"}, "word like ?", new String[]{guardword + "%"}, " _id");
            return newCursor;
        }
        protected void onPostExecute(Cursor newCursor) {
            adapter2.changeCursor(newCursor);
            cursor2.close();
            cursor2 = newCursor;
        }
    }

    private class RefreshListlookfor extends AsyncTask<Void, Void, Cursor> {
        protected Cursor doInBackground(Void... params) {
            Cursor newCursor = resolver.query(uri, new String[]{"word as _id,explanation,level"}, "word like ?", new String[]{"%"+guardword + "%"}, " _id");
            return newCursor;
        }
        protected void onPostExecute(Cursor newCursor) {
            adapter.changeCursor(newCursor);
            cursor.close();
            cursor = newCursor;
        }
    }
    private class RefreshListexplookfor extends AsyncTask<Void, Void, Cursor> {
        protected Cursor doInBackground(Void... params) {
            Cursor newCursor = resolver.query(uri, new String[]{"word as _id,explanation,level"}, "word like ?", new String[]{"%"+guardword + "%"}, " _id");

            return newCursor;
        }
        protected void onPostExecute(Cursor newCursor) {
            adapter2.changeCursor(newCursor);
            cursor2.close();
            cursor2 = newCursor;
        }
    }

    public void simpledelete() {
        dialogBuilder = new AlertDialog.Builder(mContext);
        alertDialog = dialogBuilder
                .setTitle("删除单词")
                .setMessage("是否确定要删除该单词？")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(mContext, "取消删除", Toast.LENGTH_SHORT).show();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        resolver.delete(uri, "word=?", new String[]{tmpword});
                        new RefreshList().execute();
                        new RefreshListexp().execute();
                        Toast.makeText(mContext, "删除成功", Toast.LENGTH_SHORT).show();
                    }
                })
                .create();             // 创建AlertDialog对象
        alertDialog.show();             // 显示对话框
    }

    public void simplecorrect() {
        TableLayout wordcorrect = (TableLayout) getLayoutInflater()
                .inflate(R.layout.correctword, null);
        dialogBuilder = new AlertDialog.Builder(mContext);
        alertDialog = dialogBuilder
                // 设置对话框标题
                .setTitle("修改单词")
                // 设置对话框显示的View对象
                .setView(wordcorrect)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(mContext, "取消修改", Toast.LENGTH_SHORT).show();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText wordex = (EditText) alertDialog.findViewById(R.id.word_exp);
                        EditText level = (EditText) alertDialog.findViewById(R.id.level);
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("word", tmpword);
                        contentValues.put("explanation", String.valueOf(wordex.getText()));
                        contentValues.put("level", Integer.parseInt(String.valueOf(level.getText())));
                        resolver.update(uri, contentValues, "word=?", new String[]{tmpword});
                        new RefreshList().execute();
                        new RefreshListexp().execute();
                        Toast.makeText(mContext, "修改成功", Toast.LENGTH_SHORT).show();
                        textViewbottom.setText(tmpword + "\n" + String.valueOf(wordex.getText()));
                    }
                })
                .create();             // 创建AlertDialog对象
        alertDialog.show();
    }

    public void lookforword(){
        TableLayout wordfind = (TableLayout) getLayoutInflater()
                .inflate(R.layout.lookfor, null);
        dialogBuilder = new AlertDialog.Builder(mContext);
        alertDialog = dialogBuilder
                // 设置对话框标题
                .setTitle("查询单词")
                // 设置对话框显示的View对象
                .setView(wordfind)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(mContext, "取消查询", Toast.LENGTH_SHORT).show();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText wordex = (EditText) alertDialog.findViewById(R.id.lookforword);
                        guardword=String.valueOf(wordex.getText());
                        Log.v("lll",guardword);
                        new RefreshListlookfor().execute();
                        new RefreshListexplookfor().execute();
                        Toast.makeText(mContext, "查询成功", Toast.LENGTH_SHORT).show();
                    }
                })
                .create();             // 创建AlertDialog对象
        alertDialog.show();
    }

    public void addword() {
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
                            new RefreshList().execute();
                            new RefreshListexp().execute();
                            Toast.makeText(mContext, "添加成功", Toast.LENGTH_SHORT).show();
                        } else {
                            textView2 = (CheckedTextView) alertDialog.findViewById(R.id.cb);
                            if (textView2.isChecked()) {
                                resolver.update(uri, contentValues, "word=?", new String[]{String.valueOf(newword.getText())});
                                Toast.makeText(mContext, "覆盖成功", Toast.LENGTH_SHORT).show();
                            } else {
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
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.support, menu);
        return true;
    }

    public static WordRec parserJSON(String str) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(str);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        WordRec wordRec = new WordRec();
        wordRec.setWord(jsonObject.optString("word"));
        wordRec.setExplanation(jsonObject.optString("explanation"));
        wordRec.setLevel(Integer.parseInt(jsonObject.optString("level")));
        return wordRec;
    }

    public void getdata() {
        show(100);
        new Thread() {
            @Override
            public void run() {
                try {
                    Log.d("out:", "downloading...");
                    String tmp = "http://172.18.187.9:8080/dict/";
                    URL url = new URL(tmp);
                    URLConnection connection = url.openConnection();
                    InputStream inputStream = connection.getInputStream();
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    String str = stringBuilder.toString();
                    JSONArray jsonArray = new JSONArray(str);
                    for (int i = 0; i < jsonArray.length(); ++i) {
                        WordRec wordRec = parserJSON(jsonArray.get(i).toString());
                        Log.v("out:", wordRec.toString());
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("word", wordRec.getWord());
                        contentValues.put("explanation", wordRec.getExplanation());
                        contentValues.put("level", wordRec.getLevel());
                        resolver.insert(uri, contentValues);
                        ifover = 100 * (i + 1) / jsonArray.length();
                        handler.sendEmptyMessage(0);
                    }
                    pd.dismiss();
                    handler.sendEmptyMessage(1);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }.start();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        ViewGroup.LayoutParams params = listview.getLayoutParams();

        switch (item.getItemId()) {
            case R.id.addword:
                addword();
                break;
            case R.id.lookfor:
                lookforword();
                break;
            case R.id.menu1:
                getdata();
                break;
            case R.id.menu2:
                if (item.isChecked()) {
                    flag = 0;
                    listview.setAdapter(adapter);
                    params.height = nowheight;
                    listview.setLayoutParams(params);
                    item.setChecked(false);
                } else {
                    flag = 1;
                    listview.setAdapter(adapter2);
                    nowheight = params.height;
                    Resources resources = this.getResources();
                    DisplayMetrics dm = resources.getDisplayMetrics();
                    int height2 = dm.heightPixels;
                    params.height = height2 - 20;
                    listview.setLayoutParams(params);
                    textViewbottom.setEnabled(false);
                    item.setChecked(true);
                    new RefreshList().execute();
                }
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}
