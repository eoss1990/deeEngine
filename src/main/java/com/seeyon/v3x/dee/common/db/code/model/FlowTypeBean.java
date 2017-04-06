package com.seeyon.v3x.dee.common.db.code.model;

import com.seeyon.v3x.dee.common.base.annotation.Column;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name="dee_flow_type")
public class FlowTypeBean  implements Serializable {
	
	private static final long serialVersionUID = 5454155825314635342L;
	
	private String FLOW_TYPE_ID;
	private String FLOW_TYPE_NAME;
	private String PARENT_ID;
	private String FLOW_TYPE_DESC;
	private Integer FLOW_TYPE_ORDER;
	
	public String getFLOW_TYPE_DESC() {
		return FLOW_TYPE_DESC;
	}
	@Column(name="FLOW_TYPE_DESC")
	public void setFLOW_TYPE_DESC(String flow_type_desc) {
		FLOW_TYPE_DESC = flow_type_desc;
	}
	
	public Integer getFLOW_TYPE_ORDER() {
		return FLOW_TYPE_ORDER;
	}
	@Column(name="FLOW_TYPE_ORDER")
	public void setFLOW_TYPE_ORDER(Integer flow_type_order) {
		FLOW_TYPE_ORDER = flow_type_order;
	}
	
	@Id
	public String getFLOW_TYPE_ID() {
		return FLOW_TYPE_ID;
	}
	@Column(name="FLOW_TYPE_ID")
	public void setFLOW_TYPE_ID(String flow_type_id) {
		FLOW_TYPE_ID = flow_type_id;
	}
	public String getFLOW_TYPE_NAME() {
		return FLOW_TYPE_NAME;
	}
	@Column(name="FLOW_TYPE_NAME")
	public void setFLOW_TYPE_NAME(String flow_type_name) {
		FLOW_TYPE_NAME = flow_type_name;
	}
	public String getPARENT_ID() {
		return PARENT_ID;
	}
	@Column(name="PARENT_ID")
	public void setPARENT_ID(String parent_id) {
		PARENT_ID = parent_id;
	}
	
	

}
