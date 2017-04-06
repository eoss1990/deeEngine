package com.seeyon.v3x.dee.bean;

import com.seeyon.v3x.dee.common.db.resource.model.DeeResource;
import org.apache.commons.lang.StringEscapeUtils;

import java.util.Map.Entry;

public class JDBCWriterBean extends JDBCAdapter implements DeeResource {
	public JDBCWriterBean() {
		super();
	}

	public JDBCWriterBean(String xml) {
		super(xml);
	}

	@Override
	public String toXML() {
		StringBuffer strb = new StringBuffer(
				"<adapter class=\"com.seeyon.v3x.dee.adapter.JDBCWriter\" name=\""
						+ super.getName() + "\"><description>"
						+ StringEscapeUtils.escapeXml(super.getDesc())
						+ "</description><property name=\"dataSource\" ref=\""
						+ super.getDataSource()
						+ "\"/><map name=\"targetIds\">");
		for (Entry<String, String> entry : super.getMap().entrySet()) {
			strb.append("<key name=\"" + StringEscapeUtils.escapeXml(entry.getKey()) + "\" value=\""
					+ StringEscapeUtils.escapeXml(entry.getValue()) + "\"/>");
		}
		strb.append("</map></adapter>");
		return strb.toString();
	}
	
	public String toXML(String name) {
		super.setName(name);
		return toXML();
	}

}
