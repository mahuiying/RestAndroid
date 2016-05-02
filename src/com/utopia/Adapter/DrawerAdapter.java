package com.utopia.Adapter;

import java.util.List;

import com.utopia.Model.d_CashierInfor;
import com.utopia.Model.d_drawerInfor;
import com.utopia.activity.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DrawerAdapter extends BaseAdapter {

	private Context context;
	private List<d_CashierInfor> mList;
	
	public DrawerAdapter(Context context, List<d_CashierInfor> mList) {
		
		this.context = context;
		this.mList = mList;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		
		return mList.get(position);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup convertViewGroup) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		if(convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(context)
					.inflate(R.layout.drawer_history_select_item,null);
			viewHolder.itemTime = (TextView) convertView.findViewById(R.id.item_time);
			viewHolder.itemDiffer = (TextView) convertView.findViewById(R.id.item_differ);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.itemTime.setText(mList.get(position).getCreateTime()+" - "+
				mList.get(position).getCloseTime());
		viewHolder.itemDiffer.setText("$"+mList.get(position).getDifferent());
		return convertView;
	}

	class ViewHolder {
		TextView itemTime;
		TextView itemDiffer;
	}
}
