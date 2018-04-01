package com.cn.pageModel;

public class HlPageMode {

	private int type;
	
	private String pageView;
	
	private Object data;
	
	public HlPageMode(int type){
		this.type=type;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getPageView() {
		return pageView;
	}

	public void setPageView(String pageView) {
		this.pageView = pageView;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	} 
	
}
