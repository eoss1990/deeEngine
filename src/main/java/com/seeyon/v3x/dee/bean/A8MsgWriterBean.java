package com.seeyon.v3x.dee.bean;

import com.seeyon.v3x.dee.common.db.resource.model.DeeResource;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class A8MsgWriterBean implements DeeResource {

	private final static Log log = LogFactory.getLog(A8MsgWriterBean.class);
	
    /** 接口名称 */
    private String interfaceName;

    /** xmlns */
    private String xmlns;

    /** 方法名称 */
    private String methodName;

    /** 参数map */
    private Map<String, String> map;

    /** a8WS登录名 */
    private String userName;

    /** 密码 */
    private String password;

    /** url */
    private String a8url;
    
    /** 消息内容 */
    private String msgContent;
   
	public A8MsgWriterBean(){
		
	}
	public A8MsgWriterBean(String xml){
        xml="<a8>"+xml+"</a8>";        
        Document doc = null;
        try {
            doc = DocumentHelper.parseText(xml); // 将字符串转为XML
            Element rootElt = doc.getRootElement(); // 获取根节点
            List<Element> adapterElement = rootElt.element("adapter").elements("property");
            for(Element e:adapterElement){
                if("interfaceName".equals(e.attribute("name").getValue())) {
                    interfaceName= e.attribute("value").getValue();
                }else if("xmlns".equals(e.attribute("name").getValue())) {
                    xmlns= e.attribute("value").getValue();
                }else if("methodName".equals(e.attribute("name").getValue())) {
                    methodName= e.attribute("value").getValue();
                }else if("a8url".equals(e.attribute("name").getValue())) {
                    a8url= e.attribute("value").getValue();
                }else if("userName".equals(e.attribute("name").getValue())) {
                    userName= e.attribute("value").getValue();
                }else if("password".equals(e.attribute("name").getValue())) {
                    password= e.attribute("value").getValue();
                }else if("msgContent".equals(e.attribute("name").getValue())) {
                	msgContent= e.attribute("value").getValue();
                }
            }
            List<Element> sqlIter = rootElt.element("adapter").element("map").elements("key");
            map = new LinkedHashMap<String, String>();
            for (Element sqlElement : sqlIter) {
                map.put(sqlElement.attributeValue("name"),
                        sqlElement.attributeValue("value"));
            }
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
		// 生成A8WSWriter时，会生成一条processor记录，将其放到一起。
        StringBuffer sb = new StringBuffer("");
        sb.append("<adapter class=\"com.seeyon.v3x.dee.extend.A8MsgWriter\" name=\""+name+"\"><description></description>")
        	.append("<property name=\"userName\" value=\"" + StringEscapeUtils.escapeXml(userName) + "\"/>")
			.append("<property name=\"password\" value=\"" + StringEscapeUtils.escapeXml(password) + "\"/>")
			.append("<property name=\"a8url\" value=\"" + StringEscapeUtils.escapeXml(a8url) + "\"/>")
			.append("<property name=\"interfaceName\" value=\"" + StringEscapeUtils.escapeXml(interfaceName) + "\"/>")
			.append("<property name=\"xmlns\" value=\"" + StringEscapeUtils.escapeXml(xmlns) + "\"/>")
			.append("<property name=\"msgContent\" value=\"" + StringEscapeUtils.escapeXml(msgContent) + "\"/>")
			.append("<property name=\"methodName\" value=\"" + StringEscapeUtils.escapeXml(methodName) + "\" /><map name=\"paraMap\">");
        for(Entry<String, String> entry : map.entrySet()) {
            sb.append("<key name=\"" + StringEscapeUtils.escapeXml(entry.getKey()) + "\" value=\"" + StringEscapeUtils.escapeXml(entry.getValue()) + "\"/>");
        }
        sb.append("</map></adapter>");
        return sb.toString();
	}
	public String getInterfaceName() {
		return interfaceName;
	}
	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}
	public String getXmlns() {
		return xmlns;
	}
	public void setXmlns(String xmlns) {
		this.xmlns = xmlns;
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public Map<String, String> getMap() {
		return map;
	}
	public void setMap(Map<String, String> map) {
		this.map = map;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getA8url() {
		return a8url;
	}
	public void setA8url(String a8url) {
		this.a8url = a8url;
	}
	public String getMsgContent() {
		return msgContent;
	}
	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}
}
