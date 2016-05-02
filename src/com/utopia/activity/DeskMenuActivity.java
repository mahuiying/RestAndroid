package com.utopia.activity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.utopia.Adapter.DeliveryAdapter;
import com.utopia.Adapter.DeskAdapter;
import com.utopia.Adapter.DrawerAdapter;
import com.utopia.Adapter.PayHistoryAdapter;
import com.utopia.Adapter.SalesAdapter;
import com.utopia.Adapter.payInOutHistory;
import com.utopia.Base.BaseActivity;
import com.utopia.Dao.sql_Saleandpdt;
import com.utopia.Dao.sql_desk;
import com.utopia.Dialog.pop_drawer_dialog;
import com.utopia.Dialog.pop_end_drawer;
import com.utopia.Model.d_Cashier;
import com.utopia.Model.d_CashierInfor;
import com.utopia.Model.d_Desk;
import com.utopia.Model.d_Sale;
import com.utopia.Model.d_Saleandpdt;
import com.utopia.Model.d_pay_in_out;
import com.utopia.utils.Constant;
import com.utopia.utils.DateUtils;
import com.utopia.utils.DensityUtil;
import com.utopia.utils.ExitApplication;
import com.utopia.utils.JsonResolveUtils;
import com.utopia.widget.MyScrollLayout;
import com.utopia.widget.SlideMenu;

@SuppressLint("HandlerLeak")
public class DeskMenuActivity extends BaseActivity implements OnClickListener {
	private com.utopia.widget.SlideMenu slideMenu;
	DecimalFormat df = new DecimalFormat("###.00");
	private DeskAdapter localAdapter = null;
	private List<d_Desk> desks;
	private sql_desk sqldesk;
	private GridView localGridView, deliverGridView, pickupGridView;
	private MyScrollLayout curPage;
	private List<d_Desk> lstDate = new ArrayList<d_Desk>();
	/** 每页显示的数量，Adapter保持一致. */
	private static final float PAGE_SIZE = 35.0f;
	private ImageView iv;

	private TextView tv_liquors;
	private TextView tv_main_tables;
	private TextView tv_sushi_bar;
	private TextView tv_take_out;
	private TextView tv_delivery;
	private TextView tv_my_sales_report;
	private TextView tv_current_drawer;
	private TextView tv_sales_report;
	private TextView tv_drawers_history;

	/** 总页数. */
	private int PageCount;

	private TextView manager_Shift_Time;
	private TextView manager_net_sale;
	private TextView manager_transactions;
	private TextView manager_tips;
	private ListView manager_shift_history;
	private LinearLayout shiftAll;
	private LinearLayout shiftSingle;
	private Button back_btn;
	private LinearLayout drawer_history_ll;
	private LinearLayout drawer_detial_ll;
	private TextView back_btn1;
	
	private List<d_Sale> shift_history_data;

	// private LinearLayout menu_setting;
	private LinearLayout menu_liquors;
	private LinearLayout menu_regular_tables;
	private LinearLayout menu_sushi_bar;
	private LinearLayout menu_take_out;
	private LinearLayout menu_dilivery;
	private LinearLayout menu_staff_sales;// 员工的销售记录
	private LinearLayout menu_current_drawer;// 柜台里的钱数
	private LinearLayout menu_manager_sales;// 经理的销售记录
	private LinearLayout menu_drawers_history;// 柜台里的收入支出记录

	private LinearLayout linearLayout;

	/*
	 * 钱箱
	 */
	private boolean tag = false;// 标记点击了payIn还是payOut
	private pop_drawer_dialog drawer_dialog;
	private LinearLayout ll_start_drawer;
	private LinearLayout ll_started_drawer;
	private TextView start_time_tv;
	private TextView start_time;

	private int mViewCount;
	private int mCurSel;
	private ImageView[] mImageViews;

