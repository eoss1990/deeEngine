package com.seeyon.v3x.dee.common.db.flow.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name="dee_flow_metadata")
public class FlowMetaDataBean implements Serializable {

	private static final long serialVersionUID = 8411051767553928589L;

	private String metadata_id;
	
	private FlowBean flow;
	
	private String metadata_code;

	@Id
	public String getMetadata_id() {
		return metadata_id;
	}

	public void setMetadata_id(String metadata_id) {
		this.metadata_id = metadata_id;
	}

	@OneToOne
	@JoinColumn(name="flow_id")
	public FlowBean getFlow() {
		return flow;
	}

	public void setFlow(FlowBean flow) {
		this.flow = flow;
	}

	public String getMetadata_code() {
		return metadata_code;
	}

	public void setMetadata_code(String metadata_code) {
		this.metadata_code = metadata_code;
	}
	
}
