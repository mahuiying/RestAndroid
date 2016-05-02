package com.utopia.activity;

import java.io.UnsupportedEncodingException;
import java.util.List;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.utopia.Adapter.CookOrderSaleRecordAdapter;
import com.utopia.Base.BaseActivity;
import com.utopia.Dao.sql_SaleRecord;
import com.utopia.Dao.sql_Saleandpdt;
import com.utopia.Dao.sql_Sales;
import com.utopia.Model.d_Sale;
import com.utopia.Model.d_Saleandpdt;
import com.utopia.Service.BluetoothService;
import com.utopia.Service.HomeKeyLocker;
import com.utopia.utils.Constant;
import com.utopia.utils.ExitApplication;
import com.utopia.utils.JsonResolveUtils;

@SuppressLint("HandlerLeak")
public class CookActivity extends BaseActivity implements View.OnClickListener {

	public static CookOrderSaleRecordAdapter sladapter;

	private Button current;
	private Button history;
	private List<d_Sale> saleRecord;
	private List<d_Saleandpdt> sale;
	private ListView localListView;
	private HomeKeyLocker mHomeKeyLocker;
	boolean isHistory = false;

	private BluetoothAdapter mBluetoothAdapter;

	@Override
	public void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		setContentView(R.layout.cook);
		ExitApplication.getInstance().addActivity(this);// 加入退出栈
		mHomeKeyLocker = new HomeKeyLocker();
		// mHomeKeyLocker.lock(CookActivity.this);

		// add by tcl
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

