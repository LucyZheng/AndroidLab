package com.example.isszym.actionbar;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Created by 64635 on 2019/5/15.
 */

public class DictProvider extends ContentProvider {
    private Context mContext;
    Database mDbHelper;
    SQLiteDatabase db = null;
    @Override
    public boolean onCreate()  {
        mDbHelper=new Database(getContext(),"dictionary",null,1);
        return true;
    }

    @Override
    public String getType(Uri uri){    // 返回本ContentProvider所提供数据的MIME类型
        return null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        db = mDbHelper.getWritableDatabase();
        return db.query("dictionary",projection,selection,selectionArgs,null,null,sortOrder,null);
    }

    @Override
    public Uri insert(Uri uri, ContentValues cv){
        db = mDbHelper.getWritableDatabase();
        db.insert("dictionary", null, cv);
        db.close();
        return uri;
    }

    public int delete(Uri uri, String where, String[] whereArgs) {
        db = mDbHelper.getWritableDatabase();
        int ret = db.delete("dictionary", where, whereArgs);
        db.close();
        return ret;//影响的记录数
    }

    public int update(Uri uri, ContentValues cv, String where, String[] whereArgs) {
        db =mDbHelper.getWritableDatabase();
        int ret = db.update("dictionary", cv, where, whereArgs);
        db.close();
        return ret; //影响的记录数
    }

    private String sh(Uri uri){
        return "\r\n-----"+uri+"-----\r\n";
    }
}
