package com.example.jianglei.asyncsqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jianglei on 2016/4/6.
 */
public class DataBase extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "demo.db";

    public static final String TABLE = "info";

    public DataBase(Context context, int version_code) {
        super(context, DATABASE_NAME, null, version_code);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE + " ("
                + "id" + " INTEGER PRIMARY KEY,"
                + "name" + " TEXT NOT NULL,"
                + "age" + " TEXT );");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
