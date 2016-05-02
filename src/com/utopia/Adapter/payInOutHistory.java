package com.utopia.Adapter;

import java.util.List;

import com.utopia.Model.d_pay_in_out;
import com.utopia.activity.R;


import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class payInOutHistory extends BaseAdapter{

	private Context context;
	private List<d_pay_in_out> mList;
	
	public payInOutHistory(Context context,List<d_pay_in_out> mList){
		this.context = context;
		this.mList = mList;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return mList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View paramView, ViewGroup viewGroup) {
		// TODO Auto-generated method stub
		
		if(paramView==null){
			View view = LayoutInflater.from(context).
					inflate(R.layout.pay_in_out_history, null);
			ParamItem paramItem = new ParamItem();
			paramItem.time = (TextView)view.findViewById(R.id.history_time);
			paramItem.reason = (TextView)view.findViewById(R.id.history_reason);
		    paramItem.money = (TextView) view.findViewById(R.id.history_money);
		   
		    view.setTag(paramItem);
		    paramView = view;
		}
			ParamItem paramIteam2 = (ParamItem) paramView.getTag();
			paramIteam2.time.setText(mList.get(position).getTime());
			paramIteam2.reason.setText(mList.get(position).getDescription());
			String str1;
			if(mList.get(position).getMoney()<0){
				str1=mList.get(position).getMoney()+"";
				str1=str1.replace("-", "");
				str1="-$"+str1;
			}else{
				str1="$"+mList.get(position).getMoney();
			}
			paramIteam2.money.setText(str1);
		return paramView;
	}
	class ParamItem{
		TextView time;
		TextView reason;
		TextView money;
	}

}