	String status = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ExitApplication.getInstance().addActivity(this);
		initRegular();
	}

	@SuppressWarnings("deprecation")
	public void initlist() {
		curPage.getLayoutParams().height = this.getWindowManager()
				.getDefaultDisplay().getHeight();

		linearLayout = (LinearLayout) findViewById(R.id.llayout);
		mViewCount = curPage.getChildCount();
		mImageViews = new ImageView[linearLayout.getChildCount()];

		for (int i = 0; i < mViewCount; i++) {
			mImageViews[i] = (ImageView) linearLayout.getChildAt(i);
			mImageViews[i].setEnabled(true);
			mImageViews[i].setOnClickListener(this);
			mImageViews[i].setTag(i);
		}

		for (int i = mViewCount; i < linearLayout.getChildCount(); i++) {
			mImageViews[i] = (ImageView) linearLayout.getChildAt(i);
			mImageViews[i].setVisibility(8);
		}

		mCurSel = 0;
		mImageViews[mCurSel].setEnabled(false);
		mImageViews[mCurSel].setImageDrawable(getResources().getDrawable(
				R.drawable.current));
	}

	public void initViews() {
		// drops = (RelativeLayout) settings.findViewById(R.id.drops);
		// pay_out = (RelativeLayout) settings.findViewById(R.id.pay_out);
		// personal_report = (RelativeLayout) settings
		// .findViewById(R.id.personal_report);
		// purchase = (RelativeLayout) settings.findViewById(R.id.purchase);
		// close_shift = (RelativeLayout)
		// settings.findViewById(R.id.close_shift);

		iv = (ImageView) findViewById(R.id.title_bar_menu_btn);
		// menu_setting = (LinearLayout) findViewById(R.id.menu_setting);

		menu_liquors = (LinearLayout) findViewById(R.id.menu_liquors);
		menu_dilivery = (LinearLayout) findViewById(R.id.menu_dilivery);
		menu_regular_tables = (LinearLayout) findViewById(R.id.menu_regular_tables);
		menu_sushi_bar = (LinearLayout) findViewById(R.id.menu_sushi_bar);
		menu_take_out = (LinearLayout) findViewById(R.id.menu_take_out);
		menu_staff_sales = (LinearLayout) findViewById(R.id.staff_ll1);
		menu_current_drawer = (LinearLayout) findViewById(R.id.staff_ll2);
		menu_manager_sales = (LinearLayout) findViewById(R.id.manager_ll1);
		menu_drawers_history = (LinearLayout) findViewById(R.id.manager_ll2);

		tv_main_tables = (TextView) findViewById(R.id.tv_main_tables);
		tv_liquors = (TextView) findViewById(R.id.tv_liquors);
		tv_sushi_bar = (TextView) findViewById(R.id.tv_sushi_bar);
		tv_take_out = (TextView) findViewById(R.id.tv_take_out);
		tv_delivery = (TextView) findViewById(R.id.tv_delivery);
		tv_my_sales_report = (TextView) findViewById(R.id.tv_my_sales_report);
		tv_current_drawer = (TextView) findViewById(R.id.tv_current_drawer);
		tv_sales_report = (TextView) findViewById(R.id.tv_sales_report);
		tv_drawers_history = (TextView) findViewById(R.id.tv_drawers_history);

		if (Constant.Area.equals("Tables")) {
			menu_regular_tables.setBackgroundColor(Color.parseColor("#797979"));
			tv_main_tables.setTextColor(Color.parseColor("#97DA00"));
		} else if (Constant.Area.equals("Liquor Bar")) {
			menu_liquors.setBackgroundColor(Color.parseColor("#797979"));
			tv_liquors.setTextColor(Color.parseColor("#97DA00"));
		} else if (Constant.Area.equals("Sushi Bar")) {
			menu_sushi_bar.setBackgroundColor(Color.parseColor("#797979"));
			tv_sushi_bar.setTextColor(Color.parseColor("#97DA00"));
		} else if (Constant.Area.equals("Take Out")) {
			menu_take_out.setBackgroundColor(Color.parseColor("#797979"));
			tv_take_out.setTextColor(Color.parseColor("#97DA00"));
		} else if (Constant.Area.equals("Delivery")) {
			menu_dilivery.setBackgroundColor(Color.parseColor("#797979"));
			tv_delivery.setTextColor(Color.parseColor("#97DA00"));
		} else if (Constant.Area.equals("MY SALES REPORT")) {
			menu_staff_sales.setBackgroundColor(Color.parseColor("#797979"));
			tv_my_sales_report.setTextColor(Color.parseColor("#97DA00"));
		} else if (Constant.Area.equals("CURRENT DRAWER")) {
			menu_current_drawer.setBackgroundColor(Color.parseColor("#797979"));
			tv_current_drawer.setTextColor(Color.parseColor("#97DA00"));
		} else if (Constant.Area.equals("SALES REPORT")) {
			menu_manager_sales.setBackgroundColor(Color.parseColor("#797979"));
			tv_sales_report.setTextColor(Color.parseColor("#97DA00"));
		} else if (Constant.Area.equals("DRAWERS HISTORY")) {
			menu_drawers_history
					.setBackgroundColor(Color.parseColor("#797979"));
			tv_drawers_history.setTextColor(Color.parseColor("#97DA00"));
		}
		lstDate = new sql_desk(this).queryMenus(Constant.Area);
		PageCount = (int) Math.ceil(lstDate.size() / PAGE_SIZE);
		if (localGridView != null && curPage != null) {
			curPage.removeAllViews();
		}
		for (int i = 0; i < PageCount; i++) {
			localGridView = new GridView(DeskMenuActivity.this);
			localAdapter = new DeskAdapter(this, lstDate, i);
			localGridView.setAdapter(localAdapter);
			localGridView.setNumColumns(7);
			localGridView.setHorizontalSpacing(20);
			localGridView.setVerticalSpacing(20);
			curPage.addView(localGridView);
		}
	}

	public void Refresh() {
		// localAdapter.open();
		new RefreshAsyncTask().execute();
		localAdapter.notifyDataSetChanged();
	}

	@Override
	public void onResume() {
		super.onResume();
		handler.post(task);// 立即调用刷新代码
		// 此处可以更新UI
		if (Constant.Area.equals("Delivery")) {
			initDeliver();
		} else if (Constant.Area.equals("Take Out")) {
			initTakeOut();
		} else {
			initViews();
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		handler.removeCallbacks(task);
	}

	private Runnable task = new Runnable() {
		public void run() {
			handler.postDelayed(this, 10 * 1000);// 设置延迟时间，此处是5秒
			// 需要执行的代码
			new RefreshAsyncTask().execute();
		}
	};

	private Handler handler = new Handler() {
		/**
		 * 子类必须重写此方法,接受数据 接收消息并更新界面信息
		 */
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			// 此处可以更新UI
			if (Constant.Area.equals("Delivery")) {
				initDeliver();
			} else if (Constant.Area.equals("Take Out")) {
				initTakeOut();
			} else {
				initViews();
			}

		}
	};

	private PopupWindow startPopupWindow;
	private View startPopupView;
	private View startView;

	private PopupWindow endPopupWindow;
	private View endPopupView;

	// private View endView;

	// 构造函数AsyncTask<Params, Progress, Result>参数说明:
	// Params 启动任务执行的输入参数
	// Progress 后台任务执行的进度
	// Result 后台计算结果的类型
	private class RefreshAsyncTask extends AsyncTask<String, Integer, String> {
		// onPreExecute()方法用于在执行异步任务前,主线程做一些准备工作
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		List<d_Saleandpdt> saleandpdt;

		// doInBackground()方法用于在执行异步任务,不可以更改主线程中UI
		@Override
		protected String doInBackground(String... params) {
			System.out.println("调用doInBackground()方法--->开始执行异步任务");
			desks = new JsonResolveUtils(DeskMenuActivity.this)
					.getDesks(Constant.Area); // 从后台根据桌子所在区域返回值。
			String time = DateUtils.getDateEN();
			time = time.substring(0, 10) + " 00:00:00";
			Log.i("tag", time);
			Constant.lastTime = time;
			saleandpdt = new JsonResolveUtils(DeskMenuActivity.this)
					.getSaleandpdt();
			return null;
		}

		// onPostExecute()方法用于异步任务执行完成后,在主线程中执行的操作
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			System.out.println("调用onPostExecute()方法--->异步任务执行完毕");
			for (int i = 0; i < desks.size(); i++) {
				sqldesk.opendesk(desks.get(i));
			}

			for (int i = 0; i < saleandpdt.size(); i++) {
				(new sql_Saleandpdt()).saveInit(saleandpdt.get(i));

				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			handler.sendEmptyMessage(0);
		}

		// onCancelled()方法用于异步任务被取消时,在主线程中执行相关的操作
		@Override
		protected void onCancelled() {
			super.onCancelled();
			System.out.println("调用onCancelled()方法--->异步任务被取消");
		}
	}

	@Override
	public void onBackPressed() {

		if (Constant.pop) {
			Constant.pop = false;
			getSlideMenu().closeMenu();
			// curPage.setAlpha(1.0f);
			iv.setBackgroundColor(Color.parseColor("#00FFFFFF"));
			// if (popup.isShowing()) {
			// popup.dismiss();
			// }
		}

		if (startPopupWindow != null && startPopupView != null
				&& startPopupWindow.isShowing()) {
			startPopupWindow.dismiss();
		}

		if (endPopupView != null && endPopupWindow != null
				&& endPopupWindow.isShowing()) {
			endPopupWindow.dismiss();
		}
	}

	@Override
	protected void initEvents() {
		iv.setOnClickListener(this);
		// menu_setting.setOnClickListener(this);

		menu_liquors.setOnClickListener(this);
		menu_dilivery.setOnClickListener(this);
		menu_regular_tables.setOnClickListener(this);
		menu_sushi_bar.setOnClickListener(this);
		menu_take_out.setOnClickListener(this);
		menu_staff_sales.setOnClickListener(this);
		menu_current_drawer.setOnClickListener(this);
		menu_manager_sales.setOnClickListener(this);
		menu_drawers_history.setOnClickListener(this);
		// drops.setOnClickListener(this);
		// pay_out.setOnClickListener(this);
		// personal_report.setOnClickListener(this);
		// purchase.setOnClickListener(this);
		// close_shift.setOnClickListener(this);

		curPage.setPageListener(new MyScrollLayout.PageListener() {
			@Override
			public void page(int page) {
				// Log.e("Desk",page+"");
				setCurPoint(page);
			}
		});
	}

	private void setCurPoint(int index) {
		if (index < 0 || index > mViewCount - 1 || mCurSel == index) {
			return;
		}
		mImageViews[mCurSel].setEnabled(true);
		mImageViews[index].setEnabled(false);
		mImageViews[mCurSel].setImageDrawable(getResources().getDrawable(
				R.drawable.notcurrent));
		mImageViews[index].setImageDrawable(getResources().getDrawable(
				R.drawable.current));
		mCurSel = index;

	}

	public com.utopia.widget.SlideMenu getSlideMenu() {
		return slideMenu;
	}

	public void setSlideMenu(com.utopia.widget.SlideMenu slideMenu) {
		this.slideMenu = slideMenu;
	}

	private TextView choose_drawer;
	private String start_cash_amount_s = "";
	private TextView startring_time;
	private TextView startring_cash;
	private EditText start_cash_amount;
	// LinearLayout ll_current_drawer_view;

	private LinearLayout ll_payIn_out_view;
	private LinearLayout ll_start_cash_view;
	private LinearLayout ll_started_cash_view;
	private Button start_drawer;
	private TextView cash_sales;
	private TextView payin_out_cash;
	private TextView expect_cash;
	private TextView paidin_out_sum;
	private LinearLayout paidin_out_sum_view;
	private Button end_drawer;

	private TextView starting_cash;
	private TextView end_starting_time;
	private EditText end_actual_cash;
	private TextView end_difference;
	private Button end_drawer_p;
	private TextView end_expected_cash;
	private TextView choose_drawer_des;

	private Button payIn_out_cancel;
	private Button payIn_out_save;
	private EditText payIn_out_des;
	private Button payIn;
	private Button payOut;
	private TextView payIn_out_enter_amount;
	private TextView payIn_out_enter_des;
	private TextView net_cash_paid_in;
	private ListView pay_history;
	private TextView net_pay_in_cash;

	private ListView lv_payIn_out;

	private String difference_cash;
	private String actual_cash;
	private String expected_cash;

	private d_Cashier cashier = new d_Cashier();

	class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return 0;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			return null;
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_bar_menu_btn:
			Constant.pop = true;
			iv.setBackgroundColor(Color.parseColor("#65696A"));
			if (getSlideMenu().isMainScreenShowing()) {
				getSlideMenu().openMenu();
				// curPage.setAlpha(0.5f);
				if (choose_drawer != null) {
					choose_drawer.setEnabled(false);
				}
				if (start_drawer != null) {
					start_drawer.setEnabled(false);
				}
				if (start_cash_amount != null) {
					start_cash_amount.setEnabled(false);
				}
				if (paidin_out_sum_view != null) {
					paidin_out_sum_view.setEnabled(false);
				}
				if (end_drawer != null) {
					end_drawer.setEnabled(false);
				}
			} else {
				if (choose_drawer != null) {
					choose_drawer.setEnabled(true);
				}
				if (start_drawer != null) {
					start_drawer.setEnabled(true);
				}
				if (start_cash_amount != null) {
					start_cash_amount.setEnabled(true);
				}
				if (paidin_out_sum_view != null) {
					paidin_out_sum_view.setEnabled(true);
				}
				if (end_drawer != null) {
					end_drawer.setEnabled(true);
				}
				closePopMenu();
			}
			if (startPopupView != null && startPopupWindow.isShowing()) {
				startPopupWindow.dismiss();
			}
			break;

		case R.id.menu_liquors:
			closePopMenu();
			initRegular();
			Constant.Area = "Liquor Bar";
			changeColor();
			initlist();
			Refresh();
			break;
		// Tables \ Liquor Bar \ Sushi Bar \ Take Out \ Delivery
		case R.id.menu_dilivery:
			closePopMenu();
			Constant.Area = "Delivery";
			changeColor();
			// initlist();
			// Refresh();
			initDeliver();
			break;
		case R.id.menu_regular_tables:
			closePopMenu();
			initRegular();
			Constant.Area = "Tables";
			changeColor();
			initlist();
			Refresh();
			break;
		case R.id.menu_sushi_bar:
			closePopMenu();
			initRegular();
			Constant.Area = "Sushi Bar";
			changeColor();
			initlist();
			Refresh();
			break;
		case R.id.menu_take_out:
			closePopMenu();
			initRegular();
			Constant.Area = "Take Out";
			changeColor();
			// initlist();
			initTakeOut();
			// Refresh();

			break;
		case R.id.staff_ll1:
			closePopMenu();
			Constant.Area = "MY SALES REPORT";
			initStaff1();
			changeColor();
			break;
		case R.id.staff_ll2:
			closePopMenu();
			Constant.Area = "CURRENT DRAWER";
			initStaff2();
			changeColor();
			break;

		case R.id.payIn:
			tag = true;
			break;
		case R.id.payOut:
			tag = false;
			break;
		case R.id.payIn_out_cancel:
			findViewById(R.id.drawer_shift1).setVisibility(View.VISIBLE);
			findViewById(R.id.pay_in_out_shift1).setVisibility(View.GONE);
			break;
		case R.id.payIn_out_save:
			d_pay_in_out payInOut = new d_pay_in_out();
			if (tag) {
				payInOut.setMoney(Float.parseFloat(payIn_out_enter_amount
						.getText().toString().replace("$", "")));
			} else {
				payInOut.setMoney(Float.parseFloat("-"
						+ payIn_out_enter_amount.getText().toString()
								.replace("$", "")));
			}
			payInOut.setTime(DateUtils.getDateEN());
			payInOut.setWaiter(Constant.currentStaff.getS_account());
			payInOut.setDescription(payIn_out_des.getText().toString());
			payInOut.setCashierId(((TextView) findViewById(R.id.choose_drawer))
					.getText().toString().trim());
			savepayInOut(payInOut);
			break;
		case R.id.paidin_out_sum:
			findViewById(R.id.drawer_shift1).setVisibility(View.GONE);
			findViewById(R.id.pay_in_out_shift1).setVisibility(View.VISIBLE);
			getPayInOutShift(choose_drawer.getText().toString().trim());
			break;
		case R.id.start_drawer:
			float initMoney;
			initMoney = Float.parseFloat(start_cash_amount.getText().toString()
					.trim());
			String drawerName = choose_drawer.getText().toString().trim();

			openDrawer(initMoney, drawerName);
			break;
		case R.id.end_drawer:

			String drawerName0 = choose_drawer.getText().toString();
			String startTime0 = start_time.getText().toString();
			float cash = Float.parseFloat(expect_cash.getText().toString()
					.replace("$", ""));

			new pop_end_drawer(DeskMenuActivity.this, start_time, drawerName0,
					startTime0, cash, ll_start_drawer, ll_started_drawer);
			break;
		case R.id.choose_drawer:
			ll_start_drawer = (LinearLayout) findViewById(R.id.ll_start_cash);
			ll_started_drawer = (LinearLayout) findViewById(R.id.ll_started_cash);
			start_time_tv = (TextView) findViewById(R.id.start_time_tv);
			start_time = (TextView) findViewById(R.id.start_time);

			drawer_dialog = new pop_drawer_dialog(DeskMenuActivity.this,
					ll_start_drawer, ll_started_drawer, choose_drawer);
			
