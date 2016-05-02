package com.utopia.Dao;

import java.math.BigDecimal;
import java.util.ArrayList;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.utopia.Model.d_SaleRecord;
import com.utopia.utils.Constant;
import com.utopia.utils.DateUtils;

public class sql_SaleRecord {
	// private static final String String = null;
	SQLiteDatabase db = null;

	public sql_SaleRecord() {
		if (db == null)
			db = openDatabase();
	}

	private SQLiteDatabase openDatabase() {
		return SQLiteDatabase.openOrCreateDatabase(
				Constant.getDatabaseFilename(), null);
	}

	public void clearBill(String paramString) {
		String str = "select itemNo from SaleRecord where deskName='"
				+ paramString
				+ "' and"
				+ " createTime=(select max( createTime ) from SaleRecord where deskName='"
				+ paramString + "')";
		Cursor localCursor = this.db.rawQuery(str, null);
		localCursor.moveToFirst();
		int itemNo = localCursor.getInt(0);
		localCursor.close();
		this.db.execSQL("delete from saleandpdt where salerecordId=?",
				new Object[] { itemNo });
		this.db.execSQL("delete from SaleRecord where deskName=?",
				new Object[] { paramString });
	}

	public void clear_all() {
		this.db.execSQL("delete from saleandpdt");
		this.db.execSQL("delete from SaleRecord");
	}

	// /删除某一道菜
	// public void delete(String id) {
	//
	// this.db.execSQL("delete from saleandpdt where id=?",
	// new Object[] { id });
	//
	// }

	public void deleteNotSent(String desk_name, String status) {
		String str = "select itemNo from SaleRecord where deskName='"
				+ desk_name
				+ "' and "
				+ " createTime=(select max( createTime ) from SaleRecord where deskName='"
				+ desk_name + "')";
		Cursor localCursor = this.db.rawQuery(str, null);
		localCursor.moveToFirst();
		int itemNo = localCursor.getInt(0);
		localCursor.close();
		this.db.execSQL(
				"delete from saleandpdt where salerecordId=? and status1=?",
				new Object[] { itemNo, status });
		// this.db.execSQL(
		// "delete from SaleRecord where deskName=? and status=?",
		// new Object[] { desk_name, status });

	}

	public void deleteLocal(String desk_name) {
		String str = "select itemNo from SaleRecord where deskName='"
				+ desk_name + "'";
		Cursor localCursor = this.db.rawQuery(str, null);
		while (localCursor.moveToNext()) {
			int itemNo = localCursor.getInt(0);

			this.db.execSQL("delete from saleandpdt where salerecordId=?",
					new Object[] { itemNo });
		}
		localCursor.close();
		this.db.execSQL("delete from SaleRecord where deskName=?",
				new Object[] { desk_name });
		// this.db.execSQL("delete from SaleRecord where desk_name=?",
		// new Object[] { desk_name });

	}

	/*
	 * 删除指定的某一道菜
	 */
	public void deletePreSelect(int id) {
		db.execSQL("delete from saleandpdt where id = " + id);
	}

	public void delete(String desk_name, String pdtName) {

		String str = "select itemNo from SaleRecord where deskName='"
				+ desk_name
				+ "' and "
				+ " createTime=(select max( createTime ) from SaleRecord where deskName='"
				+ desk_name + "')";

		Cursor localCursor = this.db.rawQuery(str, null);
		localCursor.moveToFirst();
		int itemNo = localCursor.getInt(0);
		localCursor.close();
		Object[] ob = new Object[2];
		ob[0] = itemNo;
		ob[1] = pdtName;
		db.execSQL(
				"delete from saleandpdt where salerecordId = ? and pdtName=?",
				ob);
	}

	/*
	 * Order Form 界面 总价
	 */
	public float getOneOrderTotalMoney(String deskName) {

		String str2 = "select ifnull(sum(number*Price),0.0) from saleandpdt as s1 join SaleRecord as s2 on "
				+ "s1.salerecordId=s2.itemNo where deskName='"
				+ deskName
				+ "' and status1!='Finish'";
		Cursor localCursor1 = this.db.rawQuery(str2, null);
		localCursor1.moveToFirst();
		float f = localCursor1.getFloat(0);
		localCursor1.close();
		return f;
	}

