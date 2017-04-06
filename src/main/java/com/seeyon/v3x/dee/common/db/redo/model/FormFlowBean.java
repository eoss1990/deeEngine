package com.seeyon.v3x.dee.common.db.redo.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="form_flow_history")
public class FormFlowBean implements Serializable {
	private String flow_sync_id;
	private String form_flow_id;
	private String form_flow_name;
	private String operate_person;
	private String flow_action;
	public String getFlow_sync_id() {
		return flow_sync_id;
	}
	public void setFlow_sync_id(String flow_sync_id) {
		this.flow_sync_id = flow_sync_id;
	}
	public String getForm_flow_id() {
		return form_flow_id;
	}
	public void setForm_flow_id(String form_flow_id) {
		this.form_flow_id = form_flow_id;
	}
	public String getForm_flow_name() {
		return form_flow_name;
	}
	public void setForm_flow_name(String form_flow_name) {
		this.form_flow_name = form_flow_name;
	}
	public String getOperate_person() {
		return operate_person;
	}
	public void setOperate_person(String operate_person) {
		this.operate_person = operate_person;
	}
	public String getFlow_action() {
		return flow_action;
	}
	public void setFlow_action(String flow_action) {
		this.flow_action = flow_action;
	}
}
