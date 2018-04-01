package com.cn.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.cn.annotation.hlAutowired;
import com.cn.enumclass.PageModelType;
import com.cn.listenter.HlWebListener;
import com.cn.mode.InvokeMode;
import com.cn.pageModel.HlPageMode;
import com.cn.proxy.ProxyFactory;
import com.cn.util.MapUtil;

public class HlDispatchServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String s = req.getContextPath();
		try {
			String url =req.getRequestURI().substring(req.getRequestURI().indexOf(req.getContextPath())+req.getContextPath().length(), req.getRequestURI().length());
			doAllRequest(url,req,resp);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			
			doAllRequest(req.getRequestURI(),req,resp);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void doAllRequest(String url, HttpServletRequest req,
			HttpServletResponse resp) throws Exception {
		InvokeMode mode = HlWebListener.getUrlmap().get(url);
		Object obj = this.getController(mode);
		Method mtd = mode.getMethod();
		Class[] parameterCls = mtd.getParameterTypes();
		Object[] parameters = new Object[parameterCls.length];
		for (int i = 0; i < parameterCls.length; i++) {
			if (parameterCls[i].getName().equals(
					HttpServletRequest.class.getName())) {
				parameters[i] = req;
			} else if (parameterCls[i].getName().equals(
					HttpServletResponse.class.getName())) {
				parameters[i] = resp;
			} else if (parameterCls[i].getName()
					.equals(Integer.class.getName())) {
				parameters[i] = 0;
			} else {
				parameters[i] = null;
			}
		}
		HlPageMode pageMode = (HlPageMode)mode.getMethod().invoke(obj,
				parameters);
		if (pageMode.getType() == PageModelType.JSON.getValue()) {
			resp.setCharacterEncoding("UTF-8");
			resp.setContentType("application/json; charset=utf-8");
			PrintWriter out = null;
			try {
				out = resp.getWriter();
				out.write((pageMode.getData()).toString());
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (out != null) {
					out.close();
				}
			}

		}
	}

	private Object getController(InvokeMode mode) throws Exception {
		Object obj = null;
		if (mode != null) {
			obj = HlWebListener.getBeanmap().get(mode.getBeenName());
			if (obj == null) {
				Object target = mode.getCls().newInstance();
				if (target != null) {
					obj = target;
				}
				if (obj != null) {
					getProxy(obj);
					MapUtil.putMap(mode.getBeenName(),
							HlWebListener.getBeanmap(), target);
				}
			}
		}
		return obj;
	}

	public void getProxy(Object proxy) throws Exception {
		Field[] fields = proxy.getClass().getDeclaredFields();
		for (Field field : fields) {
			if (field.isAnnotationPresent(hlAutowired.class)) {
				Object obj = HlWebListener.getBeanmap().get(field.getName());
				if (obj == null) {
					Object target = HlWebListener.getBeanclassmap().get(field.getName()).newInstance();
					if (target != null) {
						getProxy(target);
						ProxyFactory.bind(target);
						obj = ProxyFactory.getPoxy();
						if(obj!=null){
							field.setAccessible(true);
							field.set(proxy, obj);
							MapUtil.putMap(field.getName(), HlWebListener.getBeanmap(), obj);
						}
					}
					if (obj != null) {
						MapUtil.putMap(field.getClass().getName(),
								HlWebListener.getBeanmap(), target);
					}
				}
			}
		}
	}
}
