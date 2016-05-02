package com.utopia.Dao;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.utopia.Model.d_Bill;
import com.utopia.utils.Constant;
import com.utopia.utils.DateUtils;

public class sql_Bill {
	Context context;
	SQLiteDatabase db;

	public sql_Bill() {
		this.db = Constant.openDatabase();
	}

	sql_Bill(Context paramContext) {
		this.context = paramContext;
		if (this.db != null)
			return;
		this.db = openDatabase();
	}

	private SQLiteDatabase openDatabase() {
		return SQLiteDatabase.openOrCreateDatabase(
				Constant.getDatabaseFilename(), null);
	}

	public void save(d_Bill localBill) {
		Object[] arrayOfObject = new Object[15];
		arrayOfObject[0] = localBill.getBillId();
		arrayOfObject[1] = localBill.getWaiter();
		arrayOfObject[2] = localBill.getSubtotal();
		arrayOfObject[3] = localBill.getTaxTotal();
		arrayOfObject[4] = localBill.getTotal();
		arrayOfObject[5] = localBill.getSalerecordId();
		arrayOfObject[6] = localBill.getDistant();
		arrayOfObject[7] = DateUtils.getDateEN();
		arrayOfObject[8] = localBill.getTip();
		arrayOfObject[9] = localBill.getPayment();
		arrayOfObject[10] = localBill.getInitTotal();
		arrayOfObject[11] = localBill.getCashierId();
		arrayOfObject[12] = localBill.getTipPayment();
		arrayOfObject[13] = localBill.getRebate();
		arrayOfObject[14] = localBill.getDept();
		Cursor localCursor = this.db.rawQuery(
				"select * from Bill where BillId='" + localBill.getBillId()
						+ "'", null);
		if (localCursor.moveToNext()) {
			db.execSQL(
					"UPDATE Bill SET waiter=?,subtotal=?,total=?,taxTotal=?,createTime=?,"
							+ "Distant=?,tip=?,rebate=?, initTotal=?,tipPayment=?, payment=?, "
							+ "cashierId=?, dept=? where BillId=?",
					new Object[] { localBill.getWaiter(),
							localBill.getSubtotal(), localBill.getTotal(),
							localBill.getTaxTotal(), localBill.getCreateTime(),
							localBill.getDistant(), localBill.getTip(),
							localBill.getRebate(), localBill.getInitTotal(),
							localBill.getTipPayment(), localBill.getPayment(),
							localBill.getCashierId(), localBill.getBillId(),
							localBill.getDept() });
		} else {
			db.execSQL(
					"INSERT INTO Bill(BillId,waiter,subtotal,taxTotal,total,salerecordId,Distant,createTime,tip,payment,initTotal,cashierId,tipPayment,rebate,dept) "
							+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
					arrayOfObject);
		}
		localCursor.close();

		// select();
	}

	public void clear_all() {
		db.execSQL("delete from Bill");
	}

	public void saveInit(d_Bill localBill) {
		// db.rawQuery("delete from Bill", null);
		Object[] arrayOfObject = new Object[15];
		arrayOfObject[0] = localBill.getBillId();
		arrayOfObject[1] = localBill.getWaiter();
		arrayOfObject[2] = localBill.getSubtotal();
		arrayOfObject[3] = localBill.getTaxTotal();
		arrayOfObject[4] = localBill.getTotal();
		arrayOfObject[5] = localBill.getSalerecordId();
		arrayOfObject[6] = localBill.getDistant();
		arrayOfObject[7] = DateUtils.getDateEN();
		arrayOfObject[8] = localBill.getTip();
		arrayOfObject[9] = localBill.getPayment();
		arrayOfObject[10] = localBill.getInitTotal();
		arrayOfObject[11] = localBill.getCashierId();
		arrayOfObject[12] = localBill.getTipPayment();
		arrayOfObject[13] = localBill.getRebate();
		arrayOfObject[14] = localBill.getDept();
		db.execSQL(
				"INSERT INTO Bill(BillId,waiter,subtotal,taxTotal,total,salerecordId,Distant,createTime,tip,payment,initTotal,cashierId,tipPayment,rebate,dept) "
						+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
				arrayOfObject);
		// Log.e("sql_Bill", localBill.getBillId());
		// select();
	}

