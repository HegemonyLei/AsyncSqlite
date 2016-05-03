package com.example.jianglei.asyncsqlite;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.os.StrictMode;
import android.util.Log;

import com.example.jianglei.asyncsqlite.model.db.DataBaseOpenHelper;
import com.example.jianglei.asyncsqlite.model.db.DataBaseOperateToken;
import com.example.jianglei.asyncsqlite.model.db.IInitDatabaseCallback;

/**
 * Created by jianglei on 2016/4/5.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Log.e("lei", "MyApplication   onCreate");

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectAll()   // or .detectAll() for all detectable problems
                .penaltyLog()
                .penaltyFlashScreen()
                .build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build());

        DataBaseOpenHelper.init(getApplicationContext());
        DataBaseOpenHelper.getInstance().initDatabase(DataBaseOperateToken.TOKEN_INIT_DATABASE, new IInitDatabaseCallback() {
            @Override
            public void onInitDatabaseComplete(int token, SQLiteDatabase db) {
                Log.e("lei", "MyApplication  onInitDatabaseComplete");
            }

            @Override
            public void onAsyncOperateFailed() {

            }
        });
    }
}
