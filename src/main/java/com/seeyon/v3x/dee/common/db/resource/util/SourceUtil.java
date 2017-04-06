package com.seeyon.v3x.dee.common.db.resource.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * @author Zhang.Wei
 * @date Dec 28, 20111:51:18 PM
 * @Copyright(c) Beijing Seeyon Software Co.,LTD
 */
public class SourceUtil {
	private final static Log log = LogFactory.getLog(SourceUtil.class);
    /**
     * 从xml文件中获取需要的属性值
     * @param resource_code
     * @param eleName
     * @param subEleName
     * @return
     */
    public static String getValueFromXml(String resource_code, String eleName, String subEleName) {
        try {
            if("".equals(eleName) || eleName == null) {
                eleName = "property";
            }
            Document document = DocumentHelper.parseText(resource_code);
            Element root = document.getRootElement();
            List nodes = root.elements(eleName);
            for(Iterator it = nodes.iterator(); it.hasNext();) {
                Element elm = (Element)it.next();
                if(subEleName.equals(elm.attribute("name").getValue())) {
                    return elm.attribute("value").getValue();
                }
            }
        } catch(DocumentException e) {
			log.error(e.getMessage(), e);
        }
        return "";
    }

    /**
     * 获取数据源类型
     * @return
     */
    public static List<SourceType> getDataSourceType(){
        List<SourceType> list=new ArrayList<SourceType>();
        list.add(new SourceType("JDBC",DeeResourceEnum.JDBCDATASOURCE.ordinal()+""));
        list.add(new SourceType("JNDI",DeeResourceEnum.JNDIDataSource.ordinal()+""));
        return list;
    }
    /**
     * 获取服务对象信息（暂时固定三种，没有对应的代码表）
     * @return
     */
    public static List<SourceType> getServiceObjectType(){
        List<SourceType> list=new ArrayList<SourceType>();
        list.add(new SourceType("NC","NC"));
        list.add(new SourceType("Portal","Portal"));
        list.add(new SourceType("Form","Form"));
        return list;
    }
    
	/**
	 * @description 解析XML中的字典配置项
	 * @date 2013-10-09
	 * @author dkywolf
	 * @param XML
	 * @return
	 * @throws Exception
	 */
    public static String getExprValue(String xml) {
		Document doc = null;
		StringBuffer retVals = new StringBuffer();
		try {
			doc = DocumentHelper.parseText(xml); // 将字符串转为XML
			Element rootElt = doc.getRootElement(); // 获取根节点
			List<Element> propertys = rootElt.elements("property");
			String tempStr = "";
			for(Element e:propertys){
				if(e.attribute("expr")!=null){
					tempStr = (String)e.attribute("expr").getValue();
					if(tempStr != null && tempStr.indexOf("dict") > -1){
						String tmpVal = tempStr.substring(6, tempStr.indexOf("')"));
						if(!"".equals(tmpVal))
							retVals.append(tmpVal+",");
					}
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return retVals.toString();
	}
    
	/**
	 * @description  将字符串（34341,1343214,1234,2412）改写成sql查询
	 *               需要的字符串形式('34341','1343214','1234','2412')
	 * @date 2012-3-28
	 * @author liuls
	 * @param ids
	 * @return
	 */
    public static String getSqlIds( String ids) {
		String newIdsStr = "";
		String[] idsArr = ids.split(",");
		Set<String> set = new HashSet<String>();
		for(String s:idsArr){
			if("".equals(s))
				continue;
			set.add(s);
		}
		if(set.size()>0){
			for(String id:set){
				newIdsStr+="'"+id+"'"+",";
			}
			if(newIdsStr.endsWith(",")){
				newIdsStr = newIdsStr.substring(0,newIdsStr.length()-1);
			}
		}
		return newIdsStr;
	}
	
    public static Map<String, String> getDictXMLToMap(String dictInfo) {
    	Map<String, String> map = null;
		if(dictInfo == null){
			return map;
		}
		try{
			map = new LinkedHashMap();
			String[] dictList;
			if(dictInfo.indexOf("\r\n") > -1)
				dictList = dictInfo.split("\r\n");
			else
				dictList = dictInfo.split("\n");
			for(String nDict:dictList){
				if(nDict != null && nDict.indexOf("=")>-1){
					nDict = nDict.substring(nDict.indexOf("=")+1);
					String[] nd = nDict.split(":");
					map.put(nd[0], nd[1]);
				}
			}
		}catch(Exception e){
			map = null;
		}
		return map;
	}
    public static String getDictMapToXML(String name,Map<String, String> map) {
		StringBuffer dictInfo = new StringBuffer();
		for (Entry<String, String> entry : map.entrySet()) {
			dictInfo.append(name).append(".").append(entry.getKey())
					.append("=").append(entry.getKey()).append(":")
					.append(entry.getValue()).append("\r\n");
		}
		return dictInfo.toString();
	}
   public static void main(String[] args){
    	String abc = SourceUtil.getExprValue("<column-mapping name=\"mapping2\"><property target=\"1\" expr=\"dict('12345')\" targetDis=\"\"  source=\"\"  sourceDis=\"\" desc=\"1\" /><property target=\"1\" expr=\"dict('张三')\" targetDis=\"\"  source=\"\"   sourceDis=\"\" desc=\"1\" /></column-mapping>");
		System.out.println(abc);
    }
}
