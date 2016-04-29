package com.example.jianglei.ormsqlitedemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.jianglei.ormsqlitedemo.db.DataBaseOperateToken;
import com.example.jianglei.ormsqlitedemo.db.IDeleteCallback;
import com.example.jianglei.ormsqlitedemo.db.IInitDatabaseCallback;
import com.example.jianglei.ormsqlitedemo.db.IMultiInsertCallback;
import com.example.jianglei.ormsqlitedemo.db.IQueryCallback;
import com.example.jianglei.ormsqlitedemo.db.ISingleInsertCallback;
import com.example.jianglei.ormsqlitedemo.db.IUpdateCallback;
import com.example.jianglei.ormsqlitedemo.db.MasAsyncHandler;

import java.util.List;

/**
 * Created by jianglei on 2016/4/6.
 */
public class DataBaseOpenHelper {

    private static DataBaseOpenHelper sDbHelper;

    private DataBase mDbOpenHelper;

    private SQLiteDatabase mDataBase;

    private Context mContext;

    private static Context sContext;

    private MasAsyncHandler masAsyncHandler;

    public static void init(Context ctx) {
        sContext = ctx;
        getInstance();
    }

    private DataBaseOpenHelper(Context context) {
        mContext = context;
        mDbOpenHelper = new DataBase(context.getApplicationContext(), 1);
        // 异步操作数据库初始化
        masAsyncHandler = new MasAsyncHandler();
    }

    public static DataBaseOpenHelper getInstance() {
        if (sDbHelper == null) {
            syncInt(sContext);
        }
        return sDbHelper;
    }

    private synchronized static void syncInt(Context context) {
        if (sDbHelper == null) {
            sDbHelper = new DataBaseOpenHelper(context);
        }
    }

    public void setDataBase(SQLiteDatabase mDataBase) {
        this.mDataBase = mDataBase;
    }

    public void initDatabase(int token, IInitDatabaseCallback callback) {
        masAsyncHandler.initDataBase(token, mDbOpenHelper, callback);
    }

    public void insertSingleValues(final int insertToken, final String table, final String nullColumnHack, final ContentValues values, final ISingleInsertCallback callback) {
        if (mDataBase != null) {
            masAsyncHandler.startSingleInsert(insertToken, mDataBase, table, nullColumnHack, values, callback);
        } else {
            initDatabase(DataBaseOperateToken.TOKEN_INIT_DATABASE, new IInitDatabaseCallback() {
                @Override
                public void onInitDatabaseComplete(int token, SQLiteDatabase db) {
                    setDataBase(db);
                    masAsyncHandler.startSingleInsert(insertToken, mDataBase, table, nullColumnHack, values, callback);
                }

                @Override
                public void onAsyncOperateFailed() {

                }
            });
        }
    }

    public void insertMultiValues(final int insertToken, final String table, final String nullColumnHack, final List<ContentValues> valuesList, final IMultiInsertCallback callback) {
        if (mDataBase != null) {
            masAsyncHandler.startMultiInsert(insertToken, mDataBase, table, nullColumnHack, valuesList, callback);
        } else {
            initDatabase(DataBaseOperateToken.TOKEN_INIT_DATABASE, new IInitDatabaseCallback() {
                @Override
                public void onInitDatabaseComplete(int token, SQLiteDatabase db) {
                    setDataBase(db);
                    masAsyncHandler.startMultiInsert(insertToken, mDataBase, table, nullColumnHack, valuesList, callback);
                }

                @Override
                public void onAsyncOperateFailed() {

                }
            });
        }
    }

    public void queryValues(final int queryToken, final boolean distinct, final String table, final String[] columns, final String whereClause, final String[] whereArgs,
                            final String groupBy, final String having, final String orderBy, final String limit, final IQueryCallback callback) {
        if (mDataBase != null) {
            masAsyncHandler.startQuery(queryToken, mDataBase, distinct, table, columns, whereClause, whereArgs, groupBy, having, orderBy, limit, callback);
        } else {
            initDatabase(DataBaseOperateToken.TOKEN_INIT_DATABASE, new IInitDatabaseCallback() {
                @Override
                public void onInitDatabaseComplete(int token, SQLiteDatabase db) {
                    setDataBase(db);
                    masAsyncHandler.startQuery(queryToken, mDataBase, distinct, table, columns, whereClause, whereArgs, groupBy, having, orderBy, limit, callback);
                }

                @Override
                public void onAsyncOperateFailed() {

                }
            });
        }
    }

    public void updateValues(final int updateToken, final String table, final ContentValues values, final String whereClause, final String[] whereArgs, final IUpdateCallback callback) {
        if (mDataBase != null) {
            masAsyncHandler.startUpdate(updateToken, mDataBase, table, values, whereClause, whereArgs, callback);
        } else {
            initDatabase(DataBaseOperateToken.TOKEN_INIT_DATABASE, new IInitDatabaseCallback() {
                @Override
                public void onInitDatabaseComplete(int token, SQLiteDatabase db) {
                    setDataBase(db);
                    masAsyncHandler.startUpdate(updateToken, mDataBase, table, values, whereClause, whereArgs, callback);
                }

                @Override
                public void onAsyncOperateFailed() {

                }
            });
        }
    }

    public void deleteValues(final int deleteToken, final String table, final String whereClause, final String[] whereArgs, final IDeleteCallback callback) {
        if (mDataBase != null) {
            masAsyncHandler.startDelete(deleteToken, mDataBase, table, whereClause, whereArgs, callback);
        } else {
            initDatabase(DataBaseOperateToken.TOKEN_INIT_DATABASE, new IInitDatabaseCallback() {
                @Override
                public void onInitDatabaseComplete(int token, SQLiteDatabase db) {
                    setDataBase(db);
                    masAsyncHandler.startDelete(deleteToken, mDataBase, table, whereClause, whereArgs, callback);
                }

                @Override
                public void onAsyncOperateFailed() {

                }
            });
        }
    }
}
