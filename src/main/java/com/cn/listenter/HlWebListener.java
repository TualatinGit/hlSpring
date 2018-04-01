package com.cn.listenter;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.cn.annotation.hlAutowired;
import com.cn.annotation.hlController;
import com.cn.annotation.hlDao;
import com.cn.annotation.hlService;
import com.cn.annotation.hlUrl;
import com.cn.mode.InvokeMode;
import com.cn.util.MapUtil;

public class HlWebListener implements ServletContextListener {

	final static String config_path = "hlApplication";

	//been-class文件映射类型
	final static Map<String, Class> beanClassMap = new HashMap<String, Class>();

	//访问路径映射
	final static Map<String, InvokeMode> urlMap = new HashMap<String, InvokeMode>();

	//已经创建的been的映射
	final static Map<String, Object> beanMap = new HashMap<String, Object>();

	@Override
	public void contextDestroyed(ServletContextEvent event) {

	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		try {
			String applicationPath = event.getServletContext().getInitParameter(config_path);
			String realyPath = event.getServletContext().getRealPath("/");
			File f = new File(realyPath + applicationPath);
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(f);
			NodeList nl = doc.getElementsByTagName("controller");
			String filePath = null;
			if (nl != null) {
				for (int i = 0; i < nl.getLength(); i++) {
					filePath = nl.item(i).getFirstChild() == null ? "" : nl.item(i).getFirstChild().getNodeValue();
				}
			}
			List<String> allControllerClass = this.getAllClass(filePath, realyPath);
			for (String controller : allControllerClass) {
				Class cls = Class.forName(controller);
				this.intMap(cls);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void intMap(Class cls) {
		if (cls.isAnnotationPresent(hlController.class)) {
			if (cls.isAnnotationPresent(hlUrl.class)) {
				hlUrl controllerUrl = (hlUrl) cls.getAnnotation(hlUrl.class);
				Method[] mtds = cls.getMethods();
				for (Method mtd : mtds) {
					if (mtd.isAnnotationPresent(hlUrl.class)) {
						hlUrl methodUrl = mtd.getAnnotation(hlUrl.class);
						getUrlmap().put(
								controllerUrl.url() + methodUrl.url(),
								new InvokeMode(cls.getName().substring(cls.getName().lastIndexOf("."),
										cls.getName().length()), mtd, cls));
						MapUtil.putMap(cls.getName(), getBeanclassmap(), cls);
					}
				}
				Field[] fields = cls.getDeclaredFields();
				for (Field fd : fields) {
					if (fd.isAnnotationPresent(hlAutowired.class)) {
						this.intMap(fd.getClass());
					}
				}
			}
		} else if (cls.isAnnotationPresent(hlService.class) || cls.isAnnotationPresent(hlDao.class)) {
			// 设置bean映射
			MapUtil.putMap(
					cls.getName().substring(cls.getName().lastIndexOf(".") + 1, cls.getName().lastIndexOf(".") + 2)
							.toLowerCase()
							+ cls.getName().substring(cls.getName().lastIndexOf(".") + 2, cls.getName().length()),
					getBeanclassmap(), cls);
			Field[] fields = cls.getDeclaredFields();
			for (Field fd : fields) {
				if (fd.isAnnotationPresent(hlAutowired.class)) {
					this.intMap(fd.getClass());
				}
			}
		}
	}

	public List<String> getAllClass(String filePath, String realyPath) {
		List<String> reList = new ArrayList<String>();
		String[] path = filePath.split(",");
		if (path.length > 0) {
			for (String pt : path) {
				getClass(reList, pt, realyPath, null);
			}
		} else {
			getClass(reList, "", realyPath, null);
		}

		return reList;
	}

	public void getClass(List<String> reList, String pt, String realyPath, String ptPath) {
		// boolean getALLSubFile = false;
		if (!pt.isEmpty() && "*".equals(pt.substring(pt.length() - 1, pt.length()))) {
			pt = pt.substring(0, pt.length() - 2);
			// getALLSubFile = true;
		}
		String allPath = "";
		if (realyPath != null && !realyPath.isEmpty()) {
			allPath = realyPath + "\\WEB-INF\\classes\\" + pt.replace(".", "\\");
		} else {
			allPath = ptPath;
		}

		File[] allFiles = new File(allPath).listFiles();
		if (allFiles.length > 0) {
			for (File subFile : allFiles) {
				if (subFile.isFile() && subFile.getName().contains(".class")) {
					reList.add(pt + "." + subFile.getName().substring(0, subFile.getName().indexOf(".class")));
				} else if (subFile.isDirectory()) {
					getClass(reList, pt != null && !pt.isEmpty() ? pt + "." + subFile.getName() : subFile.getName(),
							null, subFile.getPath());
				}
			}
		}
	}

	public List<File> getAllClassFile(String filePath, String realyPath) {
		List<File> reList = new ArrayList<File>();
		String[] path = filePath.split(",");
		if (path.length > 0) {
			for (String pt : path) {
				boolean getALLSubFile = false;
				if ("*".equals(pt.substring(pt.length() - 1, pt.length()))) {
					pt = pt.substring(0, pt.length() - 2);
					getALLSubFile = true;
				}
				File file = new File(realyPath + pt);
				if (file.isFile() && !getALLSubFile) {
					reList.add(file);
				} else if (getALLSubFile) {
					File[] subfiles = file.listFiles();
					for (File subfile : subfiles) {
						if (subfile.isFile()) {
							reList.add(subfile);
						}
					}
				}
			}
		}
		return reList;
	}

	public static Map<String, Class> getBeanclassmap() {
		return beanClassMap;
	}

	public static Map<String, InvokeMode> getUrlmap() {
		return urlMap;
	}

	public static Map<String, Object> getBeanmap() {
		return beanMap;
	}

}
