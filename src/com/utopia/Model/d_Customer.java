package com.utopia.Model;

public class d_Customer {
	private int id;
	private String customNo; 
	//private String ItemNo ;   
	private int ItemNo; //一个销售纪录上每道菜的id
	
	
	public d_Customer() {
		super();
	}
 

	public d_Customer( String customNo, int itemNo ) {
		super();
		//this.id = id;
		this.customNo = customNo; 
		this.ItemNo=itemNo;
	}


 
	public int getId() {
		return id;
	}

//	public String getItemNo() {
//		return ItemNo;
//	}
//
//	public void setItemNo(String itemNo) {
//		ItemNo = itemNo;
//	}
	public int getItemNo(){
		return ItemNo;
	}
    public void setItemNo(int itemNo){
    	this.ItemNo=itemNo;
    }
	public void setId(int id) {
		this.id = id;
	}
    
	public String getCustomNo() {
		return customNo;
	}

	public void setCustomNo(String customNo) {
		this.customNo = customNo;
	}

 
}