package com.seeyon.v3x.dee.common.importflow;

import com.seeyon.v3x.dee.Document;
import com.seeyon.v3x.dee.Document.Element;
import com.seeyon.v3x.dee.TransformException;
import com.seeyon.v3x.dee.adapter.database.JDBCWriter;
import com.seeyon.v3x.dee.common.base.dao.BaseDAO;
import com.seeyon.v3x.dee.common.db.codelib.dao.CodeLibDao;
import com.seeyon.v3x.dee.common.db.codelib.model.CodeLibBean;
import com.seeyon.v3x.dee.common.db.flow.dao.FlowDAO;
import com.seeyon.v3x.dee.common.db.resource.dao.DeeResourceDAO;
import com.seeyon.v3x.dee.common.db.resource.model.DeeResourceBean;
import com.seeyon.v3x.dee.common.db.resource.util.SourceUtil;
import com.seeyon.v3x.dee.datasource.JDBCDataSource;
import com.seeyon.v3x.dee.util.DocumentUtil;
import org.dom4j.DocumentException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class ImportFlow extends BaseDAO {
	private FlowDAO fd = new FlowDAO();
	private DeeResourceDAO drDao = new DeeResourceDAO();

	private CodeLibDao codeLibDao = new CodeLibDao();

	/**
	 * @description 执行reader
	 * @date 2012-3-28
	 * @author liuls
	 * @throws org.dom4j.DocumentException
	 * @throws com.seeyon.v3x.dee.TransformException
	 */
	public void doWriter(String xml) throws TransformException, DocumentException {
		JDBCWriter r = new JDBCWriter();
		Document doc = DocumentUtil.parse(xml);
		Element flowElement = doc.getRootElement().getChild("dee_flow");
		List<Element> flowValue = flowElement.getChildren();

		for (Element element : flowValue) {
			String flowid = (String) DocumentUtil.getElValByName(element, "FLOW_ID");
			fd.delFlowInfo(flowid);
		}

		//获取导入字典信息
		Element dictElement = doc.getRootElement().getChild("dee_resource");
		List<String> rsIds = new ArrayList<String>();
		if(dictElement != null){
			List<Element> dictValue = dictElement.getChildren();
			for (Element element : dictValue) {
				String templateId = (String) DocumentUtil.getElValByName(element, "RESOURCE_TEMPLATE_ID");
				if("13".equals(templateId) && saveChgDictInfo(element)){
					//rsIds存放已经作了合并的字典id
					String rsId = (String) DocumentUtil.getElValByName(element, "RESOURCE_ID");
					rsIds.add(rsId);
				}
			}
		}
		//移除已经合并的字典
		for(String id:rsIds){
			doc = DocumentUtil.remove(doc, "dee_resource", "RESOURCE_ID", id);
		}
		JDBCDataSource ds = super.getDs();
		r.setDataSource(ds);
		Map<String,String> m = new HashMap<String,String>();
		m.put("dee_download", "DOWNLOAD_ID");
		m.put("dee_flow", "FLOW_ID");
		m.put("dee_flow_parameter", "PARA_ID");
		m.put("dee_flow_sub", "FLOW_SUB_ID");
		m.put("dee_metaflow", "METAFLOW_ID");
		m.put("dee_resource", "RESOURCE_ID");
		m.put("dee_schedule", "SCHEDULE_ID");
		m.put("dee_flow_type", "FLOW_TYPE_ID");
		m.put("dee_flow_exetype", "EXETYPE_ID");
		m.put("dee_flow_module", "MODULE_ID");
		m.put("dee_cod_resourcetype", "TYPE_ID");
		m.put("dee_flow_exetype", "EXETYPE_ID");
		m.put("dee_resource_template", "RESOURCE_TEMPLATE_ID");

		adaptCodeLib(doc);
		m.put("dee_code_lib", "id");
		m.put("dee_code_pkg", "name");

		r.setTargetIds(m);
		r.execute(doc);

	}

	/**
	 * @description 根据静态字典引用名合并字典内容
	 * @date 2013-10-11
	 * @author dkywolf
	 * @param element
	 * @return
	 * @throws com.seeyon.v3x.dee.TransformException
	 */
	private boolean saveChgDictInfo(Element element){
		if(element == null)
			return false;
		String disName = (String) DocumentUtil.getElValByName(element, "DIS_NAME");
		String sSql = " where resource_template_id='13' and dis_name='"+disName+"'";
		try {
			List<DeeResourceBean> drList = drDao.find(sSql);
			if(drList != null && drList.size() == 1){
				DeeResourceBean dr = drList.get(0);
				Map<String, String> mapDB = SourceUtil.getDictXMLToMap(dr.getResource_code());
				String rsCode = (String) DocumentUtil.getElValByName(element, "RESOURCE_CODE");
				Map<String, String> inputMap = SourceUtil.getDictXMLToMap(rsCode);
				//合并字典
				for (Entry<String, String> entry : inputMap.entrySet()) {
					mapDB.put(entry.getKey(), entry.getValue());
				}
				dr.setResource_code(SourceUtil.getDictMapToXML(disName, mapDB));
				drDao.updateResourceCode(dr);
				return true;
			}
		} catch (TransformException e) {
			// TODO Auto-generated catch block
			return false;
		}
		return false;
	}

	/**
	 * 为适配代码库的导入，转换document，需要做两件事：<br/>
	 * 1、如果同包名下存在相同的类，修改导入document中的类ID为实际库中的类ID；<br/>
	 * 2、在根节点下生成包名节点
	 *
	 * @param document 待适配的document
	 * @throws TransformException
	 */
	private void adaptCodeLib(Document document) throws TransformException {
		// 生成(包名+类名-->代码库bean)的键值对
		Map<String, CodeLibBean> pkgClassMap = new HashMap<String, CodeLibBean>();
		Map<String, CodeLibBean> codeLibBeanMap = codeLibDao.findAll();
		for (Entry<String, CodeLibBean> entry : codeLibBeanMap.entrySet()) {
			if (entry == null || entry.getValue() == null) {
				continue;
			}
			CodeLibBean bean = entry.getValue();
			String className = bean.getPkgName() + "." + bean.getClassName();
			pkgClassMap.put(className, bean);
		}

		Element deeCodeLibElement = document.getRootElement().getChild("dee_code_lib");
		List<Element> codeLibRows = new ArrayList<Element>();
		if (deeCodeLibElement != null) {
			codeLibRows = deeCodeLibElement.getChildren();
		}

		Set<String> pkgList = new HashSet<String>();

		for (Element element : codeLibRows) {
			if (element == null) {
				continue;
			}
			String pkgName = (String) element.getChild("PKG_NAME").getValue();
			String className = (String) element.getChild("CLASS_NAME").getValue();
			pkgList.add(pkgName);
			String key = pkgName + "." + className;
			if (pkgClassMap.containsKey(key)) {
				element.getChild("ID").setValue(pkgClassMap.get(key).getId());
			}
		}

		Element pkgElement = document.getRootElement().addChild("dee_code_pkg");
		pkgElement.setAttribute("count", pkgList.size());
		pkgElement.setAttribute("totalCount", pkgList.size());
		for (String pkgName : pkgList) {
			Element pkgRow = pkgElement.addChild("row");
			pkgRow.addChild("NAME").setValue(pkgName);
			pkgRow.addChild("DESC").setValue(null);
		}
	}
	
//	private synchronized void initDataSource() throws TransformException{
	//	DataSource ds1 = DataSourceManager.getInstance().lookup("dee_meta");
	//	if(ds1==null) throw new TransformException("没有找到dee的数据源dee_meta，请确定已使用DataSourceManager.getInstance().bind绑定。");
	//	if(ds1 instanceof JDBCDataSource){
	//		ds = (JDBCDataSource)ds1;
	//	}else{
	//		throw new TransformException("dee的数据源dee_meta类型错误，只支持JDBCDataSource。"+ds1.getClass().getCanonicalName());
	//	}
//}
//	private synchronized void initDataSource() {
//		String driver = "org.h2.Driver";
//		String url = "jdbc:h2:tcp://10.5.4.100/dee;AUTO_RECONNECT=TRUE;USER=sa;PASSWORD=seeyondee";
//		String userName = "sa";
//		String password = "seeyondee";
//		ds = new JDBCDataSource(driver, url, userName, password);
//	}

}
