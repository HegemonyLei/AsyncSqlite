package com.example.jianglei.asyncsqlite.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import com.example.jianglei.asyncsqlite.Logger;

import java.util.List;


/**
 * 数据库异步框架
 * Created by jianglei on 2016/4/6.
 */
public class AsyncHandler extends Handler {

    private static final String TAG = "MasAsyncQueryHandler";

    public static final long SUCCESS = 1;
    public static final long FAIL = -1;

    private static final int EVENT_ARG_SINGLE_INSERT = 0;
    private static final int EVENT_ARG_MULTI_INSERT = 1;
    private static final int EVENT_ARG_QUERY = 2;
    private static final int EVENT_ARG_UPDATE = 3;
    private static final int EVENT_ARG_DELETE = 4;
    private static final int EVENT_INIT_DATABASE = 5;

    private static Looper sLooper = null;

    private Handler mWorkerThreadHandler;

    private IAsyncHandlerCallback mIAsyncHandlerCallback;

    protected static class SqliteArgs {
        public SQLiteDatabase db;
        public String table;
        public Handler handler;
        public IAsyncHandlerCallback callback;
    }

    protected static final class InsertSingleArgs extends SqliteArgs {
        public String nullColumnHack;
        public ContentValues values;
        public long result;
    }

    protected static final class InsertMultiArgs extends SqliteArgs {
        public String nullColumnHack;
        public List<ContentValues> valuesList;
        public long result;
    }

    protected static final class QueryArgs extends SqliteArgs {
        public boolean distinct;
        public String[] columns;
        public String whereClause;
        public String[] whereArgs;
        public String groupBy;
        public String having;
        public String orderBy;
        public String limit;
        public Cursor result;
    }

    protected static final class UpdateArgs extends SqliteArgs {
        public ContentValues values;
        public String whereClause;
        public String[] whereArgs;
        public long result;
    }

    protected static final class DeleteArgs extends SqliteArgs {
        public String whereClause;
        public String[] whereArgs;
        public long result;
    }

    protected static final class InitArgs extends SqliteArgs {
        public DataBase dbOpenHelper;
        public SQLiteDatabase result;
    }

    public AsyncHandler() {
        super();
        synchronized (AsyncHandler.class) {
            if (sLooper == null) {
                HandlerThread thread = new HandlerThread(TAG);
                thread.start();
                sLooper = thread.getLooper();
            }
        }
        mWorkerThreadHandler = new WorkerHandler(sLooper);
    }

    /**
     * 初始化数据库
     *
     * @param token        插入数据库标识
     * @param dbOpenHelper 数据库
     */
    public void initDataBase(int token, DataBase dbOpenHelper, IInitDatabaseCallback callback) {
        Message msg = mWorkerThreadHandler.obtainMessage(token);
        msg.arg1 = EVENT_INIT_DATABASE;

        InitArgs args = new InitArgs();
        args.handler = this;
        args.dbOpenHelper = dbOpenHelper;
        args.callback = callback;

        msg.obj = args;
        mWorkerThreadHandler.sendMessage(msg);
    }

    /**
     * 单条插入数据库
     *
     * @param token          插入数据库标识
     * @param db             数据库对象
     * @param table          数据库表名
     * @param nullColumnHack 当values为空时设置的空列数据
     * @param values         插入数据库内容
     */
    public void startSingleInsert(int token, SQLiteDatabase db, String table, String nullColumnHack, ContentValues values, ISingleInsertCallback callback) {
        Message msg = mWorkerThreadHandler.obtainMessage(token);
        msg.arg1 = EVENT_ARG_SINGLE_INSERT;

        InsertSingleArgs args = new InsertSingleArgs();
        args.handler = this;
        args.db = db;
        args.table = table;
        args.nullColumnHack = nullColumnHack;
        args.values = values;
        args.callback = callback;

        msg.obj = args;
        mWorkerThreadHandler.sendMessage(msg);
    }

    /**
     * 多条插入数据库
     *
     * @param token          插入数据库标识
     * @param db             数据库对象
     * @param table          数据库表名
     * @param nullColumnHack 当values为空时设置的空列数据
     * @param valuesList     插入数据库内容
     */
    public void startMultiInsert(int token, SQLiteDatabase db, String table, String nullColumnHack, List<ContentValues> valuesList, IMultiInsertCallback callback) {
        Message msg = mWorkerThreadHandler.obtainMessage(token);
        msg.arg1 = EVENT_ARG_MULTI_INSERT;

        InsertMultiArgs args = new InsertMultiArgs();
        args.handler = this;
        args.db = db;
        args.table = table;
        args.nullColumnHack = nullColumnHack;
        args.valuesList = valuesList;
        args.callback = callback;

        msg.obj = args;
        mWorkerThreadHandler.sendMessage(msg);
    }