	/*
	 * Order Form 界面 总数
	 */
	public float getOneOrderTotalNum(String desk_name) {

		String str = "select ifnull(sum(number*Price),0.0) from saleandpdt as s1 join SaleRecord as s2 on "
				+ "s1.salerecordId=s2.itemNo where deskName='"
				+ desk_name
				+ "' and status1!='Finish'";
		Cursor localCursor1 = this.db.rawQuery(str, null);
		localCursor1.moveToFirst();
		float f = localCursor1.getFloat(0);
		localCursor1.close();
		return f;
	}

	/*
	 * Order Form 界面 总数
	 */
	public int getNotCheckedNum() {

		String str = "select count(*) from saleandpdt as s1 join SaleRecord as s2 on s1.salerecordId=s2.itemNo"
				+ " where s1.status1!='Finish' and s1.status1!='Doned'";
		Cursor localCursor = db.rawQuery(str, null);
		localCursor.moveToFirst();
		int count = localCursor.getInt(0);
		localCursor.close();
		return count;
	}

	// 得到订单总额 消费合计
	public float sumTotal(String desk_name) {

		float mSumTotal = (float) 0.0;
		float mSum = (float) 0.0;
		String str = "select * from saleandpdt as s1 join SaleRecord as s2 on"
				+ " s1.salerecordId = s2.itemNo where s2.deskName='"
				+ desk_name + "' and s1.status1!='Finish'";
		Log.i("dingdan",
				"select * from saleandpdt as s1 join SaleRecord as s2 on"
						+ " s1.salerecordId=s2.itemNo where s2.deskName='"
						+ desk_name + "' and s1.status1!='Finish'");
		Cursor localCursor = this.db.rawQuery(str, null);
		int i = 0;
		while (localCursor.moveToNext()) {
			String[] columnNames = localCursor.getColumnNames();

			Log.i("columnNames", columnNames[i] + " ");

			mSum = Float.valueOf(
					localCursor.getString(localCursor.getColumnIndex("price")))
					.floatValue()
					* (int) Float.valueOf(
							localCursor.getString(localCursor
									.getColumnIndex("number"))).floatValue();
			mSumTotal += mSum;

			Log.i("tag", "msumTotal" + i++ + "=" + mSum);
		}
		localCursor.close();
		return mSumTotal;
	}

	// 得到订单总额 消费合计
	public float sumTotalDone(String paramString) {

		float mSumTotal = (float) 0.0;
		float mSum = (float) 0.0;
		String str = "select * from SaleRecord as a JOIN saleandpdt as b ON a.itemNo=b.salerecordId where a.deskName='"
				+ paramString + "' and b.status1!='Finish'";
		Cursor localCursor = this.db.rawQuery(str, null);
		while (localCursor.moveToNext()) {
			mSum = Float.valueOf(
					localCursor.getString(localCursor.getColumnIndex("price")))
					.floatValue()
					* (int) Float.valueOf(
							localCursor.getString(localCursor
									.getColumnIndex("number"))).floatValue()
					* Float.valueOf(
							localCursor.getString(localCursor
									.getColumnIndex("rebate"))).floatValue();
			mSumTotal += mSum;
		}
		localCursor.close();
		return mSumTotal;
	}

	// 由桌子名称获得当前该桌子消费记录的id(唯一标识的序号)
	public String getDeskId(String desk_name) {
		String id;

		Cursor localCursor = this.db
				.rawQuery(
						"select itemNo from SaleRecord where deskName=? and"
								+ " createTime=(select max( createTime ) from SaleRecord where deskName==?)",
						new String[] { desk_name, desk_name });
		if (localCursor.moveToFirst()) {
			id = localCursor.getString(0);
			localCursor.close();
		} else {
			id = "";
		}
		return id;
	}

