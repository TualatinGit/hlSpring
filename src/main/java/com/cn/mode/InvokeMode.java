package com.cn.mode;

import java.lang.reflect.Method;

public class InvokeMode {

	private String beenName;
	
	private Method method;
	
	private Class cls;
	
	public InvokeMode(String beenName,Method method,Class cls){
		this.beenName=beenName;
		this.method=method;
		this.cls=cls;
	}

	public String getBeenName() {
		return beenName;
	}

	public void setBeenName(String beenName) {
		this.beenName = beenName;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public Class getCls() {
		return cls;
	}

	public void setCls(Class cls) {
		this.cls = cls;
	}
	
}
