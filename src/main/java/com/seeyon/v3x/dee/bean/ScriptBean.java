package com.seeyon.v3x.dee.bean;

import com.seeyon.v3x.dee.common.db.resource.model.DeeResource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class ScriptBean implements DeeResource {

	private final static Log log = LogFactory.getLog(ScriptBean.class);
	/**
	 * 脚本内容
	 */
	private String script;
	private String scriptType = "groovy";
	private String name;


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getScriptType() {
		return scriptType;
	}

	public void setScriptType(String scriptType) {
		this.scriptType = scriptType;
	}

	public ScriptBean() {
	}

	/**
	 * 例：<script><![CDATA[context.setAttribute(\"WSToken\",tokenStr); println tokenStr]]></script>
	 */
	public ScriptBean(String xml) {
		Document doc = null;
		try {
			doc = DocumentHelper.parseText(xml); // 将字符串转为XML
			Element rootElt = doc.getRootElement(); // 获取根节点
			this.name = rootElt.attributeValue("name");
			this.scriptType = rootElt.getQName().getName();
			this.script = rootElt.getText();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
//		int beginIndex = xml.indexOf("<script>");
//		int endIndex = xml.indexOf("</script>");
//		if(beginIndex>-1){
//			this.script = xml.substring(beginIndex + 17, endIndex - 3);
//		}else{
//			this.script = xml;
//			this.scriptType = "xml";
//		}
	}

	@Override
	public String toXML() {
		StringBuffer scriptInfo = new StringBuffer();
		if("xml".equals(scriptType)){
			scriptInfo.append(script);
		}else{
			scriptInfo.append("<script><![CDATA[");
			scriptInfo.append(script);
			scriptInfo.append("]]></script>");			
		}
		return scriptInfo.toString();
	}

	@Override
	public String toXML(String name) {
		StringBuffer scriptInfo = new StringBuffer();
		if("xml".equals(scriptType)){
			scriptInfo.append(script);
		}else{
			scriptInfo.append("<script name=\""+name+"\"><![CDATA[");
			scriptInfo.append(script);
			scriptInfo.append("]]></script>");			
		}
		return scriptInfo.toString();
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

}
