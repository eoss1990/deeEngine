package com.seeyon.v3x.dee.bean;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.seeyon.v3x.dee.common.db.resource.model.DeeResource;

public class SrProcessorBean implements DeeResource {

	private final static Log log = LogFactory.getLog(WSProcessorBean.class);
	
	/**访问地址 */
	private String url;
	/**访问方式(POST、GET、PUT...)*/
	private String urlType;
	/**Content-type*/
	private String contentType;
	/**是否为A8接口*/
	private String isA8;
	/**Headers*/
	private Map<String, Object> headers;
	/**Body */
	private Map<String, Object> bodys;
	
	public SrProcessorBean(){
		
	}
	
	public SrProcessorBean(String xml){
		Document doc = null;
		try {
			doc = DocumentHelper.parseText(xml); // 将字符串转为XML
			Element rootElt = doc.getRootElement(); // 获取根节点
			List<Element> processorElement = rootElt.elements("property");
			for (Element e : processorElement) {
				if ("url".equals(e.attribute("name").getValue())) {
					url = e.attribute("value").getValue();
				} else if ("urlType".equals(e.attribute("name").getValue())) {
					urlType = e.attribute("value").getValue();
				} else if ("contentType".equals(e.attribute("name").getValue())) {
					contentType = e.attribute("value").getValue();
				} else if ("isA8".equals(e.attribute("name").getValue())) {
					isA8 = e.attribute("value").getValue();
                }
			}
			List<Element> mapElement = rootElt.elements("map");
			headers = new LinkedHashMap<String, Object>();
			bodys = new LinkedHashMap<String, Object>();
			for (Element e : mapElement) {
				if ("headers".equals(e.attribute("name").getValue())) {
					List<Element> keyElement = e.elements("key");
					for (Element ek : keyElement) {
						headers.put(ek.attributeValue("name"),ek.attributeValue("value"));
					}
				} else if ("bodys".equals(e.attribute("name").getValue())) {
					List<Element> keyElement = e.elements("key");
					for (Element ek : keyElement) {
						bodys.put(ek.attributeValue("name"),ek.attributeValue("value"));
					}
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
	
	@Override
	public String toXML() {
		// TODO Auto-generated urlType stub
		return null;
	}

	@Override
	public String toXML(String name) {
		StringBuffer sb = new StringBuffer();
		sb.append("<processor class=\"com.seeyon.v3x.dee.adapter.rest.SRProcessor\" name=\""
				+ name + "\"><description></description>");
		sb.append("<property name=\"url\" value=\"" + StringEscapeUtils.escapeXml(url)
				+ "\"/>");
		sb.append("<property name=\"urlType\" value=\"" + StringEscapeUtils.escapeXml(urlType) + "\"/>");
		sb.append("<property name=\"contentType\" value=\"" + StringEscapeUtils.escapeXml(contentType) + "\"/>");
		sb.append("<property name=\"isA8\" value=\"" + StringEscapeUtils.escapeXml(isA8) + "\"/>");
		sb.append("<map name=\"headers\">");
		for (Entry<String, Object> entry : headers.entrySet()) {
			sb.append("<key name=\"" + StringEscapeUtils.escapeXml(entry.getKey()) + "\" value=\""
					+ StringEscapeUtils.escapeXml(entry.getValue().toString()) + "\"/>");
		}
		sb.append("</map>");
		sb.append("<map name=\"bodys\">");
		for (Entry<String, Object> entry : bodys.entrySet()) {
			sb.append("<key name=\"" + StringEscapeUtils.escapeXml(entry.getKey()) + "\" value=\""
					+ StringEscapeUtils.escapeXml(entry.getValue().toString()) + "\"/>");
		}
		sb.append("</map>");
		sb.append("</processor>");
		return sb.toString();
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrlType() {
		return urlType;
	}

	public void setUrlType(String urlType) {
		this.urlType = urlType;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getIsA8() {
		return isA8;
	}

	public void setIsA8(String isA8) {
		this.isA8 = isA8;
	}

	public Map<String, Object> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, Object> headers) {
		this.headers = headers;
	}

	public Map<String, Object> getBodys() {
		return bodys;
	}

	public void setBodys(Map<String, Object> bodys) {
		this.bodys = bodys;
	}
}
