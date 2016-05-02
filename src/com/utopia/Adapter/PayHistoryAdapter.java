package com.utopia.Adapter;

import java.util.List;

import com.utopia.Model.d_Cashier;
import com.utopia.activity.DeskMenuActivity;
import com.utopia.activity.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PayHistoryAdapter extends BaseAdapter {

	private Context context;
	private List<d_Cashier> cashiers;

	public PayHistoryAdapter(DeskMenuActivity deskMenuActivity,
			List<d_Cashier> cashiers) {
		this.context = deskMenuActivity;
		this.cashiers = cashiers;
	}

	@Override
	public int getCount() {
		return cashiers.size();
	}

	@Override
	public Object getItem(int position) {
		return cashiers.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View
					.inflate(context, R.layout.item_pay_history, null);
			holder.pay_history_time = (TextView) convertView
					.findViewById(R.id.pay_history_time);
			holder.pay_history_des = (TextView) convertView
					.findViewById(R.id.pay_history_des);
			holder.pay_history_money = (TextView) convertView
					.findViewById(R.id.pay_history_money);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.pay_history_time.setText(cashiers.get(position).getCreateTime());
		holder.pay_history_des.setText(cashiers.get(position).getOtherspec());
		holder.pay_history_money.setText("$"
				+ cashiers.get(position).getChangeMoney());
		return convertView;
	}

	class ViewHolder {
		TextView pay_history_time;
		TextView pay_history_des;
		TextView pay_history_money;
	}

}