	// 得到该桌子上未发送的所有的菜的信息
	public ArrayList<d_SaleRecord> recordlist(String desk_name) {
		ArrayList<d_SaleRecord> localArrayList = new ArrayList<d_SaleRecord>();
		Cursor localCursor = this.db
				.rawQuery(
						"select * from SaleRecord as a JOIN saleandpdt as b ON a.itemNo=b.salerecordId where a.desk_name==? and b.status1=='Not Sent'",
						new String[] { desk_name });
		while (localCursor.moveToNext()) {
			d_SaleRecord locald_SaleRecord = new d_SaleRecord();
			// locald_SaleRecord.setPayId(localCursor.getString(localCursor
			// .getColumnIndex("PayId")));
			// locald_SaleRecord.setBILLID(localCursor.getString(localCursor
			// .getColumnIndex("BILLID")));

			locald_SaleRecord.setPdtCODE(localCursor.getString(localCursor
					.getColumnIndex("pdtCode")));
			locald_SaleRecord.setPdtName(localCursor.getString(localCursor
					.getColumnIndex("pdtName")));
			locald_SaleRecord.setNumber((int) (Float.valueOf(localCursor
					.getString(localCursor.getColumnIndex("number")))
					.floatValue()));
			locald_SaleRecord.setPrice(Float.valueOf(
					localCursor.getString(localCursor.getColumnIndex("price")))
					.floatValue());
			locald_SaleRecord.setDiscount(localCursor.getFloat(localCursor
					.getColumnIndex("rebate")));
			locald_SaleRecord.setCreateTime(localCursor.getString(localCursor
					.getColumnIndex("createTime1")));
			locald_SaleRecord.setStatus1(localCursor.getString(localCursor
					.getColumnIndex("status1")));
			locald_SaleRecord.setDesk_name(localCursor.getString(localCursor
					.getColumnIndex("deskName")));
			locald_SaleRecord.setOtherSpecNo1(localCursor.getString(localCursor
					.getColumnIndex("otherspec1")));
			locald_SaleRecord.setOtherSpecNo2(localCursor.getString(localCursor
					.getColumnIndex("otherspec2")));
			locald_SaleRecord.setOtherSpec(localCursor.getString(localCursor
					.getColumnIndex("otherSpec0")));
			locald_SaleRecord.setWaiter(localCursor.getString(localCursor
					.getColumnIndex("waiter")));
			locald_SaleRecord.setTax(localCursor.getFloat(localCursor
					.getColumnIndex("tax")));
			locald_SaleRecord.setItemNo(localCursor.getString(localCursor
					.getColumnIndex("itemNo")));
			locald_SaleRecord.setPriority(localCursor.getInt(localCursor
					.getColumnIndex("priority")));
			localArrayList.add(locald_SaleRecord);
		}
		localCursor.close();
		return localArrayList;
	}

	// 得到该桌子上当前被服务的所有的菜
	public ArrayList<d_SaleRecord> getAllSalerecord(String desk_name) {
		ArrayList<d_SaleRecord> localArrayList = new ArrayList<d_SaleRecord>();
		Cursor localCursor = this.db
				.rawQuery(
						"select * from SaleRecord as a JOIN saleandpdt as b ON a.itemNo=b.salerecordId where a.deskName=? and b.status1!='Finish'"
								+ " and createTime=(select max( createTime ) from SaleRecord where deskName=?)",
						new String[] { desk_name, desk_name });
		while (localCursor.moveToNext()) {
			d_SaleRecord locald_SaleRecord = new d_SaleRecord();
			// locald_SaleRecord.setPayId(localCursor.getString(localCursor
			// .getColumnIndex("PayId")));
			// locald_SaleRecord.setBILLID(localCursor.getString(localCursor
			// .getColumnIndex("BILLID")));
			locald_SaleRecord.setId(localCursor.getInt(localCursor
					.getColumnIndex("id")));
			locald_SaleRecord.setPdtCODE(localCursor.getString(localCursor
					.getColumnIndex("pdtCode")));
			locald_SaleRecord.setPdtName(localCursor.getString(localCursor
					.getColumnIndex("pdtName")));
			locald_SaleRecord.setNumber((int) (Float.valueOf(localCursor
					.getString(localCursor.getColumnIndex("number")))
					.floatValue()));
			locald_SaleRecord.setPrice(Float.valueOf(
					localCursor.getString(localCursor.getColumnIndex("price")))
					.floatValue());
			locald_SaleRecord.setDiscount(localCursor.getFloat(localCursor
					.getColumnIndex("rebate")));
			locald_SaleRecord.setCreateTime(localCursor.getString(localCursor
					.getColumnIndex("createTime1")));
			locald_SaleRecord.setStatus1(localCursor.getString(localCursor
					.getColumnIndex("status1")));
			locald_SaleRecord.setDesk_name(localCursor.getString(localCursor
					.getColumnIndex("deskName")));
			locald_SaleRecord.setOtherSpecNo1(localCursor.getString(localCursor
					.getColumnIndex("otherspec1")));
			locald_SaleRecord.setOtherSpecNo2(localCursor.getString(localCursor
					.getColumnIndex("otherspec2")));
			locald_SaleRecord.setOtherSpec(localCursor.getString(localCursor
					.getColumnIndex("otherspec0")));
			locald_SaleRecord.setWaiter(localCursor.getString(localCursor
					.getColumnIndex("waiter")));
			locald_SaleRecord.setTax(localCursor.getFloat(localCursor
					.getColumnIndex("tax")));
			locald_SaleRecord.setItemNo(localCursor.getString(localCursor
					.getColumnIndex("itemNo")));
			locald_SaleRecord.setPriority(localCursor.getInt(localCursor
					.getColumnIndex("priority")));
			locald_SaleRecord.setContactNumber(localCursor.getInt(localCursor
					.getColumnIndex("contactNumber")));
			localArrayList.add(locald_SaleRecord);
		}
		localCursor.close();
		return localArrayList;
	}

