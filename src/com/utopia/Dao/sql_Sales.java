package com.utopia.Dao;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.utopia.Model.d_Sale;
import com.utopia.utils.Constant;

public class sql_Sales {
	SQLiteDatabase db;

	public sql_Sales() {
		if (db == null)
			db = openDatabase();
	}

	private SQLiteDatabase openDatabase() {
		return SQLiteDatabase.openOrCreateDatabase(
				Constant.getDatabaseFilename(), null);
	}

	public void clear_all() {
		this.db.execSQL("delete from SaleRecord");
	}

	public void saveInit(d_Sale paramd_SaleRecord) {
		Object[] arrayOfObject = new Object[18];
		arrayOfObject[0] = paramd_SaleRecord.getItemNo();
		arrayOfObject[1] = paramd_SaleRecord.getCloseTime();
		arrayOfObject[2] = paramd_SaleRecord.getCreateTime();
		arrayOfObject[3] = paramd_SaleRecord.getDeskName();
		arrayOfObject[4] = paramd_SaleRecord.getOtherSpec();
		arrayOfObject[5] = paramd_SaleRecord.getOtherSpecNo1();
		arrayOfObject[6] = paramd_SaleRecord.getOtherSpecNo2();
		arrayOfObject[7] = paramd_SaleRecord.getStatus();
		arrayOfObject[8] = paramd_SaleRecord.getDept();
		arrayOfObject[9] = paramd_SaleRecord.getSubtotal();
		arrayOfObject[10] = paramd_SaleRecord.getTiptotal();
		arrayOfObject[11] = paramd_SaleRecord.getTotal();
		arrayOfObject[12] = paramd_SaleRecord.getInitTotal();
		arrayOfObject[13] = paramd_SaleRecord.getRebate();
		arrayOfObject[14] = paramd_SaleRecord.getTax();
		arrayOfObject[15] = paramd_SaleRecord.getWaiter();
		arrayOfObject[16] = paramd_SaleRecord.getCashTotal();
		arrayOfObject[17] = paramd_SaleRecord.getCardTotal();
		// Object[] arrayOfObject1 = new Object[17];
		// arrayOfObject[0] = paramd_SaleRecord.getCloseTime();
		// arrayOfObject[1] = paramd_SaleRecord.getCreateTime();
		// arrayOfObject[2] = paramd_SaleRecord.getDeskName();
		// arrayOfObject[3] = paramd_SaleRecord.getOtherSpec();
		// arrayOfObject[4] = paramd_SaleRecord.getOtherSpecNo1();
		// arrayOfObject[5] = paramd_SaleRecord.getOtherSpecNo2();
		// arrayOfObject[6] = paramd_SaleRecord.getStatus();
		// arrayOfObject[7] = paramd_SaleRecord.getDept();
		// arrayOfObject[8] = paramd_SaleRecord.getSubtotal();
		// arrayOfObject[9] = paramd_SaleRecord.getTiptotal();
		// arrayOfObject[10] = paramd_SaleRecord.getTotal();
		// arrayOfObject[11] = paramd_SaleRecord.getInitTotal();
		// arrayOfObject[12] = paramd_SaleRecord.getRebate();
		// arrayOfObject[13] = paramd_SaleRecord.getTax();
		// arrayOfObject[14] = paramd_SaleRecord.getWaiter();
		// arrayOfObject[15] = paramd_SaleRecord.getCashTotal();
		// arrayOfObject[16] = paramd_SaleRecord.getCardTotal();
		Cursor mCursor = this.db.rawQuery(
				"select itemNo from SaleRecord where itemNo='"
						+ paramd_SaleRecord.getItemNo() + "'", null);
		if (mCursor.moveToFirst()) {

			this.db.execSQL(
					"update SaleRecord set itemNo=?,closeTime=?,createTime=?,deskName=?,otherSpec=?,otherSpecNo1=?,"
							+ "otherSpecNo2=?,status=?,dept=?,subtotal=?,tiptotal=?,total=?,initTotal=?,rebate=?,tax=?,waiter=?,"
							+ "cashTotal=?,cardTotal=? where  itemNo='"
							+ paramd_SaleRecord.getItemNo() + "'",
					arrayOfObject);
		} else {
			this.db.execSQL(
					"INSERT INTO SaleRecord(itemNo,closeTime,createTime,deskName,otherSpec,otherSpecNo1,"
							+ "otherSpecNo2,status,dept,subtotal,tiptotal,total,initTotal , rebate,tax,waiter,"
							+ "cashTotal,cardTotal) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
					arrayOfObject);
		}
		mCursor.close();
	}

