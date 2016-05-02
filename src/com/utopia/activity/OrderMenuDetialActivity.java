package com.utopia.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.utopia.Base.BaseActivity;
import com.utopia.Dao.sql_Saleandpdt;
import com.utopia.Model.d_Product;
import com.utopia.Model.d_Saleandpdt;
import com.utopia.utils.DateUtils;
import com.utopia.utils.ExitApplication;

//菜单页面
public class OrderMenuDetialActivity extends BaseActivity implements
		OnClickListener {

	private Button done, cancel;
	private Button size_regular, size_small, size_large, size_extra_large,
			size_super_large;
	private Button hotness_regular, hotness_none, hotness_strong, hotness_mid;

	private EditText notes;
	private TextView menu_title;
	private String size = "regular", hotness = "regular", note = "none";
	private d_Product product;
	// private String md5 = "12";
	private String salerecordId = "";
	private int tag = 0;
	private int id = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_detial_dialog);
		ExitApplication.getInstance().addActivity(this);// 加入退出栈

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		product = (d_Product) getIntent().getSerializableExtra("d_Product");
		// md5 = getIntent().getStringExtra("md5");
		// 修改
		salerecordId = getIntent().getStringExtra("salerecordId");
		Log.i("tag", "所点的菜所属的销售纪录的编号:::" + salerecordId);
		tag = getIntent().getIntExtra("tag", tag);
		id = getIntent().getIntExtra("id", id);
		initViews();
		initEvents();

		menu_title.setText(product.getPdtName());// 设置名称
	}

	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View paramView) {
		switch (paramView.getId()) {
		case R.id.order_done:
			done.setBackgroundResource(R.drawable.b);
			if (!notes.getText().toString().equals("")) {
				note = notes.getText().toString();
			}
			// /修改处///////////////////
			// d_SaleRecord locald_SaleRecord2 = new d_SaleRecord();
			//
			// locald_SaleRecord2.setDesk_name(Constant.table_id);
			// locald_SaleRecord2.setPdtCODE(product.getPdtCode());
			// locald_SaleRecord2.setPdtName(product.getPdtName());
			// locald_SaleRecord2.setPrice(product.getPdtSalePrice1());
			// locald_SaleRecord2.setBILLID(md5);
			// locald_SaleRecord2.setNumber(1);
			// locald_SaleRecord2.setOtherSpec(note);
			// locald_SaleRecord2.setStatus("Not Sent");
			// locald_SaleRecord2.setOtherSpecNo1(size);
			// locald_SaleRecord2.setOtherSpecNo2(hotness);
			// // 时间
			// locald_SaleRecord2.setCreateTime(DateUtils.getDateEN());
			// locald_SaleRecord2.setWaiter(Constant.currentStaff.getS_account());
			// // 默认税收为0 折扣为 1
			// locald_SaleRecord2.setTax(0);
			// locald_SaleRecord2.setDiscount(1);
			// locald_SaleRecord2.setCustomerNo(1);
			// new sql_SaleRecord().save(locald_SaleRecord2);
			// 修改后
			d_Saleandpdt salesSelectItem = new d_Saleandpdt();
			salesSelectItem.setSalerecordId(salerecordId);
			salesSelectItem.setPdtCode(product.getPdtCode());
			salesSelectItem.setPdtName(product.getPdtName());
			salesSelectItem.setPrice((float) product.getPdtSalePrice1());
			salesSelectItem.setNumber(1);
			salesSelectItem.setOtherspec(note);
			salesSelectItem.setOtherspec1(size);
			salesSelectItem.setOtherspec2(hotness);
			salesSelectItem.setCreateTime(DateUtils.getDateEN());
			salesSelectItem.setStatus("Not Sent");
			salesSelectItem.setPriority(0);
			salesSelectItem.setCustomerNumber(0);
			(new sql_Saleandpdt()).save(salesSelectItem, tag, id);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			this.finish();
			break;
		case R.id.order_cancel:
			this.finish();
			break;

		case R.id.size_large:
			size_large.setBackgroundResource(R.drawable.a);
			size_regular.setBackgroundResource(R.drawable.c);
			size_small.setBackgroundResource(R.drawable.c);
			size_extra_large.setBackgroundResource(R.drawable.c);
			size_super_large.setBackgroundResource(R.drawable.c);

			size_large.setTextColor(Color.WHITE);
			size_regular.setTextColor(Color.BLACK);
			size_small.setTextColor(Color.BLACK);
			size_extra_large.setTextColor(Color.BLACK);
			size_super_large.setTextColor(Color.BLACK);
			size = "large";
			break;
		case R.id.size_regular:
			size_large.setBackgroundResource(R.drawable.c);
			size_regular.setBackgroundResource(R.drawable.a);
			size_small.setBackgroundResource(R.drawable.c);
			size_extra_large.setBackgroundResource(R.drawable.c);
			size_super_large.setBackgroundResource(R.drawable.c);

			size_large.setTextColor(Color.BLACK);
			size_regular.setTextColor(Color.WHITE);
			size_small.setTextColor(Color.BLACK);
			size_extra_large.setTextColor(Color.BLACK);
			size_super_large.setTextColor(Color.BLACK);

			size = "regular";
			break;
		case R.id.size_small:
			size_large.setBackgroundResource(R.drawable.c);
			size_regular.setBackgroundResource(R.drawable.c);
			size_small.setBackgroundResource(R.drawable.a);
			size_extra_large.setBackgroundResource(R.drawable.c);
			size_super_large.setBackgroundResource(R.drawable.c);

			size_large.setTextColor(Color.BLACK);
			size_regular.setTextColor(Color.BLACK);
			size_small.setTextColor(Color.WHITE);
			size_extra_large.setTextColor(Color.BLACK);
			size_super_large.setTextColor(Color.BLACK);

			size = "small";
			break;
		case R.id.size_extra_large:
			size_large.setBackgroundResource(R.drawable.c);
			size_regular.setBackgroundResource(R.drawable.c);
			size_small.setBackgroundResource(R.drawable.c);
			size_extra_large.setBackgroundResource(R.drawable.a);
			size_super_large.setBackgroundResource(R.drawable.c);

			size_large.setTextColor(Color.BLACK);
			size_regular.setTextColor(Color.BLACK);
			size_small.setTextColor(Color.BLACK);
			size_extra_large.setTextColor(Color.WHITE);
			size_super_large.setTextColor(Color.BLACK);

			size = "extra_large";
			break;
		case R.id.size_super_large:
			size_large.setBackgroundResource(R.drawable.c);
			size_regular.setBackgroundResource(R.drawable.c);
			size_small.setBackgroundResource(R.drawable.c);
			size_extra_large.setBackgroundResource(R.drawable.c);
			size_super_large.setBackgroundResource(R.drawable.a);

			size_large.setTextColor(Color.BLACK);
			size_regular.setTextColor(Color.BLACK);
			size_small.setTextColor(Color.BLACK);
			size_extra_large.setTextColor(Color.BLACK);
			size_super_large.setTextColor(Color.WHITE);

			size = "super_large";
			break;

		case R.id.hotness_mid:
			hotness_strong.setBackgroundResource(R.drawable.c);
			hotness_regular.setBackgroundResource(R.drawable.c);
			hotness_none.setBackgroundResource(R.drawable.c);
			hotness_mid.setBackgroundResource(R.drawable.a);

			hotness_strong.setTextColor(Color.BLACK);
			hotness_regular.setTextColor(Color.BLACK);
			hotness_none.setTextColor(Color.BLACK);
			hotness_mid.setTextColor(Color.WHITE);
			hotness = "mid";
			break;
		case R.id.hotness_no:
			hotness_strong.setBackgroundResource(R.drawable.c);
			hotness_regular.setBackgroundResource(R.drawable.c);
			hotness_none.setBackgroundResource(R.drawable.a);
			hotness_mid.setBackgroundResource(R.drawable.c);

			hotness_strong.setTextColor(Color.BLACK);
			hotness_regular.setTextColor(Color.BLACK);
			hotness_none.setTextColor(Color.WHITE);
			hotness_mid.setTextColor(Color.BLACK);

			hotness = "none";
			break;
		case R.id.hotness_regular:
			hotness_strong.setBackgroundResource(R.drawable.c);
			hotness_regular.setBackgroundResource(R.drawable.a);
			hotness_none.setBackgroundResource(R.drawable.c);
			hotness_mid.setBackgroundResource(R.drawable.c);

			hotness_strong.setTextColor(Color.BLACK);
			hotness_regular.setTextColor(Color.WHITE);
			hotness_none.setTextColor(Color.BLACK);
			hotness_mid.setTextColor(Color.BLACK);

			hotness = "regular";
			break;
		case R.id.hotness_strong:
			hotness_strong.setBackgroundResource(R.drawable.a);
			hotness_regular.setBackgroundResource(R.drawable.c);
			hotness_none.setBackgroundResource(R.drawable.c);
			hotness_mid.setBackgroundResource(R.drawable.c);

			hotness_strong.setTextColor(Color.WHITE);
			hotness_regular.setTextColor(Color.BLACK);
			hotness_none.setTextColor(Color.BLACK);
			hotness_mid.setTextColor(Color.BLACK);

			hotness = "strong";
			break;
		}
	}

	@Override
	protected void initViews() {

		done = (Button) findViewById(R.id.order_done);
		cancel = (Button) findViewById(R.id.order_cancel);

		size_regular = (Button) this.findViewById(R.id.size_regular);
		size_small = (Button) this.findViewById(R.id.size_small);
		size_large = (Button) this.findViewById(R.id.size_large);
		size_extra_large = (Button) this.findViewById(R.id.size_extra_large);
		size_super_large = (Button) this.findViewById(R.id.size_super_large);

		hotness_regular = (Button) this.findViewById(R.id.hotness_regular);
		hotness_none = (Button) this.findViewById(R.id.hotness_no);
		hotness_strong = (Button) this.findViewById(R.id.hotness_strong);
		hotness_mid = (Button) this.findViewById(R.id.hotness_mid);

		notes = (EditText) this.findViewById(R.id.order_notes);
		menu_title = (TextView) this.findViewById(R.id.menu_title);
	}

	@Override
	protected void initEvents() {
		done.setOnClickListener(this);
		cancel.setOnClickListener(this);

		size_regular.setOnClickListener(this);
		size_small.setOnClickListener(this);
		size_large.setOnClickListener(this);
		size_extra_large.setOnClickListener(this);
		size_super_large.setOnClickListener(this);

		hotness_regular.setOnClickListener(this);
		hotness_none.setOnClickListener(this);
		hotness_strong.setOnClickListener(this);
		hotness_mid.setOnClickListener(this);

	}

	@Override
	public void onBackPressed() {

	}
}
