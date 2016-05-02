package com.utopia.Adapter;

import java.text.DecimalFormat;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.utopia.Model.d_SaleRecord;
import com.utopia.activity.R;
import com.utopia.baseadapter.utils.CommomAdapter;
import com.utopia.baseadapter.utils.ViewHolder;

public class MenuBillAdapter1<T> extends CommomAdapter<T> {
	DecimalFormat decimalFormat = new DecimalFormat("0.00");// 构造方法的字符格式这里如果小数不足2位,会以0补足.
	DecimalFormat decimalFormat1 = new DecimalFormat("0");// 构造方法的字符格式这里如果小数不足2位,会以0补足.

	public MenuBillAdapter1(Context context, List<T> datas, int layoutId,
			int count) {
		super(context, datas, layoutId);
		// this.count = count;
	}

	// private d_SaleRecord ds;
	// private int count;

	@Override
	public void convert(ViewHolder holder, T t) {
		// ds = (d_SaleRecord) t;
		Log.i("getNumber", ((d_SaleRecord) t).getNumber() + "");
		Log.i("customNo", ((d_SaleRecord) t).getContactNumber() + "");
		holder.setText(R.id.tv_qty1,
				"1/" + ((d_SaleRecord) t).getContactNumber()).setText(
				R.id.tv_price1,
				(decimalFormat.format(((d_SaleRecord) t).getPrice()
						* ((d_SaleRecord) t).getNumber()
						/ ((d_SaleRecord) t).getContactNumber())));
	}
}
