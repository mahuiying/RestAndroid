package com.utopia.Dialog;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.utopia.Base.BaseDialog;
import com.utopia.Dao.sql_Bill;
import com.utopia.Model.d_Bill;
import com.utopia.Model.d_Cashier;
import com.utopia.activity.R;
import com.utopia.utils.Constant;
import com.utopia.utils.JsonResolveUtils;
import com.utopia.widget.MyTextView;

public class chooseDrawerDialog extends BaseDialog {
   
	private Context context;
	private ListView drawer_list;
    private ArrayAdapter<String> adapter;
    private d_Bill bill;
    private String[] cashiers;
	public chooseDrawerDialog(Context context,d_Bill bill) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context=context;
		this.bill=bill;
		init();
		initDrawerList();
		
		
	}
	private void init(){
		setContentView(R.layout.choose_drawer_dialog);
		setCanceledOnTouchOutside(false);	//禁用框外点击
		drawer_list=(ListView) findViewById(R.id.drawer_list);
		drawer_list.setAdapter(adapter);
		drawer_list.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long id) {
				//TextView textView = (TextView) view;
				final String drawerName = cashiers[position];
				Log.i("tag","钱箱为:"+drawerName);
			    new AsyncTask<Void,Void,d_Cashier>(){
			
				@Override
				protected d_Cashier doInBackground(Void... arg0) {
					// TODO Auto-generated method stub
					d_Cashier cashierInfo = 
							new JsonResolveUtils(context).getCashiers(drawerName);
					return cashierInfo;
				}
				
				@Override
				protected void onPostExecute(d_Cashier cashier) {
					if(cashier != null){
						if(cashier.getStatus().equals("1")){
							bill.setCashierId(cashier.getCashierId());
							(new sql_Bill()).save(bill);
							Log.i("tag","保存成功。。。。");
						}else{
							showCustomToast("The cashier is not open!");
						}
					}else{
						showCustomToast("The operation is failed!");
					}
					
				    dismiss();
				}
			};
			}
		});
	}
	private void initDrawerList(){
		cashiers=new String[]{"Front Desk Drawer","Lounge Bar Drawer","Dining Tables Drawer"};
		adapter=new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,cashiers);
		drawer_list.setAdapter(adapter);
		
		
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
