package com.cn.service.imp;

import com.cn.annotation.hlAutowired;
import com.cn.annotation.hlDao;
import com.cn.annotation.hlService;
import com.cn.dao.IMyDao;
import com.cn.dao.imp.MyDao;
import com.cn.service.IMyService;

@hlService
public class MyService implements IMyService {
	
	@hlAutowired
	private IMyDao myDao;

	@Override
	public int myServiceSay() {
		return myDao.MyDaoSay();
	}

}
