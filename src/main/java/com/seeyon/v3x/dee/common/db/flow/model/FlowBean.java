package com.seeyon.v3x.dee.common.db.flow.model;

import com.seeyon.v3x.dee.common.base.annotation.Column;
import com.seeyon.v3x.dee.common.db.code.model.FlowTypeBean;
import com.seeyon.v3x.dee.common.db.schedule.model.ScheduleBean;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="dee_flow")
public class FlowBean implements Serializable {

	private static final long serialVersionUID = 5454155825314635342L;

	private String FLOW_DESC;
	private String FLOW_ID;
	private String FLOW_NAME;
	private String FLOW_TYPE_ID;
	private FlowTypeBean flowType;
	private String FLOW_TYPE_NAME;
	private String EXETYPE_ID;
	private String EXE_TYPE_NAME;
	private FlowExeTypeBean flowExeType;
	private String MODULE_IDS;
	private String MODULE_NAMES;
	private Set<ScheduleBean> schedules = new HashSet<ScheduleBean>();
	private String FLOW_META;
	private String DIS_NAME;
	private FlowMetaDataBean flowMetaData;
	private Set<FlowSubBean> flowSubs = new HashSet<FlowSubBean>();
	private String CREATE_TIME;    // 创建时间
	private String EXT1;           // listener 拓展字段用来保存监听器，用于保存listener的template_id
	private String EXT3;           // 拓展字段用来保存数据源名称
	private String EXT4;

	@Transient
	public String getMODULE_NAMES() {
		return MODULE_NAMES;
	}

	public void setMODULE_NAMES(String module_names) {
		MODULE_NAMES = module_names;
	}

	public String getMODULE_IDS() {
		return MODULE_IDS;
	}

	public String getFLOW_DESC() {
		return FLOW_DESC;
	}

	@Id
	public String getFLOW_ID() {
		return FLOW_ID;
	}

	public String getFLOW_NAME() {
		return FLOW_NAME;
	}

	@Transient
	public String getFLOW_TYPE_ID() {
		return FLOW_TYPE_ID;
	}

	@Transient
	public String getFLOW_TYPE_NAME() {
		return FLOW_TYPE_NAME;
	}

	public String getFLOW_META() {
		return FLOW_META;
	}

	@Transient
	public String getEXETYPE_ID() {
		return EXETYPE_ID;
	}

	@Column(name = "EXETYPE_ID")
	public void setEXETYPE_ID(String fLOW_EXE_TYPE) {
		EXETYPE_ID = fLOW_EXE_TYPE;
	}

	@Column(name = "FLOW_META")
	public void setFLOW_META(String fLOW_META) {
		FLOW_META = fLOW_META;
	}

	@Column(name = "FLOW_DESC")
	public void setFLOW_DESC(String flow_desc) {
		FLOW_DESC = flow_desc;
	}

	@Column(name = "FLOW_ID")
	public void setFLOW_ID(String flow_id) {
		FLOW_ID = flow_id;
	}

	@Column(name = "FLOW_NAME")
	public void setFLOW_NAME(String flow_name) {
		FLOW_NAME = flow_name;
	}

	@Column(name = "FLOW_TYPE_ID")
	public void setFLOW_TYPE_ID(String flow_type_id) {
		FLOW_TYPE_ID = flow_type_id;
	}

	@Column(name = "FLOW_TYPE_NAME")
	public void setFLOW_TYPE_NAME(String flow_type_name) {
		FLOW_TYPE_NAME = flow_type_name;
	}

	@Column(name = "MODULE_IDS")
	public void setMODULE_IDS(String mODULE_IDS) {
		MODULE_IDS = mODULE_IDS;
	}

	/**
	 * 获取dIS_NAME
	 * 
	 * @return dIS_NAME
	 */
	public String getDIS_NAME() {
		return DIS_NAME;
	}

	/**
	 * 设置dIS_NAME
	 * 
	 * @param dis_name
	 *            dIS_NAME
	 */
	@Column(name = "DIS_NAME")
	public void setDIS_NAME(String dis_name) {
		DIS_NAME = dis_name;
	}

	@Transient
	public String getEXE_TYPE_NAME() {
		return EXE_TYPE_NAME;
	}

	@Column(name = "EXE_TYPE_NAME")
	public void setEXE_TYPE_NAME(String eXE_TYPE_NAME) {
		EXE_TYPE_NAME = eXE_TYPE_NAME;
	}

	public String getEXT1() {
		return EXT1;
	}

	@Column(name = "EXT1")
	public void setEXT1(String eXT1) {
		EXT1 = eXT1;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="flow_type_id")
	public FlowTypeBean getFlowType() {
		return flowType;
	}

	public void setFlowType(FlowTypeBean flowType) {
		this.flowType = flowType;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="exetype_id")
	public FlowExeTypeBean getFlowExeType() {
		return flowExeType;
	}

	public void setFlowExeType(FlowExeTypeBean flowExeType) {
		this.flowExeType = flowExeType;
	}

	@javax.persistence.Column(name="EXT2")
    public String getCREATE_TIME() {
        return CREATE_TIME;
    }

    public void setCREATE_TIME(String cREATE_TIME) {
        CREATE_TIME = cREATE_TIME;
    }

    @Transient
    public String getEXT2() {
        return CREATE_TIME;
    }

    @Column(name = "EXT2")
    public void setEXT2(String EXT2) {
        CREATE_TIME = EXT2;
    }

    public String getEXT3() {
		return EXT3;
	}

	public void setEXT3(String eXT3) {
		EXT3 = eXT3;
	}
	
	public String getEXT4() {
		return EXT4;
	}
	
	@Column(name = "EXT4")
	public void setEXT4(String eXT4) {
		EXT4 = eXT4;
	}

	@OneToMany(mappedBy="flow", fetch=FetchType.LAZY)
    public Set<ScheduleBean> getSchedules() {
        return schedules;
    }

    public void setSchedules(Set<ScheduleBean> schedules) {
        this.schedules = schedules;
    }

    @OneToOne(mappedBy="flow")
    public FlowMetaDataBean getFlowMetaData() {
        return flowMetaData;
    }

    public void setFlowMetaData(FlowMetaDataBean flowMetaData) {
        this.flowMetaData = flowMetaData;
    }

    @OneToMany(mappedBy="flow", fetch=FetchType.LAZY)
    public Set<FlowSubBean> getFlowSubs() {
        return flowSubs;
    }

    public void setFlowSubs(Set<FlowSubBean> flowSubs) {
        this.flowSubs = flowSubs;
    }

}