	public void insertSalerecord(String id, String deskName, String creatTime,
			String waiter_name, String area) {
		Object[] arrayOfObject = new Object[] { id, "", creatTime, deskName,
				"", "", "", area, "", 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.025f,
				waiter_name, 0.0f, 0.0f };

		this.db.execSQL(
				"INSERT INTO SaleRecord(itemNo,closeTime,createTime,deskName,otherSpec,otherSpecNo1,"
						+ "otherSpecNo2,status,dept,subtotal,tiptotal,total,initTotal , rebate,tax,waiter,"
						+ "cashTotal,cardTotal) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
				arrayOfObject);
		Log.i("tag", "创建成功。。。。");
	}

	public List<d_Sale> getSales(String itemNo) {
		List<d_Sale> salerecords = new ArrayList<d_Sale>();

		Cursor mCursor = this.db.rawQuery(
				"select * from SaleRecord where itemNo='" + itemNo + "'", null);
		if (mCursor.moveToFirst()) {
			d_Sale salerecord = new d_Sale();
			salerecord.setItemNo(mCursor.getString(mCursor
					.getColumnIndex("itemNo")));
			salerecord.setCloseTime(mCursor.getString(mCursor
					.getColumnIndex("closeTime")));
			salerecord.setCreateTime(mCursor.getString(mCursor
					.getColumnIndex("createTime")));
			salerecord.setDeskName(mCursor.getString(mCursor
					.getColumnIndex("deskName")));
			salerecord.setOtherSpec(mCursor.getString(mCursor
					.getColumnIndex("otherSpec")));
			salerecord.setOtherSpecNo1(mCursor.getString(mCursor
					.getColumnIndex("otherSpecNo1")));
			salerecord.setOtherSpecNo2(mCursor.getString(mCursor
					.getColumnIndex("otherSpecNo2")));
			salerecord.setStatus(mCursor.getString(mCursor
					.getColumnIndex("status")));
			salerecord
					.setDept(mCursor.getString(mCursor.getColumnIndex("dept")));
			float subtotal = TextUtils.isEmpty(mCursor.getString(mCursor
					.getColumnIndex("subtotal"))) ? 0.0f : Float
					.parseFloat(mCursor.getString(mCursor
							.getColumnIndex("subtotal")));
			salerecord.setSubtotal(subtotal);
			Log.i("tag", mCursor.getColumnIndex("tiptotal") + "");
			salerecord.setTiptotal(Float.parseFloat(mCursor.getString(mCursor
					.getColumnIndex("tiptotal"))));
			salerecord.setTotal(Float.parseFloat(mCursor.getString(mCursor
					.getColumnIndex("total"))));
			salerecord.setInitTotal(Float.parseFloat(mCursor.getString(mCursor
					.getColumnIndex("initTotal"))));
			salerecord.setRebate(Float.parseFloat(mCursor.getString(mCursor
					.getColumnIndex("rebate"))));
			salerecord.setTax(Float.parseFloat(mCursor.getString(mCursor
					.getColumnIndex("tax"))));
			salerecord.setWaiter(mCursor.getString(mCursor
					.getColumnIndex("waiter")));
			salerecord.setCashTotal(Float.parseFloat(mCursor.getString(mCursor
					.getColumnIndex("cashTotal"))));
			salerecord.setCardTotal(Float.parseFloat(mCursor.getString(mCursor
					.getColumnIndex("cardTotal"))));
			salerecords.add(salerecord);
		}
		mCursor.close();
		return salerecords;
	}

	public void updateMoney(float initTotal, float cardTotal, float cashTotal,
			float subTotal, float tip, float total, String itemNo) {
		this.db.execSQL("update SaleRecord set initTotal=initTotal+"
				+ initTotal + ",cardTotal=cardTotal+" + cardTotal
				+ ",cashTotal=cashTotal+" + cashTotal + ",subtotal=subtotal+"
				+ subTotal + ",tiptotal=tiptotal+" + tip + ",total=total+"
				+ total + " where itemNo='" + itemNo + "'");

	}
}
