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
 * 表单回填Bean
 *
 * @author zhangfb
 */
public class A8FormWriteBackWriterBean implements DeeResource {
    private final static Log log = LogFactory.getLog(A8FormWriteBackWriterBean.class);

    /**
     * 表单ID
     */
    private String formId;

    /**
     * 表单名称
     */
    private String formName;

    /**
     * 字段名和字段值
     */
    private Map<String, String> fieldMap;

    /**
     * 数据源ID
     */
    private String dataSource;

    public A8FormWriteBackWriterBean() {
    }

    public A8FormWriteBackWriterBean(String xml) {
        try {
            // 将字符串转为XML
            Document doc = DocumentHelper.parseText(xml);
            // 获取根节点
            Element rootElement = doc.getRootElement();
            List<Element> processorElement = rootElement.elements("property");
            for (Element e : processorElement) {
                if ("formId".equals(e.attribute("name").getValue())) {
                    formId = e.attribute("value").getValue();
                } else if ("formName".equals(e.attribute("name").getValue())) {
                    formName = e.attribute("value").getValue();
                } else if ("dataSource".equals(e.attribute("name").getValue())) {
                    dataSource = e.attribute("ref").getValue();
                }
            }

            fieldMap = new LinkedHashMap<String, String>();
            List<Element> maps = rootElement.element("map").elements("key");
            for (Element m : maps) {
                String name = m.attributeValue("name");
                String value = m.attributeValue("value");
                fieldMap.put(name, value);
            }
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public String toXML(String name) {
        StringBuffer sb = new StringBuffer();

        sb.append("<adapter class=\"com.seeyon.v3x.dee.extend.A8FormWriteWriter\" name=\"").append(name).append("\">");
        sb.append("<description></description>");
        sb.append("<property name=\"formId\" value=\"").append(StringEscapeUtils.escapeXml(formId)).append("\"/>");
        sb.append("<property name=\"formName\" value=\"").append(StringEscapeUtils.escapeXml(formName)).append("\"/>");
        sb.append("<property name=\"dataSource\" ref=\"").append(StringEscapeUtils.escapeXml(dataSource)).append("\"/>");
        sb.append("<map name=\"fieldMap\">");
        for (Map.Entry<String, String> entry : fieldMap.entrySet()) {
            sb.append("<key name=\"").append(StringEscapeUtils.escapeXml(entry.getKey())).append("\"");
            sb.append(" value=\"").append(StringEscapeUtils.escapeXml(entry.getValue())).append("\"/>");
        }
        sb.append("</map>");
        sb.append("</adapter>");

        return sb.toString();
    }

    @Override
    public String toXML() {
        return "";
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public Map<String, String> getFieldMap() {
        return fieldMap;
    }

    public void setFieldMap(Map<String, String> fieldMap) {
        this.fieldMap = fieldMap;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }
}
