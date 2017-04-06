package com.seeyon.v3x.dee.common.db.parameter.model;

import com.seeyon.v3x.dee.common.base.annotation.Column;
import com.seeyon.v3x.dee.common.db.flow.model.FlowBean;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;

@Entity
@Table(name="dee_flow_parameter")
public class ParameterBean implements Serializable {
	private String PARA_ID;
	private String FLOW_ID;
	private FlowBean flowBean;
	private String PARA_NAME;
	private String DIS_NAME;
	private String PARA_VALUE;
	private String PARA_DESC = "";
	@Id
	public String getPARA_ID() {
		return PARA_ID;
	}
	@Transient
	public String getFLOW_ID() {
		return FLOW_ID;
	}
	public String getPARA_NAME() {
		return PARA_NAME;
	}
	public String getPARA_VALUE() {
		return PARA_VALUE;
	}
	public String getPARA_DESC() {
		return PARA_DESC;
	}
	public String getDIS_NAME() {
		return DIS_NAME;
	}
	@Column(name="DIS_NAME")
	public void setDIS_NAME(String dIS_NAME) {
		DIS_NAME = dIS_NAME;
	}
	@Column(name="PARA_ID")
	public void setPARA_ID(String pARA_ID) {
		PARA_ID = pARA_ID;
	}
	@Column(name="FLOW_ID")	
	public void setFLOW_ID(String fLOW_ID) {
		FLOW_ID = fLOW_ID;
	}
	@Column(name="PARA_NAME")
	public void setPARA_NAME(String pARA_NAME) {
		PARA_NAME = pARA_NAME;
	}
	@Column(name="PARA_VALUE")
	public void setPARA_VALUE(String pARA_VALUE) {
		PARA_VALUE = pARA_VALUE;
	}
	@Column(name="PARA_DESC")
	public void setPARA_DESC(String pARA_DESC) {
		PARA_DESC = pARA_DESC;
	}
	@ManyToOne
	@JoinColumn(name="flow_id")
	public FlowBean getFlowBean() {
		return flowBean;
	}
	public void setFlowBean(FlowBean flowBean) {
		this.flowBean = flowBean;
	}

}
