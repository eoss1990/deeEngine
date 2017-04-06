package com.seeyon.v3x.dee.bean;

import com.seeyon.v3x.dee.common.db.resource.model.DeeResource;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;


/**
 * 静态枚举字典
 * 
 * @author lilong
 * @date 2012-02-18
 * 
 */
public class StaticDictBean implements DeeResource {

	private String name;// 字典名称
	private Map<String, String> map;// 键值对

	public StaticDictBean() {
	}

	public StaticDictBean(String dictInfo) {
		if(dictInfo == null){
			map = null;
			return;
		}
		try{
			map = new LinkedHashMap();
			String[] dictList;
			if(dictInfo.indexOf("\r\n") > -1)
				dictList = dictInfo.split("\r\n");
			else
				dictList = dictInfo.split("\n");
			for(String nDict:dictList){
				if(nDict != null && nDict.indexOf("=")>-1){
					nDict = nDict.substring(nDict.indexOf("=")+1);
					String[] nd = nDict.split(":");
					map.put(nd[0], nd[1]);
				}
			}
		}catch(Exception e){
			map = null;
		}
	}

	@Override
	public String toXML() {
		StringBuffer dictInfo = new StringBuffer();
		for (Entry<String, String> entry : this.map.entrySet()) {
			dictInfo.append(name).append(".").append(entry.getKey())
					.append("=").append(entry.getKey()).append(":")
					.append(entry.getValue()).append("\r\n");
		}
		return dictInfo.toString();
	}

	@Override
	public String toXML(String name) {
		return toXML();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, String> getMap() {
		return map;
	}

	public void setMap(Map<String, String> map) {
		this.map = map;
	}
	
	
}
