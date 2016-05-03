package com.example.jianglei.asyncsqlite.model.db;

/**
 * Created by jianglei on 2016/4/7.
 */
public interface ISingleInsertCallback extends IAsyncHandlerCallback {
    /**
     * 单条插入成功
     */
    void onSingleInsertComplete(int token, long result);
}
