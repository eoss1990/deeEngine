package com.seeyon.v3x.dee.common.db.download.model;

import com.seeyon.v3x.dee.common.base.annotation.Column;
import com.seeyon.v3x.dee.common.db.resource.model.DeeResourceBean;
import org.apache.commons.lang.StringEscapeUtils;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;

@Entity
@Table(name="dee_download")
public class DownloadBean  implements Serializable {
	private static final long serialVersionUID = 7987351973329636045L;
	private String DOWNLOAD_ID;
	private String RESOURCE_ID;
	private DeeResourceBean refResource;
	private String FILENEME;
	private String CONTENT;
	@Id
	public String getDOWNLOAD_ID() {
		return DOWNLOAD_ID;
	}
	@Transient
	public String getRESOURCE_ID() {
		return RESOURCE_ID;
	}
	public String getFILENEME() {
		return FILENEME;
	}
	public String getCONTENT() {
		return StringEscapeUtils.unescapeHtml(CONTENT);
	}
	@Column(name = "DOWNLOAD_ID")
	public void setDOWNLOAD_ID(String dOWNLOAD_ID) {
		DOWNLOAD_ID = dOWNLOAD_ID;
	}
	@Column(name = "RESOURCE_ID")
	public void setRESOURCE_ID(String rESOURCE_ID) {
		RESOURCE_ID = rESOURCE_ID;
	}
	@Column(name = "FILENEME")
	public void setFILENEME(String fILENEME) {
		FILENEME = fILENEME;
	}
	@Column(name = "CONTENT")
	public void setCONTENT(String cONTENT) {
		CONTENT = cONTENT;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="resource_id")
	public DeeResourceBean getRefResource() {
		return refResource;
	}
	public void setRefResource(DeeResourceBean refResource) {
		this.refResource = refResource;
	}
}
