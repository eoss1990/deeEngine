package com.seeyon.v3x.dee.common.base.util;

import com.seeyon.v3x.dee.common.base.annotation.Column;
import com.seeyon.v3x.dee.common.base.page.Page;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Resultset2List {
	private static Log log = LogFactory.getLog(Resultset2List.class);
    /**
     * @description 从ResultSet绑定到JavaBean 
     * @date 2011-12-29
     * @author liuls
     * @param rs  ResultSet
     * @param clazz  Object（JavaBean）   
     * @return Object
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
	public static List getListFromRS(ResultSet rs, Class clazz)
			throws Exception {
		List dataList = new LinkedList();
		ResultSetMetaData rsmd = rs.getMetaData(); // 取得ResultSet的列名
		int columnsCount = rsmd.getColumnCount();
		String[] columnNames = new String[columnsCount];
		for (int i = 0; i < columnsCount; i++) {
			columnNames[i] = rsmd.getColumnLabel(i + 1);
		}
		Map<String, String> map = getBeanAnnMap(clazz);
		// TODO: 如果map不存在，就直接用rs的列名
		// ，如果列名跟属性名一致，就直接读，如果不一致，如列名是大写，属性是小写，就需要用影射，没有影射就直接写rs.getXX(列名)一一对应;
		while (rs.next()) { // 遍历ResultSet
			Object obj = Class.forName(clazz.getName()).newInstance();
			// 反射, 从ResultSet绑定到JavaBean
			for (int i = 0; i < columnNames.length; i++) {
				// 取得Set方法
				String methodName = map.get(columnNames[i]);
				if (methodName != null) {
					Object value = rs.getObject(columnNames[i]);
					try {
						// JavaBean内部属性和ResultSet中一致时候
						if (value != null) {
							Method setMethod = clazz.getMethod(methodName,
									value.getClass());
							setMethod.invoke(obj, value);
						}
					} catch (Exception e) {
						log.error("ResultSet转bean时赋值出现异常提示：" + "method:"
								+ methodName + "---value:" + value, e);

						try{
							// JavaBean内部属性和ResultSet中不一致时候，使用String来输入值。
							Method setMethod = clazz.getMethod(methodName,
									String.class);
							setMethod.invoke(obj,
									value == null ? "" : value.toString());
						}
						catch (Exception e1){
							log.error("在异常中处理赋值（默认为String类型）继续出现异常提示：" + "method:"
									+ methodName + "---value:" + value, e1);
						}
					}
				} else {
					// log.warn("数据库字段："+columnNames[i]+"在bean:"+clazz.getName()+"中没有与其想匹配的属性或没有在对应的set方法上加@Column注解");
				}
			}
			dataList.add(obj);
		}
		return dataList;
	}  
    /**
     * @description 从ResultSet绑定到JavaBean 
     * @date 2011-12-29
     * @author liuls
     * @param rs  ResultSet
     * @param  page Page对象
     * @param clazz  Object（JavaBean）   
     * @return Object
     * @throws Exception
     */
	@SuppressWarnings("unchecked")
	public static Page getListFromRS(Page page,ResultSet rs, Class clazz) throws Exception {
		page.setResult(getListFromRS(rs,clazz));
		return page; 
	}
	/**
	 * @description 根据注解类Colunm获取将注解所在的方法及所有的数据库属性放到map
	 * @Column(name="FLOW_DESC")
		public void setFLOW_DESC(String flow_desc) {
			FLOW_DESC = flow_desc;
		}
		FLOW_DESC 表示数据库对应的字段
		将FLOW_DESC , setFLOW_DESC分别作为map的key，value存放在一起
	 * 
	 * 
	 * @date 2011-12-29
	 * @author liuls
	 * @param clazz
	 */
	@SuppressWarnings("unchecked")
	public static Map<String,String> getBeanAnnMap(Class clazz){
		Map<String,String> map = new LinkedHashMap<String,String>();
		Method[] methods =clazz.getDeclaredMethods();
	    for (Method method : methods) {
	        /* 
	         * 判断方法中是否有指定注解类型的注解 
	         */ 
	        boolean hasAnnotation = method.isAnnotationPresent(Column.class);   
	        if (hasAnnotation) {   
	            /* 
	             * 根据注解类型返回方法的指定类型注解 
	             */ 
	        	Column annotation = method.getAnnotation(Column.class); 
	        	map.put(annotation.name(), method.getName());
	        }
	    }
	    return map;
		
	}


}
