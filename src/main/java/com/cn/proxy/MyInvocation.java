package com.cn.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class MyInvocation implements InvocationHandler {

	public Map<String,ThreadLocal> fieldMap = new HashMap<String,ThreadLocal>() ;
	
	Object target;

	public MyInvocation(Object obj) {
		this.target = obj;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		/*String name = method.getName();
		if("set".equals(name.substring(0, 3))||"get".equals(name.substring(0, 3))){
			String pramString = name.substring(3, 4).toLowerCase()+name.substring(4, name.length());
			target.getClass().getField(pramString);
			if(){
				
			}
		}*/
		Object obj = method.invoke(target, args);
		return obj;
	}

}