	public d_Bill getBillDetial(String billId) {
		d_Bill mBill = new d_Bill();
		Cursor mCursor = this.db.rawQuery("select * from Bill where BillId='"
				+ billId + "'", null);
		if (mCursor.moveToFirst()) {
			mBill.setBillId(mCursor.getString(mCursor.getColumnIndex("BillId")));
			mBill.setSalerecordId(mCursor.getString(mCursor
					.getColumnIndex("salerecordId")));
			mBill.setCreateTime(mCursor.getString(mCursor
					.getColumnIndex("createTime")));
			mBill.setSubtotal(mCursor.getFloat(mCursor
					.getColumnIndex("subTotal")));
			mBill.setRebate(mCursor.getFloat(mCursor.getColumnIndex("rebate")));
			mBill.setTaxTotal(mCursor.getFloat(mCursor
					.getColumnIndex("taxTotal")));
			mBill.setTip(mCursor.getFloat(mCursor.getColumnIndex("tip")));
			mBill.setTotal(mCursor.getFloat(mCursor.getColumnIndex("total")));
			mBill.setWaiter(mCursor.getString(mCursor.getColumnIndex("waiter")));
			mBill.setInitTotal(mCursor.getFloat(mCursor
					.getColumnIndex("initTotal")));
			mBill.setTipPayment(mCursor.getString(mCursor
					.getColumnIndex("tipPayment")));
			mBill.setPayment(mCursor.getString(mCursor
					.getColumnIndex("payment")));
			mBill.setCashierId(mCursor.getString(mCursor
					.getColumnIndex("cashierId")));
			mBill.setDistant(mCursor.getFloat(mCursor.getColumnIndex("Distant")));
			mBill.setDept(mCursor.getString(mCursor.getColumnIndex("dept")));
		}
		mCursor.close();
		return mBill;
	}

	public void select() {
		Cursor c = this.db.rawQuery("select * from Bill ", null);
		while (c.moveToNext()) {
		}
		c.close();
	}

	public String sum_cash() {
		String sum = "";
		float s = (float) 0.00;
		Cursor c = this.db.rawQuery(
				"select sum(Total) from Bill where createTime >='"
						+ Constant.clockInTime + "'", null);
		if (c.moveToFirst()) {
			s = c.getFloat(0);
		}
		/*
		 * while(c.moveToNext()){ Log.e("sql",c.getFloat(0)+""); }
		 */
		sum = Constant.decimalFormat.format(s);
		c.close();
		return sum;
	}

	//
	public String sum_cashAll() {
		String sum = "";
		float s = (float) 0.00;
		Cursor c = this.db.rawQuery(
				"select sum(Total) from Bill where createTime>='"
						+ Constant.clockInTime + "'", null);
		if (c.moveToFirst()) {
			s = c.getFloat(0);
		}
		/*
		 * while(c.moveToNext()){ Log.e("sql",c.getFloat(0)+""); }
		 */
		sum = Constant.decimalFormat.format(s);
		c.close();
		return sum;
	}

	public String getTransactions() {
		int number = 0;
		Cursor c = db.rawQuery("select count(*) from Bill where createTime>='"
				+ Constant.clockInTime + "'", null);
		if (c.moveToFirst()) {
			number = c.getInt(0);
		}
		c.close();
		return number + "";
	}

	public String getTip() {
		float sum = 0;
		Cursor c = db.rawQuery(
				"select count(tip) from Bill where createTime>='"
						+ Constant.clockInTime + "'", null);

		if (c.moveToFirst()) {
			sum = c.getFloat(0);
		}
		c.close();
		detail();
		return Constant.decimalFormat.format(sum);
	}

	public List<d_Bill> getTodayDetail() {
		detail();
		List<d_Bill> d_bill = new ArrayList<d_Bill>();
		d_Bill d1;
		Cursor c = db
				.rawQuery(
						"select * from Bill WHERE strftime('%Y',createTime)== strftime('%Y','now') AND strftime('%m',CreateTime)== strftime('%m','now') AND strftime('%d',CreateTime)== strftime('%d','now') and waiter=?",
						new String[] { Constant.currentStaff.getS_name() });
		while (c.moveToNext()) {
			d1 = new d_Bill("", "", "",
					c.getFloat(c.getColumnIndex("subtotal")), c.getInt(c
							.getColumnIndex("taxTotal")), c.getFloat(c
							.getColumnIndex("total")), c.getString(c
							.getColumnIndex("createTime")), c.getInt(c
							.getColumnIndex("Distant")), c.getFloat(c
							.getColumnIndex("tip")), 1, 0, "", "","",
					Constant.Area);
			d_bill.add(d1);
		}
		c.close();
		return d_bill;
	}

	public void detail() {
		Cursor c = db.rawQuery("select *from Bill ", null);
		while (c.moveToNext()) {
			Log.e("tips", c.getString(c.getColumnIndex("createTime")));
		}
		c.close();
	}

	public Cursor recordlist(String paramString) {
		return db.rawQuery(paramString, null);
	}

}