//			if (ll_started_drawer.getVisibility()==(View.VISIBLE)) {
//			     getCurrentDrawerInfo(choose_drawer.getText().toString().trim());
//			 }
			break;
		case R.id.refresh:
			 getCurrentDrawerInfo(choose_drawer.getText().toString().trim());
			break;
		case R.id.back_btn1:
			drawer_history_ll.setVisibility(View.VISIBLE);
			drawer_detial_ll.setVisibility(View.GONE);
			break;
		case R.id.back_btn:
			back_btn.setVisibility(View.GONE);
			shiftAll.setVisibility(View.VISIBLE);
			shiftSingle.setVisibility(View.GONE);
			break;
		case R.id.manager_ll1:
			closePopMenu();
			Constant.Area = "SALES REPORT";
			initManager1();
			changeColor();
			break;
		case R.id.manager_ll2:
			closePopMenu();
			Constant.Area = "DRAWERS HISTORY";
			initManager2();
			changeColor();
			break;
		}
	}

	private void savepayInOut(final d_pay_in_out payInOut) {
		// TODO Auto-generated method stub
		putAsyncTask(new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(Void... arg0) {
				// TODO Auto-generated method stub
				return new JsonResolveUtils(DeskMenuActivity.this)
						.setDrawerPayInOutTransaction(payInOut);
			}

			@Override
			protected void onPostExecute(Boolean result) {
				if (result) {
					findViewById(R.id.drawer_shift1)
							.setVisibility(View.VISIBLE);
					findViewById(R.id.pay_in_out_shift1).setVisibility(
							View.GONE);
					getCurrentDrawerInfo(choose_drawer.getText().toString()
							.trim());
				} else {
					showCustomToast("The operation is failed");
				}
			}
		});
	}

	private void getPayInOutShift(final String cashierId) {
		// TODO Auto-generated method stub
		putAsyncTask(new AsyncTask<Void, Void, Boolean>() {
			private List<d_pay_in_out> payInOutHistory;

			@Override
			protected Boolean doInBackground(Void... arg0) {
				// TODO Auto-generated method stub
				payInOutHistory = new JsonResolveUtils(DeskMenuActivity.this)
						.getDrawerPayInOutTransaction(cashierId);
				if (payInOutHistory != null)
					return true;
				return false;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				if (result) {
					if (payInOutHistory.size() > 0) {
						payInOutHistory history = new payInOutHistory(
								DeskMenuActivity.this, payInOutHistory);
						pay_history.setAdapter(history);
						float money = 0;
						for (int i = 0; i < payInOutHistory.size(); i++) {
							money += payInOutHistory.get(i).getMoney();
						}
						((TextView) findViewById(R.id.net_pay_in_cash))
								.setText(money + "");
					}

				} else {
					showCustomToast("The operation is failed");
				}
			}

		});

	}

	private void openDrawer(final float initMoney, final String drawerName) {
		// TODO Auto-generated method stub
		putAsyncTask(new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(Void... arg0) {
				// TODO Auto-generated method stub
				boolean f1, f2;
				f1 = new JsonResolveUtils(DeskMenuActivity.this)
						.setDrawerOpen(drawerName);
				f2 = new JsonResolveUtils(DeskMenuActivity.this)
						.setDrawerOpenTransaction(initMoney, drawerName);
				if (f1 == true && f2 == true) {
					return true;
				}
				return false;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				if (result) {
					ll_start_drawer.setVisibility(View.GONE);
					ll_started_drawer.setVisibility(View.VISIBLE);
					initCurrentDrawerInfo(drawerName);
				} else {
					showCustomToast("The drawer start failed");
				}
			}
		});

	}

	private void initCurrentDrawerInfo(String drawerName) {
		// TODO Auto-generated method stub
		start_time.setText(DateUtils.getDateEN());
		starting_cash.setText(start_cash_amount.getText().toString());
		cash_sales.setText("$0.00");
		payin_out_cash.setText("$0.00");
		expect_cash.setText(start_cash_amount.getText().toString());
		paidin_out_sum.setText("0");
	}

	/*
	 * 获取当前钱箱的信息 startTime startCash cashSales Pay In/Out的总钱数和记录的数目
	 */
	private void getCurrentDrawerInfo(final String cashier_id) {
		// TODO Auto-generated method stub
		putAsyncTask(new AsyncTask<Void, Void, Boolean>() {
			float totalMoney = 0;

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				showLoadingDialog("Just a moment, please...");
			}

			private d_CashierInfor CashierInfor;
			private List<d_pay_in_out> pay_in_out;

			@Override
			protected Boolean doInBackground(Void... params) {
				try {
					CashierInfor = new JsonResolveUtils(DeskMenuActivity.this)
							.getCurrentDrawerInfor(cashier_id);
					Thread.sleep(500);
					totalMoney = new JsonResolveUtils(DeskMenuActivity.this)
							.getCurrentDrawerSalesCash(cashier_id,
									CashierInfor.getCreateTime());
					pay_in_out = new JsonResolveUtils(DeskMenuActivity.this)
							.getDrawerPayInOutTransaction(cashier_id);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (pay_in_out.size() > 0)
					return true;
				return false;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				dismissLoadingDialog();
				if (result) {
					super.onPostExecute(result);
					start_time.setText(CashierInfor.getCreateTime());
					starting_cash.setText("$" + CashierInfor.getInitMoney());
					float cashSalesMoney = 0;
					cash_sales.setText(totalMoney + "");
					float money = 0;
					for (int i = 0; i < pay_in_out.size(); i++) {
						money += pay_in_out.get(i).getMoney();
					}
					if (money < 0) {
						String str1 = money + "";
						str1 = str1.replace("-", "");
						payin_out_cash.setText("-$" + str1);
					} else
						payin_out_cash.setText("$" + money);
					float exceptMoney = cashSalesMoney + money
							+ CashierInfor.getInitMoney();
					if (exceptMoney < 0) {
						String str1 = exceptMoney + "";
						str1 = str1.replace("-", "");
						expect_cash.setText("-$" + str1);
					} else {
						expect_cash.setText("$" + exceptMoney);
					}

					paidin_out_sum.setText(pay_in_out.size() + "");
				} else {
					showCustomToast("Loading data failed!");
				}
			}
		});

	}

	List<d_Cashier> cashiers = new ArrayList<d_Cashier>();

	private void closePopMenu() {
		Constant.pop = false;
		getSlideMenu().closeMenu();
		// curPage.setAlpha(1.0f);
		iv.setBackgroundColor(Color.parseColor("#00FFFFFF"));
		// if (popup.isShowing()) {
		// popup.dismiss();
		// }
	}

	private void initManager1() {
		setContentView(R.layout.manager_sales_report);
		setSlideMenu((SlideMenu) findViewById(R.id.slide_menu));
		curPage = (MyScrollLayout) findViewById(R.id.gv_desk);
		// settings = this.getLayoutInflater().inflate(
		// R.layout.layout_menu_setting, null);
		// popup = new PopupWindow(settings, 250, 490);
		initStaffView();
		initStaffEvents();
		initManagerView1();
		getAllStaffSales();
		// initManagerEvent1();
	}

	private void initManager2() {
		setContentView(R.layout.drawers_history);
		setSlideMenu((SlideMenu) findViewById(R.id.slide_menu));
		curPage = (MyScrollLayout) findViewById(R.id.gv_desk);
		// settings = this.getLayoutInflater().inflate(
		// R.layout.layout_menu_setting, null);
		// popup = new PopupWindow(settings, 250, 490);

		initStaffView();
		initStaffEvents();
		initManagerView2();
		initManagerEvent2();
	}

	private void initManagerView1() {
		manager_Shift_Time = (TextView) findViewById(R.id.Shift_Time1);
		manager_net_sale = (TextView) findViewById(R.id.total1);
		manager_transactions = (TextView) findViewById(R.id.m_transactions1);
		manager_tips = (TextView) findViewById(R.id.tips1);
		manager_shift_history = (ListView) findViewById(R.id.shift_history_list);
		shiftAll = (LinearLayout) findViewById(R.id.shiftAll1);
		shiftSingle = (LinearLayout) findViewById(R.id.shiftSingle1);
		back_btn = (Button) findViewById(R.id.back_btn);
		back_btn.setVisibility(View.VISIBLE);
		back_btn.setOnClickListener(this);
	}

	/*
	 * 经理销售金额的统计
	 */
	private void initManagerEvent1(List<d_Sale> d_Sales) {

		String startTime = d_Sales.get(0).getCreateTime();
		String endTime = d_Sales.get(0).getCloseTime();
		float netSales = d_Sales.get(0).getInitTotal();
		float tips = d_Sales.get(0).getTiptotal();
		float rebate = 0;
		rebate += (d_Sales.get(0).getInitTotal() * (1 - d_Sales.get(0)
				.getRebate()));
		for (int i = 1; i < d_Sales.size(); i++) {
			startTime = (startTime.compareTo(d_Sales.get(i).getCreateTime()) < 0) ? startTime
					: d_Sales.get(i).getCreateTime();
			endTime = (endTime.compareTo(d_Sales.get(i).getCloseTime()) > 0) ? endTime
					: d_Sales.get(i).getCloseTime();
			rebate += (d_Sales.get(i).getInitTotal() * (1 - d_Sales.get(i)
					.getRebate()));
			netSales += d_Sales.get(i).getInitTotal();
			tips += d_Sales.get(i).getTiptotal();
		}

		manager_Shift_Time.setText(startTime + "-" + endTime);
		manager_net_sale.setText("$" + df.format(netSales - rebate));
		manager_transactions.setText(d_Sales.size() + "");
		manager_tips.setText("$" + df.format(tips));
		List<d_Sale> sales = new ArrayList<d_Sale>();
		d_Sale sale = new d_Sale();
		sale.setWaiter(d_Sales.get(0).getWaiter());
		sale.setCreateTime(startTime);
		sale.setCloseTime(endTime);
		sale.setInitTotal(netSales - rebate);
		sale.setTiptotal(tips);
		sales.add(sale);
		SalesAdapter adapter = new SalesAdapter(DeskMenuActivity.this, sales);
		manager_shift_history.setAdapter(adapter);
		manager_shift_history.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				TextView text = (TextView) view.findViewById(R.id.name);
				shiftAll.setVisibility(View.GONE);
				shiftSingle.setVisibility(View.VISIBLE);
				back_btn.setVisibility(View.VISIBLE);
				getSales(text.getText().toString());

			}

		});
	}

	private void getAllStaffSales() {
		// TODO Auto-generated method stub
		putAsyncTask(new AsyncTask<Void, Void, Boolean>() {
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				showLoadingDialog("Just a moment, please...");
			}

			List<d_Sale> d_Sales = new ArrayList<d_Sale>();

			@Override
			protected Boolean doInBackground(Void... params) {
				try {
					d_Sales = new JsonResolveUtils(DeskMenuActivity.this)
							.getStaffSale("6666");
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (d_Sales.size() > 0)
					return true;
				return false;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				dismissLoadingDialog();
				if (result) {
					
					super.onPostExecute(result);
					initManagerEvent1(d_Sales);

				}
			}
		});
	}

	private ListView front_desk;
	private ListView lounge_bar;
	private ListView dinner_tables;

	private TextView start_time1;
	private TextView starting_cash1;
	private TextView cash_sales1;
	private TextView payin_out_cash1;
	private TextView expect_cash1;
	private TextView actual_cash1;
	private TextView difference1;
	
	private ListView pay_history1;
	
	private void initManagerView2() {

		front_desk = (ListView) findViewById(R.id.front_desk);
		lounge_bar = (ListView) findViewById(R.id.lounge_bar);
		dinner_tables = (ListView) findViewById(R.id.dinner_tables);
		drawer_history_ll = (LinearLayout) findViewById(R.id.drawer_history_ll);
		drawer_detial_ll = (LinearLayout) findViewById(R.id.drawer_detial_ll);
		back_btn1 = (TextView) findViewById(R.id.back_btn1);
		back_btn1.setOnClickListener(this);
		start_time1 = (TextView) findViewById(R.id.start_time1);
		starting_cash1 = (TextView) findViewById(R.id.starting_cash1);
		cash_sales1 = (TextView) findViewById(R.id.cash_sales1);
		payin_out_cash1 = (TextView) findViewById(R.id.payin_out_cash1);
		expect_cash1 = (TextView) findViewById(R.id.expect_cash1);
		actual_cash1 = (TextView) findViewById(R.id.actual_cash1);
		difference1 = (TextView) findViewById(R.id.difference1);
		pay_history1 = (ListView) findViewById(R.id.pay_history1);
		
		front_desk.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long id) {
				// TODO Auto-generated method stub
				TextView tv = (TextView) view.findViewById(R.id.item_time);
				String time = tv.getText().toString().substring(0,19);
				Log.i("tag","时间是"+time);
				getDrawerHistoryDetial(time,"Front Desk Drawer");
			}
			
		});
		
		lounge_bar.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				TextView tv = (TextView) view.findViewById(R.id.item_time);
				String time = tv.getText().toString().substring(0,18);
				getDrawerHistoryDetial(time,"Lounge Bar Drawer");
			}
			
		});
		
		dinner_tables.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				TextView tv = (TextView) view.findViewById(R.id.item_time);
				String time = tv.getText().toString().substring(0,18);
				getDrawerHistoryDetial(time,"Dinner Tables Drawer");
			}
			
		});

	}

	protected void getDrawerHistoryDetial(final String time, final String cashier_id) {
		// TODO Auto-generated method stub
		putAsyncTask(new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected void onPreExecute(){
				super.onPreExecute();
				showLoadingDialog("Just a moment, please...");
			}
		
			private List<d_CashierInfor> drawerInfor;
			private List<d_pay_in_out> pay_in_out;
			@Override
			protected Boolean doInBackground(Void... arg0) {
				// TODO Auto-generated method stub
				drawerInfor = new JsonResolveUtils(DeskMenuActivity.this)
                  .getDrawerInfor(cashier_id, "correct", time);
				pay_in_out = new JsonResolveUtils(DeskMenuActivity.this)
				   .getDrawerPayInOutTransaction1(cashier_id, time);
				if(drawerInfor.size() > 0) 
				    return true;
			    return false;
			}
			
			@Override
			protected void onPostExecute(Boolean result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				dismissLoadingDialog();
				if(result) {
					drawer_history_ll.setVisibility(View.GONE);
					drawer_detial_ll.setVisibility(View.VISIBLE);
					showConcertDrawerInfor(drawerInfor,pay_in_out);
				}else{
					showCustomToast("The operation is failed!");
				}
			}
		});
	}

	protected void showConcertDrawerInfor(List<d_CashierInfor> drawerInfor,
			List<d_pay_in_out> pay_in_out) {
		// TODO Auto-generated method stub
		float totalMoney = 0;
		for(int i=0; i<pay_in_out.size(); i++) {
			totalMoney += pay_in_out.get(i).getMoney();
		}
		float except_cash = drawerInfor.get(0).getActualMoney() +
				drawerInfor.get(0).getDifferent();
		float cash_sales = except_cash - drawerInfor.get(0).getInitMoney()
				- totalMoney;
		start_time1.setText(drawerInfor.get(0).getCreateTime() + " - "
				+ drawerInfor.get(0).getCloseTime());
		starting_cash1.setText("$"+drawerInfor.get(0).getInitMoney());
		cash_sales1.setText("$"+cash_sales);
		payin_out_cash1.setText("$"+totalMoney);
		expect_cash1.setText("$"+except_cash);
		actual_cash1.setText("$"+drawerInfor.get(0).getActualMoney());
		difference1.setText("$"+drawerInfor.get(0).getDifferent());
		payInOutHistory adapter = new payInOutHistory(DeskMenuActivity.this, pay_in_out);
		pay_history1.setAdapter(adapter);
	}

	private void initManagerEvent2() {

		putAsyncTask(new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected void onPreExecute(){
				super.onPreExecute();
				showLoadingDialog("Just a moment, please...");
			}
			
			private List<d_CashierInfor> frontDrawer;
			private List<d_CashierInfor> loungDrawer;
			private List<d_CashierInfor> dinnerDrawer;
			
			@Override
			protected Boolean doInBackground(Void... arg0) {
				// TODO Auto-generated method stub
				
				frontDrawer = new JsonResolveUtils(DeskMenuActivity.this).
				getDrawerInfor("Front Desk Drawer", "bytime", DateUtils.getDateEN());
				
				loungDrawer = new JsonResolveUtils(DeskMenuActivity.this).
				getDrawerInfor("Lounge Bar Drawer", "bytime", DateUtils.getDateEN());
				
				dinnerDrawer = new JsonResolveUtils(DeskMenuActivity.this).
				getDrawerInfor("Dinner Tables Drawer", "bytime", DateUtils.getDateEN());
				return true;
			}
			
			@Override
			protected void onPostExecute(Boolean result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				dismissLoadingDialog();
				if(result) {
					DrawerAdapter frontAdapter = 
							new DrawerAdapter(DeskMenuActivity.this, frontDrawer);
					front_desk.setAdapter(frontAdapter);
					DrawerAdapter loungAdapter = 
							new DrawerAdapter(DeskMenuActivity.this, loungDrawer);
					lounge_bar.setAdapter(loungAdapter);
					DrawerAdapter dinnerAdapter = 
							new DrawerAdapter(DeskMenuActivity.this, dinnerDrawer);
					dinner_tables.setAdapter(dinnerAdapter);	
				}else{
					showCustomToast("The operation is failed!");
				}
				
			}
		});
	}

	private void initStaff1() {
		setContentView(R.layout.staff_sales_report);

		showStaffSaleInfo();

		setSlideMenu((SlideMenu) findViewById(R.id.slide_menu));
		curPage = (MyScrollLayout) findViewById(R.id.gv_desk);
		// settings = this.getLayoutInflater().inflate(
		// R.layout.layout_menu_setting, null);
		// popup = new PopupWindow(settings, 250, 490);
		if (Constant.currentStaff.getPriority() != 0)
			findViewById(R.id.menu_manager).setVisibility(View.INVISIBLE);
		initStaffView();
		initStaffEvents();
	}

	private void showStaffSaleInfo() {
		// TextView staff_name = (TextView) findViewById(R.id.staff_name);
		// staff_name.setText(Constant.currentStaff.getS_name());
		// TextView shift_time = (TextView) findViewById(R.id.Shift_Time);
		// shift_time.setText(Constant.clockInTime);
		// 得到该员工的今天当前的销售情况
		getSales(Constant.currentStaff.getS_account());

	}

	/*
	 * 得到该员工的当天的销售记录
	 */
	private void getSales(final String account) {
		putAsyncTask(new AsyncTask<Void, Void, Boolean>() {
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				showLoadingDialog("Just a moment, please...");
			}

			List<d_Sale> d_Sales = new ArrayList<d_Sale>();
			float net_Sales = 0;
			float tips = 0;
			float initTotal = 0;
			float rebate = 0;
			float tax = 0;
			float cashTotal = 0;
			float cardTotal = 0;
			float cashTip = 0;
			float cardTip = 0;

			@Override
			protected Boolean doInBackground(Void... params) {
				try {
					d_Sales = new JsonResolveUtils(DeskMenuActivity.this)
							.getStaffSale(account);
					Log.i("tag", "当前账号" + account);
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (d_Sales.size() > 0)
					return true;
				return false;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				dismissLoadingDialog();
				if (result) {
					super.onPostExecute(result);
					String startTime = d_Sales.get(0).getCreateTime();
					String endTime = startTime;
					for (int i = 0; i < d_Sales.size(); i++) {
						net_Sales += d_Sales.get(i).getInitTotal();
						tips += d_Sales.get(i).getTiptotal();
						Log.i("tag","tips="+tips);
						// initTotal = d_Sales.get(i).getInitTotal();
						rebate += (d_Sales.get(i).getInitTotal() * (1 - d_Sales
								.get(i).getRebate()));
						tax += d_Sales.get(i).getTax();
						cashTotal += d_Sales.get(i).getCashTotal();
						cardTotal += d_Sales.get(i).getCardTotal();
						if (d_Sales.get(i).getCashTotal() != 0) {
							cashTip += d_Sales.get(i).getTiptotal();
						} else if (d_Sales.get(i).getCardTotal() != 0) {
							cardTip += d_Sales.get(i).getTiptotal();
						}
						startTime = (startTime.compareTo(d_Sales.get(i)
								.getCreateTime()) < 0) ? startTime : d_Sales
								.get(i).getCreateTime();
						endTime = (endTime.compareTo(d_Sales.get(i)
								.getCloseTime()) > 0) ? endTime : d_Sales
								.get(i).getCloseTime();
					}
					TextView staff_name = (TextView) findViewById(R.id.staff_name);
					staff_name.setText(d_Sales.get(0).getWaiter());
					TextView shift_time = (TextView) findViewById(R.id.Shift_Time);
					shift_time.setText(startTime + "-" + endTime);
					TextView total = (TextView) findViewById(R.id.total_1);
					TextView transactions = (TextView) findViewById(R.id.transactions_1);
					TextView gross_sales = (TextView) findViewById(R.id.gross_sales_1);
					TextView discount = (TextView) findViewById(R.id.discount_1);
					TextView net_sales_tv = (TextView) findViewById(R.id.net_sales_1);
					TextView tax_tv = (TextView) findViewById(R.id.tax_1);
					TextView total_collected = (TextView) findViewById(R.id.total_collected_1);
					TextView cash = (TextView) findViewById(R.id.cash_1);
					TextView credit_card = (TextView) findViewById(R.id.credit_card_1);
					TextView tips_1 = (TextView) findViewById(R.id.tips_1);
					TextView tips_11 = (TextView) findViewById(R.id.tips_11);
					TextView cash_tip = (TextView) findViewById(R.id.cash_tips_1);
					TextView card_tip = (TextView) findViewById(R.id.credit_card_tips_1);
					total.setText("$" + df.format((net_Sales - rebate)));
					transactions.setText(d_Sales.size() + "");
					tips_1.setText("$" + df.format(tips));
					gross_sales.setText("$" + df.format(net_Sales));
					discount.setText("$" + df.format(rebate));
					net_sales_tv.setText("$" + df.format(net_Sales - rebate));
					tax_tv.setText("$" + df.format(tax));
					total_collected.setText("$"
							+ df.format(cashTotal + cardTotal));
					cash.setText("$" + df.format(cashTotal));
					credit_card.setText("$" + df.format(cardTotal));
					
					tips_11.setText("$" + df.format(tips));
					cash_tip.setText("$" + df.format(cashTip));
					card_tip.setText("$" + df.format(cardTip));
				} else {
					showCustomToast("Loading data failed!");
				}
			}
		});
	}

	private void initStaff2() {
		setContentView(R.layout.drawer);
		setSlideMenu((SlideMenu) findViewById(R.id.slide_menu));
		curPage = (MyScrollLayout) findViewById(R.id.gv_desk);
		if (Constant.currentStaff.getPriority() != 0)
			findViewById(R.id.menu_manager).setVisibility(View.INVISIBLE);

		choose_drawer = (TextView) findViewById(R.id.choose_drawer);
		choose_drawer.setOnClickListener(this);
		start_cash_amount = (EditText) findViewById(R.id.start_cash_amount);
		start_drawer = (Button) findViewById(R.id.start_drawer);
		start_drawer.setOnClickListener(this);
		start_time = (TextView) findViewById(R.id.start_time);
		starting_cash = (TextView) findViewById(R.id.starting_cash);
		cash_sales = (TextView) findViewById(R.id.cash_sales);
		payin_out_cash = (TextView) findViewById(R.id.payin_out_cash);
		expect_cash = (TextView) findViewById(R.id.expect_cash);
		paidin_out_sum = (TextView) findViewById(R.id.paidin_out_sum);
		paidin_out_sum.setOnClickListener(this);

		payIn_out_cancel = (Button) findViewById(R.id.payIn_out_cancel);
		payIn_out_cancel.setOnClickListener(this);
		payIn_out_save = (Button) findViewById(R.id.payIn_out_save);
		payIn_out_save.setOnClickListener(this);
		payIn_out_enter_amount = (TextView) findViewById(R.id.payIn_out_enter_amount);
		payIn_out_des = (EditText) findViewById(R.id.payIn_out_des);
		payIn = (Button) findViewById(R.id.payIn);
		payIn.setOnClickListener(this);
		payOut = (Button) findViewById(R.id.payOut);
		payOut.setOnClickListener(this);
		pay_history = (ListView) findViewById(R.id.pay_history);
		net_pay_in_cash = (TextView) findViewById(R.id.net_pay_in_cash);
		((Button) findViewById(R.id.end_drawer)).setOnClickListener(this);
		((LinearLayout) findViewById(R.id.ll_start_cash)).setVisibility(View.GONE);
		((LinearLayout) findViewById(R.id.ll_started_cash)).setVisibility(View.GONE);
		((LinearLayout) findViewById(R.id.drawer_shift1)).setVisibility(View.VISIBLE);
		((Button) findViewById(R.id.refresh)).setOnClickListener(this);
		initStaffView();
		initStaffEvents();
	}

	private void initStaffView() {
		// drops = (RelativeLayout) settings.findViewById(R.id.drops);
		// pay_out = (RelativeLayout) settings.findViewById(R.id.pay_out);
		// personal_report = (RelativeLayout) settings
		// .findViewById(R.id.personal_report);
		// purchase = (RelativeLayout) settings.findViewById(R.id.purchase);
		// close_shift = (RelativeLayout)
		// settings.findViewById(R.id.close_shift);

		iv = (ImageView) findViewById(R.id.title_bar_menu_btn);
		iv.setBackgroundResource(R.drawable.ic_top_bar_category);

		// menu_setting = (LinearLayout) findViewById(R.id.menu_setting);
		menu_liquors = (LinearLayout) findViewById(R.id.menu_liquors);
		menu_dilivery = (LinearLayout) findViewById(R.id.menu_dilivery);
		menu_regular_tables = (LinearLayout) findViewById(R.id.menu_regular_tables);
		menu_sushi_bar = (LinearLayout) findViewById(R.id.menu_sushi_bar);
		menu_take_out = (LinearLayout) findViewById(R.id.menu_take_out);
		menu_staff_sales = (LinearLayout) findViewById(R.id.staff_ll1);
		menu_current_drawer = (LinearLayout) findViewById(R.id.staff_ll2);
		menu_manager_sales = (LinearLayout) findViewById(R.id.manager_ll1);
		menu_drawers_history = (LinearLayout) findViewById(R.id.manager_ll2);

		tv_main_tables = (TextView) findViewById(R.id.tv_main_tables);
		tv_liquors = (TextView) findViewById(R.id.tv_liquors);
		tv_sushi_bar = (TextView) findViewById(R.id.tv_sushi_bar);
		tv_take_out = (TextView) findViewById(R.id.tv_take_out);
		tv_delivery = (TextView) findViewById(R.id.tv_delivery);
		tv_my_sales_report = (TextView) findViewById(R.id.tv_my_sales_report);
		tv_current_drawer = (TextView) findViewById(R.id.tv_current_drawer);
		tv_sales_report = (TextView) findViewById(R.id.tv_sales_report);
		tv_drawers_history = (TextView) findViewById(R.id.tv_drawers_history);

		if (Constant.Area.equals("Tables")) {
			menu_regular_tables.setBackgroundColor(Color.parseColor("#797979"));
			tv_main_tables.setTextColor(Color.parseColor("#97DA00"));
		} else if (Constant.Area.equals("Liquor Bar")) {
			menu_liquors.setBackgroundColor(Color.parseColor("#797979"));
			tv_liquors.setTextColor(Color.parseColor("#97DA00"));
		} else if (Constant.Area.equals("Sushi Bar")) {
			menu_sushi_bar.setBackgroundColor(Color.parseColor("#797979"));
			tv_sushi_bar.setTextColor(Color.parseColor("#97DA00"));
		} else if (Constant.Area.equals("Take Out")) {
			menu_take_out.setBackgroundColor(Color.parseColor("#797979"));
			tv_take_out.setTextColor(Color.parseColor("#97DA00"));
		} else if (Constant.Area.equals("Delivery")) {
			menu_dilivery.setBackgroundColor(Color.parseColor("#797979"));
			tv_delivery.setTextColor(Color.parseColor("#97DA00"));
		} else if (Constant.Area.equals("MY SALES REPORT")) {
			menu_staff_sales.setBackgroundColor(Color.parseColor("#797979"));
			tv_my_sales_report.setTextColor(Color.parseColor("#97DA00"));
		} else if (Constant.Area.equals("CURRENT DRAWER")) {
			menu_current_drawer.setBackgroundColor(Color.parseColor("#797979"));
			tv_current_drawer.setTextColor(Color.parseColor("#97DA00"));
		} else if (Constant.Area.equals("SALES REPORT")) {
			menu_manager_sales.setBackgroundColor(Color.parseColor("#797979"));
			tv_sales_report.setTextColor(Color.parseColor("#97DA00"));
		} else if (Constant.Area.equals("DRAWERS HISTORY")) {
			menu_drawers_history
					.setBackgroundColor(Color.parseColor("#797979"));
			tv_drawers_history.setTextColor(Color.parseColor("#97DA00"));
		}
	}

	private void initStaffEvents() {
		iv.setOnClickListener(this);
		// menu_setting.setOnClickListener(this);
		menu_liquors.setOnClickListener(this);
		menu_dilivery.setOnClickListener(this);
		menu_regular_tables.setOnClickListener(this);
		menu_sushi_bar.setOnClickListener(this);
		menu_take_out.setOnClickListener(this);
		menu_staff_sales.setOnClickListener(this);
		menu_current_drawer.setOnClickListener(this);
		menu_manager_sales.setOnClickListener(this);
		menu_drawers_history.setOnClickListener(this);
		// drops.setOnClickListener(this);
		// pay_out.setOnClickListener(this);
		// personal_report.setOnClickListener(this);
		// purchase.setOnClickListener(this);
		// close_shift.setOnClickListener(this);
	}

	private void initRegular() {
		setContentView(R.layout.activity_main);
		setSlideMenu((SlideMenu) findViewById(R.id.slide_menu));
		curPage = (MyScrollLayout) findViewById(R.id.gv_desk);
		// settings = this.getLayoutInflater().inflate(
		// R.layout.layout_menu_setting, null);
		// popup = new PopupWindow(settings, 250, 490);
		if (Constant.currentStaff.getPriority() != 0)
			findViewById(R.id.menu_manager).setVisibility(View.INVISIBLE);
		sqldesk = new sql_desk();
		initViews();
		initlist();
		initEvents();
	}

	private void initDeliver() {
		setContentView(R.layout.delivery);
		setSlideMenu((SlideMenu) findViewById(R.id.slide_menu));
		curPage = (MyScrollLayout) findViewById(R.id.gv_desk);
		// settings = this.getLayoutInflater().inflate(
		// R.layout.layout_menu_setting, null);
		// popup = new PopupWindow(settings, 250, 490);
		if (Constant.currentStaff.getPriority() != 0)
			findViewById(R.id.menu_manager).setVisibility(View.INVISIBLE);
		sqldesk = new sql_desk();

		initDeliverViews();
		initDeliverlist();
		initDeliverEvents();
	}

	private void initTakeOut() {
		setContentView(R.layout.takeout);
		setSlideMenu((SlideMenu) findViewById(R.id.slide_menu));
		curPage = (MyScrollLayout) findViewById(R.id.gv_desk);
		// settings = this.getLayoutInflater().inflate(
		// R.layout.layout_menu_setting, null);
		// popup = new PopupWindow(settings, 250, 490);
		if (Constant.currentStaff.getPriority() != 0)
			findViewById(R.id.menu_manager).setVisibility(View.INVISIBLE);
		sqldesk = new sql_desk();

		initDeliverViews();
		initTakeoutlist();
		initDeliverEvents();
	}

	private void initDeliverEvents() {

		iv.setOnClickListener(this);
		// menu_setting.setOnClickListener(this);

		menu_liquors.setOnClickListener(this);
		menu_dilivery.setOnClickListener(this);
		menu_regular_tables.setOnClickListener(this);
		menu_sushi_bar.setOnClickListener(this);
		menu_take_out.setOnClickListener(this);
		menu_staff_sales.setOnClickListener(this);
		menu_current_drawer.setOnClickListener(this);
		menu_manager_sales.setOnClickListener(this);
		menu_drawers_history.setOnClickListener(this);
		// drops.setOnClickListener(this);
		// pay_out.setOnClickListener(this);
		// personal_report.setOnClickListener(this);
		// purchase.setOnClickListener(this);
		// close_shift.setOnClickListener(this);

	}

	private void initTakeoutlist() {
		lstDate = new sql_desk(this).queryMenus("Pick Up");
		lstDate.add(0, new d_Desk(0, "0", "0", "0", "0", -1,
				"2014-12-12 12:12:12", 0, 0, 0, 0, 0));
		DeliveryAdapter pickupAdapter = new DeliveryAdapter(this, lstDate, 1);
		pickupGridView.setAdapter(pickupAdapter);
	}

	private void initDeliverlist() {
		// lstDate = new sql_desk(this).queryMenus("Pick Up");
		// lstDate.add(0, new d_Desk(0, "0", "0", "0", "0", -1,
		// "2014-12-12 12:12:12", 0, 0, 0, 0, 0));
		// DeliveryAdapter pickupAdapter = new DeliveryAdapter(this, lstDate,
		// 1);
		// pickupGridView.setAdapter(pickupAdapter);

		lstDate = new sql_desk(this).queryMenus("Delivery");
		lstDate.add(0, new d_Desk(0, "0", "0", "0", "0", -1,
				"2014-12-12 12:12:12", 0, 0, 0, 0, 0));
		DeliveryAdapter deliverAdapter = new DeliveryAdapter(this, lstDate, 0);
		deliverGridView.setAdapter(deliverAdapter);

	}

	private void initDeliverViews() {

		// drops = (RelativeLayout) settings.findViewById(R.id.drops);
		// pay_out = (RelativeLayout) settings.findViewById(R.id.pay_out);
		// personal_report = (RelativeLayout) settings
		// .findViewById(R.id.personal_report);
		// purchase = (RelativeLayout) settings.findViewById(R.id.purchase);
		// close_shift = (RelativeLayout)
		// settings.findViewById(R.id.close_shift);

		iv = (ImageView) findViewById(R.id.title_bar_menu_btn);
		iv.setBackgroundResource(R.drawable.ic_top_bar_category);
		// menu_setting = (LinearLayout) findViewById(R.id.menu_setting);

		menu_liquors = (LinearLayout) findViewById(R.id.menu_liquors);
		menu_dilivery = (LinearLayout) findViewById(R.id.menu_dilivery);
		menu_regular_tables = (LinearLayout) findViewById(R.id.menu_regular_tables);
		menu_sushi_bar = (LinearLayout) findViewById(R.id.menu_sushi_bar);
		menu_take_out = (LinearLayout) findViewById(R.id.menu_take_out);
		menu_staff_sales = (LinearLayout) findViewById(R.id.staff_ll1);
		menu_current_drawer = (LinearLayout) findViewById(R.id.staff_ll2);
		menu_manager_sales = (LinearLayout) findViewById(R.id.manager_ll1);
		menu_drawers_history = (LinearLayout) findViewById(R.id.manager_ll2);
		deliverGridView = (GridView) this.findViewById(R.id.delivery_grid);
		pickupGridView = (GridView) this.findViewById(R.id.pickup_grid);

		tv_main_tables = (TextView) findViewById(R.id.tv_main_tables);
		tv_liquors = (TextView) findViewById(R.id.tv_liquors);
		tv_sushi_bar = (TextView) findViewById(R.id.tv_sushi_bar);
		tv_take_out = (TextView) findViewById(R.id.tv_take_out);
		tv_delivery = (TextView) findViewById(R.id.tv_delivery);
		tv_my_sales_report = (TextView) findViewById(R.id.tv_my_sales_report);
		tv_current_drawer = (TextView) findViewById(R.id.tv_current_drawer);
		tv_sales_report = (TextView) findViewById(R.id.tv_sales_report);
		tv_drawers_history = (TextView) findViewById(R.id.tv_drawers_history);

		if (Constant.Area.equals("Tables")) {
			menu_regular_tables.setBackgroundColor(Color.parseColor("#797979"));
			tv_main_tables.setTextColor(Color.parseColor("#97DA00"));
		} else if (Constant.Area.equals("Liquor Bar")) {
			menu_liquors.setBackgroundColor(Color.parseColor("#797979"));
			tv_liquors.setTextColor(Color.parseColor("#97DA00"));
		} else if (Constant.Area.equals("Sushi Bar")) {
			menu_sushi_bar.setBackgroundColor(Color.parseColor("#797979"));
			tv_sushi_bar.setTextColor(Color.parseColor("#97DA00"));
		} else if (Constant.Area.equals("Take Out")) {
			menu_take_out.setBackgroundColor(Color.parseColor("#797979"));
			tv_take_out.setTextColor(Color.parseColor("#97DA00"));
		} else if (Constant.Area.equals("Delivery")) {
			menu_dilivery.setBackgroundColor(Color.parseColor("#797979"));
			tv_delivery.setTextColor(Color.parseColor("#97DA00"));
		} else if (Constant.Area.equals("MY SALES REPORT")) {
			menu_staff_sales.setBackgroundColor(Color.parseColor("#797979"));
			tv_my_sales_report.setTextColor(Color.parseColor("#97DA00"));
		} else if (Constant.Area.equals("CURRENT DRAWER")) {
			menu_current_drawer.setBackgroundColor(Color.parseColor("#797979"));
			tv_current_drawer.setTextColor(Color.parseColor("#97DA00"));
		} else if (Constant.Area.equals("SALES REPORT")) {
			menu_manager_sales.setBackgroundColor(Color.parseColor("#797979"));
			tv_sales_report.setTextColor(Color.parseColor("#97DA00"));
		} else if (Constant.Area.equals("DRAWERS HISTORY")) {
			menu_drawers_history
					.setBackgroundColor(Color.parseColor("#797979"));
			tv_drawers_history.setTextColor(Color.parseColor("#97DA00"));
		}
	}

	// 改变侧边栏的颜色
	private void changeColor() {
		if (Constant.Area.equals("Tables")) {
			tv_main_tables.setTextColor(Color.parseColor("#97DA00"));
			tv_drawers_history.setTextColor(Color.parseColor("#FFFFFF"));
			tv_current_drawer.setTextColor(Color.parseColor("#FFFFFF"));
			tv_delivery.setTextColor(Color.parseColor("#FFFFFF"));
			tv_liquors.setTextColor(Color.parseColor("#FFFFFF"));
			tv_my_sales_report.setTextColor(Color.parseColor("#FFFFFF"));
			tv_sales_report.setTextColor(Color.parseColor("#FFFFFF"));
			tv_sushi_bar.setTextColor(Color.parseColor("#FFFFFF"));
			tv_take_out.setTextColor(Color.parseColor("#FFFFFF"));

			menu_regular_tables.setBackgroundColor(Color.parseColor("#797979"));
			menu_liquors.setBackgroundColor(Color.parseColor("#5E5E5E"));
			menu_sushi_bar.setBackgroundColor(Color.parseColor("#5E5E5E"));
			menu_take_out.setBackgroundColor(Color.parseColor("#5E5E5E"));
			menu_dilivery.setBackgroundColor(Color.parseColor("#5E5E5E"));
			menu_staff_sales.setBackgroundColor(Color.parseColor("#5E5E5E"));
			menu_current_drawer.setBackgroundColor(Color.parseColor("#5E5E5E"));
			menu_manager_sales.setBackgroundColor(Color.parseColor("#5E5E5E"));
			menu_drawers_history
					.setBackgroundColor(Color.parseColor("#5E5E5E"));
		} else if (Constant.Area.equals("Liquor Bar")) {

			tv_main_tables.setTextColor(Color.parseColor("#FFFFFF"));
			tv_drawers_history.setTextColor(Color.parseColor("#FFFFFF"));
			tv_current_drawer.setTextColor(Color.parseColor("#FFFFFF"));
			tv_delivery.setTextColor(Color.parseColor("#FFFFFF"));
			tv_liquors.setTextColor(Color.parseColor("#97DA00"));
			tv_my_sales_report.setTextColor(Color.parseColor("#FFFFFF"));
			tv_sales_report.setTextColor(Color.parseColor("#FFFFFF"));
			tv_sushi_bar.setTextColor(Color.parseColor("#FFFFFF"));
			tv_take_out.setTextColor(Color.parseColor("#FFFFFF"));

			menu_regular_tables.setBackgroundColor(Color.parseColor("#5E5E5E"));
			menu_liquors.setBackgroundColor(Color.parseColor("#797979"));
			menu_sushi_bar.setBackgroundColor(Color.parseColor("#5E5E5E"));
			menu_take_out.setBackgroundColor(Color.parseColor("#5E5E5E"));
			menu_dilivery.setBackgroundColor(Color.parseColor("#5E5E5E"));
			menu_staff_sales.setBackgroundColor(Color.parseColor("#5E5E5E"));
			menu_current_drawer.setBackgroundColor(Color.parseColor("#5E5E5E"));
			menu_manager_sales.setBackgroundColor(Color.parseColor("#5E5E5E"));
			menu_drawers_history
					.setBackgroundColor(Color.parseColor("#5E5E5E"));
		} else if (Constant.Area.equals("Sushi Bar")) {

			tv_main_tables.setTextColor(Color.parseColor("#FFFFFF"));
			tv_drawers_history.setTextColor(Color.parseColor("#FFFFFF"));
			tv_current_drawer.setTextColor(Color.parseColor("#FFFFFF"));
			tv_delivery.setTextColor(Color.parseColor("#FFFFFF"));
			tv_liquors.setTextColor(Color.parseColor("#FFFFFF"));
			tv_my_sales_report.setTextColor(Color.parseColor("#FFFFFF"));
			tv_sales_report.setTextColor(Color.parseColor("#FFFFFF"));
			tv_sushi_bar.setTextColor(Color.parseColor("#97DA00"));
			tv_take_out.setTextColor(Color.parseColor("#FFFFFF"));

			menu_regular_tables.setBackgroundColor(Color.parseColor("#5E5E5E"));
			menu_liquors.setBackgroundColor(Color.parseColor("#5E5E5E"));
			menu_sushi_bar.setBackgroundColor(Color.parseColor("#797979"));
			menu_take_out.setBackgroundColor(Color.parseColor("#5E5E5E"));
			menu_dilivery.setBackgroundColor(Color.parseColor("#5E5E5E"));
			menu_staff_sales.setBackgroundColor(Color.parseColor("#5E5E5E"));
			menu_current_drawer.setBackgroundColor(Color.parseColor("#5E5E5E"));
			menu_manager_sales.setBackgroundColor(Color.parseColor("#5E5E5E"));
			menu_drawers_history
					.setBackgroundColor(Color.parseColor("#5E5E5E"));
		} else if (Constant.Area.equals("Take Out")) {

			tv_main_tables.setTextColor(Color.parseColor("#FFFFFF"));
			tv_drawers_history.setTextColor(Color.parseColor("#FFFFFF"));
			tv_current_drawer.setTextColor(Color.parseColor("#FFFFFF"));
			tv_delivery.setTextColor(Color.parseColor("#FFFFFF"));
			tv_liquors.setTextColor(Color.parseColor("#FFFFFF"));
			tv_my_sales_report.setTextColor(Color.parseColor("#FFFFFF"));
			tv_sales_report.setTextColor(Color.parseColor("#FFFFFF"));
			tv_sushi_bar.setTextColor(Color.parseColor("#FFFFFF"));
			tv_take_out.setTextColor(Color.parseColor("#97DA00"));

			menu_regular_tables.setBackgroundColor(Color.parseColor("#5E5E5E"));
			menu_liquors.setBackgroundColor(Color.parseColor("#5E5E5E"));
			menu_sushi_bar.setBackgroundColor(Color.parseColor("#5E5E5E"));
			menu_take_out.setBackgroundColor(Color.parseColor("#797979"));
			menu_dilivery.setBackgroundColor(Color.parseColor("#5E5E5E"));
			menu_staff_sales.setBackgroundColor(Color.parseColor("#5E5E5E"));
			menu_current_drawer.setBackgroundColor(Color.parseColor("#5E5E5E"));
			menu_manager_sales.setBackgroundColor(Color.parseColor("#5E5E5E"));
			menu_drawers_history
					.setBackgroundColor(Color.parseColor("#5E5E5E"));
		} else if (Constant.Area.equals("Delivery")) {

			tv_main_tables.setTextColor(Color.parseColor("#FFFFFF"));
			tv_drawers_history.setTextColor(Color.parseColor("#FFFFFF"));
			tv_current_drawer.setTextColor(Color.parseColor("#FFFFFF"));
			tv_delivery.setTextColor(Color.parseColor("#97DA00"));
			tv_liquors.setTextColor(Color.parseColor("#FFFFFF"));
			tv_my_sales_report.setTextColor(Color.parseColor("#FFFFFF"));
			tv_sales_report.setTextColor(Color.parseColor("#FFFFFF"));
			tv_sushi_bar.setTextColor(Color.parseColor("#FFFFFF"));
			tv_take_out.setTextColor(Color.parseColor("#FFFFFF"));

			menu_regular_tables.setBackgroundColor(Color.parseColor("#5E5E5E"));
			menu_liquors.setBackgroundColor(Color.parseColor("#5E5E5E"));
			menu_sushi_bar.setBackgroundColor(Color.parseColor("#5E5E5E"));
			menu_take_out.setBackgroundColor(Color.parseColor("#5E5E5E"));
			menu_dilivery.setBackgroundColor(Color.parseColor("#797979"));
			menu_staff_sales.setBackgroundColor(Color.parseColor("#5E5E5E"));
			menu_current_drawer.setBackgroundColor(Color.parseColor("#5E5E5E"));
			menu_manager_sales.setBackgroundColor(Color.parseColor("#5E5E5E"));
			menu_drawers_history
					.setBackgroundColor(Color.parseColor("#5E5E5E"));
		} else if (Constant.Area.equals("MY SALES REPORT")) {

			tv_main_tables.setTextColor(Color.parseColor("#FFFFFF"));
			tv_drawers_history.setTextColor(Color.parseColor("#FFFFFF"));
			tv_current_drawer.setTextColor(Color.parseColor("#FFFFFF"));
			tv_delivery.setTextColor(Color.parseColor("#FFFFFF"));
			tv_liquors.setTextColor(Color.parseColor("#FFFFFF"));
			tv_my_sales_report.setTextColor(Color.parseColor("#97DA00"));
			tv_sales_report.setTextColor(Color.parseColor("#FFFFFF"));
			tv_sushi_bar.setTextColor(Color.parseColor("#FFFFFF"));
			tv_take_out.setTextColor(Color.parseColor("#FFFFFF"));

			menu_regular_tables.setBackgroundColor(Color.parseColor("#5E5E5E"));
			menu_liquors.setBackgroundColor(Color.parseColor("#5E5E5E"));
			menu_sushi_bar.setBackgroundColor(Color.parseColor("#5E5E5E"));
			menu_take_out.setBackgroundColor(Color.parseColor("#5E5E5E"));
			menu_dilivery.setBackgroundColor(Color.parseColor("#5E5E5E"));
			menu_staff_sales.setBackgroundColor(Color.parseColor("#797979"));
			menu_current_drawer.setBackgroundColor(Color.parseColor("#5E5E5E"));
			menu_manager_sales.setBackgroundColor(Color.parseColor("#5E5E5E"));
			menu_drawers_history
					.setBackgroundColor(Color.parseColor("#5E5E5E"));
		} else if (Constant.Area.equals("CURRENT DRAWER")) {

			tv_main_tables.setTextColor(Color.parseColor("#FFFFFF"));
			tv_drawers_history.setTextColor(Color.parseColor("#FFFFFF"));
			tv_current_drawer.setTextColor(Color.parseColor("#97DA00"));
			tv_delivery.setTextColor(Color.parseColor("#FFFFFF"));
			tv_liquors.setTextColor(Color.parseColor("#FFFFFF"));
			tv_my_sales_report.setTextColor(Color.parseColor("#FFFFFF"));
			tv_sales_report.setTextColor(Color.parseColor("#FFFFFF"));
			tv_sushi_bar.setTextColor(Color.parseColor("#FFFFFF"));
			tv_take_out.setTextColor(Color.parseColor("#FFFFFF"));

			menu_regular_tables.setBackgroundColor(Color.parseColor("#5E5E5E"));
			menu_liquors.setBackgroundColor(Color.parseColor("#5E5E5E"));
			menu_sushi_bar.setBackgroundColor(Color.parseColor("#5E5E5E"));
			menu_take_out.setBackgroundColor(Color.parseColor("#5E5E5E"));
			menu_dilivery.setBackgroundColor(Color.parseColor("#5E5E5E"));
			menu_staff_sales.setBackgroundColor(Color.parseColor("#5E5E5E"));
			menu_current_drawer.setBackgroundColor(Color.parseColor("#797979"));
			menu_manager_sales.setBackgroundColor(Color.parseColor("#5E5E5E"));
			menu_drawers_history
					.setBackgroundColor(Color.parseColor("#5E5E5E"));
		} else if (Constant.Area.equals("SALES REPORT")) {

			tv_main_tables.setTextColor(Color.parseColor("#FFFFFF"));
			tv_drawers_history.setTextColor(Color.parseColor("#FFFFFF"));
			tv_current_drawer.setTextColor(Color.parseColor("#FFFFFF"));
			tv_delivery.setTextColor(Color.parseColor("#FFFFFF"));
			tv_liquors.setTextColor(Color.parseColor("#FFFFFF"));
			tv_my_sales_report.setTextColor(Color.parseColor("#FFFFFF"));
			tv_sales_report.setTextColor(Color.parseColor("#97DA00"));
			tv_sushi_bar.setTextColor(Color.parseColor("#FFFFFF"));
			tv_take_out.setTextColor(Color.parseColor("#FFFFFF"));

			menu_regular_tables.setBackgroundColor(Color.parseColor("#5E5E5E"));
			menu_liquors.setBackgroundColor(Color.parseColor("#5E5E5E"));
			menu_sushi_bar.setBackgroundColor(Color.parseColor("#5E5E5E"));
			menu_take_out.setBackgroundColor(Color.parseColor("#5E5E5E"));
			menu_dilivery.setBackgroundColor(Color.parseColor("#5E5E5E"));
			menu_staff_sales.setBackgroundColor(Color.parseColor("#5E5E5E"));
			menu_current_drawer.setBackgroundColor(Color.parseColor("#5E5E5E"));
			menu_manager_sales.setBackgroundColor(Color.parseColor("#797979"));
			menu_drawers_history
					.setBackgroundColor(Color.parseColor("#5E5E5E"));
		} else if (Constant.Area.equals("DRAWERS HISTORY")) {
			tv_main_tables.setTextColor(Color.parseColor("#FFFFFF"));
			tv_sushi_bar.setTextColor(Color.parseColor("#FFFFFF"));
			tv_current_drawer.setTextColor(Color.parseColor("#FFFFFF"));
			tv_delivery.setTextColor(Color.parseColor("#FFFFFF"));
			tv_liquors.setTextColor(Color.parseColor("#FFFFFF"));
			tv_my_sales_report.setTextColor(Color.parseColor("#FFFFFF"));
			tv_sales_report.setTextColor(Color.parseColor("#FFFFFF"));
			tv_take_out.setTextColor(Color.parseColor("#FFFFFF"));
			tv_drawers_history.setTextColor(Color.parseColor("#97DA00"));

			menu_regular_tables.setBackgroundColor(Color.parseColor("#5E5E5E"));
			menu_liquors.setBackgroundColor(Color.parseColor("#5E5E5E"));
			menu_sushi_bar.setBackgroundColor(Color.parseColor("#5E5E5E"));
			menu_take_out.setBackgroundColor(Color.parseColor("#5E5E5E"));
			menu_dilivery.setBackgroundColor(Color.parseColor("#5E5E5E"));
			menu_staff_sales.setBackgroundColor(Color.parseColor("#5E5E5E"));
			menu_current_drawer.setBackgroundColor(Color.parseColor("#5E5E5E"));
			menu_manager_sales.setBackgroundColor(Color.parseColor("#5E5E5E"));
			menu_drawers_history
					.setBackgroundColor(Color.parseColor("#797979"));
		}

	}

	PayHistoryAdapter historyAdapter = new PayHistoryAdapter(
			DeskMenuActivity.this, cashiers);
}