	public Cursor recordlist3(String paramString) {
		/*
		 * Cursor c = db.rawQuery("select *from SaleRecord", null);
		 * while(c.moveToNext()){ Log.e("sql_SaleRecord"
		 * ,c.getString(c.getColumnIndex("PdtName")) +
		 * c.getString(c.getColumnIndex("status"))); }
		 */

		return db.rawQuery(paramString, null);
	}

	public void recordlist5(String paramString) {
		/*
		 * Cursor c = db.rawQuery("select *from SaleRecord", null);
		 * while(c.moveToNext()){ Log.e("sql_SaleRecord"
		 * ,c.getString(c.getColumnIndex("PdtName")) +
		 * c.getString(c.getColumnIndex("status"))); }
		 */

		db.execSQL(paramString);
	}

	// public ArrayList<String> recordlist4(String paramString) {
	// ArrayList<String> localArrayList = new ArrayList<String>();
	// Cursor localCursor = this.db.rawQuery(paramString, null);
	// while (localCursor.moveToNext()) {
	// localArrayList.add(localCursor.getString(0));
	// }
	// localCursor.close();
	// return localArrayList;
	// }

	// public void log() {
	// Cursor localCursor = this.db
	// .rawQuery("select * from SaleRecord ", null);
	// /*
	// * if (localCursor.moveToNext()) {
	// * Log.e("sql",localCursor.getString(localCursor
	// * .getColumnIndex("status"))); }
	// */
	//
	// }

	public String getTransactions() {
		int number = 0;
		Cursor cursor = db
				.rawQuery(
						"select count(s2.closeTime1) from SaleRecord as s1 join saleandpdt as s2 on s1.itemNo=s2.salerecordId"
								+ " where s1.waiter = '"
								+ Constant.currentStaff.getS_account()
								+ "'group by s2.closeTime1", null);
		number = cursor.getCount(); // 获得所查询信息的条数
		cursor.close();
		return number + "";
	}

	public String getTip() {
		float tip = 0;
		Cursor cursor = db.rawQuery(
				"select tiptotal as  sum from SaleRecord where waiter = '"
						+ Constant.currentStaff.getS_account(), null);
		while (cursor.moveToNext()) {
			tip += cursor.getFloat(cursor.getColumnIndex("sum"));
		}

		cursor.close();
		return tip + "";
	}

	// 得到销售记录里的所有小费
	public String getTipAll() {
		float tip = 0;
		Cursor cursor = db.rawQuery("select tiptotal as sum from SaleRecord ",
				null);
		while (cursor.moveToNext()) {
			tip += cursor.getFloat(cursor.getColumnIndex("sum"));
		}

		cursor.close();
		return tip + "";
	}

