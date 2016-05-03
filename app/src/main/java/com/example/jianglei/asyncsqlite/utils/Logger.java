package com.example.jianglei.asyncsqlite.utils;

import android.util.Log;


/**
 * Logger
 * log统一输出类
 * @author nfhu
 * @time 2014-1-16 下午10:44:04
 **/
public class Logger {
	
	/**
	 * log的开关
	 */
	private static boolean debuging = true;
	
	/**
	 * log的输出tag
	 */
	private static String TAG = "mime";

	
	/**
	 * <setDebuging>
	 * 设置log的开关
	 * @param debuging
	 **/
	public static void setDebuging(boolean debuging) {
		Logger.debuging = debuging;
	}

	/**
	 * <setTAG>
	 * 设置log的TAG
	 * @param tAG
	 **/
	public static void setTAG(String tAG) {
		TAG = tAG;
	}

	/**
	 * <i>
	 * 正常调试log输出
	 * @param msg msg log的内容
	 **/
	public static void i(String msg){
		if(debuging){
			Log.i(TAG, msg);
		}
	}
	
	/**
	 * <e>
	 * 异常log输出
	 * @param msg log的内容
	 **/
	public static void e(String msg){
		Log.e(TAG, msg);
	}
	
	public static void d(String msg){
		if(debuging){
			Log.d(TAG, msg);
		}
	}
	public static void d(String tag,String msg){
		if(debuging){
			Log.d(tag, msg);
		}
	}
	/**
	 * <w>
	 * 
	 * @param msg
	 */
	public static void w(String msg){
		if(debuging){
			Log.w(TAG, msg);
		}
	}
	
	/**
	 * <e>
	 * 异常log输出
	 * @param msg log的内容
	 * @param tr 异常信息
	 **/
	public static void e(String msg,Throwable tr){
		Log.e(TAG, msg, tr);
	}
	


}
