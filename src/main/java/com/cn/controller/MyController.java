package com.cn.controller;

import java.net.HttpRetryException;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cn.annotation.hlAutowired;
import com.cn.annotation.hlController;
import com.cn.annotation.hlUrl;
import com.cn.enumclass.PageModelType;
import com.cn.pageModel.HlPageMode;
import com.cn.service.IMyService;
import com.cn.service.imp.MyService;

@hlController
@hlUrl(url = "/myController")
public class MyController {

	@hlAutowired
	private IMyService myService;

	@hlUrl(url = "/doStart")
	public HlPageMode doStart(HttpServletRequest request,HttpServletResponse response) {
		System.out.println("doStart );
		HlPageMode pageModel = new HlPageMode(PageModelType.JSON.getValue());
		int reData = myService.myServiceSay();
		pageModel.setData(reData);
		return pageModel;
	}

	
	@hlUrl(url = "/doAny")
	public HlPageMode doAny(HttpServletRequest request,HttpServletResponse response) {
		HlPageMode pageModel = new HlPageMode(PageModelType.JSON.getValue());
		int reData = myService.myServiceSay();
		pageModel.setData(reData);
		return pageModel;
	}
}
