package com.utopia.Adapter;

import java.text.DecimalFormat;
import java.util.List;
import java.util.UUID;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.utopia.Dao.sql_SaleRecord;
import com.utopia.Model.d_Desk;
import com.utopia.activity.OrdersAcitvity;
import com.utopia.activity.R;
import com.utopia.utils.Constant;
import com.utopia.utils.DateUtils;
import com.utopia.utils.Md5;
import com.utopia.utils.Snippet;

public class DeliveryAdapter extends BaseAdapter implements OnClickListener {
	private Context context;
	private List<d_Desk> lstDate;
	private int done_count = 0;
	private String md5 = Md5.md5(Snippet.generateID());
	private int type = 0; // 0 -- delivery    1--pickup
	public DeliveryAdapter(Context mContext, List<d_Desk> list, int tag) {
		this.context = mContext;
		this.lstDate = list;
		this.type = tag;
		int i = lstDate.size();
		for(int j = i ; j < i+4 ; j++){
			lstDate.add(j, new d_Desk(0, "0", "0", "0", "0", 2,
					"2014-12-12 12:12:12", 0, 0, 0, 0, 0));
		}
	}

	public int getCount() {
		return lstDate.size();

	}

	public Object getItem(int position) {
		return lstDate.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {
		AppItem localAppItem = new AppItem();

		if (paramView == null) {
			if (paramInt == 0) {// 首个按钮置0
				paramView = LayoutInflater.from(this.context).inflate(
						R.layout.delivery_items2, null);
				(paramView.findViewById(R.id.desk_content))
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								Constant.table_id = UUID.randomUUID().toString();
								Intent intent = new Intent(context,
										OrdersAcitvity.class);
								intent.putExtra("currentPage", 2);
								intent.putExtra("type", type+"");
								context.startActivity(intent);
							}
						});
			} else {
				paramView = LayoutInflater.from(this.context).inflate(
						R.layout.delivery_items, null);
				if (lstDate.get(paramInt).getState().equals("Unpaid")) {
//					(paramView.findViewById(R.id.desk_content))
//							.setBackgroundColor(Color.parseColor("#eee9b3"));
//					((TextView) paramView.findViewById(R.id.desk_state))
//							.setTextColor(Color.RED);
					((Button) paramView.findViewById(R.id.waiter_name))
					        .setBackgroundColor(Color.parseColor("#FF4E4E"));
				}else{
					((Button) paramView.findViewById(R.id.waiter_name))
			        .setBackgroundColor(Color.parseColor("#838E66"));
				}
			}
			localAppItem.ll = (LinearLayout) paramView
					.findViewById(R.id.delivery_desk);
			localAppItem.waiter_name = ((Button) paramView
					.findViewById(R.id.waiter_name));
			localAppItem.desk_money = ((TextView) paramView
					.findViewById(R.id.desk_money));
			localAppItem.desk_state = ((TextView) paramView
					.findViewById(R.id.desk_state));
			localAppItem.desk_time = ((TextView) paramView
					.findViewById(R.id.desk_time));
			localAppItem.desk_name = ((TextView) paramView
					.findViewById(R.id.desk_name));
			localAppItem.clock = ((ImageView) paramView
					.findViewById(R.id.clock));
			paramView.setTag(localAppItem);

		} else {
			localAppItem = (AppItem) paramView.getTag();
		}
		float money = 0;
		
		int customerNo=0;
		
		if (lstDate.get(paramInt).getStatetime() == 0) {
			localAppItem.desk_time.setText(lstDate.get(paramInt).getStarttime()
					.subSequence(11, 16));
			Cursor m_cursor = (new sql_SaleRecord()).recordlist3("select * from SaleRecord as s1 join saleandpdt as s2" +
					" on s1.itemNo=s2.salerecordId where deskName='"+
					lstDate.get(paramInt).getDesk_name()+"' and s2.status1!='Finish'");
			while(m_cursor.moveToNext()){
				money += (Float.parseFloat(m_cursor.getString(m_cursor.getColumnIndex("price"))))*(m_cursor.getInt(m_cursor.getColumnIndex("number")));
			}
			String current_time=DateUtils.getDateEN();	
			String sendtime;	
			if(m_cursor.moveToFirst()){
			    sendtime=m_cursor.getString(m_cursor.getColumnIndex("createTime"));
			    customerNo=m_cursor.getInt(m_cursor.getColumnIndex("customerId"));
			}
			else
				sendtime=lstDate.get(paramInt).getStarttime();
			if(sendtime.compareTo(current_time)>0){
				localAppItem.desk_state.setText(sendtime.subSequence(11, 16));
			}else{
			      localAppItem.desk_state.setText("Ordered");
			}
			
			if (done_count % 4 != 0) {// 留空行
				++done_count;
				lstDate.add(paramInt, new d_Desk(0, "0", "0", "0", "0", 2,
						"2014-12-12 12:12:12", 0, 0, 0, 0, 0));
			}
			m_cursor.close();
		} else {// done
			++done_count;
			//localAppItem.desk_time.setText("Done");
			localAppItem.desk_time.setText(lstDate.get(paramInt).getStarttime()
					.subSequence(11, 16));
			localAppItem.desk_state.setText("Done");
		}
        
		if (lstDate.get(paramInt).getStatetime() == 2) {// 空格
			paramView.setVisibility(View.INVISIBLE);
			
		}
		localAppItem.waiter_name.setText(lstDate.get(paramInt).getS_account());
		if(customerNo!=0){
		    Cursor m_cursor1 = (new sql_SaleRecord()).recordlist3("select Name from contact where id="+customerNo);
		    localAppItem.waiter_name.setText(m_cursor1.getInt(0));
		}
		localAppItem.ll.setTag(lstDate.get(paramInt));
		
		//localAppItem.desk_money.setText("$21.5");
		
		localAppItem.desk_money.setText("$"+ new DecimalFormat("0.00").format(money));
		//localAppItem.desk_state.setText(lstDate.get(paramInt).getState());
		if(lstDate.get(paramInt).getDesk_name().length()>7){
			localAppItem.desk_name.setText(lstDate.get(paramInt).getDesk_name().substring(0, 7));
		}else{
			localAppItem.desk_name.setText(lstDate.get(paramInt).getDesk_name());
		}
		localAppItem.ll.setOnClickListener(this);
		return paramView;
	}

	class AppItem {
		LinearLayout ll;
		Button waiter_name;
		TextView desk_money;
		TextView desk_state;
		TextView desk_time;
		TextView desk_name;
		ImageView clock;
	}

	public void onClick(View paramView) {
		if (!Constant.pop) {
			d_Desk locald_keycode = (d_Desk) paramView.getTag();

			Constant.table_id = locald_keycode.getDesk_name().toString();

			Intent intent = new Intent(context, OrdersAcitvity.class);
			Bundle bundle = new Bundle();
			bundle.putString("Md5", md5);
			intent.putExtra("currentPage", 1);
			intent.putExtra("type", 2);
			intent.putExtras(bundle);
			context.startActivity(intent);

		}
	}
}