package com.utopia.Model;

public class d_drawerInfor {

	private String createTime;
	private String closeTime;
	private float differ;
	
	public d_drawerInfor(String createTime, String closeTime, float differ){
		
		this.createTime = createTime;
		this.closeTime = closeTime;
		this.differ = differ;
		
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

	public float getDiffer() {
		return differ;
	}

	public void setDiffer(float differ) {
		this.differ = differ;
	}
	
	
}
