package com.seeyon.v3x.dee.bean;

import com.seeyon.v3x.dee.common.db.resource.model.DeeResource;

public class SysFunctionBean implements DeeResource {

	private String resource_para;
	
	public SysFunctionBean(){}
	public SysFunctionBean(String xml){
		this.resource_para = xml==null?"":xml;
	}
	@Override
	public String toXML() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toXML(String name) {
		// TODO Auto-generated method stub
		return this.resource_para == null?"":this.resource_para;
	}


	public String getResource_para() {
		return resource_para;
	}

	public void setResource_para(String resource_para) {
		this.resource_para = resource_para;
	}
	
	
}
