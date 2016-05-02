package com.utopia.Model;

public class d_pay_in_out {

	private int id; //编号，标识该次操作输入那次钱箱记录
	private String cashierId;
	private String waiter; //服务员的编号
	private String time; //放入或拿出的时间
	private float money; //放入或取出的钱数
	private String description; //放入或取出的原因
	
	public d_pay_in_out(){
		
	}
	
	public d_pay_in_out(int id,String cashierId,String waiter,String time,
			float money,String description){
		this.id = id;
		this.cashierId=cashierId;
		this.waiter = waiter;
		this.time = time;
		this.money = money;
		this.description = description;
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

	public String getWaiter() {
		return waiter;
	}

	public void setWaiter(String waiter) {
		this.waiter = waiter;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public float getMoney() {
		return money;
	}

	public void setMoney(float money) {
		this.money = money;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "d_pay_in_out [id=" + id + ", waiter=" + waiter + ", time="
				+ time + ", money=" + money + ", description=" + description
				+ "]";
	}
	
	
}
