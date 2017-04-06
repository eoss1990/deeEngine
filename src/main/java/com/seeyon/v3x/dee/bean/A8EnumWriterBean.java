package com.seeyon.v3x.dee.bean;

import com.seeyon.v3x.dee.common.base.annotation.Column;
import com.seeyon.v3x.dee.common.db.resource.model.DeeResource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.List;

/**
 * @author zhangfb
 */
public class A8EnumWriterBean implements DeeResource {

    private final static Log log = LogFactory.getLog(A8EnumWriterBean.class);

    /**
     * 接口名称
     */
    private String interfaceName;

    /**
     * xmlns
     */
    private String xmlns;

    /**
     * 方法名称
     */
    private String methodName;

    /**
     * a8WS登录名
     */
    private String userName;

    /**
     * 密码
     */
    private String password;

    /**
     * url
     */
    private String a8url;

    /**
     * 枚举ID列表，格式如下："单位ID:{id1,id2}" 或 "枚举类别ID:{id1,id2}"
     */
    private String enumId;

    /**
     * 枚举名称
     */
    private String enumName;

    /**
     * 数据源
     */
    private String dataSource;

    /**
     * 枚举数据
     */
    private String enumDataXml;

    public A8EnumWriterBean() {
    }

    public A8EnumWriterBean(String xml) {
        xml = "<a8>" + xml + "</a8>";
        try {
            Document doc = DocumentHelper.parseText(xml);        // 将字符串转为XML
            Element rootElt = doc.getRootElement();     // 获取根节点
            List<Element> adapterElement = rootElt.element("adapter").elements("property");
            for (Element e : adapterElement) {
                if ("interfaceName".equals(e.attribute("name").getValue())) {
                    interfaceName = e.attribute("value").getValue();
                } else if ("xmlns".equals(e.attribute("name").getValue())) {
                    xmlns = e.attribute("value").getValue();
                } else if ("methodName".equals(e.attribute("name").getValue())) {
                    methodName = e.attribute("value").getValue();
                } else if ("a8url".equals(e.attribute("name").getValue())) {
                    a8url = e.attribute("value").getValue();
                } else if ("userName".equals(e.attribute("name").getValue())) {
                    userName = e.attribute("value").getValue();
                } else if ("password".equals(e.attribute("name").getValue())) {
                    password = e.attribute("value").getValue();
                } else if ("enumId".equals(e.attribute("name").getValue())) {
                    enumId = e.attribute("value").getValue();
                } else if ("enumName".equals(e.attribute("name").getValue())) {
                    enumName = e.attribute("value").getValue();
                } else if ("dataSource".equals(e.attribute("name").getValue())) {
                    dataSource = e.attribute("ref").getValue();
                } else if ("enumDataXml".equals(e.attribute("name").getValue())) {
                    enumDataXml = e.attribute("value").getValue();
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 获取userName
     *
     * @return userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * 设置userName
     *
     * @param userName userName
     */
    @Column(name = "USERNAME")
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * 获取password
     *
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置password
     *
     * @param password password
     */
    @Column(name = "PASSWORD")
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 获取a8url
     *
     * @return a8url
     */
    public String getA8url() {
        return a8url;
    }

    /**
     * 设置a8url
     *
     * @param a8url a8url
     */
    @Column(name = "A8URL")
    public void setA8url(String a8url) {
        this.a8url = a8url;
    }

    @Override
    public String toXML() {
        return "";
    }

    public String toXML(String name) {
        StringBuffer sb = new StringBuffer("");
        sb.append("<adapter class=\"com.seeyon.v3x.dee.extend.A8EnumWriter\" name=\"" + name + "\"><description></description>")
                .append("<property name=\"userName\" value=\"" + userName + "\"/>")
                .append("<property name=\"password\" value=\"" + password + "\"/>")
                .append("<property name=\"a8url\" value=\"" + a8url + "\"/>")
                .append("<property name=\"interfaceName\" value=\"" + interfaceName + "\"/>")
                .append("<property name=\"xmlns\" value=\"" + xmlns + "\"/>")
                .append("<property name=\"methodName\" value=\"" + methodName + "\" />")
                .append("<property name=\"enumId\" value=\"" + enumId + "\" />")
                .append("<property name=\"enumName\" value=\"" + enumName + "\" />")
                .append("<property name=\"dataSource\" ref=\"" + dataSource + "\" />")
                .append("<property name=\"enumDataXml\" value=\"" + enumDataXml + "\" /></adapter>");
        return sb.toString();
    }

    /**
     * 获取interfaceName
     *
     * @return interfaceName
     */
    public String getInterfaceName() {
        return interfaceName;
    }

    /**
     * 设置interfaceName
     *
     * @param interfaceName interfaceName
     */
    @Column(name = "INTERFACENAME")
    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    /**
     * 获取xmlns
     *
     * @return xmlns
     */
    public String getXmlns() {
        return xmlns;
    }

    /**
     * 设置xmlns
     *
     * @param xmlns xmlns
     */
    @Column(name = "XMLNS")
    public void setXmlns(String xmlns) {
        this.xmlns = xmlns;
    }

    /**
     * 获取methodName
     *
     * @return methodName
     */
    public String getMethodName() {
        return methodName;
    }

    /**
     * 设置methodName
     *
     * @param methodName methodName
     */
    @Column(name = "METHODNAME")
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getEnumId() {
        return enumId;
    }

    public void setEnumId(String enumId) {
        this.enumId = enumId;
    }

    public String getEnumName() {
        return enumName;
    }

    public void setEnumName(String enumName) {
        this.enumName = enumName;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public String getEnumDataXml() {
        return enumDataXml;
    }

    public void setEnumDataXml(String enumDataXml) {
        this.enumDataXml = enumDataXml;
    }
}
