package com.example.jianglei.asyncsqlite.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils {

	public static Toast mToast;

	public static void makeText(Context context, String msg) {
		if (mToast == null) {
			mToast = Toast.makeText(context.getApplicationContext(), msg + "", Toast.LENGTH_SHORT);
		} else {
			mToast.setText(msg);
		}
		mToast.show();
	}

}
