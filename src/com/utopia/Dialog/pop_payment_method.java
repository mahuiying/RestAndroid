package com.utopia.Dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;

import com.utopia.Dao.sql_Bill;
import com.utopia.Model.d_Bill;
import com.utopia.activity.R;

public class pop_payment_method implements View.OnClickListener {
	private PopupWindow popupWindow;
	private Context context;
	private Button payment_btn;
	private d_Bill bill;

	public pop_payment_method(Context context, Button payment_btn, d_Bill mbill) {
		super();
		this.context = context;
		this.payment_btn = payment_btn;
		this.bill = mbill;
		// Log.i("tag",mbill.getSalerecordId());
		if (this.popupWindow != null)
			return;
		View localView = LayoutInflater.from(context).inflate(
				R.layout.payment_method, null);
		this.popupWindow = new PopupWindow(localView, 320, 220);// 设置窗体的大小
		this.popupWindow.setFocusable(true);
		this.popupWindow.update();
		this.popupWindow.showAsDropDown(payment_btn, -100, 0);// 设置弹出窗体的位置（相对于payment_btn控件）
		localView.findViewById(R.id.method1).setOnClickListener(this);
		localView.findViewById(R.id.method2).setOnClickListener(this);
		localView.findViewById(R.id.method3).setOnClickListener(this);
		localView.findViewById(R.id.method4).setOnClickListener(this);
		localView.findViewById(R.id.method5).setOnClickListener(this);
		localView.findViewById(R.id.method6).setOnClickListener(this);
	}

	public void closePop() {
		if (popupWindow != null)
			popupWindow.dismiss();
	}

	@Override
	public void onClick(View v) {
	
		switch (v.getId()) {
		case R.id.method1: // 银行卡
			payment_btn.setText("");
			payment_btn.setBackgroundResource(R.drawable.pay_method1);
			bill.setTipPayment("Credit Card");
			bill.setPayment("visa");
			
			(new sql_Bill()).save(bill);
			
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			closePop();

			break;
		case R.id.method2: // 银行卡
			payment_btn.setText("");
			payment_btn.setBackgroundResource(R.drawable.pay_method2);
			bill.setTipPayment("Credit Card");
			bill.setPayment("mastercard");
			(new sql_Bill()).save(bill);
			
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			closePop();

			break;
		case R.id.method3: // 银行卡
			payment_btn.setText("");
			payment_btn.setBackgroundResource(R.drawable.pay_method3);
			bill.setTipPayment("Credit Card");
			bill.setPayment("american express");
			(new sql_Bill()).save(bill);

			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			closePop();

			break;
		case R.id.method4: // 银行卡
			payment_btn.setText("");
			payment_btn.setBackgroundResource(R.drawable.pay_method4);
			bill.setTipPayment("Credit Card");
			bill.setPayment("discover");
			(new sql_Bill()).save(bill);
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			closePop();

			break;
		case R.id.method5: // 银行卡
			payment_btn.setText("");
			payment_btn.setBackgroundResource(R.drawable.pay_method5);
			bill.setTipPayment("Credit Card");
			bill.setPayment("other card");
			(new sql_Bill()).save(bill);
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			closePop();

			break;
		case R.id.method6:// 现金支付
			payment_btn.setText("");
			payment_btn.setBackgroundResource(R.drawable.pay_method6);
			bill.setTipPayment("Cash");
			bill.setPayment("cash");
			closePop();
			new chooseDrawerDialog(context, bill).show();
			break;
		}

	}

}
