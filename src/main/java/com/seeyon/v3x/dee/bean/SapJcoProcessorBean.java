package com.seeyon.v3x.dee.bean;

import com.seeyon.v3x.dee.common.db.resource.model.DeeResource;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class SapJcoProcessorBean implements DeeResource {

	private final static Log log = LogFactory.getLog(SapJcoProcessorBean.class);
    /** sap连接基本信息 */
	private String jco_ashost;
	private String jco_sysnr;
	private String jco_client;
	private String jco_user;
	private String jco_passwd;
	
    /** sap 方法名及输出参数 */
	private String func;
	private String in_param;
	private String out_param;
	
	private String pr_type;
	
    /** 参数map */
    private Map<String, String> map = new HashMap<String,String>();
    private Map<String, String> jcoReturnMap = new HashMap<String,String>();
    private Map<String, String> jcoStructureMap = new HashMap<String,String>();
    private Map<String, String> jcoTableMap = new HashMap<String,String>();
    
    
	public String getJco_ashost() {
		return jco_ashost;
	}

	public void setJco_ashost(String jco_ashost) {
		this.jco_ashost = jco_ashost;
	}

	public String getJco_sysnr() {
		return jco_sysnr;
	}

	public void setJco_sysnr(String jco_sysnr) {
		this.jco_sysnr = jco_sysnr;
	}

	public String getJco_client() {
		return jco_client;
	}

	public void setJco_client(String jco_client) {
		this.jco_client = jco_client;
	}

	public String getJco_user() {
		return jco_user;
	}

	public void setJco_user(String jco_user) {
		this.jco_user = jco_user;
	}

	public String getJco_passwd() {
		return jco_passwd;
	}

	public void setJco_passwd(String jco_passwd) {
		this.jco_passwd = jco_passwd;
	}

	public String getFunc() {
		return func;
	}

	public void setFunc(String func) {
		this.func = func;
	}

	public String getIn_param() {
		return in_param;
	}

	public void setIn_param(String in_param) {
		this.in_param = in_param;
	}

	public String getOut_param() {
		return out_param;
	}

	public void setOut_param(String out_param) {
		this.out_param = out_param;
	}

	public Map<String, String> getMap() {
		return map;
	}

	public void setMap(Map<String, String> map) {
		this.map = map;
	}

	public String getPr_type() {
		return pr_type;
	}

	public void setPr_type(String pr_type) {
		this.pr_type = pr_type;
	}
	
	public Map<String, String> getJcoReturnMap() {
		return jcoReturnMap;
	}

	public void setJcoReturnMap(Map<String, String> jcoReturnMap) {
		this.jcoReturnMap = jcoReturnMap;
	}
	
	public SapJcoProcessorBean(){}
	
	public SapJcoProcessorBean(String xml){
        Document doc = null;
        try {
            doc = DocumentHelper.parseText(xml); // 将字符串转为XML
            Element rootElt = doc.getRootElement(); // 获取根节点
            List<Element> processorElement = rootElt.elements("property");
            for(Element e:processorElement){
                if("jco_ashost".equals(e.attribute("name").getValue())) {
                	jco_ashost= e.attribute("value").getValue();
                }else if("jco_sysnr".equals(e.attribute("name").getValue())) {
                	jco_sysnr= e.attribute("value").getValue();
                }else if("jco_client".equals(e.attribute("name").getValue())) {
                	jco_client= e.attribute("value").getValue();
                }else if("jco_user".equals(e.attribute("name").getValue())) {
                	jco_user= e.attribute("value").getValue();
	            }else if("jco_passwd".equals(e.attribute("name").getValue())) {
	            	jco_passwd= e.attribute("value").getValue();
		        }else if("func".equals(e.attribute("name").getValue())) {
		        	func= e.attribute("value").getValue();
//		        }else if("in_param".equals(e.attribute("name").getValue())) {
//		        	in_param= e.attribute("value").getValue();
		        }else if("out_param".equals(e.attribute("name").getValue())) {
//		        	out_param= e.attribute("value").getValue();
		        	if(StringUtils.isNotBlank(e.attribute("name").getValue())||!e.attribute("name").getValue().equals("null"))
		        		jcoReturnMap.put(e.attribute("value").getValue(), "String");
		        }else if("pr_type".equals(e.attribute("name").getValue())) {
		        	pr_type= e.attribute("value").getValue();
		        }
            }
//            dataSource = dataSourceElement.attributeValue("ref");
            List<Element> paraMaps = rootElt.element("map").elements("key");
//            map = new LinkedHashMap<String, String>();
            for (Element paraMap : paraMaps) {
                map.put(paraMap.attributeValue("name"),
                		paraMap.attributeValue("value"));
            }
            if(map.isEmpty())
            	this.map = null;
            
            //获取jcoreturn数据
            if(rootElt.selectSingleNode("jcoreturnmap")!=null)
            {
	            List<Element> jcoReMaps = rootElt.element("jcoreturnmap").elements("key");
	            for (Element paraMap : jcoReMaps) {
	            	jcoReturnMap.put(paraMap.attributeValue("name"),
	                		paraMap.attributeValue("value"));
	            }
            }
            if(jcoReturnMap.isEmpty())
            	this.jcoReturnMap = null;
            
            //获取jcostructure数据
//            jcoStructureMap = new LinkedHashMap<String, String>();
            if(rootElt.selectSingleNode("jcostructuremap")!=null)
            {
	            List<Element> jcoStrucMaps = rootElt.element("jcostructuremap").elements("key");
	            for (Element paraMap : jcoStrucMaps) {
	            	jcoStructureMap.put(paraMap.attributeValue("name"),
	                		paraMap.attributeValue("value"));
	            }
            }
            if(jcoStructureMap.isEmpty())
            	this.jcoStructureMap = null;
            
            //获取jcotable数据
//            jcoTableMap = new LinkedHashMap<String, String>();
            if(rootElt.selectSingleNode("jcotablemap")!=null)
            {
	            List<Element> jcoTableMaps = rootElt.element("jcotablemap").elements("key");
	            for (Element paraMap : jcoTableMaps) {
	            	jcoTableMap.put(paraMap.attributeValue("name"),
	                		paraMap.attributeValue("value"));
	            }
            }
            if(jcoTableMap.isEmpty())
            	this.jcoTableMap = null;
            
        } catch (Exception e) {
			log.error(e.getMessage(), e);
        }
	}

	@Override
	public String toXML() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toXML(String name) {
		// TODO Auto-generated method stub
		// 生成WSCommonWriter时，会生成一条processor记录，将其放到一起。
        StringBuffer sb = new StringBuffer("");
        sb.append("<processor class=\"com.seeyon.v3x.dee.extend.SapJcoProcessor\" name=\""+name+"\"><description></description>")
			.append("<property name=\"jco_ashost\" value=\"" + StringEscapeUtils.escapeXml(jco_ashost) + "\"/>")
			.append("<property name=\"jco_sysnr\" value=\"" + StringEscapeUtils.escapeXml(jco_sysnr) + "\"/>")
			.append("<property name=\"jco_client\" value=\"" + StringEscapeUtils.escapeXml(jco_client) + "\" />")
			.append("<property name=\"jco_user\" value=\"" + StringEscapeUtils.escapeXml(jco_user) + "\"/>")
			.append("<property name=\"jco_passwd\" value=\"" + StringEscapeUtils.escapeXml(jco_passwd) + "\"/>")
			.append("<property name=\"func\" value=\"" + StringEscapeUtils.escapeXml(func) + "\"/>")
//			.append("<property name=\"in_param\" value=\"" + in_param + "\"/>")
//			.append("<property name=\"out_param\" value=\"" + StringEscapeUtils.escapeXml(out_param) + "\"/>")
			.append("<property name=\"pr_type\" value=\"" + StringEscapeUtils.escapeXml(pr_type) + "\"/>")
			.append("<map name=\"paraMap\">");
        if(map!=null)
        {
	        for(Entry<String, String> entry : map.entrySet()) {
	            sb.append("<key name=\"" + StringEscapeUtils.escapeXml(entry.getKey()) + "\" value=\"" + StringEscapeUtils.escapeXml(entry.getValue()) + "\"/>");
	        }
        }
//        sb.append("</map></processor>");
        sb.append("</map>");
        
        //添加jcoStructureMap
        sb.append("<jcostructuremap name=\"jcoStructureMap\">");
        if(jcoStructureMap!=null){        	
	        for(Entry<String, String> entry : jcoStructureMap.entrySet()) {
	            sb.append("<key name=\"" + StringEscapeUtils.escapeXml(entry.getKey()) + "\" value=\"" + StringEscapeUtils.escapeXml(entry.getValue()) + "\"/>");
	        }
        }
        sb.append("</jcostructuremap>");
        
        //添加jcoTableMap
        sb.append("<jcotablemap name=\"jcoTableMap\">");
        if(jcoTableMap!=null)
        {
	        for(Entry<String, String> entry : jcoTableMap.entrySet()) {
	            sb.append("<key name=\"" + StringEscapeUtils.escapeXml(entry.getKey()) + "\" value=\"" + StringEscapeUtils.escapeXml(entry.getValue()) + "\"/>");
	        }
        }
        sb.append("</jcotablemap>");
        
        //添加jcoReturnMap
        sb.append("<jcoreturnmap name=\"jcoReturnMap\">");
        if(jcoReturnMap!=null)
        {
	        for(Entry<String, String> entry : jcoReturnMap.entrySet()) {
	            sb.append("<key name=\"" + entry.getKey() + "\" value=\"" + entry.getValue() + "\"/>");
	        }
        }
        sb.append("</jcoreturnmap></processor>");
        return sb.toString();
	}

	public Map<String, String> getJcoStructureMap() {
		return jcoStructureMap;
	}

	public void setJcoStructureMap(Map<String, String> jcoStructureMap) {
		this.jcoStructureMap = jcoStructureMap;
	}

	public Map<String, String> getJcoTableMap() {
		return jcoTableMap;
	}

	public void setJcoTableMap(Map<String, String> jcoTableMap) {
		this.jcoTableMap = jcoTableMap;
	}

}