	public String getTransactionsAll() {
		int number = 0;
		Cursor cursor = db.rawQuery("select count(closeTime1) from saleandpdt "
				+ " group by closeTime1", null);
		number = cursor.getCount();
		cursor.close();
		return number + "";
	}

	// 更新该餐桌 时间 ， 结账时间 。
	public void update_time(String desk_name) {
		String str = "select itemNo from SaleRecord where deskName='"
				+ desk_name
				+ "' and createTime=(select max( createTime ) from SaleRecord where "
				+ "deskName='" + desk_name + "')";
		Cursor cursor = db.rawQuery(str, null);
		cursor.moveToFirst();
		int itemNo = cursor.getInt(0);
		cursor.close();
		db.execSQL("update saleandpdt set closeTime1 = ? where salerecordId=?",
				new Object[] { DateUtils.getDateEN(), itemNo });
	}

	// public void update_tip(String desk_name, float tip) {
	// db.execSQL("update SaleRecord set tip = ? where desk_name=?",
	// new Object[] { tip, desk_name });
	// }

	// public void update(d_SaleRecord paramd_SaleRecord, String paramString1,
	// String paramString2) {
	// SQLiteDatabase localSQLiteDatabase = this.db;
	// Object[] arrayOfObject1 = new Object[1];
	// arrayOfObject1[0] = paramString2;
	// localSQLiteDatabase.execSQL(
	// "update salandpdt set pdtCode=?", arrayOfObject1);
	// Object[] arrayOfObject2 = new Object[1];
	// arrayOfObject2[0] = paramString1;
	// localSQLiteDatabase.execSQL(
	// "update Bill set BillId=?", arrayOfObject2);
	// }

	/*
	 * public void update(String table) { Object[] arrayObject = new Object[3];
	 * arrayObject[0] = "prepare"; arrayObject[1] = "notsave"; arrayObject[2] =
	 * table; db.execSQL(
	 * "update SaleRecord set status = ? where status = ? and  desk_name = ?",
	 * arrayObject); }
	 */

	/*
	 * 更新数据库 ， 使status为done , 更新时间 。
	 */
	public void update(String done, String closeTime, String PdtName,
			String itemNo, String createTime) {

		SQLiteDatabase localSqliteDatebase = this.db;
		localSqliteDatebase
				.execSQL(
						"update saleandpdt set status1 = ? , closeTime1 = ? where  pdtName=? and salerecordId=?"
								+ " and createTime1=?", new Object[] { done,
								closeTime, PdtName, itemNo, createTime });
		localSqliteDatebase.close();
	}

	/*
	 * 更新数据库 ， 使status为doned , 更新时间 。
	 */
	public void update1(String done, String closeTime, String PdtName,
			String desk_name, int BILLID, String ItemNo, String createTime) {

		// String str = "select itemNo from SaleRecord where deskName='"
		// + desk_name +
		// "' and createTime=(select max( createTime ) from SaleRecord where " +
		// "deskName='"+ desk_name +"')";
		// Cursor cursor = db.rawQuery(str, null);
		// cursor.moveToFirst();
		// String itemNo = cursor.getString(0);
		// cursor.close();
		SQLiteDatabase localSqliteDatebase = this.db;
		localSqliteDatebase
				.execSQL(
						"update saleandpdt set status1 = ? , closeTime1 = ? where  pdtName=? and salerecordId=? and id=?"
								+ BILLID, new Object[] { done, closeTime,
								PdtName, ItemNo });
	}

	/*
	 * 更新数据库 ， 使status为done
	 */
	public void update(String done, String desk_name) {

		String str = "select itemNo from SaleRecord where deskName='"
				+ desk_name
				+ "' and createTime=(select max( createTime ) from SaleRecord where "
				+ "deskName='" + desk_name + "')";

		Cursor cursor = db.rawQuery(str, null);
		cursor.moveToFirst();
		String itemNo = cursor.getString(0);
		cursor.close();
		SQLiteDatabase localSqliteDatebase = this.db;
		localSqliteDatebase.execSQL(
				"update saleandpdt set status1 = ? where  salerecordId=?",
				new Object[] { done, itemNo });
		/*
		 * Cursor c = db.rawQuery("select *from SaleRecord", null);
		 * while(c.moveToNext()){ Log.e("sql_SaleRecord"
		 * ,c.getString(c.getColumnIndex("PdtName")) +
		 * c.getString(c.getColumnIndex("status"))); }
		 */

	}

