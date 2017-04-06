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

public class WSProcessorBean implements DeeResource {

	private final static Log log = LogFactory.getLog(WSProcessorBean.class);

	/** webservice的wsdl地址 */
	private String serviceurl;
	/** webservice命名空间 */
	private String namespace;
	/** 方法名称 */
	private String method;
	/** 返回值类型 */
	private String returnType;
	/** 用户名 */
	private String username;
	/** 密码 */
	private String password;
    /** 超时时间 */
    private String timeout;

    /** 参数集 */
	private Map<String, Object> parameter;

	public WSProcessorBean() {
	}

	public WSProcessorBean(String xml) {
		Document doc = null;
		try {
			doc = DocumentHelper.parseText(xml); // 将字符串转为XML
			Element rootElt = doc.getRootElement(); // 获取根节点
			List<Element> processorElement = rootElt.elements("property");
			for (Element e : processorElement) {
				if ("serviceurl".equals(e.attribute("name").getValue())) {
					serviceurl = e.attribute("value").getValue();
				} else if ("namespace".equals(e.attribute("name").getValue())) {
					namespace = e.attribute("value").getValue();
				} else if ("method".equals(e.attribute("name").getValue())) {
					method = e.attribute("value").getValue();
				} else if ("username".equals(e.attribute("name").getValue())) {
                    username = e.attribute("value").getValue();
                } else if ("password".equals(e.attribute("name").getValue())) {
                    password = e.attribute("value").getValue();
                } else if ("returnType".equals(e.attribute("name").getValue())) {
					returnType = e.attribute("value").getValue();
				} else if ("timeout".equals(e.attribute("name").getValue())) {
                    timeout = e.attribute("value").getValue();
                }
			}
			List<Element> maps = rootElt.element("map").elements("key");
			parameter = new LinkedHashMap<String, Object>();
			for (Element m : maps) {
				parameter.put(m.attributeValue("name"),
						m.attributeValue("value"));
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	@Override
	public String toXML() {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String toXML(String name) {
		StringBuffer sb = new StringBuffer();
		sb.append("<processor class=\"com.seeyon.v3x.dee.processor.WSProcessor\" name=\""
				+ name + "\"><description></description>");
		sb.append("<property name=\"serviceurl\" value=\"" + StringEscapeUtils.escapeXml(serviceurl)
				+ "\"/>");
		sb.append("<property name=\"namespace\" value=\"" + StringEscapeUtils.escapeXml(namespace) + "\"/>");
		sb.append("<property name=\"method\" value=\"" + StringEscapeUtils.escapeXml(method) + "\"/>");
		sb.append("<property name=\"username\" value=\"" + StringEscapeUtils.escapeXml(username) + "\"/>");
		sb.append("<property name=\"password\" value=\"" + StringEscapeUtils.escapeXml(password) + "\"/>");
		sb.append("<property name=\"returnType\" value=\"" + returnType
				+ "\"/>");
        sb.append("<property name=\"timeout\" value=\"" + StringEscapeUtils.escapeXml(timeout) + "\"/>");
		sb.append("<map name=\"parameter\">");
		for (Entry<String, Object> entry : parameter.entrySet()) {
			sb.append("<key name=\"" + StringEscapeUtils.escapeXml(entry.getKey()) + "\" value=\""
					+ StringEscapeUtils.escapeXml(entry.getValue().toString()) + "\"/>");
		}
		sb.append("</map>");
		sb.append("</processor>");
		return sb.toString();
	}

	/************************************/
	public String getServiceurl() {
		return serviceurl;
	}

	public void setServiceurl(String serviceurl) {
		this.serviceurl = serviceurl;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}
	
	public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

	public String getReturnType() {
		return returnType;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}

	public Map<String, Object> getParameter() {
		return parameter;
	}

	public void setParameter(Map<String, Object> parameter) {
		this.parameter = parameter;
	}

    public String getTimeout() {
        return timeout;
    }

    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }
}
