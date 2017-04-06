package com.seeyon.v3x.dee.common.db.flow.model;

import com.seeyon.v3x.dee.common.base.annotation.Column;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;

/**
 * @description 对应表DEE_FLOW_EXETYPE
 */
@Entity
@Table(name="dee_flow_exetype")
public class FlowExeTypeBean implements Serializable {

	private String exetype_id;
	private String exetype_name;

	@Transient
	public String getId() {
		return exetype_id;
	}

	@Column(name = "EXETYPE_ID")
	public void setId(String exetype_id) {
		this.exetype_id = exetype_id;
	}

	@Transient
	public String getName() {
		return exetype_name;
	}

	@Column(name = "EXETYPE_NAME")
	public void setName(String exetype_name) {
		this.exetype_name = exetype_name;
	}
	
	@Id
	public String getExetype_id() {
		return exetype_id;
	}
	public void setExetype_id(String exetype_id) {
		this.exetype_id = exetype_id;
	}
	public String getExetype_name() {
		return exetype_name;
	}
	public void setExetype_name(String exetype_name) {
		this.exetype_name = exetype_name;
	}

}
