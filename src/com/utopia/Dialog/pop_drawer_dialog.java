package com.utopia.Dialog;

import com.utopia.Base.BaseActivity;
import com.utopia.Model.d_Cashier;
import com.utopia.activity.DeskMenuActivity;
import com.utopia.activity.R;
import com.utopia.utils.DateUtils;
import com.utopia.utils.JsonResolveUtils;
import com.utopia.widget.MyTextView;

import android.content.Context;

import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class pop_drawer_dialog implements OnClickListener {

	private PopupWindow popupWindow;
	private Context context;
	private LinearLayout ll_start_drawer;
	private LinearLayout ll_started_drawer;
	private TextView choose_button;

	
	private View localView;
	private TextView front_drawer;
	private TextView lounge_drawer;
	private TextView dining_drawer;
	private ImageView front_drawer_img;
	private ImageView lounge_drawer_img;
	private ImageView dining_drawer_img;
 	private d_Cashier cashiers;
 	

	public pop_drawer_dialog(Context context, LinearLayout ll_start_drawer,
			LinearLayout ll_started_drawer,TextView choose_button) {
		super();
		this.context = context;
		this.ll_start_drawer = ll_start_drawer;
		this.ll_started_drawer = ll_started_drawer;
	
		this.choose_button = choose_button;
		// Log.i("tag",mbill.getSalerecordId());
		if (this.popupWindow != null)
			return;
		localView = LayoutInflater.from(context).inflate(R.layout.drawerchoose,
				null);
		this.popupWindow = new PopupWindow(localView, 320, 220);// 设置窗体的大小
		this.popupWindow.setFocusable(true);
		this.popupWindow.update();
		//解决在弹出窗外点击，弹出窗不消失得情况，为该弹出窗体设置背景图片
		popupWindow.setBackgroundDrawable(context.getResources().getDrawable(
                R.drawable.drawer_choose));
		this.popupWindow.showAsDropDown(choose_button, -100, 0);// 设置弹出窗体的位置（相对于payment_btn控件）
		front_drawer = (TextView) localView.findViewById(R.id.front_drawer);
		lounge_drawer = (TextView) localView.findViewById(R.id.lounge_drawer);
		dining_drawer = (TextView) localView.findViewById(R.id.dining_drawer);
		front_drawer_img = (ImageView) localView.findViewById(R.id.front_drawer_img);
		lounge_drawer_img = (ImageView) localView.findViewById(R.id.lounge_drawer_img);
		dining_drawer_img = (ImageView) localView.findViewById(R.id.dining_drawer_img);
		front_drawer.setOnClickListener(this);
		lounge_drawer.setOnClickListener(this);
		dining_drawer.setOnClickListener(this);
	}

	public void closePop() {
		if (popupWindow != null)
			popupWindow.dismiss();
	}

	
	public String cashierId=null;
	@Override
	public void onClick(View v) {
		
		front_drawer_img.setVisibility(View.INVISIBLE);
		lounge_drawer_img.setVisibility(View.INVISIBLE);
		dining_drawer_img.setVisibility(View.INVISIBLE);
		

		switch (v.getId()) {
		case R.id.front_drawer:
			front_drawer_img.setVisibility(View.VISIBLE);
			front_drawer.setTextColor(android.graphics.Color
					.parseColor("#8BC607"));
			lounge_drawer.setTextColor(android.graphics.Color
					.parseColor("#FFFFFF"));
			dining_drawer.setTextColor(android.graphics.Color
					.parseColor("#FFFFFF"));
			choose_button.setText("Front Desk Drawer");
			cashierId = "Front Desk Drawer";
			break;
		case R.id.lounge_drawer:
			lounge_drawer_img.setVisibility(View.VISIBLE);
			front_drawer.setTextColor(android.graphics.Color
					.parseColor("#FFFFFF"));
			lounge_drawer.setTextColor(android.graphics.Color
					.parseColor("#8BC607"));
			dining_drawer.setTextColor(android.graphics.Color
					.parseColor("#FFFFFF"));
			choose_button.setText("Lounge Bar Drawer");
			cashierId = "Lounge Bar Drawer";
			break;
		case R.id.dining_drawer:
			dining_drawer_img.setVisibility(View.VISIBLE);
			front_drawer.setTextColor(android.graphics.Color
					.parseColor("#FFFFFF"));
			lounge_drawer.setTextColor(android.graphics.Color
					.parseColor("#FFFFFF"));
			dining_drawer.setTextColor(android.graphics.Color
					.parseColor("#8BC607"));
			choose_button.setText("Dining Tables Drawer");
			cashierId = "Dining Tables Drawer";
			break;
		}

		new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected void onPreExecute() {

			}

			@Override
			protected Boolean doInBackground(Void... arg0) {
				// TODO Auto-generated method stub
				//cashierId
				cashiers = new JsonResolveUtils(context).getCashiers(cashierId);
				
				
				if (cashiers != null)
					   return true;
				return false;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				if (result) {
					
					if (cashiers.getStatus().equals("0")) {
						closePop();
						ll_started_drawer.setVisibility(View.GONE);
						ll_start_drawer.setVisibility(View.VISIBLE);

					} else {
						closePop();
						ll_start_drawer.setVisibility(View.GONE);
						ll_started_drawer.setVisibility(View.VISIBLE);

					}
				}else{
					closePop();
					showCustomToast("The operation is failed!");
				}
			}

		}.execute();

	}
	/** 显示自定义Toast提示(来自String) **/
	public void showCustomToast(String text) {
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
