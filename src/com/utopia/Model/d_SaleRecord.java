package com.utopia.Model;

//
public class d_SaleRecord {
	
	private String PayId;
	//private String BILLID;
	private String PdtCODE;    //菜的编码
	private String PdtName;
	private int number;         //该菜的数量
	private double Price;       //该菜的价格
	private int rebate;         //该菜的折扣
	private String CreateTime;  //该菜的点菜时间
	private String closeTime;     //该菜的做完时间
	private String status;    //该销售纪录的状态
	private String status1; //该销售纪录上每道菜的状态
	private String desk_name;
	private String OtherSpecNo1;	//size
	private String OtherSpecNo2;	//hotness
	private String OtherSpec;	//备注
	private String Waiter ;
	private float tax ; 		//税收
	private float tip ; 		//小费
	private float discount ;    //折扣
	private String ItemNo;
	private int customerNo ;    //顾客数量（目前没用）
	private int priority;       //优先级
	private int id;   //该桌子上销售的每道菜的编号
	private int contactNumber;  //顾客数量
	
	public d_SaleRecord(){
		
	}
	
	public d_SaleRecord(String payId, int id , String pdtCODE,
			String pdtName, int number, double price, int rebate,
			String createTime, String closeTime, String status, String desk_name,
			String otherSpecNo1, String otherSpecNo2, String otherSpec,
			String waiter, float tax,float tip,float discount,String ItemNo,int customerNo,
			int priority,String status1,int contactNumber) {
		super();
		this.PayId = payId;
		this.id = id;
		this.PdtCODE = pdtCODE;
		this.PdtName = pdtName;
		this.number = number;
		this.Price = price;
		this.rebate = rebate;
		this.CreateTime = createTime;
		this.closeTime = closeTime;
		this.status = status;
		this.desk_name = desk_name;
		this.OtherSpecNo1 = otherSpecNo1;
		this.OtherSpecNo2 = otherSpecNo2;
		this.OtherSpec = otherSpec;
		this.Waiter = waiter;
		this.tax = tax;
		this.tip = tip;
		this.discount = discount ;
		this.ItemNo = ItemNo;
		this.customerNo = customerNo ;
		this.priority=priority;
		this.status1=status1;
		this.contactNumber=contactNumber;
	}

    public int getContactNumber(){
    	return contactNumber;
    }
    
    public void setContactNumber(int contactNumber){
    	this.contactNumber=contactNumber;
    }
    
	public String getPayId() {
		return PayId;
	}

	public void setPayId(String payId) {
		PayId = payId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id =id;
	}
   
	public String getPdtCODE() {
		return PdtCODE;
	}

	public void setPdtCODE(String pdtCODE) {
		PdtCODE = pdtCODE;
	}

	public String getPdtName() {
		return PdtName;
	}