    /**
     * 查询数据库
     *
     * @param token       插入数据库标识
     * @param db          数据库对象
     * @param table       数据库表名
     * @param distinct    是否去除重复项
     * @param columns     查询的列数组
     * @param whereClause 条件
     * @param whereArgs   条件参数数组
     * @param groupBy     分组依据
     * @param having      过滤
     * @param orderBy     排序
     * @param limit       限制条数
     */
    public void startQuery(int token, SQLiteDatabase db, boolean distinct, String table, String[] columns, String whereClause, String[] whereArgs,
                           String groupBy, String having, String orderBy, String limit, IQueryCallback callback) {
        Message msg = mWorkerThreadHandler.obtainMessage(token);
        msg.arg1 = EVENT_ARG_QUERY;

        QueryArgs args = new QueryArgs();
        args.handler = this;
        args.db = db;
        args.distinct = distinct;
        args.table = table;
        args.columns = columns;
        args.whereClause = whereClause;
        args.whereArgs = whereArgs;
        args.groupBy = groupBy;
        args.having = having;
        args.orderBy = orderBy;
        args.limit = limit;
        args.callback = callback;

        msg.obj = args;
        mWorkerThreadHandler.sendMessage(msg);
    }

    /**
     * 更新数据库
     *
     * @param token       插入数据库标识
     * @param db          数据库对象
     * @param table       数据库表名
     * @param values      更新的内容
     * @param whereClause 条件
     * @param whereArgs   条件参数数组
     */
    public void startUpdate(int token, SQLiteDatabase db, String table, ContentValues values, String whereClause, String[] whereArgs, IUpdateCallback callback) {
        Message msg = mWorkerThreadHandler.obtainMessage(token);
        msg.arg1 = EVENT_ARG_UPDATE;

        UpdateArgs args = new UpdateArgs();
        args.handler = this;
        args.db = db;
        args.table = table;
        args.values = values;
        args.whereClause = whereClause;
        args.whereArgs = whereArgs;
        args.callback = callback;

        msg.obj = args;
        mWorkerThreadHandler.sendMessage(msg);
    }

    /**
     * 删除数据库某条
     *
     * @param token       插入数据库标识
     * @param db          数据库对象
     * @param table       数据库表名
     * @param whereClause 条件
     * @param whereArgs   条件参数数组
     */
    public void startDelete(int token, SQLiteDatabase db, String table, String whereClause, String[] whereArgs, IDeleteCallback callback) {
        Message msg = mWorkerThreadHandler.obtainMessage(token);
        msg.arg1 = EVENT_ARG_DELETE;

        DeleteArgs args = new DeleteArgs();
        args.handler = this;
        args.db = db;
        args.table = table;
        args.whereClause = whereClause;
        args.whereArgs = whereArgs;
        args.callback = callback;

        msg.obj = args;
        mWorkerThreadHandler.sendMessage(msg);
    }

    /**
     * 数据库处理
     */
    protected class WorkerHandler extends Handler {
        public WorkerHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int token = msg.what;
            int event = msg.arg1;

            InsertSingleArgs insertSingleArgs;
            InsertMultiArgs insertMultiArgs;
            QueryArgs queryArgs;
            UpdateArgs updateArgs;
            DeleteArgs deleteArgs;
            InitArgs initArgs;

