package com.utopia.Model;


public class d_Bill {
	private String BillId ;
    private String salerecordId;   //交易记录编号
	private String Waiter ; //服务者
	private float Subtotal ; //应支付总额
	private float taxTotal ;  //税收
	private float Total ;    //共支付总额
	private String CreateTime;  //开始时间
	private float Distant ;  
	private float tip;  //小费
	private float rebate;  //折扣
	private float initTotal;  //原总额
	private String tipPayment;//小费的支付方式
	private String payment;  //其他费用的支付方式
	private String cashierId;  //收银机编号
	private String dept;//所属的部门
	
	public d_Bill(){
		
	}
	public d_Bill(String billId, String waiter, float subtotal, float tax,
			float total, String createTime, float distant, float tip) {
		super();
		this.BillId = billId;
		this.Waiter = waiter;
		this.Subtotal = subtotal;
		this.taxTotal = tax;
		this.Total = total;
		this.CreateTime = createTime;
		this.Distant = distant;
		this.tip = tip;
	}


	public d_Bill(String billId, String salerecordId,String waiter, float subtotal,
			float taxTotal,float total, String createTime, float distant, float tip,
			float rebate,float initTotal,String tipPayment,String payment,
			String cashierId,String dept) {
		super();
		this.BillId = billId;
		this.salerecordId=salerecordId;
		this.Waiter = waiter;
		this.Subtotal = subtotal;
		this.taxTotal = taxTotal;
		this.Total = total;
		this.CreateTime = createTime;
		this.Distant = distant;
		this.tip = tip;
		this.rebate=rebate;
		this.initTotal=initTotal;
		this.tipPayment=tipPayment;
		this.payment=payment;
		this.cashierId=cashierId;
		this.dept=dept;
	}

	public String getBillId() {
		return BillId;
	}

	public void setBillId(String billId) {
		BillId = billId;
	}

	public String getSalerecordId() {
		return salerecordId;
	}
 
	public void setSalerecordId(String salerecordId) {
		this.salerecordId = salerecordId;
	}

	public String getWaiter() {
		return Waiter;
	}

	public void setWaiter(String waiter) {
		Waiter = waiter;
	}

	public float getSubtotal() {
		return Subtotal;
	}

	public void setSubtotal(float subtotal) {
		Subtotal = subtotal;
	}

	public float getTaxTotal() {
		return taxTotal;
	}

	public void setTaxTotal(float taxTotal) {
		this.taxTotal = taxTotal;
	}

	public float getTotal() {
		return Total;
	}

	public void setTotal(float total) {
		Total = total;
	}

	public String getCreateTime() {
		return CreateTime;
	}

	public void setCreateTime(String createTime) {
		CreateTime = createTime;
	}

	public float getDistant() {
		return Distant;
	}

	public void setDistant(float distant) {
		Distant = distant;
	}

	public float getTip() {
		return tip;
	}

	public void setTip(float tip) {
		this.tip = tip;
	}

	public float getRebate() {
		return rebate;
	}

	public void setRebate(float rebate) {
		this.rebate = rebate;
	}

	public float getInitTotal() {
		return initTotal;
	}

	public void setInitTotal(float initTotal) {
		this.initTotal = initTotal;
	}

	public String getTipPayment() {
		return tipPayment;
	}

	public void setTipPayment(String tipPayment) {
		this.tipPayment = tipPayment;
	}

	public String getPayment() {
		return payment;
	}

	public void setPayment(String payment) {
		this.payment = payment;
	}

	public String getCashierId() {
		return cashierId;
	}

	public void setCashierId(String cashierId) {
		this.cashierId = cashierId;
	}
	
	public void setDept(String dept){
		this.dept=dept;
	}
	public String getDept(){
		return dept;
	}
	@Override
	public String toString() {
		return "d_Bill [BillId=" + BillId + ", salerecordId=" + salerecordId
				+ ", Waiter=" + Waiter + ", Subtotal=" + Subtotal
				+ ", taxTotal=" + taxTotal + ", Total=" + Total
				+ ", CreateTime=" + CreateTime + ", Distant=" + Distant
				+ ", tip=" + tip + ", rebate=" + rebate + ", initTotal="
				+ initTotal + ", tipPayment=" + tipPayment + ", payment="
				+ payment + ", cashierId=" + cashierId + ", dept=" + dept + "]";
	}
	
    

	
	
}
