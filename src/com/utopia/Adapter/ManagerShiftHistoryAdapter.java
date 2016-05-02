package com.utopia.Adapter;

import java.util.List;

import com.utopia.Model.d_Sale;
import com.utopia.activity.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ManagerShiftHistoryAdapter extends BaseAdapter{

	private Context context;
	private List<d_Sale> saff_sales;
	public ManagerShiftHistoryAdapter(Context context,List<d_Sale> saff_sales){
		this.context = context;
		this.saff_sales = saff_sales;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return saff_sales.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return saff_sales.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {
		// TODO Auto-generated method stub
		View localView = null;
		if(paramView==null){
			localView = LayoutInflater.from(this.context).inflate(
					R.layout.shift_history,null);
			AppItem localAppItem=  new AppItem();
			localAppItem.shiftHistory = (LinearLayout) localView.findViewById(R.id.shiftHistory);
			localAppItem.name = (TextView) localView.findViewById(R.id.name);
			localAppItem.time = (TextView) localView.findViewById(R.id.time);
			localAppItem.netSales = (TextView) localView.findViewById(R.id.netSales);
			localAppItem.tips = (TextView) localView.findViewById(R.id.tips);
			localAppItem.imageView1 = (ImageView) localView.findViewById(R.id.imageView1);
		    
			localView.setTag(localAppItem);
			paramView = localView;
		}else{
			AppItem localAppItem1 = (AppItem) localView.getTag();
			localAppItem1.name.setText(saff_sales.get(paramInt).getWaiter());
			localAppItem1.time.setText(saff_sales.get(paramInt).getCreateTime()+'-');
			for(int i=0;i<saff_sales.size();i++){
				
			}
			//localAppItem1.netSales.setText(text)
			
		}
		return null;
	}
	class AppItem{
		LinearLayout shiftHistory;
		TextView name;
		TextView time;
		TextView netSales;
		TextView tips;
		ImageView imageView1;
	}

}
