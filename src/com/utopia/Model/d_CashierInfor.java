package com.utopia.Model;

public class d_CashierInfor {
	private int id; // 每次交易记录的编号
	private String cashierId; // 钱箱的编号
	private String createTime; // 钱箱开启的时间
	private String closeTime; // 钱箱关闭的时间
	private float initMoney; // 钱箱初始的钱数
	private float different; // 钱箱实际的钱数和预期的差值
	private float actualMoney; // 钱箱实际的钱数

	public d_CashierInfor() {

	}

	public d_CashierInfor(int id,String cashierId,String createTime,String closeTime,
			float initMoney,float different,float actualMoney) {
		this.id = id;
		this.cashierId = cashierId;
		this.createTime = createTime;
		this.closeTime = closeTime;
		this.initMoney = initMoney;
		this.different = different;
		this.actualMoney = actualMoney;

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCashierId() {
		return cashierId;
	}

	public void setCashierId(String cashierId) {
		this.cashierId = cashierId;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getCloseTime() {
		return closeTime;
	}

	public void setCloseTime(String closeTime) {
		this.closeTime = closeTime;
	}

	public float getInitMoney() {
		return initMoney;
	}

	public void setInitMoney(float initMoney) {
		this.initMoney = initMoney;
	}

	public float getDifferent() {
		return different;
	}

	public void setDifferent(float different) {
		this.different = different;
	}

	public float getActualMoney() {
		return actualMoney;
	}

	public void setActualMoney(float actualMoney) {
		this.actualMoney = actualMoney;
	}

	@Override
	public String toString() {
		return "d_CashierInfor [id=" + id + ", cashierId=" + cashierId
				+ ", createTime=" + createTime + ", closeTime=" + closeTime
				+ ", initMoney=" + initMoney + ", different=" + different
				+ ", actualMoney=" + actualMoney + "]";
	}
	
	
}
