package com.utopia.Dao;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.utopia.Model.d_Saleandpdt;
import com.utopia.utils.Constant;

public class sql_Saleandpdt {
	SQLiteDatabase db;

	public sql_Saleandpdt() {
		if (db == null)
			db = openDatabase();
	}

	private SQLiteDatabase openDatabase() {
		return SQLiteDatabase.openOrCreateDatabase(
				Constant.getDatabaseFilename(), null);
	}

	public void clear_all() {
		this.db.execSQL("delete from saleandpdt");
	}

	public void saveInit(d_Saleandpdt paramd_Saleandpdt) {
		Object[] arrayOfObject = new Object[14];
		arrayOfObject[0] = paramd_Saleandpdt.getId();
		arrayOfObject[1] = paramd_Saleandpdt.getSalerecordId();
		arrayOfObject[2] = paramd_Saleandpdt.getPdtCode();
		arrayOfObject[3] = paramd_Saleandpdt.getPdtName();
		arrayOfObject[4] = paramd_Saleandpdt.getNumber();
		arrayOfObject[5] = paramd_Saleandpdt.getPrice();
		arrayOfObject[6] = paramd_Saleandpdt.getOtherspec();
		arrayOfObject[7] = paramd_Saleandpdt.getOtherspec1();
		arrayOfObject[8] = paramd_Saleandpdt.getOtherspec2();
		arrayOfObject[9] = paramd_Saleandpdt.getPriority();
		arrayOfObject[10] = paramd_Saleandpdt.getStatus();
		arrayOfObject[11] = paramd_Saleandpdt.getCreateTime();
		arrayOfObject[12] = paramd_Saleandpdt.getCloseTime();
		arrayOfObject[13] = paramd_Saleandpdt.getCustomerNumber();
		Cursor localCursor = this.db.rawQuery(
				"select * from saleandpdt where id="
						+ paramd_Saleandpdt.getId(), null);
		if (localCursor.moveToNext()) {
			db.execSQL(
					"UPDATE saleandpdt SET id=?,salerecordId=?,pdtCode=?,pdtName=?,number=? ,price=?,otherspec0=?, otherspec1 = ? , otherspec2 = ?,priority=?,status1=?,createTime1=?,closeTime1=?,contactNumber=? WHERE id="
							+ paramd_Saleandpdt.getId(), arrayOfObject);
		} else {
			db.execSQL(
					"INSERT INTO saleandpdt(id,salerecordId,pdtCode,pdtName,number,price,otherspec0,otherspec1,otherspec2,priority,status1,createTime1,closeTime1,contactNumber) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
					arrayOfObject);
		}
		localCursor.close();
	}

	public void save(d_Saleandpdt salesSelectItem, int tag, int id) {
		Object[] arrayOfObject = new Object[13];
		arrayOfObject[0] = salesSelectItem.getSalerecordId();
		arrayOfObject[1] = salesSelectItem.getPdtCode();
		arrayOfObject[2] = salesSelectItem.getPdtName();
		arrayOfObject[3] = salesSelectItem.getNumber();
		arrayOfObject[4] = salesSelectItem.getPrice();
		arrayOfObject[5] = salesSelectItem.getOtherspec();
		arrayOfObject[6] = salesSelectItem.getOtherspec1();
		arrayOfObject[7] = salesSelectItem.getOtherspec2();
		arrayOfObject[8] = salesSelectItem.getPriority();
		arrayOfObject[9] = salesSelectItem.getStatus();
		arrayOfObject[10] = salesSelectItem.getCreateTime();
		arrayOfObject[11] = "";
		arrayOfObject[12] = salesSelectItem.getCustomerNumber();
		Log.i("tag", "当前菜所属的销售纪录编号=" + arrayOfObject[0]);
		if (tag == 0) {
			Cursor localCursor = this.db.rawQuery(
					"select id from saleandpdt where pdtCode='"
							+ arrayOfObject[1] + "' and salerecordId='"
							+ arrayOfObject[0] + "' and otherspec0='"
							+ arrayOfObject[5] + "' and otherspec1='"
							+ arrayOfObject[6] + "' and otherspec2='"
							+ arrayOfObject[7] + "' and "
							+ "status1='Not Sent'", null);
			Log.i("tag", "查询是否有相同的菜。。+=");
			// 如果这份菜已经点过一次并且未发送到厨房，就不插入新纪录，只对number加1，否则插入一条新纪录
			if (localCursor.moveToFirst()) {
				id = localCursor.getInt(0);
				Log.i("tag", "有相同的菜。。");
				this.db.execSQL("update saleandpdt set number=number+1 where id="
						+ id);
				// Log.i("tag","有相同的菜。。");
			} else {
				Log.i("tag", "没有相同的菜。。");
				this.db.execSQL(
						"INSERT INTO saleandpdt(salerecordId,pdtCode,pdtName,number,price,otherspec0,otherspec1,otherspec2,priority,status1,createTime1,closeTime1,contactNumber) values(?,?,?,?,?,?,?,?,?,?,?,?,?)",
						arrayOfObject);
			}
			localCursor.close();
		} else {
			this.db.execSQL("delete from saleandpdt where id=" + id);
			this.db.execSQL(
					"INSERT INTO saleandpdt(salerecordId,pdtCode,pdtName,number,price,otherspec0,otherspec1,otherspec2,priority,status1,createTime1,closeTime1,contactNumber) values(?,?,?,?,?,?,?,?,?,?,?,?,?)",
					arrayOfObject);
		}
		Log.i("tag", "保存成功。。。。");
	}