		new sql_SaleRecord().clear_all();
		// 得到数据库数据 ， 并且显示在GridView中
		initViews();
		initEvents();
		initlist();

	}

	protected void onDestroy() {
		mHomeKeyLocker.unlock();
		mHomeKeyLocker = null;
		super.onDestroy();
	}

	@Override
	public void onPause() {
		super.onPause();
		handler.removeCallbacks(task);
	}

	/*
	 * 得到数据库中数据
	 */
	public void initlist() {
		ListView localListView = (ListView) findViewById(R.id.cook_menu_list);
		sladapter = new CookOrderSaleRecordAdapter(this, Constant.table_id,
				false);

		// 显示数据
		localListView.setAdapter(sladapter);
	}

	/*
	 * 得到历史数据库中数据
	 */
	public void initHistoryList() {
		sladapter = new CookOrderSaleRecordAdapter(this, Constant.table_id,
				true);
		// 显示数据
		localListView.setAdapter(sladapter);
	}

	@Override
	public void onClick(View paramView) {
		switch (paramView.getId()) {
		default:
			break;
		case R.id.goback:
			ExitApplication.getInstance().exit();
			break;

		case R.id.current:
			isHistory = false;
			initlist();
			current.setBackgroundColor(Color.parseColor("#7EA343"));
			history.setBackgroundColor(Color.parseColor("#666666"));
			break;
		case R.id.history:
			isHistory = true;
			initHistoryList();
			current.setBackgroundColor(Color.parseColor("#666666"));
			history.setBackgroundColor(Color.parseColor("#7EA343"));
			break;

		}
	}

	public void Refresh() {
		if (isHistory) {
			sladapter.openHistory();
		} else {
			sladapter.open();
		}
		sladapter.notifyDataSetChanged();
	}

	private boolean isFirst = true;

	@Override
	protected void onResume() {
		super.onResume();
		if (isFirst) {
			Toast.makeText(CookActivity.this, "please wait...",
					Toast.LENGTH_LONG).show();
			isFirst = false;
		}
		Refresh();
		handler.post(task);// 立即调用刷新代码
	}

	@Override
	protected void initViews() {
		localListView = (ListView) findViewById(R.id.cook_menu_list);
		current = (Button) findViewById(R.id.current);
		history = (Button) findViewById(R.id.history);
		current.setBackgroundColor(Color.parseColor("#7EA343"));
		history.setBackgroundColor(Color.parseColor("#666666"));
	}

	@Override
	protected void initEvents() {
		// 返回
		((Button) findViewById(R.id.goback)).setOnClickListener(this);

		// current
		findViewById(R.id.current).setOnClickListener(this);
		// history
		findViewById(R.id.history).setOnClickListener(this);

	}

	// 构造函数AsyncTask<Params, Progress, Result>参数说明:
	// Params 启动任务执行的输入参数
	// Progress 后台任务执行的进度
	// Result 后台计算结果的类型
	private class RefreshAsyncTask extends AsyncTask<String, Integer, String> {
		// onPreExecute()方法用于在执行异步任务前,主线程做一些准备工作
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// if (isFirstRun) {
			// showLoadingDialog("please wait...");
			// isFirstRun = false;
			// }
		}

		// doInBackground()方法用于在执行异步任务,不可以更改主线程中UI
		@Override
		protected String doInBackground(String... params) {
			System.out.println("调用doInBackground()方法--->开始执行异步任务");
			saleRecord = new JsonResolveUtils(CookActivity.this)
					.getSaleRecords();
			sale = new JsonResolveUtils(CookActivity.this).getSaleandpdt();
			if (sale.size() < 1) {
				return null;
			}
			return sale.size() + "";
		}

		// onPostExecute()方法用于异步任务执行完成后,在主线程中执行的操作
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			System.out.println("调用onPostExecute()方法--->异步任务执行完毕");
			handler.sendEmptyMessage(0);

			// if (!TextUtils.isEmpty(result)) {
			//
			// }
			// dismissLoadingDialog();
		}

		// onCancelled()方法用于异步任务被取消时,在主线程中执行相关的操作
		@Override
		protected void onCancelled() {
			super.onCancelled();
			System.out.println("调用onCancelled()方法--->异步任务被取消");
		}
	}

	private Handler handler = new Handler() {
		/**
		 * 子类必须重写此方法,接受数据 接收消息并更新界面信息
		 */
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			// 此处可以更新UI
			if (sale.size() > 0) {
				for (int i = 0; i < sale.size(); i++)
					(new sql_Saleandpdt()).saveInit(sale.get(i));
				if (saleRecord.size() > 0) {
					for (int i = 0; i < saleRecord.size(); i++)
						(new sql_Sales()).saveInit(saleRecord.get(i));
					Constant.lastTime = (new sql_SaleRecord()).getLastTime();
					Refresh();
				}
			}

		}
	};

	private Runnable task = new Runnable() {
		public void run() {

			// 设置定时器，实现每10秒 向后台请求一下数据
			handler.postDelayed(this, 5 * 1000);// 设置延迟时间，此处是5秒

			// 需要执行的代码
			// 从服务器获取数据
			new RefreshAsyncTask().execute();
			Refresh();
		}
	};
	private String type;
	private int id;

	private BluetoothService mService;

	public void prePrint(int id, String type) {
		this.type = type;
		this.id = id;
		startActivityForResult(new Intent(CookActivity.this,
				BluetoothListActivity.class), 1);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) { // resultCode为回传的标记，我在B中回传的是RESULT_OK
		case RESULT_OK:
			BluetoothDevice device = mBluetoothAdapter
					.getRemoteDevice(Constant.printerAddress);
			if (mBluetoothAdapter == null) {
				showCustomToast("Printer is not available");
			} else {
				mService.connect(device);
				printTest();
			}
		}
	}

	private void printTest() {
		putAsyncTask(new AsyncTask<Void, Void, Boolean>() {
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				showLoadingDialog("Just a moment, please...");
			}

			@Override
			protected Boolean doInBackground(Void... params) {
				try {
					int i = 0;
					while (mService.getState() != BluetoothService.STATE_CONNECTED) {
						Thread.sleep(500);
						if (i++ > 30) {
							mService.stop();
							Thread.sleep(500);
							return false;
						}
					}
				} catch (InterruptedException e) {
					mService.stop();
					e.printStackTrace();
					return false;
				}
				return true;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);
				dismissLoadingDialog();
				if (!result) {
					showCustomToast("Printer authentication failed !");
				} else {
					print();
				}
			}
		});
	}

	protected void print() {
		Log.i("tag", "print");
		if (mService == null)
			mService = new BluetoothService(this, mHandler);

		if (!fontGrayscaleSet(4)) {
			return;
		}
		// 初始化打印机
		byte[] byteInit = new byte[] { 0x1B, 0x40 };
		mService.write(byteInit);

		// 左对齐
		byte[] byteC = new byte[] { 0x1B, 0x61, (byte) 0x30 };

		// 打印标题
		byte[] byteA = new byte[] { 0x1D, 0x21, (byte) 0x0110 };
		mService.write(byteA);
		sendMessage("\n\nShangri-La\n\n");

		byte[] byteB = new byte[] { 0x1D, 0x21, (byte) 0x0000 };
		mService.write(byteB);

		sendMessage("\n\n");
		sendMessage("-----------" + type + "------------\n");
		Cursor m_CallCursor;
		m_CallCursor = new sql_SaleRecord()
				.recordlist3("select * from saleandpdt where id = " + id);
		if (m_CallCursor.moveToNext()) {
			mService.write(byteB);

			sendMessage("CreateTime:  "
					+ lpad(m_CallCursor.getString(m_CallCursor
							.getColumnIndex("createTime1")), 9) + "\n");
			mService.write(byteB);
			sendMessage("CloseTime:   "
					+ lpad(m_CallCursor.getString(m_CallCursor
							.getColumnIndex("closeTime1")), 9) + "\n");
			mService.write(byteB);

			sendMessage("Size:        "
					+ lpad(m_CallCursor.getString(m_CallCursor
							.getColumnIndex("otherspec1")), 9) + "\n");
			mService.write(byteB);
			sendMessage("Notes:       "
					+ lpad(m_CallCursor.getString(m_CallCursor
							.getColumnIndex("otherspec0")), 9) + "\n");
			mService.write(byteB);
			sendMessage("Hotness:     "
					+ lpad(m_CallCursor.getString(m_CallCursor
							.getColumnIndex("otherspec2")), 9) + "\n");
			mService.write(byteB);
			sendMessage("PdtName:     "
					+ lpad(m_CallCursor.getString(m_CallCursor
							.getColumnIndex("pdtName")), 9) + "\n");
			sendMessage("Number:      "
					+ lpad(m_CallCursor.getString(m_CallCursor
							.getColumnIndex("number")), 9) + "\n");
			mService.write(byteB);
			sendMessage("Price:       "
					+ lpad(m_CallCursor.getString(m_CallCursor
							.getColumnIndex("price")), 9) + "\n");
			mService.write(byteB);
			sendMessage("-------------------------------\n");
			mService.write(byteB);
			sendMessage("Total:       "
					+ lpad((Float.parseFloat(m_CallCursor
							.getString(m_CallCursor.getColumnIndex("price"))) * Integer.parseInt(m_CallCursor
							.getString(m_CallCursor.getColumnIndex("number"))))
							+ "", 9) + "\n");
			mService.write(byteB);

			mService.write(byteC);
		}
		mService.write(byteB);
		openBox();
	}

	private void openBox() {
		byte[] byteA = new byte[] { 0x1B, 0x70, 0x00, (byte) 0xFE, (byte) 0xFE };
		mService.write(byteA);

		if (mService != null)
			mService.stop();
	}

	private void sendMessage(String message) {
		// Check that we're actually connected before trying anything
		if (mService.getState() != BluetoothService.STATE_CONNECTED) {

			// showCustomToast("The printer connection error");
			return;
		}

		// Check that there's actually something to send
		if (message.length() > 0) {
			// Get the message bytes and tell the BluetoothService to write
			byte[] send;
			try {
				send = message.getBytes("GB2312");
			} catch (UnsupportedEncodingException e) {
				send = message.getBytes();
			}
			mService.write(send);
		}
	}

	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
		}
	};

	/**
	 * Set font gray scale
	 */
	private boolean fontGrayscaleSet(int ucFontGrayscale) {
		// Check that we're actually connected before trying anything
		if (mService.getState() != BluetoothService.STATE_CONNECTED) {
			showCustomToast("Connection error");
			return false;
		}
		if (ucFontGrayscale < 1)
			ucFontGrayscale = 4;
		if (ucFontGrayscale > 8)
			ucFontGrayscale = 8;
		byte[] send = new byte[3];// ESC m n
		send[0] = 0x1B;
		send[1] = 0x6D;
		send[2] = (byte) ucFontGrayscale;
		mService.write(send);
		return true;
	}

	@Override
	protected void onStart() {
		super.onStart();
		if (mService == null)
			mService = new BluetoothService(this, mHandler);
	}

	/**
	 * 补齐不足长度
	 * 
	 * @param length
	 *            长度
	 * @param str
	 *            字符串
	 * @return
	 */
	private String lpad(String str, int length) {
		StringBuilder sb = new StringBuilder(str);
		while (sb.length() < length) {
			sb.append(" ");
		}

		return sb.toString();
	}

}
