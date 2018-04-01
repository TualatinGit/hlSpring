package com.cn.util;

import java.util.Map;

public class MapUtil {

	public synchronized static void putMap(String key,Map map,Object target,boolean single){
		map.put(key, target);
	}
	
	public synchronized static void putMap(String key,Map map,Object target){
		map.put(key, target);
	}
	
	
}
