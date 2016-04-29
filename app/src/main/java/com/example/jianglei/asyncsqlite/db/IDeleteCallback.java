package com.example.jianglei.asyncsqlite.db;

/**
 * Created by jianglei on 2016/4/7.
 */
public interface IDeleteCallback extends IAsyncHandlerCallback {

    /**
     * 删除成功
     */
    void onDeleteComplete(int token, long result);
}