	public List<d_Saleandpdt> getDeskSalesNotSent(String deskId) {
		List<d_Saleandpdt> sales = new ArrayList<d_Saleandpdt>();
		Cursor localCursor = this.db.rawQuery(
				"select * from saleandpdt where salerecordId='" + deskId
						+ "' and status1='Not Sent'", null);
		while (localCursor.moveToNext()) {
			d_Saleandpdt locald_Saleandpdt = new d_Saleandpdt();
			locald_Saleandpdt.setId(localCursor.getInt(localCursor
					.getColumnIndex("id")));
			locald_Saleandpdt.setSalerecordId(localCursor.getString(localCursor
					.getColumnIndex("salerecordId")));
			locald_Saleandpdt.setPdtCode(localCursor.getString(localCursor
					.getColumnIndex("pdtCode")));
			locald_Saleandpdt.setPdtName(localCursor.getString(localCursor
					.getColumnIndex("pdtName")));
			locald_Saleandpdt.setNumber((int) (Float.valueOf(localCursor
					.getString(localCursor.getColumnIndex("number")))
					.floatValue()));
			locald_Saleandpdt.setPrice(Float.valueOf(
					localCursor.getString(localCursor.getColumnIndex("price")))
					.floatValue());
			locald_Saleandpdt.setStatus(localCursor.getString(localCursor
					.getColumnIndex("status1")));
			locald_Saleandpdt.setOtherspec1(localCursor.getString(localCursor
					.getColumnIndex("otherspec1")));
			locald_Saleandpdt.setOtherspec2(localCursor.getString(localCursor
					.getColumnIndex("otherspec2")));
			locald_Saleandpdt.setOtherspec(localCursor.getString(localCursor
					.getColumnIndex("otherspec0")));
			locald_Saleandpdt.setPriority(localCursor.getInt(localCursor
					.getColumnIndex("priority")));
			locald_Saleandpdt.setCreateTime(localCursor.getString(localCursor
					.getColumnIndex("createTime1")));
			locald_Saleandpdt.setCloseTime(localCursor.getString(localCursor
					.getColumnIndex("closeTime1")));
			locald_Saleandpdt.setCustomerNumber(localCursor.getInt(localCursor
					.getColumnIndex("contactNumber")));
			sales.add(locald_Saleandpdt);
		}
		localCursor.close();
		return sales;
	}

	public List<d_Saleandpdt> getDeskSalesDone(String deskId) {
		List<d_Saleandpdt> sales = new ArrayList<d_Saleandpdt>();
		Cursor localCursor = this.db.rawQuery(
				"select * from saleandpdt where salerecordId='" + deskId
						+ "' and status1='Done'", null);
		while (localCursor.moveToNext()) {
			d_Saleandpdt locald_Saleandpdt = new d_Saleandpdt();
			locald_Saleandpdt.setId(localCursor.getInt(localCursor
					.getColumnIndex("id")));
			locald_Saleandpdt.setSalerecordId(localCursor.getString(localCursor
					.getColumnIndex("salerecordId")));
			locald_Saleandpdt.setPdtCode(localCursor.getString(localCursor
					.getColumnIndex("pdtCode")));
			locald_Saleandpdt.setPdtName(localCursor.getString(localCursor
					.getColumnIndex("pdtName")));
			locald_Saleandpdt.setNumber((int) (Float.valueOf(localCursor
					.getString(localCursor.getColumnIndex("number")))
					.floatValue()));
			locald_Saleandpdt.setPrice(Float.valueOf(
					localCursor.getString(localCursor.getColumnIndex("price")))
					.floatValue());
			locald_Saleandpdt.setStatus(localCursor.getString(localCursor
					.getColumnIndex("status1")));
			locald_Saleandpdt.setOtherspec1(localCursor.getString(localCursor
					.getColumnIndex("otherspec1")));
			locald_Saleandpdt.setOtherspec2(localCursor.getString(localCursor
					.getColumnIndex("otherspec2")));
			locald_Saleandpdt.setOtherspec(localCursor.getString(localCursor
					.getColumnIndex("otherspec0")));
			locald_Saleandpdt.setPriority(localCursor.getInt(localCursor
					.getColumnIndex("priority")));
			locald_Saleandpdt.setCreateTime(localCursor.getString(localCursor
					.getColumnIndex("createTime1")));
			locald_Saleandpdt.setCloseTime(localCursor.getString(localCursor
					.getColumnIndex("closeTime1")));
			locald_Saleandpdt.setCustomerNumber(localCursor.getInt(localCursor
					.getColumnIndex("contactNumber")));
			sales.add(locald_Saleandpdt);
		}
		localCursor.close();
		return sales;

	}

	public void updateDone(d_Saleandpdt sr) {
		if (sr.getStatus().equals("Done"))
			db.execSQL("update saleandpdt set status1='" + sr.getStatus()
					+ "' where id=" + sr.getId());
	}

	public int getPreId(String pdtcode, String salerecordId, String other,
			String other1, String other2) {
		int id;
		Cursor localCursor = this.db.rawQuery(
				"select id from saleandpdt where pdtCode='" + pdtcode
						+ "' and salerecordId='" + salerecordId
						+ "' and otherspec0='" + other + "' and otherspec1='"
						+ other1 + "' and otherspec2='" + other2 + "' and "
						+ " status1='Not Sent'", null);
		localCursor.moveToFirst();
		id = localCursor.getInt(0);
		Log.i("tag", id + "aaaaaaaaaaaaaaa");
		localCursor.close();
		return id;
	}

	public void setSalesPriority(String itemNo, int tag) {
		this.db.execSQL("update saleandpdt set priority=1"
				+ " where salerecordId='" + itemNo + "'");
	}

}
