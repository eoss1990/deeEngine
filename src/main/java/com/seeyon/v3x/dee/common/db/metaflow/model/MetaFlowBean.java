package com.seeyon.v3x.dee.common.db.metaflow.model;

import com.seeyon.v3x.dee.common.base.annotation.Column;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 元数据存储实体
 * @author lilong
 * @date 2012-02-28
 *
 */
@Entity
@Table(name="dee_metaflow")
public class MetaFlowBean implements Serializable {

	private static final long serialVersionUID = 4673090970738985199L;
	/** METAFLOW_ID */
	private String metaflow_id;
	/** METAFLOW_NAME */
	private String metaflow_name;
	/** METAFLOW_CODE */
	private String metaflow_code;

	/********/

	@Id
	public String getMetaflow_id() {
		return metaflow_id;
	}

	@Column(name = "METAFLOW_ID")
	public void setMetaflow_id(String metaflow_id) {
		this.metaflow_id = metaflow_id;
	}

	public String getMetaflow_name() {
		return metaflow_name;
	}

	@Column(name = "METAFLOW_NAME")
	public void setMetaflow_name(String metaflow_name) {
		this.metaflow_name = metaflow_name;
	}

	public String getMetaflow_code() {
		return metaflow_code;
	}

	@Column(name = "METAFLOW_CODE")
	public void setMetaflow_code(String metaflow_code) {
		this.metaflow_code = metaflow_code;
	}

}
