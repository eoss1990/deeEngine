package com.seeyon.v3x.dee.common.db.flow.model;

import com.seeyon.v3x.dee.common.base.annotation.Column;
import com.seeyon.v3x.dee.common.db.resource.model.DeeResourceBean;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;

@Entity
@Table(name="dee_flow_sub")
public class FlowSubBean implements Serializable {
	private String flow_sub_id;
	private String flow_id;
	private String resource_id;
	private FlowBean flow;
	private DeeResourceBean deeResource;
	private int sort;

	@Id
	public String getFlow_sub_id() {
		return flow_sub_id;
	}

	@Column(name="FLOW_SUB_ID")
	public void setFlow_sub_id(String flow_sub_id) {
		this.flow_sub_id = flow_sub_id;
	}

	@Transient
	public String getFlow_id() {
		return flow_id;
	}

	@Column(name="FLOW_ID")
	public void setFlow_id(String flow_id) {
		this.flow_id = flow_id;
	}

	@Transient
	public String getResource_id() {
		return resource_id;
	}

	@Column(name="RESOURCE_ID")
	public void setResource_id(String resource_id) {
		this.resource_id = resource_id;
	}

	public int getSort() {
		return sort;
	}

	@Column(name="SORT")
	public void setSort(int sort) {
		this.sort = sort;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="flow_id")
	public FlowBean getFlow() {
		return flow;
	}

	public void setFlow(FlowBean flow) {
		this.flow = flow;
	}

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="resource_id")
	public DeeResourceBean getDeeResource() {
		return deeResource;
	}

	public void setDeeResource(DeeResourceBean deeResource) {
		this.deeResource = deeResource;
	}

}