	/*
	 * 更新done为delivered状态
	 */
	public void update_delivered(String itemNo, String status) {

		this.db.execSQL("update saleandpdt set status1 = '" + status
				+ "' where salerecordId='" + itemNo + "' and status1='Done'");

	}

	public void update_numac(d_SaleRecord paramd_SaleRecord, float paramFloat) {
		String str = "select itemNo from SaleRecord where deskName='"
				+ Constant.table_id
				+ "' and createTime=(select max( createTime ) from SaleRecord where "
				+ "deskName='" + Constant.table_id + "')";
		Cursor cursor = db.rawQuery(str, null);
		cursor.moveToFirst();
		String itemNo = cursor.getString(0);
		cursor.close();
		SQLiteDatabase localSQLiteDatabase = this.db;
		BigDecimal localBigDecimal = BigDecimal.valueOf(paramd_SaleRecord
				.getNumber());
		float f = localBigDecimal.add(new BigDecimal(paramFloat))
				.setScale(2, 4).floatValue();
		if (f >= 1.0F) {
			Object[] arrayOfObject = new Object[3];
			arrayOfObject[0] = Float.valueOf(f);
			arrayOfObject[1] = paramd_SaleRecord.getPdtCODE();
			arrayOfObject[2] = itemNo;
			localSQLiteDatabase
					.execSQL(
							"update saleandpdt set number=? where pdtCode=? and salerecordId=?",
							arrayOfObject);
		}
		localSQLiteDatabase.close();
	}

	// public void update_numspec(d_SaleRecord paramd_SaleRecord) {
	// if (paramd_SaleRecord.getNumber() == 0.0F) {
	// return;
	// }
	// Object[] arrayOfObject = new Object[2];
	// arrayOfObject[0] = Float.valueOf(paramd_SaleRecord.getNumber());
	// arrayOfObject[1] = paramd_SaleRecord.getOtherSpec();
	// db.execSQL("update SaleRecord set number=?,foodSpec=? where ItemNo=?",
	// arrayOfObject);
	// }

	public void update_send(String desk, String id, String status) {

		this.db.execSQL("update saleandpdt set status1=? where salerecordId='"
				+ id + "' and status1='Not Sent'", new Object[] { status });
	}

	/*
	 * 全部报告页面 ， 总共花费多少钱 。
	 */
	public float sum_cashAll() {
		float mSumTotal = (float) 0.0;
		float mSum = (float) 0.0;
		String str = "select * from saleandpdt ";
		Cursor localCursor = this.db.rawQuery(str, null);
		while (localCursor.moveToNext()) {
			mSum = Float.valueOf(
					localCursor.getString(localCursor.getColumnIndex("price")))
					.floatValue()
					* (int) Float.valueOf(
							localCursor.getString(localCursor
									.getColumnIndex("number"))).floatValue();
			mSumTotal += mSum;
		}
		localCursor.close();
		return mSumTotal;

	}

	/*
	 * 返回数据库最后更新时间
	 */
	public String getLastTime() {
		String time = "";
		String str = "select createTime1 from saleandpdt as s1 join SaleRecord as s2 on"
				+ " s1.salerecordId=s2.itemNo order by createTime1 desc";
		Cursor localCursor = db.rawQuery(str, null);

		if (localCursor.moveToFirst()) {
			time = localCursor.getString(0);
		}
		if (time == null || time.equals("")) {
			time = "2015-01-01 12:12:12";
		}

		localCursor.close();
		return time;
	}

	/*
	 * 更新折扣
	 */
	public void update_discount(d_SaleRecord localSaleRecord) {
		SQLiteDatabase localSqliteDatebase = this.db;
		localSqliteDatebase.execSQL(
				"update SaleRecord set rebate = ? where  deskName=?",
				new String[] { localSaleRecord.getDiscount() + "",
						localSaleRecord.getDesk_name(), });
		localSqliteDatebase.close();
	}

