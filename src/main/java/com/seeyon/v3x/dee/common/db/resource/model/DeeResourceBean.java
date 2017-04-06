package com.seeyon.v3x.dee.common.db.resource.model;

import com.seeyon.v3x.dee.common.base.annotation.Column;
import com.seeyon.v3x.dee.common.db.flow.model.FlowSubBean;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;

/**
 * @author Zhang.Wei
 * @date Dec 27, 20112:46:04 PM
 * @Copyright(c) Beijing Seeyon Software Co.,LTD
 */
@Entity
@Table(name="dee_resource")
public class DeeResourceBean implements Serializable {

	/** resource_id */
	private String resource_id;

	/** 对应dee_resource_template.resource_template_id */
	private String resource_template_id;

	/** 对应dee_resource_template.resource_template_name */
	private String resource_template_name;
	
	private DeeResourceTemplateBean deeResourceTemplate;

	/** 数据源名称 */
	private String resource_name;

	/** 数据源配置内容（xml片段） */
	private String resource_code;

	/** 数据源描述 */
	private String resource_desc;

	/** 资源 */
	private DeeResource dr;

	/** 引用资源的id* */
	private String ref_id;

	/**
	 * 显示用中文名称
	 */
	private String dis_name;
	
	private FlowSubBean flowSub;
	
	/**
	 * 创建时间
	 */
	private String create_time;
	/**
	 * 扩展引用字典函数名
	 */
    private String func_name;
    
	public String getRef_id() {
		return ref_id;
	}

	@Column(name = "REF_ID")
	public void setRef_id(String ref_id) {
		this.ref_id = ref_id;
	}

	/**
	 * 获取dr
	 * 
	 * @return dr
	 */
	@Transient
	public DeeResource getDr() {
		return dr;
	}

	/**
	 * 设置dr
	 * 
	 * @param dr
	 *            dr
	 */
	@Column(name = "DR")
	public void setDr(DeeResource dr) {
		this.dr = dr;
	}

	/**
	 * 获取resource_id
	 * 
	 * @return resource_id
	 */
	@Id
	public String getResource_id() {
		return resource_id;
	}

	/**
	 * 设置resource_id
	 * 
	 * @param resource_id
	 *            resource_id
	 */
	@Column(name = "RESOURCE_ID")
	public void setResource_id(String resource_id) {
		this.resource_id = resource_id;
	}

	/**
	 * 获取resource_template_id
	 * 
	 * @return resource_template_id
	 */
	@Transient
	public String getResource_template_id() {
		return resource_template_id;
	}

	/**
	 * 设置resource_template_id
	 * 
	 * @param resource_template_id
	 */
	@Column(name = "RESOURCE_TEMPLATE_ID")
	public void setResource_template_id(String resource_template_id) {
		this.resource_template_id = resource_template_id;
	}

	/**
	 * 获取resource_name
	 * 
	 * @return resource_name
	 */
	public String getResource_name() {
		return resource_name;
	}

	/**
	 * 设置resource_name
	 * 
	 * @param resource_name
	 *            resource_name
	 */
	@Column(name = "RESOURCE_NAME")
	public void setResource_name(String resource_name) {
		this.resource_name = resource_name;
	}

	/**
	 * 获取resource_code
	 * 
	 * @return resource_code
	 */
	public String getResource_code() {
		return resource_code;
	}

	/**
	 * 设置resource_code
	 * 
	 * @param resource_code
	 *            resource_code
	 */
	@Column(name = "RESOURCE_CODE")
	public void setResource_code(String resource_code) {
		this.resource_code = resource_code;
	}

	/**
	 * 获取resoutce_desc
	 * 
	 * @return resoutce_desc
	 */
	public String getResource_desc() {
		return resource_desc;
	}

	/**
	 * 设置resoutce_desc
	 * 
	 * @param resoutce_desc
	 *            resoutce_desc
	 */
	@Column(name = "RESOURCE_DESC")
	public void setResource_desc(String resoutce_desc) {
		this.resource_desc = resoutce_desc;
	}

	/**
	 * 获取resource_template_name
	 * 
	 * @return resource_template_name
	 */
	@Transient
	public String getResource_template_name() {
		return resource_template_name;
	}

	/**
	 * 设置resource_template_name
	 * 
	 * @param resource_template_name
	 *            resource_template_name
	 */
	@Column(name = "RESOURCE_TEMPLATE_NAME")
	public void setResource_template_name(String resource_template_name) {
		this.resource_template_name = resource_template_name;
	}

	public String getDis_name() {
		return dis_name;
	}

	@Column(name = "DIS_NAME")
	public void setDis_name(String dis_name) {
		this.dis_name = dis_name;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="resource_template_id")
	public DeeResourceTemplateBean getDeeResourceTemplate() {
		return deeResourceTemplate;
	}

	public void setDeeResourceTemplate(DeeResourceTemplateBean deeResourceTemplate) {
		this.deeResourceTemplate = deeResourceTemplate;
	}

	@OneToOne(mappedBy="deeResource")
	public FlowSubBean getFlowSub() {
		return flowSub;
	}

	public void setFlowSub(FlowSubBean flowSub) {
		this.flowSub = flowSub;
	}

	@javax.persistence.Column(name="EXT1")
    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

	@javax.persistence.Column(name="EXT2")
	public String getFunc_name() {
		return func_name;
	}

	public void setFunc_name(String func_name) {
		this.func_name = func_name;
	}

}
