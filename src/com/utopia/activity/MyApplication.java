package com.utopia.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.lang.Thread.UncaughtExceptionHandler;

import android.app.Application;
import android.os.Environment;
import android.util.Log;

public class MyApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();

		// 设置异常的处理器
		Thread.currentThread().setUncaughtExceptionHandler(
				new MyUncaughtExceptionHandler());
	}

	private class MyUncaughtExceptionHandler implements
			UncaughtExceptionHandler {

		// 异常处理器处理的代码 先输出日志 再自杀（闪退一下，程序不崩）
		@Override
		public void uncaughtException(Thread thread, Throwable ex) {
			// 死前的遗言
			Log.i("tag", "出现异常了...");
			ex.printStackTrace();// 异常打印到控制台
			PrintStream err = null;
			try {
				err = new PrintStream(new File(Environment
						.getExternalStorageDirectory().getAbsoluteFile()
						+ "/error.txt"));
				ex.printStackTrace(err);// 把异常收集到文件
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} finally {
				if (err != null) {
					err.close();
				}
			}

			// // 把异常文件发送到服务端
			// String path =
			// "http://192.168.20.91:8080/web/servlet/UploadServlet";
			//
			// AsyncHttpClient client = new AsyncHttpClient();
			// // 封装要提交的数据
			// RequestParams params = new RequestParams();
			//
			// // 上传文件
			// File file = new File(Environment.getExternalStorageDirectory()
			// .getAbsoluteFile() + "/error.txt");
			// try {
			// params.put("error", file);
			//
			// // 执行post请求
			// // path 请求的url
			// // params 封装要提交的数据
			// // responseHandler 响应的处理器
			// client.post(path, params, new AsyncHttpResponseHandler() {
			//
			// /**
			// * 请求处理成功后调用这个方法 statusCode 响应码 200 404 503 headers 响应头信息
			// * responseBody 服务器返回的响应数据（如：登陆成功、失败等）
			// */
			// @Override
			// public void onSuccess(int statusCode, Header[] headers,
			// byte[] responseBody) {
			// }
			//
			// /**
			// * 请求处理失败后调用这个方法 statusCode 响应码 200 404 503 headers 响应头信息
			// * responseBody 服务器返回的响应数据（如：登陆成功、失败等） Throwable 异常对象
			// */
			// @Override
			// public void onFailure(int statusCode, Header[] headers,
			// byte[] responseBody, Throwable error) {
			// }
			// });
			// } catch (FileNotFoundException e) {
			// // 和java类中的process区分 // 自杀 闪退 // 杀死本进程
			// android.os.Process.killProcess(android.os.Process.myPid());
			// e.printStackTrace();
			// }

			// 和java类中的process区分 // 自杀 闪退 // 杀死本进程
			android.os.Process.killProcess(android.os.Process.myPid());
		}
	}
}
