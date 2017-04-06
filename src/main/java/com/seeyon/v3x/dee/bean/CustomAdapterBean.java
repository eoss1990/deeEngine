package com.seeyon.v3x.dee.bean;

import com.seeyon.v3x.dee.common.db.resource.model.DeeResource;

public class CustomAdapterBean implements DeeResource {

	private String customXml;
	public String getCustomXml() {
		return customXml;
	}

	public void setCustomXml(String customXml) {
		this.customXml = customXml;
	}

	@Override
	public String toXML() {
		// TODO Auto-generated method stub
		return this.customXml;
	}

	@Override
	public String toXML(String name) {
		// TODO Auto-generated method stub
		return toXML();
	}

}
