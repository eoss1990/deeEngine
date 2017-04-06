package com.seeyon.v3x.dee.bean;

import com.seeyon.v3x.dee.common.db.resource.model.DeeResource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.List;
import java.util.Map;

public class OrgSyncWriterBean implements DeeResource {

	private final static Log log = LogFactory.getLog(OrgSyncWriterBean.class);
    /** 接口名称 */
    private String interfaceName;

    /** xmlns */
    private String xmlns;

    /** 方法名称 */
    private String methodName;

    /** 单位名称 */
    private String accountName;

    /** a8WS登录名 */
    private String userName;

    /** 密码 */
    private String password;

    /** url */
    private String a8url;
    
	private Map<String, String> paraMap; // 方法内参数集
    
    public OrgSyncWriterBean(){}
    public OrgSyncWriterBean(String xml){
        Document doc = null;
        try {
            doc = DocumentHelper.parseText(xml); // 将字符串转为XML
            Element rootElt = doc.getRootElement(); // 获取根节点
            List<Element> adapterElement = rootElt.elements("property");
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
                }else if("accountName".equals(e.attribute("name").getValue())) {
                	accountName= e.attribute("value").getValue();
                }
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
		// 生成OrgSyncWriter时。
        StringBuffer sb = new StringBuffer("");
        sb.append("<adapter class=\"com.seeyon.v3x.dee.extend.OrgSyncWriter\" name=\""+name+"\"><description></description>")
        	.append("<property name=\"userName\" value=\"" + userName + "\"/>")
			.append("<property name=\"password\" value=\"" + password + "\"/>")
			.append("<property name=\"a8url\" value=\"" + a8url + "\"/>")
			.append("<property name=\"interfaceName\" value=\"" + interfaceName + "\"/>")
			.append("<property name=\"xmlns\" value=\"" + xmlns + "\"/>")
			.append("<property name=\"methodName\" value=\"" + methodName + "\" />")
			.append("<property name=\"accountName\" value=\"" + accountName + "\" />");
        sb.append("</adapter>");
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
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
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

}
