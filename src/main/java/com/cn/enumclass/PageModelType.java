package com.cn.enumclass;

public enum PageModelType {
	
	JSON("JSON",1),
	JSP("JSP",2);
	
	String name;
	int value;
	
	private PageModelType(String name,int value){
		this.name=name;
		this.value=value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	
}
