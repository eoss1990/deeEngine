package com.seeyon.v3x.dee.common.db.flow.util;

import com.seeyon.v3x.dee.common.db.resource.util.DeeResourceEnum;
import org.apache.commons.lang.StringUtils;

public class FlowSubSortTool {
	
	/**
	 * 用于管理所有adapter的顺序<br>
	 * 将重复的代码统一一下，有三处用到这的代码<br>
	 * TODO 较繁琐，待优化
	 * @author lilong
	 * @param resource_type adapter的类型
	 * @return 1=Reader，2=Processor，3=Writer
	 */
	public static int judgeResourceType(String resource_type) {
		int i = 0;
		if(StringUtils.isNotBlank(resource_type)) {
			if(resource_type.equals(""+DeeResourceEnum.JDBCREADER.ordinal())//JDBCReader
					|| resource_type.equals(""+DeeResourceEnum.XMLREADER.ordinal())//XMLReader
	    			|| resource_type.equals(""+DeeResourceEnum.ReaderScript.ordinal())
	    			|| resource_type.equals(""+DeeResourceEnum.CustomReader.ordinal())
	    			|| resource_type.equals(""+DeeResourceEnum.WSReader.ordinal())
	    			|| resource_type.equals(""+DeeResourceEnum.SAPJCOReader.ordinal())
	    			|| resource_type.equals(""+DeeResourceEnum.SAPWSReader.ordinal())) {
				i = 1;
			} else if(resource_type.equals(""+DeeResourceEnum.COLUMNMAPPINGPROCESSOR.ordinal())
					|| resource_type.equals(""+DeeResourceEnum.XSLTPROCESSOR.ordinal())
					|| resource_type.equals(""+DeeResourceEnum.A8WSGETTOKENPROCESSOR.ordinal())
					|| resource_type.equals(""+DeeResourceEnum.SCRIPTPROCESSOR.ordinal())
					|| resource_type.equals(""+DeeResourceEnum.XMLSchemaValidateProcessor.ordinal())
	    			|| resource_type.equals(""+DeeResourceEnum.ProcessorScript.ordinal())
	    			|| resource_type.equals(""+DeeResourceEnum.CustomProcessor.ordinal())
	    			|| resource_type.equals(""+DeeResourceEnum.SAPJCOProcessor.ordinal())
	    			|| resource_type.equals(""+DeeResourceEnum.WSProcessor.ordinal())) {
				i = 2;
			} else if(resource_type.equals(""+DeeResourceEnum.JDBCWRITER.ordinal())
					|| resource_type.equals(""+DeeResourceEnum.A8BPMLAUCHFORMCOLWRITER.ordinal())
					|| resource_type.equals(""+DeeResourceEnum.WriterScript.ordinal())
					|| resource_type.equals(""+DeeResourceEnum.CustomWriter.ordinal())
					|| resource_type.equals(""+DeeResourceEnum.A8CommonWSWriter.ordinal())
					|| resource_type.equals(""+DeeResourceEnum.WSWriter.ordinal())
					|| resource_type.equals(""+DeeResourceEnum.SAPJCOWriter.ordinal())
					|| resource_type.equals(""+DeeResourceEnum.SAPWSWriter.ordinal())
	    			|| resource_type.equals(""+DeeResourceEnum.A8MsgWriter.ordinal())) {
				i = 3;
			}
		}
		return i;
	}
}
