package com.cn.proxy;

import java.lang.reflect.Proxy;

public class ProxyFactory {

	static Object target;

	public static  void bind(Object obj) {
		ProxyFactory.target = obj;
	}

	public static Object getPoxy() {
		return Proxy.newProxyInstance(target.getClass().getClassLoader(),
				target.getClass().getInterfaces(), new MyInvocation(target));
	}

}
