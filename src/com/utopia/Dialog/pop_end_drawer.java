package com.utopia.Dialog;

import com.utopia.Base.BaseActivity;
import com.utopia.activity.DeskMenuActivity;
import com.utopia.activity.R;
import com.utopia.utils.JsonResolveUtils;
import com.utopia.widget.MyTextView;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class pop_end_drawer implements OnClickListener{
    
	private PopupWindow popupWindow;
	private LinearLayout ll_start_drawer;
	private LinearLayout ll_started_drawer;
	private View localView;
	private Context context;
	private String drawerName;
	private String startTime;
	private float cash;
	
	private TextView drawer_name;
	private TextView start_time;
	private TextView drawer_cash;
	private EditText actual_cash;
	private TextView difference; 
	private Button end_drawer;
	private View view;
	
	
	public pop_end_drawer(Context context,View view,String drawerName,String startTime
			,float cash, LinearLayout ll_start_drawer,
			LinearLayout ll_started_drawer){
		super();
		this.context = context;
		this.view = view;
		this.drawerName = drawerName;
		this.startTime = startTime;
		this.cash = cash;
		this.ll_start_drawer = ll_start_drawer;
		this.ll_started_drawer = ll_started_drawer;
		if (this.popupWindow != null)
			return;
		localView = LayoutInflater.from(context).inflate(R.layout.pop_end_drawer,
				null);
		this.popupWindow = new PopupWindow(localView, 400, 320);// 设置窗体的大小
		this.popupWindow.setFocusable(true);
		this.popupWindow.update();
		this.popupWindow.showAtLocation(view, 16 , 0, 0);// 设置弹出窗体的位置（相对于payment_btn控件）
		drawer_name = (TextView) localView.findViewById(R.id.end_drawer_name);
		start_time = (TextView) localView.findViewById(R.id.end_starting_time);
		drawer_cash = (TextView) localView.findViewById(R.id.end_expected_cash);
		actual_cash = (EditText) localView.findViewById(R.id.end_actual_cash);
		difference = (TextView) localView.findViewById(R.id.end_difference);
		end_drawer = (Button) localView.findViewById(R.id.end_drawer_p);
		end_drawer.setOnClickListener(this);
		drawer_name.setText(drawerName);
		start_time.setText(startTime);
		drawer_cash.setText("$"+cash);
		actual_cash.setOnKeyListener(onKey);
		
	}
	public void closePop() {
		if (popupWindow != null)
			popupWindow.dismiss();
	}

	OnKeyListener onKey=new OnKeyListener() {  
		  
		@Override
		public boolean onKey(View arg0, int keyCode, KeyEvent arg2) {
			// TODO Auto-generated method stub
			if(keyCode == KeyEvent.KEYCODE_ENTER){
				float cash_except = Float.parseFloat(drawer_cash.getText().toString()
						.replace("$", ""));
				float cash_actual = Float.parseFloat(actual_cash.getText().toString()
						.replace("$", ""));
				float  differ = cash_actual - cash_except;
				String str1=differ+"";
				if(differ<0){
					str1=str1.replace("-","");
					str1="-$"+str1;
				}else{
					str1="$"+str1;
				}
				difference.setText(str1);
				return true;
			}
			    return false;
		} 
	};
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId()==R.id.end_drawer_p){
		
			closeDrawer();
			closePop();
		}
			
	}
	private void closeDrawer() {
		// TODO Auto-generated method stub
	    new AsyncTask<Void,Void,Boolean>(){
            boolean  f1,f2;
			@Override
			protected Boolean doInBackground(Void... arg0) {
				// TODO Auto-generated method stub
			f1 = new JsonResolveUtils(context)
				    .setDrawerStateCloseed(drawerName,
				    		Float.parseFloat(actual_cash.getText().toString().
				    				trim().replace("$", "")),
				    				Float.parseFloat(difference.getText().toString().
				    						trim().replace("$", "")));
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 f2 = new JsonResolveUtils(context)
			         .setDrawerStateClose(drawerName);
			 if(f1==true && f2==true)
				 return true;
			 return false;
			}
			
			@Override
			protected void onPostExecute(Boolean result){
				if(result){
					closePop();
					
					ll_started_drawer.setVisibility(View.GONE);
					ll_start_drawer.setVisibility(View.VISIBLE);
				}else{
					showCustomToast("The operation is failed!");
				}
			}
			
		}.execute();
	}
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
