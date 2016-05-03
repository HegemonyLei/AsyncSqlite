package com.example.jianglei.asyncsqlite.model.db;

/**
 * Created by jianglei on 2016/4/7.
 */
public interface IUpdateCallback extends IAsyncHandlerCallback {

    /**
     * 更新成功
     */
    void onUpdateComplete(int token, long result);
}
