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

/**
 * Configuration Rest Bean
 *
 * @author zhangfb
 */
public class RestProcessorBean implements DeeResource {
    private final static Log log = LogFactory.getLog(RestProcessorBean.class);

    /**
     * 权限校验的用户名
     */
    private String adminUserName;

    /**
     * 权限校验的密码
     */
    private String adminPassword;

    /**
     * 地址，如：http://localhost
     */
    private String address;

    /**
     * 服务ID
     */
    private Integer serviceId;

    /**
     * 服务下的方法ID
     */
    private Integer functionId;

    /**
     * 返回值类型
     */
    private String responseType;

    /**
     * 返回值名称
     */
    private String responseName;

    /**
     * 是否合并到Document，true：合并，false：不合并
     */
    private String mergeToDocument;

    /**
     * 参数列表，参数名称+参数值
     */
    private Map<String, String> paramMap;
    /**
     * 参数列表，表名+外键
     */
    private Map<String, String> keyMap;

    public RestProcessorBean() {
    }

    public RestProcessorBean(String xml) {
        try {
            // 将字符串转为XML
            Document doc = DocumentHelper.parseText(xml);
            // 获取根节点
            Element rootElement = doc.getRootElement();
            List<Element> processorElement = rootElement.elements("property");
            for (Element e : processorElement) {
                if ("adminUserName".equals(e.attribute("name").getValue())) {
                    adminUserName = e.attribute("value").getValue();
                } else if ("adminPassword".equals(e.attribute("name").getValue())) {
                    adminPassword = e.attribute("value").getValue();
                } else if ("address".equals(e.attribute("name").getValue())) {
                    address = e.attribute("value").getValue();
                } else if ("serviceId".equals(e.attribute("name").getValue())) {
                    serviceId = Integer.parseInt(e.attribute("value").getValue());
                } else if ("functionId".equals(e.attribute("name").getValue())) {
                    functionId = Integer.parseInt(e.attribute("value").getValue());
                } else if ("responseType".equals(e.attribute("name").getValue())) {
                    responseType = e.attribute("value").getValue();
                } else if ("responseName".equals(e.attribute("name").getValue())) {
                    responseName = e.attribute("value").getValue();
                } else if ("mergeToDocument".equals(e.attribute("name").getValue())) {
                    mergeToDocument = e.attribute("value").getValue();
                }
            }

            paramMap = new LinkedHashMap<String, String>();
            keyMap = new LinkedHashMap<String, String>();
//            List<Element> maps = rootElement.element("map").elements("key");
            List<Element> maps = rootElement.elements("map");
            for (Element m : maps) {
                if (m == null) continue;
                if ("paramMap".equalsIgnoreCase(m.attributeValue("name"))){
                    List<Element> keys = m.elements("key");
                    for (Element key : keys){
                        if (key == null) continue;
                        String name = key.attributeValue("name");
                        String value = key.attributeValue("value");
                        paramMap.put(name, value);
                    }
                }
                else if ("keyMap".equalsIgnoreCase(m.attributeValue("name"))){
                    List<Element> keys = m.elements("key");
                    for (Element key : keys){
                        if (key == null) continue;
                        String name = key.attributeValue("name");
                        String value = key.attributeValue("value");
                        keyMap.put(name, value);
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public String toXML(String name) {
        StringBuffer sb = new StringBuffer();

        sb.append("<processor class=\"com.seeyon.v3x.dee.extend.RestProcessor\" name=\"").append(name).append("\">");
        sb.append("<description></description>");
        sb.append("<property name=\"adminUserName\" value=\"").append(StringEscapeUtils.escapeXml(adminUserName)).append("\"/>");
        sb.append("<property name=\"adminPassword\" value=\"").append(StringEscapeUtils.escapeXml(adminPassword)).append("\"/>");
        sb.append("<property name=\"address\" value=\"").append(StringEscapeUtils.escapeXml(address)).append("\"/>");
        sb.append("<property name=\"serviceId\" value=\"").append(StringEscapeUtils.escapeXml(String.valueOf(serviceId))).append("\"/>");
        sb.append("<property name=\"functionId\" value=\"").append(StringEscapeUtils.escapeXml(String.valueOf(functionId))).append("\"/>");
        sb.append("<property name=\"responseType\" value=\"").append(StringEscapeUtils.escapeXml(responseType)).append("\"/>");
        sb.append("<property name=\"responseName\" value=\"").append(StringEscapeUtils.escapeXml(responseName)).append("\"/>");
        sb.append("<property name=\"mergeToDocument\" value=\"").append(StringEscapeUtils.escapeXml(mergeToDocument)).append("\"/>");
        sb.append("<map name=\"paramMap\">");
        for (Map.Entry<String, String> entry : paramMap.entrySet()) {
            sb.append("<key name=\"").append(StringEscapeUtils.escapeXml(entry.getKey())).append("\"");
            sb.append(" value=\"").append(StringEscapeUtils.escapeXml(entry.getValue())).append("\"/>");
        }
        sb.append("</map>");
        sb.append("<map name=\"keyMap\">");
        for (Map.Entry<String, String> entry : keyMap.entrySet()) {
            sb.append("<key name=\"").append(StringEscapeUtils.escapeXml(entry.getKey())).append("\"");
            sb.append(" value=\"").append(StringEscapeUtils.escapeXml(entry.getValue())).append("\"/>");
        }
        sb.append("</map>");
        sb.append("</processor>");

        return sb.toString();
    }

    @Override
    public String toXML() {
        return "";
    }

    public String getAdminUserName() {
        return adminUserName;
    }

    public void setAdminUserName(String adminUserName) {
        this.adminUserName = adminUserName;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public Integer getFunctionId() {
        return functionId;
    }

    public void setFunctionId(Integer functionId) {
        this.functionId = functionId;
    }

    public String getResponseType() {
        return responseType;
    }

    public void setResponseType(String responseType) {
        this.responseType = responseType;
    }

    public String getResponseName() {
        return responseName;
    }

    public void setResponseName(String responseName) {
        this.responseName = responseName;
    }

    public String getMergeToDocument() {
        return mergeToDocument;
    }

    public void setMergeToDocument(String mergeToDocument) {
        this.mergeToDocument = mergeToDocument;
    }

    public Map<String, String> getParamMap() {
        return paramMap;
    }

    public void setParamMap(Map<String, String> paramMap) {
        this.paramMap = paramMap;
    }

    public Map<String, String> getKeyMap() {
        return keyMap;
    }

    public void setKeyMap(Map<String, String> keyMap) {
        this.keyMap = keyMap;
    }
}
