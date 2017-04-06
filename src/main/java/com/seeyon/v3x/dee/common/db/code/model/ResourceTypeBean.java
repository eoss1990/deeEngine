package com.seeyon.v3x.dee.common.db.code.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name="dee_cod_resourcetype")
public class ResourceTypeBean implements Serializable {
	private String type_id;
	private String type_name;
	
	@Id
	public String getType_id() {
		return type_id;
	}
	public void setType_id(String type_id) {
		this.type_id = type_id;
	}
	public String getType_name() {
		return type_name;
	}
	public void setType_name(String type_name) {
		this.type_name = type_name;
	}
	
}
