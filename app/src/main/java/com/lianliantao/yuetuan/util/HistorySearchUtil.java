package com.lianliantao.yuetuan.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class HistorySearchUtil {
    private String TABLE_NAME = "lichenqi";
    public Context mContext;
    private static HistorySearchUtil mHistorySearchUtil;
    private MyDatabaseHelper mMyDatabaseHelper;

    private HistorySearchUtil(Context context) {
        this.mContext = context;
        mMyDatabaseHelper = new MyDatabaseHelper(context, TABLE_NAME + ".db", null, 1);
    }

    public static HistorySearchUtil getInstance(Context context) {
        //得到一个实例
        if (mHistorySearchUtil == null) {
            mHistorySearchUtil = new HistorySearchUtil(context);
        } else if ((!mHistorySearchUtil.mContext.getClass().equals(context.getClass()))) {////判断两个context是否相同
            mHistorySearchUtil = new HistorySearchUtil(context);
        }
        return mHistorySearchUtil;
    }

    /*移除对象*/
    public static void removeHistorySearchUtil(Context context) {
        if (mHistorySearchUtil != null) {
            mHistorySearchUtil = null;
        }
    }

    /*添加一条新纪录*/
    public void putNewSearch(String name) {
        SQLiteDatabase db = mMyDatabaseHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(String.format("select * from %s where name=?", TABLE_NAME), new String[]{name});
        if (cursor.moveToFirst()) {/* 如果存在 */
            db.delete(TABLE_NAME, "name = " + "'" + name + "'", null);
        }
        ContentValues values = new ContentValues();
        values.put("name", name);
        db.insert(TABLE_NAME, null, values);
    }

    /*判断记录是否存在*/
    public boolean isExist(String name) {
        SQLiteDatabase db = mMyDatabaseHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(String.format("select * from %s where name=?", TABLE_NAME), new String[]{name});
        if (cursor.moveToFirst()) {/* 如果存在 */
            return true;
        } else {
            return false;
        }
    }

    /*查询所有历史纪录*/
    public List<String> queryHistorySearchList() {
        SQLiteDatabase db = mMyDatabaseHelper.getWritableDatabase();
        List<String> list = new ArrayList<String>();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndex("name"));
                list.add(name);
            } while (cursor.moveToNext());
        }
        return list;
    }

    /*删除单条记录*/
    public void deleteHistorySearch(String name) {
        SQLiteDatabase db = mMyDatabaseHelper.getWritableDatabase();
        if (isExist(name)) {
            db.delete(TABLE_NAME, "name = " + "'" + name + "'", null);
        }
    }

    /*删除所有记录*/
    public void deleteAllHistorySearch() {
        SQLiteDatabase db = mMyDatabaseHelper.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
    }

    public class MyDatabaseHelper extends SQLiteOpenHelper {

        public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factoty, int version) {
            super(context, name, factoty, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            /*建表语句*/
            String create_da_name = "create table " + TABLE_NAME + "(" + "id integer primary key autoincrement, " + "name text)";
            /*建表*/
            db.execSQL(create_da_name);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        }
    }
}
