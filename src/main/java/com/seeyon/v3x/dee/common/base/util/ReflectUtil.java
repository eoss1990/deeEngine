package com.seeyon.v3x.dee.common.base.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectUtil {
	private final static Log log = LogFactory.getLog(ReflectUtil.class);
	/**
	 * @description 通过method名称执行该方法
	 * @date 2011-12-28
	 * @author liuls
	 * @param owner 需要执行方法的对象
	 * @param methodName 方法
	 * @return 执行方法后的返回值
	 * @throws Exception 异常
	 */
	@SuppressWarnings("unchecked")
	public static Object invokeMethodByMethodName(Object owner, String methodName) throws Exception {
		Object reutrnObj;
		try {
			Class ownerClass = owner.getClass();
			Method method = ownerClass.getMethod(methodName);
			reutrnObj= method.invoke(owner);
		} catch (Exception e) {
			throw new Exception("invokeMethod is error:"+owner+"."+methodName,e);
		}
		return reutrnObj;
	}
	/**
	 * @description  通过对象的属性将属性值存入到对象中
	 * @date 2011-12-28
	 * @author liuls
	 * @param owner 需要存入值的对象
	 * @param fieldName  属性名称
	 * @param value 值
	 * @return 带值的对象
	 * @throws Exception 异常
	 */
	@SuppressWarnings("unchecked")
	public static Object invokeMethodByFieldName(Object owner, String fieldName,Object value) throws Exception {

		Method method = null;
		try {
			Class ownerClass = owner.getClass();
			Field field = findField(ownerClass,fieldName);
//			if(field==null) throw new Exception("field not found:"+fieldName);
			if(field==null) return owner;
			Class typeClass = field.getType();
			String methodName = getMethodByField(fieldName);
			method = ownerClass.getMethod(methodName,typeClass);
			method.invoke(owner, value);
		
		} catch (Exception e) {
			throw new Exception("invokeMethod is error:"+owner+"."+fieldName+"="+value);
		}
		
		return owner;
	}

	/**
	 * @description 获取属性
	 * @date 2011-12-22
	 * @author liuls
	 * @param ownerClass 对象
	 * @param fieldName 属性名
	 * @return
	 */
	private static Field findField(Class ownerClass,String fieldName){
		// TODO 优化，更高效的查找方法或直接移植spring inject
		for (Field field : ownerClass.getDeclaredFields()) {
			if(field.getName().equals(fieldName)) return field;
		}
		if(ownerClass != Object.class){
			return findField(ownerClass.getSuperclass(),fieldName);
		}
		return null;
	}
	/**
	 * @description 获取set方法
	 * @date 2011-12-22
	 * @author liuls
	 * @param field 属性名称
	 * @return
	 */
	public static String getMethodByField(String field){
		return  "set"+field.replaceFirst(field.substring(0, 1),field.substring(0, 1).toUpperCase()) ;
	}

}
