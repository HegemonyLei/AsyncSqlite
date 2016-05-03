package com.example.jianglei.asyncsqlite.model.db;

import android.database.Cursor;

/**
 * Created by jianglei on 2016/4/7.
 */
public interface IQueryCallback extends IAsyncHandlerCallback{

    /**
     * 查询成功
     */
    void onQueryComplete(int token, Cursor cursor);
}