	public void setPdtName(String pdtName) {
		PdtName = pdtName;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public double getPrice() {
		return Price;
	}

	public void setPrice(double price) {
		Price = price;
	}

	public int getRebate() {
		return rebate;
	}

	public void setRebate(int rebate) {
		this.rebate = rebate;
	}

	public String getCreateTime() {
		return CreateTime;
	}

	public void setCreateTime(String createTime) {
		CreateTime = createTime;
	}

	public String getCloseTime() {
		return closeTime;
	}

	public void setCloseTime(String closeTime) {
		this.closeTime = closeTime;
	}  

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getStatus1(){
		return status1;
	}
     
	public void setStatus1(String status1){
		this.status1=status1;
	}
	public String getDesk_name() {
		return desk_name;
	}

	public void setDesk_name(String desk_name) {
		this.desk_name = desk_name;
	}

	public String getOtherSpecNo1() {
		return OtherSpecNo1;
	}

	public void setOtherSpecNo1(String otherSpecNo1) {
		OtherSpecNo1 = otherSpecNo1;
	}

	public String getOtherSpecNo2() {
		return OtherSpecNo2;
	}

	public void setOtherSpecNo2(String otherSpecNo2) {
		OtherSpecNo2 = otherSpecNo2;
	}

	public String getOtherSpec() {
		return OtherSpec;
	}

	public void setOtherSpec(String otherSpec) {
		OtherSpec = otherSpec;
	}

	public String getWaiter() {
		return Waiter;
	}

	public void setWaiter(String waiter) {
		Waiter = waiter;
	}

	public float getTax() {
		return tax;
	}

	public void setTax(float tax) {
		this.tax = tax;
	}

	public float getTip() {
		return tip;
	}

	public void setTip(float tip) {
		this.tip = tip;
	}

	public float getDiscount() {
		return discount;
	}

	public void setDiscount(float discount) {
		this.discount = discount;
	}

	public String getItemNo() {
		return ItemNo;
	}

	public void setItemNo(String itemNo) {
		ItemNo = itemNo;
	}

	public int getCustomerNo() {
		return customerNo;
	}

	public void setCustomerNo(int customerNo) {
		this.customerNo = customerNo;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}
    

//	@Override
//	public String toString() {
//		return "d_SaleRecord [PayId=" + PayId + ", BILLID=" + BILLID
//				+ ", PdtCODE=" + PdtCODE + ", PdtName=" + PdtName + ", number="
//				+ number + ", Price=" + Price + ", rebate=" + rebate
//				+ ", CreateTime=" + CreateTime + ", closeTime=" + closeTime
//				+ ", status=" + status + ", desk_name=" + desk_name
//				+ ", OtherSpecNo1=" + OtherSpecNo1 + ", OtherSpecNo2="
//				+ OtherSpecNo2 + ", OtherSpec=" + OtherSpec + ", Waiter="
//				+ Waiter + ", tax=" + tax + ", tip=" + tip + ", discount="
//				+ discount + ", ItemNo=" + ItemNo + ", customerNo="
//				+ customerNo + ", priority=" + priority + ", getPayId()="
//				+ getPayId() + ", getId()=" + getId()
//				+ ", getPdtCODE()=" + getPdtCODE() + ", getPdtName()="
//				+ getPdtName() + ", getNumber()=" + getNumber()
//				+ ", getPrice()=" + getPrice() + ", getRebate()=" + getRebate()
//				+ ", getCreateTime()=" + getCreateTime() + ", getCloseTime()="
//				+ getCloseTime() + ", getStatus()=" + getStatus()
//				+ ", getDesk_name()=" + getDesk_name() + ", getOtherSpecNo1()="
//				+ getOtherSpecNo1() + ", getOtherSpecNo2()="
//				+ getOtherSpecNo2() + ", getOtherSpec()=" + getOtherSpec()
//				+ ", getWaiter()=" + getWaiter() + ", getTax()=" + getTax()
//				+ ", getTip()=" + getTip() + ", getDiscount()=" + getDiscount()
//				+ ", getItemNo()=" + getItemNo() + ", getCustomerNo()="
//				+ getCustomerNo() + ", getPriority()=" + getPriority()
//				+ ", getString()=" + getString() + ", getClass()=" + getClass()
//				+ ", hashCode()=" + hashCode() + ", toString()="
//				+ super.toString() + "]";
//	}

	public String getString() {
		return "{\"PayId\":\"" + PayId + "\", \"id\":\"" + id
				+ "\", \"PdtCODE\":\"" + PdtCODE + "\", \"PdtName\":\"" + PdtName + "\", \"number\":\""
				+ number + "\", \"Price\":\"" + Price + "\", \"rebate\":\"" + rebate
				+ "\", \"CreateTime\":\"" + CreateTime + "\", \"closeTime\":\"" + closeTime
				+ "\", \"status\":\"" + status + "\", \"desk_name\":\"" + desk_name
				+ "\", \"OtherSpec\":\"" + OtherSpec 
				+ "\", \"OtherSpecNo1\":\"" + OtherSpecNo1 + "\", \"OtherSpecNo2\":\""
				
				+ OtherSpecNo2 + "\", \"ItemNo\":\"" + ItemNo + "\", \"Waiter\":\""
				+ Waiter + "\", \"tax\":\"" + tax + "\"}";
	}
	
}