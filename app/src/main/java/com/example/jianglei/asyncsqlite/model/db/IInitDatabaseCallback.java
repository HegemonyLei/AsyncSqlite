package com.example.jianglei.asyncsqlite.model.db;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by jianglei on 2016/4/7.
 */
public interface IInitDatabaseCallback extends IAsyncHandlerCallback {

    /**
     * 初始化成功
     */
    void onInitDatabaseComplete(int token, SQLiteDatabase db);
}
