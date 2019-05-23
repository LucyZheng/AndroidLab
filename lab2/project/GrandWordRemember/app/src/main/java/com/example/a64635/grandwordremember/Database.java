package com.example.a64635.grandwordremember;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by 64635 on 2019/5/14.
 */

public class Database extends SQLiteOpenHelper {
    public static final String CREATE_DICTIONARY="CREATE TABLE rememberwords(word text primary key,explanation text," +
            "level int default 0, test_count int default 0, correct_count int default 0,last_test_time timestamp)";
    private Context mContext;
    public Database(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context,name,factory,version);
        mContext=context;
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_DICTIONARY);
        Toast.makeText(mContext,"Create succeed",Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onUpgrade(SQLiteDatabase db,int oldversion,int newversion){}


}
