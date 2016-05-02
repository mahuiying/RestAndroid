package com.utopia.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.utopia.Dao.sql_SaleRecord;
import com.utopia.Dialog.LoadingDialog;
import com.utopia.Model.d_SaleRecord;
import com.utopia.activity.CookActivity;
import com.utopia.activity.R;
import com.utopia.utils.DateUtils;
import com.utopia.utils.JsonResolveUtils;
import com.utopia.widget.MyTextView;

public class CookOrderSaleRecordAdapter extends BaseAdapter implements
		OnClickListener {

	private d_SaleRecord current_sr;
	private Context context;
	private Cursor m_CallCursor;
	private boolean isHistory = false;
	private LoadingDialog mLd;

	public CookOrderSaleRecordAdapter(Context paramContext, String desk_name,
			boolean isHistory) {
		context = paramContext;
		this.isHistory = isHistory;
		mLd = new LoadingDialog((CookActivity) context,
				"In the request submission");
		if (isHistory) {
			openHistory();
		} else {
			open();
		}
	}

	public CookOrderSaleRecordAdapter(Context paramContext) {
		context = paramContext;
		openHistory();
	}

	public void open() {
		String current_time = DateUtils.getDateEN();
		Log.i("tag", "当前时间：" + current_time);
		m_CallCursor = new sql_SaleRecord()
				.recordlist3("select * from "
						+ "saleandpdt as s1 join SaleRecord as s2 on s1.salerecordId=s2.itemNo"
						+ " where s1.status1='Sent' and s1.createTime1 <='"
						+ current_time + "'");
	}

	public void openHistory() {
		m_CallCursor = new sql_SaleRecord()
				.recordlist3("select * from "
						+ "saleandpdt as s1 join SaleRecord as s2 on s1.salerecordId=s2.itemNo"
						+ " where s1.status1 = 'Done'");

		Log.i("tag", "历史菜单个数：" + m_CallCursor.getCount());
	}

	@Override
	public int getCount() {
		return m_CallCursor.getCount();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {
		if (paramView == null) {
			View localView = LayoutInflater.from(this.context).inflate(
					R.layout.cook_order_list, null);

			AppItem localAppItem2 = new AppItem();
			// 布局
			localAppItem2.item_ll = (LinearLayout) localView
					.findViewById(R.id.LinearLayout1);
			// 菜名
			localAppItem2.menu_name = ((TextView) localView
					.findViewById(R.id.menu_name));
			// 菜数量
			localAppItem2.menu_qty = ((TextView) localView
					.findViewById(R.id.menu_qty));
			// 大份 ， 小份
			localAppItem2.size = ((TextView) localView.findViewById(R.id.size));
			// 辣味
			localAppItem2.hotness = ((TextView) localView
					.findViewById(R.id.hotness));
			// 备注
			localAppItem2.notes = ((TextView) localView
					.findViewById(R.id.notes));
			// 厨师完成情况
			localAppItem2.done = (Button) localView.findViewById(R.id.Done);
			localAppItem2.createTime = (TextView) localView
					.findViewById(R.id.createTime);
			// 向localView存储数据
			localView.setTag(localAppItem2);
			paramView = localView;
		}

		this.m_CallCursor.moveToPosition(paramInt);

		d_SaleRecord locald_SaleRecord = new d_SaleRecord();
		locald_SaleRecord.setItemNo(this.m_CallCursor.getString(m_CallCursor
				.getColumnIndex("itemNo")));
		locald_SaleRecord.setDesk_name(this.m_CallCursor.getString(m_CallCursor
				.getColumnIndex("deskName")));
		locald_SaleRecord.setPdtCODE(this.m_CallCursor.getString(m_CallCursor
				.getColumnIndex("pdtCode")));
		locald_SaleRecord.setPdtName(this.m_CallCursor.getString(m_CallCursor
				.getColumnIndex("pdtName")));
		locald_SaleRecord.setNumber(this.m_CallCursor.getInt(m_CallCursor
				.getColumnIndex("number")));
		locald_SaleRecord.setPrice(Float.valueOf(
				this.m_CallCursor.getString(m_CallCursor
						.getColumnIndex("price"))).floatValue());
		locald_SaleRecord.setOtherSpecNo1(this.m_CallCursor
				.getString(m_CallCursor.getColumnIndex("otherspec1")));
		locald_SaleRecord.setOtherSpecNo2(this.m_CallCursor
				.getString(m_CallCursor.getColumnIndex("otherspec2")));
		locald_SaleRecord.setOtherSpec(this.m_CallCursor.getString(m_CallCursor
				.getColumnIndex("otherspec0")));
		locald_SaleRecord.setCreateTime(this.m_CallCursor
				.getString(m_CallCursor.getColumnIndex("createTime1")));
		locald_SaleRecord.setCloseTime(this.m_CallCursor.getString(m_CallCursor
				.getColumnIndex("closeTime1")));
		locald_SaleRecord.setPriority(this.m_CallCursor.getInt(m_CallCursor
				.getColumnIndex("priority")));
		locald_SaleRecord.setId(this.m_CallCursor.getInt(m_CallCursor
				.getColumnIndex("id")));
		locald_SaleRecord.setStatus(this.m_CallCursor.getString(m_CallCursor
				.getColumnIndex("status")));

		Log.i("tag", locald_SaleRecord.getId() + "当前菜的id");
		// 从localView读取数据
		AppItem localAppItem1 = (AppItem) paramView.getTag();
		localAppItem1.menu_name.setText(locald_SaleRecord.getPdtName());
		localAppItem1.menu_qty.setText(locald_SaleRecord.getNumber() + "");
		localAppItem1.size.setText(locald_SaleRecord.getOtherSpecNo1());
		localAppItem1.hotness.setText(locald_SaleRecord.getOtherSpecNo2());
		localAppItem1.notes.setText(locald_SaleRecord.getOtherSpec());
		localAppItem1.createTime.setText(locald_SaleRecord.getCreateTime());
		localAppItem1.done.setEnabled(false);

		if (m_CallCursor.getString(m_CallCursor.getColumnIndex("status1"))
				.equals("Sent")) {
			localAppItem1.done.setEnabled(true);
		}
		if (isHistory) {
			localAppItem1.done.setText(locald_SaleRecord.getCloseTime());

			localAppItem1.done.setBackgroundColor(Color.parseColor("#E6E6E6"));
		} else {
			// 判断该菜是否被设置了优先级
			if (locald_SaleRecord.getPriority() == 1) {
				localAppItem1.item_ll.setBackgroundColor(Color
						.parseColor("#672424"));
			} else {
				localAppItem1.item_ll.setBackgroundColor(Color
						.parseColor("#E6E6E6"));
			}
			localAppItem1.done.setTag(locald_SaleRecord);
			localAppItem1.done.setOnClickListener(this);

		}
		return paramView;
	}

	class AppItem {
		LinearLayout item_ll;
		TextView menu_name;
		TextView menu_qty;
		TextView menu_status;
		TextView menu_price;
		Button menu_discount;
		TextView subtotal;
		Button done;
		TextView size;
		TextView hotness;
		TextView notes;
		TextView createTime;
	}

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.Done) {
			// showCustomToast("Done");
			current_sr = (d_SaleRecord) view.getTag();

			// 发送后台 ， done
			new RefreshAsyncTask().execute();
			// 刷新界面
			Log.i("tag", current_sr.getStatus());
			// CookActivity cookActivity = new CookActivity();
			// 判断是否是takeout，diliver
			if (current_sr.getStatus().equals("Take Out")
					|| current_sr.getStatus().equals("Delivery")) {
				((CookActivity) context).prePrint(current_sr.getId(),
						current_sr.getStatus());

			}

		}
	}

	// 构造函数AsyncTask<Params, Progress, Result>参数说明:
	// Params 启动任务执行的输入参数
	// Progress 后台任务执行的进度
	// Result 后台计算结果的类型
	private class RefreshAsyncTask extends AsyncTask<Void, Void, Boolean> {

		// onPreExecute()方法用于在执行异步任务前,主线程做一些准备工作
		@Override
		protected void onPreExecute() {
			mLd.setText("Is updating, please wait...");
			mLd.show();
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(Void... arg0) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return new JsonResolveUtils(context).setSaleAndPdtDone(current_sr);
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (!result) {
				if (mLd.isShowing()) {
					mLd.dismiss();
				}
				showCustomToast("send status failed !");
			} else {
				if (mLd.isShowing()) {
					mLd.dismiss();
				}
				new sql_SaleRecord().update("Done", DateUtils.getDateEN(),
						current_sr.getPdtName(), current_sr.getItemNo(),
						current_sr.getCreateTime());
				CookActivity.sladapter.open();
				CookActivity.sladapter.notifyDataSetChanged();
				showCustomToast("send status successed !");
			}
		}

	}

	/** 显示自定义Toast提示(来自String) **/
	protected void showCustomToast(String text) {
		View toastRoot = LayoutInflater.from(context).inflate(
				R.layout.common_toast, null);
		((MyTextView) toastRoot.findViewById(R.id.toast_text)).setText(text);
		Toast toast = new Toast(context);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(toastRoot);
		toast.show();
	}
}
