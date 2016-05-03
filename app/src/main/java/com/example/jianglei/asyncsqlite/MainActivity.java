package com.example.jianglei.asyncsqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.jianglei.asyncsqlite.db.DataBase;
import com.example.jianglei.asyncsqlite.db.DataBaseOpenHelper;
import com.example.jianglei.asyncsqlite.db.DataBaseOperateToken;
import com.example.jianglei.asyncsqlite.db.IDeleteCallback;
import com.example.jianglei.asyncsqlite.db.IMultiInsertCallback;
import com.example.jianglei.asyncsqlite.db.IQueryCallback;
import com.example.jianglei.asyncsqlite.db.ISingleInsertCallback;
import com.example.jianglei.asyncsqlite.db.IUpdateCallback;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button button1;
    Button button2;
    Button button3;
    Button button4;
    Button button5;
    ListView listView;
    MyAdapter adapter;
    List<Info> infos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        button4 = (Button) findViewById(R.id.button4);
        button5 = (Button) findViewById(R.id.button5);
        listView = (ListView) findViewById(R.id.list_view);

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);

        infos = new ArrayList<>();
        adapter = new MyAdapter(this, infos);
        listView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                Info info1 = new Info("test_single", "10");
                ContentValues values = new ContentValues();
                values.put("name", info1.getName());
                values.put("age", info1.getAge());
                DataBaseOpenHelper.getInstance().insertSingleValues(DataBaseOperateToken.TOKEN_INSERT_SINGLE_INFO, DataBase.TABLE, null, values, new ISingleInsertCallback() {
                    @Override
                    public void onSingleInsertComplete(int token, long result) {
                        ToastUtils.makeText(MainActivity.this, "插入一条成功");
                        queryAllInfo();
                    }

                    @Override
                    public void onAsyncOperateFailed() {
                        ToastUtils.makeText(MainActivity.this, "插入一条失败");
                    }
                });
                break;
            case R.id.button2:
                List<ContentValues> valuesList = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    Info info2 = new Info("test_multi_" + i, "11");
                    ContentValues values2 = new ContentValues();
                    values2.put("name", info2.getName());
                    values2.put("age", info2.getAge());
                    valuesList.add(values2);
                }
                DataBaseOpenHelper.getInstance().insertMultiValues(DataBaseOperateToken.TOKEN_INSERT_MULTI_INFO, DataBase.TABLE, null, valuesList, new IMultiInsertCallback() {
                    @Override
                    public void onMultiInsertComplete(int token, long result) {
                        ToastUtils.makeText(MainActivity.this, "插入三条成功");
                        queryAllInfo();
                    }

                    @Override
                    public void onAsyncOperateFailed() {
                        ToastUtils.makeText(MainActivity.this, "插入三条失败");
                    }
                });
                break;
            case R.id.button3:
                queryAllInfo();
                break;
            case R.id.button4:
                Info info4 = new Info("test_update", "100000");
                ContentValues values4 = new ContentValues();
                values4.put("name", info4.getName());
                values4.put("age", info4.getAge());
                DataBaseOpenHelper.getInstance().updateValues(DataBaseOperateToken.TOKEN_UPDATE_CURRENT_INFO, DataBase.TABLE, values4, "name=?", new String[]{"test_single"}, new IUpdateCallback() {
                    @Override
                    public void onUpdateComplete(int token, long result) {
                        ToastUtils.makeText(MainActivity.this, "更新成功");
                        queryAllInfo();
                    }

                    @Override
                    public void onAsyncOperateFailed() {
                        ToastUtils.makeText(MainActivity.this, "更新失败");
                    }
                });
                break;
            case R.id.button5:
                DataBaseOpenHelper.getInstance().deleteValues(DataBaseOperateToken.TOKEN_DELETE_TABLE, DataBase.TABLE, null, null, new IDeleteCallback() {
                    @Override
                    public void onDeleteComplete(int token, long result) {
                        ToastUtils.makeText(MainActivity.this, "删除成功");
                        queryAllInfo();
                    }

                    @Override
                    public void onAsyncOperateFailed() {
                        ToastUtils.makeText(MainActivity.this, "删除失败");
                    }
                });
                queryAllInfo();
                break;
            default:break;
        }
    }

    private void queryAllInfo() {
        DataBaseOpenHelper.getInstance().queryValues(DataBaseOperateToken.TOKEN_QUERY_TABLE, false, DataBase.TABLE, null, null, null, null, null, null, null, new IQueryCallback() {
            @Override
            public void onQueryComplete(int token, Cursor cursor) {
                getAllInfo(cursor);
            }

            @Override
            public void onAsyncOperateFailed() {

            }
        });
    }

    private void getAllInfo(Cursor cursor) {
        if (cursor != null) {
            infos.clear();
            while (cursor.moveToNext()) {
                Info info = new Info(cursor.getString(cursor.getColumnIndex("name")), cursor.getString(cursor.getColumnIndex("age")));
                infos.add(info);
            }
            adapter.notifyDataSetChanged();
            cursor.close();
        }
    }
}
