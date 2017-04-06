package com.seeyon.v3x.dee.bean;

import com.seeyon.v3x.dee.common.db.mapping.model.MappingResourceBean;
import com.seeyon.v3x.dee.common.db.resource.model.DeeResource;
import com.seeyon.v3x.dee.common.db.resource.model.DeeResourceBean;
import com.seeyon.v3x.dee.common.db.resource.model.DeeResourceTemplateBean;
import org.apache.commons.lang.StringUtils;

public class ConvertDeeResourceBean {
	private DeeResourceBean rBean;
	public ConvertDeeResourceBean(DeeResourceBean bean){
		rBean = bean;
	}
    /**
     * 获取dr
     * @return dr
     */
    public DeeResource getResource() {
    	if (StringUtils.isNotBlank(rBean.getResource_template_id())) {
            DeeResourceTemplateBean template = new DeeResourceTemplateBean();
            template.setResource_template_id(rBean.getResource_template_id());
            rBean.setDeeResourceTemplate(template);
        }
        int resource_template_id = Integer.parseInt(rBean.getDeeResourceTemplate().getResource_template_id());
        String code = rBean.getResource_code();

        // 根据resource_template_id判断转换类型
        switch (resource_template_id) {
            case 0:
                return new JDBCReaderBean(code);
            case 2:
                return new JDBCWriterBean(code);
            case 3:
                return new ColumnMappingProcessorBean(code);
            case 4:
                return new XSLTProcessorBean(code);
            case 5:
                return new JDBCResourceBean(code);
            case 6:
                return new MappingResourceBean(code);
            case 8:
                return new A8WSWriter(code);
            case 10:
                return new JNDIResourceBean(code);
            case 11:
                return new XMLSchemaValidateProcessorBean(
                        code);
            case 12:
                return new JDBCDictBean(code);
            case 13:
                return new StaticDictBean(code);
            case 14:
                return new ScriptBean(code);
            case 20:
                return new WSCommonWriter(code);
            case 21:
                return new WSProcessorBean(code);
            case 22:
                return new WSProcessorBean(code);
            case 23:
                return new WSProcessorBean(code);
            case 24:
                return new SyncListenerBean(code);
            case 25:
                return new SapJcoProcessorBean(code);
            case 27:
                return new SapJcoProcessorBean(code);
            case 28:
                return new OrgSyncWriterBean(code);
            case 29:
                return new SysFunctionBean(code);
            case 30:
                return new SapWSProcessorBean(code);
            case 32:
                return new SapWSProcessorBean(code);
            case 33:
                return new A8MetaDatasourceBean(code);
            case 34:
                return new A8EnumReaderBean(code);
            case 35:
                return new A8EnumWriterBean(code);
            case 36:
                return new RestProcessorBean(code);
            case 37:
                return new RestProcessorBean(code);
            case 38:
                return new A8MsgWriterBean(code);
            case 39:
                return new A8FormWriteBackWriterBean(code);
            case 40:
                return new SrProcessorBean(code);
            case 42:
                return new SrProcessorBean(code);
            default:
                return rBean.getDr();
        }
    }
	public void setDr(DeeResource dr) {
		rBean.setDr(dr);
	}
}