// public void startCashier() {
// // cashier.setStatus("1");
// // cashier.setCashierId("Front Desk Drawer");
// // cashier.setUserCode(Constant.currentStaff.getS_account());
// // cashier.setCreateTime(DateUtils.getDateEN());
//
// ll_start_cash_view.setVisibility(View.GONE);
// ll_started_cash_view.setVisibility(View.VISIBLE);
//
// startring_time = (TextView) ll_started_cash_view
// .findViewById(R.id.start_time);
// startring_time.setText(DateUtils.getDateEN());
//
// startring_cash = (TextView) ll_started_cash_view
// .findViewById(R.id.starting_cash);
// if (start_cash_amount != null
// && !TextUtils.isEmpty(start_cash_amount_s)) {
// startring_cash.setText("$" + start_cash_amount_s);
// cashier.setInitMoney(Float.parseFloat(start_cash_amount_s));
// } else {
// startring_cash.setText("$0.0");
// cashier.setInitMoney(0.00f);
// }
//
// if (status.equals("1")) {
// startring_cash.setText(df.format(cashTotal));
// }
//
// cash_sales = (TextView) ll_started_cash_view
// .findViewById(R.id.cash_sales);
// cash_sales.setText("$" + df.format(net_Sales));
//
// payin_out_cash = (TextView) ll_started_cash_view
// .findViewById(R.id.payin_out_cash);
//
// expect_cash = (TextView) ll_started_cash_view
// .findViewById(R.id.expect_cash);
//
// paidin_out_sum_view = (LinearLayout) ll_started_cash_view
// .findViewById(R.id.paidin_out_sum_view);
// paidin_out_sum = (TextView) ll_started_cash_view
// .findViewById(R.id.paidin_out_sum);
//
// payin_out_cash.setText("$0.00");
// expect_cash.setText("$100.00");
// paidin_out_sum.setText("0");
//
// paidin_out_sum_view.setClickable(true);
// paidin_out_sum_view.setOnClickListener(new OnClickListener() {
//
// @Override
// public void onClick(View v) {
//
// if (endPopupView != null && endPopupWindow != null
// && endPopupWindow.isShowing()) {
// endPopupWindow.dismiss();
// }
// ll_payIn_out_view = (LinearLayout) findViewById(R.id.ll_payIn_out_sum_view);
// ll_start_cash_view.setVisibility(View.GONE);
// ll_started_cash_view.setVisibility(View.INVISIBLE);
// ll_payIn_out_view.setVisibility(View.VISIBLE);
// choose_drawer_des.setVisibility(View.INVISIBLE);
// choose_drawer.setVisibility(View.INVISIBLE);
//
// lv_payIn_out = (ListView) ll_payIn_out_view
// .findViewById(R.id.pay_history);
// lv_payIn_out.setAdapter(historyAdapter);
//
// payIn_out_cancel = (Button) ll_payIn_out_view
// .findViewById(R.id.payIn_out_cancel);
// payIn_out_save = (Button) ll_payIn_out_view
// .findViewById(R.id.payIn_out_save);
//
// payIn_out_enter_amount = (TextView) ll_payIn_out_view
// .findViewById(R.id.payIn_out_enter_amount);
// payIn_out_enter_des = (TextView) ll_payIn_out_view
// .findViewById(R.id.payIn_out_des);
//
// payIn = (Button) ll_payIn_out_view.findViewById(R.id.payIn);
// payOut = (Button) ll_payIn_out_view.findViewById(R.id.payOut);
//
// payIn_out_cancel.setOnClickListener(new OnClickListener() {
//
// @Override
// public void onClick(View v) {
// ll_payIn_out_view.setVisibility(View.INVISIBLE);
// ll_started_cash_view.setVisibility(View.VISIBLE);
//
// choose_drawer.setVisibility(View.VISIBLE);
// choose_drawer_des.setVisibility(View.VISIBLE);
// }
// });
//
// payIn_out_save.setOnClickListener(new OnClickListener() {
//
// @Override
// public void onClick(View v) {
// // method stub
//
// }
// });
//
// payIn.setOnClickListener(new OnClickListener() {
//
// @Override
// public void onClick(View v) {
//
// // d_Cashier cashier = new d_Cashier();
// // if
// // (!TextUtils.isEmpty(payIn_out_enter_amount.getText()
// // .toString())
// // && !TextUtils.isEmpty(payIn_out_enter_des
// // .getText().toString())) {
// //
// // cashier.setChangeMoney(Float
// // .parseFloat(payIn_out_enter_amount
// // .getText().toString()));
// // cashier.setOtherspec(payIn_out_enter_des.getText()
// // .toString());
// // cashier.setCreateTime(DateUtils.getDateEN());
// // // // 上传
// // historyAdapter.notifyDataSetChanged();
// // } else {
// // return;
// // }
// }
// });
//
// payOut.setOnClickListener(new OnClickListener() {
// @Override
// public void onClick(View v) {
// // d_Cashier cashier = new d_Cashier();
// // if
// // (!TextUtils.isEmpty(payIn_out_enter_amount.getText()
// // .toString())
// // && !TextUtils.isEmpty(payIn_out_enter_des
// // .getText().toString())) {
// //
// // cashier.setChangeMoney(-Float
// // .parseFloat(payIn_out_enter_amount
// // .getText().toString()));
// // cashier.setOtherspec(payIn_out_enter_des.getText()
// // .toString());
// // cashier.setCreateTime(DateUtils.getDateEN());
// // // 上传
// // historyAdapter.notifyDataSetChanged();
// // } else {
// // return;
// // }
//
// }
// });
//
// }
// });
//
// end_drawer = (Button) ll_started_cash_view
// .findViewById(R.id.end_drawer);
// end_drawer.setOnClickListener(new OnClickListener() {
//
// @SuppressWarnings("deprecation")
// @Override
// public void onClick(View v) {
// endPopupView = View.inflate(DeskMenuActivity.this,
// R.layout.pop_end_drawer, null);
//
// int[] location = new int[2];
// endPopupView.getLocationInWindow(location);// 测量view相对于窗体的位置，把测量后的位置存到数组中
// endPopupWindow = new PopupWindow(endPopupView, 420, 280);
// endPopupWindow.setFocusable(true);
// endPopupWindow.setBackgroundDrawable(new BitmapDrawable());
// endPopupWindow.showAtLocation(
// endPopupView,
// Gravity.LEFT | Gravity.TOP,
// location[0]
// + DensityUtil.dp2px(DeskMenuActivity.this, 385),
// location[1]
// + DensityUtil.dp2px(DeskMenuActivity.this, 180));// 代码里写的偏移都是px
// // 需要转换成dp
//
// end_starting_time = (TextView) endPopupView
// .findViewById(R.id.end_starting_time);
// end_starting_time.setText(startring_time.getText().toString());
//
// end_expected_cash = (TextView) endPopupView
// .findViewById(R.id.end_expected_cash);
// end_expected_cash.setText("$11");
//
// end_actual_cash = (EditText) endPopupView
// .findViewById(R.id.end_actual_cash);
//
// expected_cash = end_expected_cash.getText().toString()
// .substring(1);
//
// end_actual_cash.setGravity(Gravity.RIGHT);
//
// end_actual_cash.addTextChangedListener(new TextWatcher() {
//
// @Override
// public void onTextChanged(CharSequence s, int start,
// int before, int count) {
//
// if (!TextUtils.isEmpty(end_actual_cash.getText()
// .toString())
// && !TextUtils.isEmpty(expected_cash)) {
// difference_cash = Double.parseDouble(expected_cash)
// - Double.parseDouble(end_actual_cash
// .getText().toString()) + "";
//
// } else {
// difference_cash = Double.parseDouble(expected_cash)
// - 0 + "";
// }
// if (Double.parseDouble(difference_cash) < 0) {
// end_difference.setTextColor(Color
// .parseColor("#97DA00"));
// } else {
// end_difference.setTextColor(Color
// .parseColor("#ffffff"));
// }
// end_difference.setText("$" + difference_cash);
// }
//
// @Override
// public void beforeTextChanged(CharSequence s, int start,
// int count, int after) {
//
// }
//
// @Override
// public void afterTextChanged(Editable s) {
//
// }
// });
//
// end_difference = (TextView) endPopupView
// .findViewById(R.id.end_difference);
// end_difference.setText("$1");
//
// end_drawer_p = (Button) endPopupView
// .findViewById(R.id.end_drawer_p);
// end_drawer_p.setOnClickListener(new OnClickListener() {
//
// @Override
// public void onClick(View v) {
// endPopupWindow.dismiss();
// ll_started_cash_view.setVisibility(View.GONE);
// ll_start_cash_view.setVisibility(View.VISIBLE);
// }
// });
// }
// });
// }
// }
