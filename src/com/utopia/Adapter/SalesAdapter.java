package com.utopia.Adapter;

import java.text.DecimalFormat;
import java.util.List;

import com.utopia.Model.d_Sale;
import com.utopia.activity.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SalesAdapter extends BaseAdapter {

	DecimalFormat df = new DecimalFormat("###.00");
	private Context context;
	private List<d_Sale> mList;
	public SalesAdapter(Context context, List<d_Sale> mList){
		
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
	public View getView(int position, View convertView, ViewGroup viewGroup) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		if(convertView == null){
			viewHolder = new ViewHolder();
			View view = LayoutInflater.from(context).
					inflate(R.layout.shift_history, null);
			viewHolder.name = (TextView) view.findViewById(R.id.name);
			viewHolder.time = (TextView) view.findViewById(R.id.time);
			viewHolder.netSales = (TextView) view.findViewById(R.id.netSales);
			viewHolder.tips = (TextView) view.findViewById(R.id.tips);
			convertView = view;
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.name.setText(mList.get(position).getWaiter());
		viewHolder.time.setText(mList.get(position).getCreateTime()+"-"
				+mList.get(position).getCloseTime());
		viewHolder.netSales.setText("$"+df.format(mList.get(position).getInitTotal()));
		viewHolder.tips.setText("$"+df.format(mList.get(position).getTiptotal()));
		
		return convertView;
	}

	class ViewHolder{
		TextView name;
		TextView time;
		TextView netSales;
		TextView tips;
	}
}