            Message reply;
            switch (event) {
                case EVENT_ARG_SINGLE_INSERT:
                    insertSingleArgs = (InsertSingleArgs) msg.obj;
                    insertSingleArgs.result = insertSingleArgs.db.insertOrThrow(insertSingleArgs.table,
                            insertSingleArgs.nullColumnHack, insertSingleArgs.values);
                    if ((int) insertSingleArgs.result == -1) {
                        Logger.e(TAG + " ---->> insert single args failed!");
                        insertSingleArgs.result = FAIL;
                    } else {
                        insertSingleArgs.result = SUCCESS;
                    }
                    reply = insertSingleArgs.handler.obtainMessage(token);
                    reply.obj = insertSingleArgs;
                    break;
                case EVENT_ARG_MULTI_INSERT:
                    insertMultiArgs = (InsertMultiArgs) msg.obj;
                    insertMultiArgs.db.beginTransaction();
                    for (ContentValues values : insertMultiArgs.valuesList) {
                        insertMultiArgs.result = insertMultiArgs.db.insertOrThrow(insertMultiArgs.table,
                                insertMultiArgs.nullColumnHack, values);
                        if ((int) insertMultiArgs.result == -1) {
                            Logger.e(TAG + " ---->> insert multi args failed!");
                            insertMultiArgs.result = FAIL;
                            break;
                        } else {
                            insertMultiArgs.result = SUCCESS;
                        }
                    }
                    insertMultiArgs.db.setTransactionSuccessful();
                    insertMultiArgs.db.endTransaction();
                    reply = insertMultiArgs.handler.obtainMessage(token);
                    reply.obj = insertMultiArgs;
                    break;
                case EVENT_ARG_QUERY:
                    queryArgs = (QueryArgs) msg.obj;
                    Cursor cursor;
                    try {
                        cursor = queryArgs.db.query(queryArgs.distinct, queryArgs.table,
                                queryArgs.columns, queryArgs.whereClause, queryArgs.whereArgs,
                                queryArgs.groupBy, queryArgs.having, queryArgs.orderBy, queryArgs.limit);
                        // 调用这个方法会使得主线程小小的加速，此处先遍历cursor，之后在主线程中去遍历取值时会加速，好像是这样的
                        if (cursor != null) {
                            cursor.getCount();
                        }
                    } catch (Exception e) {
                        Logger.e(TAG + " ---->> Exception thrown during handling EVENT_ARG_QUERY", e);
                        cursor = null;
                    }
                    queryArgs.result = cursor;
                    reply = queryArgs.handler.obtainMessage(token);
                    reply.obj = queryArgs;
                    break;
                case EVENT_ARG_UPDATE:
                    updateArgs = (UpdateArgs) msg.obj;
                    updateArgs.result = updateArgs.db.update(updateArgs.table, updateArgs.values, updateArgs.whereClause, updateArgs.whereArgs);
                    if ((int) updateArgs.result <= 0) {
                        Logger.e(TAG + " ---->> update args failed!");
                        updateArgs.result = FAIL;
                    } else {
                        updateArgs.result = SUCCESS;
                    }
                    reply = updateArgs.handler.obtainMessage(token);
                    reply.obj = updateArgs;
                    break;
                case EVENT_ARG_DELETE:
                    deleteArgs = (DeleteArgs) msg.obj;
                    deleteArgs.result = deleteArgs.db.delete(deleteArgs.table, deleteArgs.whereClause, deleteArgs.whereArgs);
                    if ((int) deleteArgs.result <= 0) {
                        Logger.e(TAG + " ---->> delete args failed!");
                        deleteArgs.result = FAIL;
                    } else {
                        deleteArgs.result = SUCCESS;
                    }
                    reply = deleteArgs.handler.obtainMessage(token);
                    reply.obj = deleteArgs;
                    break;
                case EVENT_INIT_DATABASE:
                    initArgs = (InitArgs) msg.obj;
                    initArgs.result = initArgs.dbOpenHelper.getWritableDatabase();
                    reply = initArgs.handler.obtainMessage(token);
                    reply.obj = initArgs;
                    break;
                default:
                    return;
            }
            reply.arg1 = msg.arg1;
            reply.sendToTarget();
        }
    }

    /**
     * 数据库处理结果回调
     */
    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        int token = msg.what;
        int event = msg.arg1;

        switch (event) {
            case EVENT_ARG_SINGLE_INSERT:
                InsertSingleArgs insertSingleArgs = (InsertSingleArgs) msg.obj;
                if (insertSingleArgs.callback != null) {
                    if (insertSingleArgs.result == SUCCESS) {
                        ((ISingleInsertCallback) insertSingleArgs.callback).onSingleInsertComplete(token, insertSingleArgs.result);
                    } else {
                        insertSingleArgs.callback.onAsyncOperateFailed();
                    }
                }
                break;
            case EVENT_ARG_MULTI_INSERT:
                InsertMultiArgs insertMultiArgs = (InsertMultiArgs) msg.obj;
                if (insertMultiArgs.callback != null) {
                    if (insertMultiArgs.result ==SUCCESS) {
                        ((IMultiInsertCallback) insertMultiArgs.callback).onMultiInsertComplete(token, insertMultiArgs.result);
                    } else {
                        insertMultiArgs.callback.onAsyncOperateFailed();
                    }
                }
                break;
            case EVENT_ARG_QUERY:
                QueryArgs queryArgs = (QueryArgs) msg.obj;
                if (queryArgs.callback != null) {
                    if (queryArgs.result != null) {
                        ((IQueryCallback) queryArgs.callback).onQueryComplete(token, queryArgs.result);
                    } else {
                        queryArgs.callback.onAsyncOperateFailed();
                    }
                }
                break;
            case EVENT_ARG_UPDATE:
                UpdateArgs updateArgs = (UpdateArgs) msg.obj;
                if (updateArgs.callback != null) {
                    if (updateArgs.result == SUCCESS) {
                        ((IUpdateCallback) updateArgs.callback).onUpdateComplete(token, updateArgs.result);
                    } else {
                        updateArgs.callback.onAsyncOperateFailed();
                    }
                }
                break;
            case EVENT_ARG_DELETE:
                DeleteArgs deleteArgs = (DeleteArgs) msg.obj;
                if (deleteArgs.callback != null) {
                    if (deleteArgs.result == SUCCESS) {
                        ((IDeleteCallback) deleteArgs.callback).onDeleteComplete(token, deleteArgs.result);
                    } else {
                        deleteArgs.callback.onAsyncOperateFailed();
                    }
                }
                break;
            case EVENT_INIT_DATABASE:
                InitArgs initArgs = (InitArgs) msg.obj;
                if (initArgs.callback != null) {
                    if (initArgs.result != null) {
                        ((IInitDatabaseCallback) initArgs.callback).onInitDatabaseComplete(token, initArgs.result);
                    } else {
                        initArgs.callback.onAsyncOperateFailed();
                    }
                }
                break;
            default:
                return;
        }
    }
}
