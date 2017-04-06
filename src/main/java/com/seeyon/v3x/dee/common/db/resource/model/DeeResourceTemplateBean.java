package com.seeyon.v3x.dee.common.db.resource.model;

import com.seeyon.v3x.dee.common.base.annotation.Column;
import com.seeyon.v3x.dee.common.db.code.model.ResourceTypeBean;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;

/**
 * 
 * @author lilong
 *
 */
@Entity
@Table(name="dee_resource_template")
public class DeeResourceTemplateBean implements Serializable {

	private String resource_template_id;
	private String resource_template_name;
	private String type_id;
	private ResourceTypeBean resourceType;
	private String template;

	@Id
	public String getResource_template_id() {
		return resource_template_id;
	}

	@Column(name = "RESOURCE_TEMPLATE_ID")
	public void setResource_template_id(String resource_template_id) {
		this.resource_template_id = resource_template_id;
	}

	public String getResource_template_name() {
		return resource_template_name;
	}

	@Column(name = "RESOURCE_TEMPLATE_NAME")
	public void setResource_template_name(String resource_template_name) {
		this.resource_template_name = resource_template_name;
	}

	@Transient
	public String getType_id() {
		return type_id;
	}

	@Column(name = "TYPE_ID")
	public void setType_id(String type_id) {
		this.type_id = type_id;
	}

	public String getTemplate() {
		return template;
	}

	@Column(name = "TEMPLATE")
	public void setTemplate(String template) {
		this.template = template;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="type_id")
	public ResourceTypeBean getResourceType() {
		return resourceType;
	}

	public void setResourceType(ResourceTypeBean resourceType) {
		this.resourceType = resourceType;
	}
}
