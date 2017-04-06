
package com.seeyon.v3x.dee.common.base.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @description Generics的util类,通过反射获得Class声明的范型Class.
 * 
 * @author liuls
 */
@SuppressWarnings("unchecked")
public class GenericsUtil {


	/**
	 * @description 通过反射,获得定义Class时声明的父类的范型参数的类型. 如public class GroupCodeDAOHibernateImpl
	 * 				extends HibernateEntityDAO<GroupCode> implements IGroupCodeDAO<GroupCode>
	 * @date 2011-12-23
	 * @author liuls
	 * @param clazz 要反射的类
	 * @return 给定类的第一个直接超类，如果没有找到,返回<code>Object.class</code>。
	 */
	public static Class getSuperClassGenricType(Class clazz) {
		return getSuperClassGenricType(clazz, 0);
	}

	/**
	 * @description 通过反射,获得定义Class时声明的父类的范型参数的类型. 如public class GroupCodeDAOHibernateImpl
	 * 				extends HibernateEntityDAO<GroupCode> implements IGroupCodeDAO<GroupCode>
	 * @date 2011-12-23
	 * @author liuhh
	 * @param clazz 要反射的类
	 * @param index 该类的超类的索引，从0开始。
	 * @return 超类。如果没有找到,返回<code>Object.class</code>。
	 */
	public static Class getSuperClassGenricType(Class clazz, int index) {
		Type genType = clazz.getGenericSuperclass();
		if (!(genType instanceof ParameterizedType)) {
			return Object.class;
		}

		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

		if (index >= params.length || index < 0) {
			return Object.class;
		}
		if (!(params[index] instanceof Class)) {
			return Object.class;
		}
		return (Class) params[index];
	}
}
