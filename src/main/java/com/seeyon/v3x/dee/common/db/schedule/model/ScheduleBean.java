package com.seeyon.v3x.dee.common.db.schedule.model;

import com.seeyon.v3x.dee.common.base.annotation.Column;
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
@Table(name="dee_schedule")
public class ScheduleBean implements Serializable {
	private static final long serialVersionUID = 5454155825314635342L;
	private String schedule_id;
	private String schedule_name;
	private String schedule_desc;
	private String schedule_code;
	private String dis_name;
	private Boolean enable;
	private String quartz_code;
	private String flow_id;
	private String flow_name;
	private FlowBean flow;
	private String create_time;        // 创建时间
	public String getQuartz_code() {
		return quartz_code;
	}
	@Column(name = "QUARTZ_CODE")
	public void setQuartz_code(String quartz_code) {
		this.quartz_code = quartz_code;
	}
	@Transient
	public String getFlow_id() {
		return flow_id;
	}
	@Column(name = "FLOW_ID")
	public void setFlow_id(String flow_id) {
		this.flow_id = flow_id;
	}
	@Transient
	public String getFlow_name() {
		return flow_name;
	}
	@Column(name = "FLOW_NAME")
	public void setFlow_name(String flow_name) {
		this.flow_name = flow_name;
	}

	@javax.persistence.Column(name = "IS_ENABLE")
	public Boolean getEnable() {
		return enable;
	}

	@Column(name = "IS_ENABLE")
	public void setEnable(Boolean enable) {
		this.enable = enable;
	}

	public String getDis_name() {
		return dis_name;
	}

	@Column(name = "DIS_NAME")
	public void setDis_name(String dis_name) {
		this.dis_name = dis_name;
	}

	@Id
	public String getSchedule_id() {
		return schedule_id;
	}

	@Column(name = "SCHEDULE_ID")
	public void setSchedule_id(String schedule_id) {
		this.schedule_id = schedule_id;
	}

	public String getSchedule_name() {
		return schedule_name;
	}

	@Column(name = "SCHEDULE_NAME")
	public void setSchedule_name(String schedule_name) {
		this.schedule_name = schedule_name;
	}

	public String getSchedule_desc() {
		return schedule_desc;
	}

	@Column(name = "SCHEDULE_DESC")
	public void setSchedule_desc(String schedule_desc) {
		this.schedule_desc = schedule_desc;
	}

	public String getSchedule_code() {
		return schedule_code;
	}

	@Column(name = "SCHEDULE_CODE")
	public void setSchedule_code(String schedule_code) {
		this.schedule_code = schedule_code;
	}
//	@javax.persistence.Column(name="is_enable")
//	public boolean getEnable() {
//		return isEnable;
//	}
//	public void setEnable(boolean enable) {
//		this.isEnable = enable;
//	}
	@javax.persistence.Column(name="EXT1")
    public String getCreate_time() {
        return create_time;
    }
    public void setCreate_time(String create_time) {
        this.create_time = create_time;
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
