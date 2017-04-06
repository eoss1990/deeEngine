package com.seeyon.v3x.dee.common.db.mapping.model;

import com.seeyon.v3x.dee.common.db.resource.model.DeeResource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


public class MappingResourceBean  implements DeeResource {
	private final static Log log = LogFactory.getLog(MappingResourceBean.class);
	private String name;
	private String[]sourceColumnName; // 源字段英文名
	private String[]sourceColumnDisplay; //源字段中文名
	private String[]sourceTableName; //源表英文名
	private String[]sourceTableDisplay; //源表中文名
	private String[]targetColumnName; //目标字段英文字
	private String[]targetColumnDisplay;//目标字段中文名
	private String[]targetTableName; //目标表英文名
	private String[]targetTableDisplay; //目标表中文名
	private String[]targetColumnInfo; //目标表中文名

	private String[]mapping;
	private String[]desc;
	private String chk;
	private static final String splitStr = "/";
	
	public MappingResourceBean(){
		
	}
	@SuppressWarnings("unchecked")
	public MappingResourceBean(String xml) {
		Document doc = null;
		try {
			doc = DocumentHelper.parseText(xml); // 将字符串转为XML
			Element rootElt = doc.getRootElement(); // 获取根节点
			String name = rootElt.attributeValue("name");
			List<Element> propertys = rootElt.elements("property");
			String temp = "";
			int columnCount = 0;
			for(Element e:propertys){ //获取有效的字段数
				temp = (String)e.attributeValue("target");
				if(temp==null||temp.indexOf(splitStr)<0);//如果没有目标属性，或者目标属性没有“/”,只有表名无字段名，就不计数
				else{ 
					columnCount++;
				}
			}
			int i = 0;
			this.sourceColumnName = new String[columnCount];
			this.sourceColumnDisplay = new String[columnCount];
			this.sourceTableName = new String[columnCount];
			this.sourceTableDisplay = new String[columnCount];
			this.targetColumnName = new String[columnCount];
			this.targetColumnDisplay = new String[columnCount];
			this.targetTableName = new String[columnCount];
			this.targetTableDisplay = new String[columnCount];
			this.targetColumnInfo = new String[columnCount];
			this.mapping = new String[columnCount];
			this.desc = new String[columnCount];
			String tmepArr[] = new String[2];
			for(Element e:propertys){
				temp = (String)e.attributeValue("target");
				if(temp==null||temp.indexOf(splitStr)<0) { //如果没有目标属性，或者目标属性没有“/”,只有表名无字段名，就不解析
					continue;
				}
				temp = (String)e.attributeValue("source");
				if(temp!=null&&temp.indexOf(splitStr)>0){ // 解析 source节点，按“/”拆分，分别存到列数组和表数组
					tmepArr = temp.split(splitStr);
					this.sourceTableName[i] =  tmepArr[0];
					if(tmepArr.length==1){
						this.sourceColumnName[i] = "";
					}else if(tmepArr.length==2){
						this.sourceColumnName[i] = tmepArr[1];
					}
				}
				temp = (String)e.attributeValue("sourceDis");
				if(temp!=null&&temp.indexOf(splitStr)>0){ // 解析 target节点，按“/”拆分，分别存到列数组和表数组
					tmepArr = temp.split(splitStr);
					this.sourceTableDisplay[i] =  tmepArr[0];
					if(tmepArr.length==1){
						this.sourceColumnDisplay[i] = "";
					}else if(tmepArr.length==2){
						this.sourceColumnDisplay[i] = tmepArr[1];
					}
				}
				temp = (String)e.attributeValue("colInfo");
				String targetEnField = "";
				if(temp!=null&&!"".equals(temp)){ // 解析 colInfo节点，存储字段信息串
					this.targetColumnInfo[i] =  temp;
					String[] colInf = temp.split("#");
					targetEnField = colInf[0];
				}
				temp = (String)e.attributeValue("target");
				if(temp!=null&&temp.indexOf(splitStr)>0){ // 解析 target节点，按“/”拆分，分别存到列数组和表数组
					tmepArr = temp.split(splitStr);
					this.targetTableName[i] =  tmepArr[0];
					if(tmepArr.length==1){
						this.targetColumnName[i] = "";
					}else if(tmepArr.length==2){
						this.targetColumnName[i] = targetEnField;
						if(!"".equals(targetEnField) && !targetEnField.equals(tmepArr[1])){
							this.chk = "CHN";
						}
					}
				}
				temp = (String)e.attributeValue("targetDis");
				if(temp!=null&&temp.indexOf(splitStr)>0){ // 解析 target节点，按“/”拆分，分别存到列数组和表数组
					tmepArr = temp.split(splitStr);
					this.targetTableDisplay[i] =  tmepArr[0];
					if(tmepArr.length==1){
						this.targetColumnDisplay[i] = "";
					}else if(tmepArr.length==2){
						this.targetColumnDisplay[i] = tmepArr[1];
					}
				}

				if(e.attribute("expr")!=null){
					this.mapping[i] = (String)e.attribute("expr").getValue();
				}
				if(e.attribute("desc")!=null){
					this.desc[i] = (String)e.attribute("desc").getValue();
				}
				i++;
			}
			this.name = name;
			log.debug(this);
			//<column-mapping name="mapping2"><property target="1" targetDis expr="1" targetDis=""  sourceDis="" desc="1" /><property target="" source="" targetDis=""  sourceDis="" desc="" /></column-mapping>
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
	
	public String[] getMapping() {
		return mapping;
	}
	public void setMapping(String[] mapping) {
		this.mapping = mapping;
	}
	public String[] getDesc() {
		return desc;
	}
	public void setDesc(String[] desc) {
		this.desc = desc;
	}
	

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String[] getSourceColumnName() {
		return sourceColumnName;
	}
	public void setSourceColumnName(String[] sourceColumnName) {
		this.sourceColumnName = sourceColumnName;
	}
	public String[] getSourceColumnDisplay() {
		return sourceColumnDisplay;
	}
	public void setSourceColumnDisplay(String[] sourceColumnDisplay) {
		this.sourceColumnDisplay = sourceColumnDisplay;
	}
	public String[] getSourceTableName() {
		return sourceTableName;
	}
	public void setSourceTableName(String[] sourceTableName) {
		this.sourceTableName = sourceTableName;
	}
	public String[] getSourceTableDisplay() {
		return sourceTableDisplay;
	}
	public void setSourceTableDisplay(String[] sourceTableDisplay) {
		this.sourceTableDisplay = sourceTableDisplay;
	}
	public String[] getTargetColumnName() {
		return targetColumnName;
	}
	public void setTargetColumnName(String[] targetColumnName) {
		this.targetColumnName = targetColumnName;
	}
	public String[] getTargetColumnDisplay() {
		return targetColumnDisplay;
	}
	public void setTargetColumnDisplay(String[] targetColumnDisplay) {
		this.targetColumnDisplay = targetColumnDisplay;
	}
	public String[] getTargetTableName() {
		return targetTableName;
	}
	public void setTargetTableName(String[] targetTableName) {
		this.targetTableName = targetTableName;
	}
	public String[] getTargetTableDisplay() {
		return targetTableDisplay;
	}
	public void setTargetTableDisplay(String[] targetTableDisplay) {
		this.targetTableDisplay = targetTableDisplay;
	}
	
	public String[] getTargetColumnInfo() {
		return targetColumnInfo;
	}
	public void setTargetColumnInfo(String[] targetColumnInfo) {
		this.targetColumnInfo = targetColumnInfo;
	}
	/* (non-Javadoc)
	 * @see com.seeyon.v3x.dee.configurator.resource.model.DeeResource#toXML()
	 */
	@SuppressWarnings("unchecked")
	public String toXML() {
		if(targetColumnName==null){ //如果目标没有值，就不要进行拼接
			return null;
		}
		StringBuffer midStr = new StringBuffer("");
		Map<String,String> map = new LinkedHashMap<String,String>(); //存储表名以便填写影射
		for(int i=0;i<targetColumnName.length;i++){
			//如果目标表或者字段没有值，不进行拼装
			if(noValue(targetTableName[i])||noValue(targetColumnName[i])){ 
				continue;
			}
			//如果源表名或源表字段不存在，并且影射也不存在，不拼装
			if((noValue(sourceTableName[i])||noValue(sourceColumnName[i]))&&(mapping[i]==null||"".equals(mapping[i].trim()))){
				continue;
			}
			//增加目标字段换为中文名字段（支持A8表单）
			if("CHN".equals(this.chk) && !noValue(targetColumnDisplay[i])){
				midStr.append("<property target=\""+targetTableName[i].trim()+splitStr+targetColumnDisplay[i].trim()+"\" ");
			}
			else{
				midStr.append("<property target=\""+targetTableName[i].trim()+splitStr+targetColumnName[i].trim()+"\" ");
			}
			map.put(targetTableName[i], sourceTableName[i]);
			midStr.append(" source=\""+sourceTableName[i].trim()+splitStr+sourceColumnName[i].trim()+"\" ");
			if(!noValue(mapping[i])){//源表如果没有进行影射配置
				midStr.append(" expr =\""+mapping[i]+"\"");
			}
			if(noValue(targetTableDisplay[i]) || noValue(targetColumnDisplay[i]))
				midStr.append(" targetDis=\""+targetTableName[i]+splitStr+targetColumnName[i]+"\" ");
			else
				midStr.append(" targetDis=\""+targetTableDisplay[i]+splitStr+targetColumnDisplay[i]+"\" ");
			if(!noValue(targetColumnInfo[i]))
				midStr.append(" colInfo=\""+targetColumnInfo[i]+"\" ");
			if(noValue(sourceTableDisplay[i]) || noValue(sourceColumnDisplay[i]))
				midStr.append(" sourceDis=\""+sourceTableName[i]+splitStr+sourceColumnName[i]+"\" ");
			else
				midStr.append(" sourceDis=\""+sourceTableDisplay[i]+splitStr+sourceColumnDisplay[i]+"\" ");
			midStr.append(" desc=\""+desc[i].trim()+"\" />");
			
		}
		StringBuffer stringXML = new StringBuffer("<column-mapping name=\""+name+"\">");
		Set<Entry<String, String>> set = map.entrySet();
		Iterator it = set.iterator();
		while(it.hasNext()){
			 Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
			 stringXML.append("<property target=\""+entry.getKey() +"\" source=\""+entry.getValue()+"\" /> ");
		}
		stringXML.append(midStr);
		stringXML.append("</column-mapping>");
        return stringXML.toString();
	}
	public boolean noValue(String str){
		if(str==null||"".equals(str)){
			return true;
		}else{
			return false;
		}
	}

	public String toXML(String name) {
		this.name = name;
		return toXML();
	}
	public String getChk() {
		return chk;
	}
	public void setChk(String chk) {
		this.chk = chk;
	}
	
}
