package com.seeyon.v3x.dee.common.db.redo.model;

import com.seeyon.v3x.dee.common.db.flow.model.FlowBean;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;

@Entity
@Table(name="dee_sync_history")
public class SyncBean implements Serializable {

	private String flow_dis_name;
	private String sync_id;
	private String sender_name;
	private String target_name;
	private int sync_mode;
	private int sync_state;
	private String sync_time;
	private String flow_id;
	private FlowBean flow;
	
	@Transient
	public String getFlow_dis_name() {
		return flow_dis_name;
	}
	public void setFlow_dis_name(String flow_dis_name) {
		this.flow_dis_name = flow_dis_name;
	}
	@Id
	public String getSync_id() {
		return sync_id;
	}
	public void setSync_id(String sync_id) {
		this.sync_id = sync_id;
	}
	public String getSender_name() {
		return sender_name;
	}
	public void setSender_name(String sender_name) {
		this.sender_name = sender_name;
	}
	public String getTarget_name() {
		return target_name;
	}
	public void setTarget_name(String target_name) {
		this.target_name = target_name;
	}
	public int getSync_mode() {
		return sync_mode;
	}
	public void setSync_mode(int sync_mode) {
		this.sync_mode = sync_mode;
	}
	public int getSync_state() {
		return sync_state;
	}
	public void setSync_state(int sync_state) {
		this.sync_state = sync_state;
	}
	public String getSync_time() {
		return sync_time;
	}
	public void setSync_time(String sync_time) {
		this.sync_time = sync_time;
	}
	@Transient
	public String getFlow_id() {
		return flow_id;
	}
	public void setFlow_id(String flow_id) {
		this.flow_id = flow_id;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="flow_id")
    public FlowBean getFlow() {
        return flow;
    }
    public void setFlow(FlowBean flow) {
        this.flow = flow;
    }
}
