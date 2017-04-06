package com.seeyon.v3x.dee.common.db.code.model;

import com.seeyon.v3x.dee.common.base.annotation.Column;

public class ResourceTemplateBean {
	private String resource_template_id;
	private String resource_template_name;
	private String type_id;
	private String template;

	public String getResource_template_id() {
		return resource_template_id;
	}
	@Column(name="RESOURCE_TEMPLATE_ID")
	public void setResource_template_id(String resource_template_id) {
		this.resource_template_id = resource_template_id;
	}

	public String getResource_template_name() {
		return resource_template_name;
	}
	@Column(name="RESOURCE_TEMPLATE_NAME")
	public void setResource_template_name(String resource_template_name) {
		this.resource_template_name = resource_template_name;
	}

	public String getType_id() {
		return type_id;
	}
	@Column(name="TYPE_ID")
	public void setType_id(String type_id) {
		this.type_id = type_id;
	}

	public String getTemplate() {
		return template;
	}
	@Column(name="TEMPLATE")
	public void setTemplate(String template) {
		this.template = template;
	}

}
