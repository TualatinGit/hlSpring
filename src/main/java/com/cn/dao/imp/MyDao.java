package com.cn.dao.imp;

import com.cn.annotation.hlDao;
import com.cn.dao.IMyDao;

@hlDao
public class MyDao implements IMyDao {

	@Override
	public int MyDaoSay() {
		return 100;
	}

}
