package com.utopia.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.utopia.Base.BaseActivity;
import com.utopia.Dao.sql_Cashier;
import com.utopia.Model.d_Cashier;
import com.utopia.Service.HomeKeyLocker;
import com.utopia.utils.Constant;
import com.utopia.utils.DateUtils;
import com.utopia.utils.ExitApplication;
import com.utopia.utils.JsonResolveUtils;

public class CashierActivity extends BaseActivity implements OnClickListener {
	private TextView editText;
	private String initMoney;
	private String curMoney = "0";
	private HomeKeyLocker mHomeKeyLocker;
	d_Cashier localCashier = new d_Cashier();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cashier);
		ExitApplication.getInstance().addActivity(this);
		mHomeKeyLocker = new HomeKeyLocker();
		mHomeKeyLocker.lock(CashierActivity.this);

		initViews();
		initEvents();
	}

	protected void onDestroy() {
		mHomeKeyLocker.unlock();
		mHomeKeyLocker = null;
		super.onDestroy();
	}

	@Override
	public void onClick(View view) {

		if (view.getId() != R.id.next && view.getId() != R.id.goback
				&& view.getId() != R.id.btn_clear) {
			if (editText.getText().toString().length() > 8) {
				return;
			}
			if (editText.getText().toString().length() == 8
					&& view.getId() == R.id.btn_dot) {
				showCustomToast("The input is illegal...");
				return;
			}
		}
		switch (view.getId()) {
		case R.id.ok:
			initMoney = editText.getText().toString();

			/*
			 * if(initMoney.equals("")){
			 * showCustomToast("Please input the initial amount of money...");
			 * break; }
			 */
			insert();
			new RefreshAsyncTask().execute();
            
			Constant.clockInTime = DateUtils.getDateEN();
			CashierActivity.this.startActivity(new Intent(CashierActivity.this,
					DeskMenuActivity.class));
			return;

		case R.id.goback:
			startActivity(new Intent(this, DeviceListActivity.class));
			break;
		case R.id.btn_one:
			curMoney = editText.getText().toString() + "1";
			break;
		case R.id.btn_two:
			curMoney = editText.getText().toString() + "2";
			break;
		case R.id.btn_three:
			curMoney = editText.getText().toString() + "3";
			break;
		case R.id.btn_four:
			curMoney = editText.getText().toString() + "4";
			break;
		case R.id.btn_five:
			curMoney = editText.getText().toString() + "5";
			break;
		case R.id.btn_six:
			curMoney = editText.getText().toString() + "6";
			break;
		case R.id.btn_seven:
			curMoney = editText.getText().toString() + "7";
			break;
		case R.id.btn_eight:
			curMoney = editText.getText().toString() + "8";
			break;
		case R.id.btn_nine:
			curMoney = editText.getText().toString() + "9";
			break;
		case R.id.btn_zero:
			curMoney = editText.getText().toString() + "0";
			break;
		case R.id.btn_dot:
			if (!editText.getText().toString().contains(".")) {
				if (editText.getText().toString().length() == 0) {
					editText.setText("0");
				}
				editText.setText(editText.getText().toString() + ".");
			}
			return;
		case R.id.btn_clear:
			if (editText.getText().toString().length() > 0)
				editText.setText(editText
						.getText()
						.toString()
						.subSequence(0,
								editText.getText().toString().length() - 1));
			return;
		}

		if (Float.parseFloat(curMoney) >= 0.0) {
			editText.setText(curMoney);
		}
	}

	private void insert() {
		if (initMoney.equals("")) {

		} else {
			localCashier.setInitMoney(Float.parseFloat(initMoney));
			localCashier.setCreateTime(DateUtils.getDateEN());
			localCashier.setUserCode(Constant.userCode);
			localCashier.setCashierId(Constant.cashierId);
			localCashier.setStatus("init");

			new sql_Cashier(this).save(localCashier);
		}

	}

	private class RefreshAsyncTask extends AsyncTask<Void, Void, Boolean> {
		// onPreExecute()方法用于在执行异步任务前,主线程做一些准备工作
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		// doInBackground()方法用于在执行异步任务,不可以更改主线程中UI
		@Override
		protected Boolean doInBackground(Void... params) {
			System.out.println("调用doInBackground()方法--->开始执行异步任务");

			return new JsonResolveUtils(CashierActivity.this)
					.setCashierInitMoney(localCashier);
		}

		// onPostExecute()方法用于异步任务执行完成后,在主线程中执行的操作
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			System.out.println("调用onPostExecute()方法--->异步任务执行完毕");

		}

		// onCancelled()方法用于异步任务被取消时,在主线程中执行相关的操作
		@Override
		protected void onCancelled() {
			super.onCancelled();
			System.out.println("调用onCancelled()方法--->异步任务被取消");
		}
	}

	@Override
	protected void initViews() {
		editText = (TextView) findViewById(R.id.cashier_initMoney);
	}

	@Override
	protected void initEvents() {
		findViewById(R.id.btn_one).setOnClickListener(this);
		findViewById(R.id.btn_two).setOnClickListener(this);
		findViewById(R.id.btn_three).setOnClickListener(this);
		findViewById(R.id.btn_four).setOnClickListener(this);
		findViewById(R.id.btn_five).setOnClickListener(this);
		findViewById(R.id.btn_six).setOnClickListener(this);
		findViewById(R.id.btn_seven).setOnClickListener(this);
		findViewById(R.id.btn_eight).setOnClickListener(this);
		findViewById(R.id.btn_nine).setOnClickListener(this);
		findViewById(R.id.btn_zero).setOnClickListener(this);
		findViewById(R.id.btn_dot).setOnClickListener(this);
		findViewById(R.id.btn_clear).setOnClickListener(this);
		findViewById(R.id.ok).setOnClickListener(this);
	}
}
