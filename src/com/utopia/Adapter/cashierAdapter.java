package com.utopia.Adapter;

import android.R;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.utopia.utils.Constant;

public class cashierAdapter extends BaseAdapter {

	private Context context;
	private String[] cashier;

	public cashierAdapter(Context context) {
		this.context = context;
		cashier = new String[20];
		SQLiteDatabase db;
		db = Constant.openDatabase();
		Cursor c = db.rawQuery("select * from cashier ", null);
		int i = 0;
		if (c.moveToFirst()) {
			cashier[i] = c.getString(c.getColumnIndex("cashierId"));
			Log.i("tag", c.getString(c.getColumnIndex("cashierId")));
			i++;
		}
		c.close();

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {
		// TODO Auto-generated method stub
		AppItem localAppItem = new AppItem();
		if (paramView == null) {
			paramView = LayoutInflater.from(this.context).inflate(
					android.R.layout.simple_list_item_1, null);
			localAppItem.cashierId = (TextView) paramView
					.findViewById(R.id.text1);
			paramView.setTag(localAppItem);
		} else {
			localAppItem = (AppItem) paramView.getTag();
			if (paramInt < 20 || !cashier[paramInt].equals("")) {
				localAppItem.cashierId.setText(cashier[paramInt]);
			}
		}
		return paramView;
	}

	public class AppItem {
		TextView cashierId;
	}

}