	/*
	 * 更新折扣
	 */
	public void update_discountAll(float discount, String desk_name) {
		SQLiteDatabase localSqliteDatebase = this.db;
		localSqliteDatebase
				.execSQL(
						"update SaleRecord set rebate = ? where deskName=? and createTime="
								+ "(select max( createTime ) from SaleRecord where deskName=?)",
						new Object[] { discount, desk_name, desk_name });
		localSqliteDatebase.close();
	}

	public boolean checkDesk(String table_id) {
		int result = 0;
		String str = "select count(*) from SaleRecord as s1 join saleandpdt as s2 where deskName=? and (s2.status1='Sent' or s2.status1='Done')";
		Cursor localCursor = db.rawQuery(str, new String[] { table_id });
		if (localCursor.moveToLast()) {
			result = localCursor.getInt(0);
		}
		localCursor.close();
		return result == 0;
	}

	public void updateDone(d_SaleRecord sr) {
		String str = "select itemNo from SaleRecord where deskName='"
				+ sr.getDesk_name()
				+ "' and createTime=(select max( createTime ) from SaleRecord where "
				+ "deskName='" + sr.getDesk_name() + "')";
		Cursor cursor = db.rawQuery(str, null);
		cursor.moveToFirst();
		String itemNo = cursor.getString(0);
		cursor.close();

		if (sr.getStatus1().equals("Done")) {
			db.execSQL(
					"update saleandpdt set status1=? where  salerecordId=? and pdtCode = ? and status1='Sent'",
					new Object[] { sr.getStatus1(), itemNo, sr.getPdtCODE() });
		}
	}

	public void updateFinish(d_SaleRecord sr) {
		db.execSQL(
				"update saleandpdt set status=? where  salerecordId='"
						+ sr.getItemNo() + "' and PdtCODE = ?", new String[] {
						sr.getStatus1(), sr.getPdtCODE() });

	}

	public boolean getStatus(String table_id) {

		int result = 0;
		String str = "select * from saleandpdt as s1 join SaleRecord as s2 on s2.itemNo=s1.salerecordId "
				+ " where salerecordId=? and status1='Not Sent'";
		Cursor localCursor = db.rawQuery(str, null);
		if (localCursor.moveToLast()) {
			result = localCursor.getCount();
		}
		localCursor.close();
		return result == 1;

	}

	// public void delete(String desk_name, int id) {
	// db.execSQL("delete from SaleRecord where desk_name =? and id=?",
	// new Object[] { desk_name, id });
	// }
	// public void update_customerNo(d_SaleRecord paramd_SaleRecord,
	// float paramFloat) {
	// BigDecimal localBigDecimal = BigDecimal.valueOf(paramd_SaleRecord
	// .getCustomerNo());
	// float f = localBigDecimal.add(new BigDecimal(paramFloat))
	// .setScale(2, 4).floatValue();
	// if (f >= 1.0F) {
	// Object[] arrayOfObject = new Object[2];
	// arrayOfObject[0] = Float.valueOf(f);
	// arrayOfObject[1] = paramd_SaleRecord.getItemNo();
	// db.execSQL("update SaleRecord set CustomerNo=? where ItemNo=?",
	// arrayOfObject);
	// }
	// }

	public void update_customerNo(int id, boolean falg) {
		String sql = "";
		if (falg) {
			sql = "update saleandpdt set contactNumber = contactNumber+1 where id=?";
		} else {
			sql = "update saleandpdt set contactNumber = contactNumber-1 where id=?";
		}
		db.execSQL(sql, new Object[] { id });
	}

	public void update_customerNo(int id, int count) {
		String sql = "";
		sql = "update saleandpdt set contactNumber=? where id=?";
		db.execSQL(sql, new Object[] { id, count });
	}

	public void updateAllCustomerNo() {
		String id;

		Cursor localCursor = this.db
				.rawQuery(
						"select itemNo from SaleRecord where deskName=? and"
								+ " createTime=(select max( createTime ) from SaleRecord where deskName=?)",
						new String[] { Constant.table_id, Constant.table_id });
		localCursor.moveToFirst();
		id = localCursor.getString(0);
		localCursor.close();
		String sql = "update saleandpdt set contactNumber=0 where salerecordId=?";
		db.execSQL(sql, new Object[] { id });
	}
}