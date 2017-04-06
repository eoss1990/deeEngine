package com.seeyon.v3x.dee.adapter.script.groovy;

import com.seeyon.v3x.dee.TransformFactory;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;

import java.io.File;
import java.util.Map;

/**
 * 获取外部Groovy脚本文件提供调用执行
 * 
 * @author wuyz
 * 
 */
public class GroovyScript {
	private GroovyObject groovyObject;
	/**
	 * 外部groovy文件路径
	 */
	private String path;
	/**
	 * key：方法名 value：参数对象数组
	 */
	private Map<String, Object[]> methodMap;

	public Object execute(String methodName) {
		ClassLoader parent = getClass().getClassLoader();
		GroovyClassLoader loader = new GroovyClassLoader(parent);
		try {
			Class groovyClass = loader.parseClass(new File(TransformFactory
					.getInstance().getHomeDirectory() + path));
			groovyObject = (GroovyObject) groovyClass.newInstance();
			return groovyObject.invokeMethod(methodName,
					methodMap.get(methodName));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Map getMethodMap() {
		return methodMap;
	}

	public void setMethodMap(Map<String, Object[]> methodMap) {
		this.methodMap = methodMap;
	}

}
