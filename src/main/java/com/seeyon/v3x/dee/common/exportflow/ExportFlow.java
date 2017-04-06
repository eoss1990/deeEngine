package com.seeyon.v3x.dee.common.exportflow;

import com.seeyon.v3x.dee.Document;
import com.seeyon.v3x.dee.Document.Element;
import com.seeyon.v3x.dee.TransformException;
import com.seeyon.v3x.dee.TransformFactory;
import com.seeyon.v3x.dee.adapter.database.JDBCReader;
import com.seeyon.v3x.dee.bean.ScriptBean;
import com.seeyon.v3x.dee.common.base.dao.BaseDAO;
import com.seeyon.v3x.dee.common.db.codelib.dao.CodeLibDao;
import com.seeyon.v3x.dee.common.db.codelib.model.CodeLibBean;
import com.seeyon.v3x.dee.common.db.flow.dao.FlowDAO;
import com.seeyon.v3x.dee.common.db.resource.dao.DeeResourceDAO;
import com.seeyon.v3x.dee.common.db.resource.model.DeeResourceBean;
import com.seeyon.v3x.dee.common.db.resource.util.SourceUtil;
import com.seeyon.v3x.dee.util.Utils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ExportFlow extends BaseDAO{
	private static Log log = LogFactory.getLog(ExportFlow.class);
	
	private FlowDAO dao = new FlowDAO();
	private DeeResourceDAO drDao = new DeeResourceDAO();
	private CodeLibDao codeLibDao = new CodeLibDao();
	/**
	 * @description 根据flowids串读取dee格式的数据包
	 * @date 2012-3-28
	 * @author liuls
	 * @param flowIds  字符串： 12321,234324,23432
	 * @return
	 * @throws com.seeyon.v3x.dee.TransformException
	 * @throws java.sql.SQLException
	 */
	@SuppressWarnings({ "static-access"})
	public Document doReader(String flowIds) throws TransformException, SQLException {
		String sqlIds = SourceUtil.getSqlIds(flowIds);
		JDBCReader r = new JDBCReader();
		Map<String,String> sql = getRelaSqlMap(sqlIds);

		//获取resource id
		String resourceIds = dao.getResourceId(sqlIds);
		String refIds = dao.getRefIds(SourceUtil.getSqlIds(resourceIds));
		//获取字典resource id
		List<DeeResourceBean> drb = drDao.getExchangeMappingByIds(SourceUtil.getSqlIds(refIds));
		String dictIds = getDictByDr(drb);
		String allResourceIds = SourceUtil.getSqlIds(resourceIds + "," + refIds + "," + dictIds);

		//注意查询的顺序不能乱，涉及外键问题
		sql.put("dee_flow", "select * from dee_flow where flow_id in("+sqlIds+")");
		sql.put("dee_resource", "select * from dee_resource where resource_id in ("+allResourceIds+")");
		sql.put("dee_download", "select * from  dee_download where resource_id in (select resource_id from dee_flow_sub where flow_id in ("+sqlIds+"))");
		sql.put("dee_flow_parameter", "select * from dee_flow_parameter where flow_id in("+sqlIds+")");
		sql.put("dee_flow_sub", "select * from dee_flow_sub where flow_id in ("+sqlIds+")");
		sql.put("dee_schedule", "select * from dee_schedule where flow_id in("+sqlIds+")");
		r.setDataSource(super.getDs());
		r.setSql(sql);

		String deeCodeLibSql = exportCodeLib(resourceIds);
		if (deeCodeLibSql != null) {
			sql.put("dee_code_lib", deeCodeLibSql);
		}

		Document doc = r.execute(TransformFactory.getInstance().newDocument("root"));
		return doc;
	}

	/**
	 * @description 根据flowid获取跟flow相关的分类等sql合成的map，与
	 *              reader要求的map一致
	 * @date 2012-3-28
	 * @author liuls
	 * @param flowIds flow id串：'23422','13432','12342'
	 * @return
	 * @throws java.sql.SQLException
	 * @throws com.seeyon.v3x.dee.TransformException
	 */
	private Map<String,String> getRelaSqlMap(String flowIds) throws TransformException, SQLException {
		String[] ids = dao.getRelaIds(flowIds);

		Map<String,String> m = new LinkedHashMap<String,String>();
		m.put("dee_cod_resourcetype", "select * from dee_cod_resourcetype");
		m.put("dee_flow_exetype", "select * from dee_flow_exetype");
		m.put("dee_resource_template", "select * from  dee_resource_template");
		if(ids[0].length()>1){
			m.put("dee_flow_type", "select * from dee_flow_type where flow_type_id in("+getFlowTypeIds(ids[0])+")");
		}
		if(ids[1].length()>1){
			m.put("dee_flow_exetype", "select * from dee_flow_exetype where exetype_id in("+ SourceUtil.getSqlIds(ids[1])+")");
		}
		if(ids[2].length()>1){
			m.put("dee_flow_module", "select * from dee_flow_module where module_id in("+ SourceUtil.getSqlIds(ids[2])+")");
		}
		// test
		return m;
	}
	/**
	 * @description 更加子类别获取其所在树上的所有父类别
	 * @date 2012-4-1
	 * @author liuls
	 * @param s
	 * @return
	 * @throws com.seeyon.v3x.dee.TransformException
	 */
	private String getFlowTypeIds(String s) throws TransformException {

		Map<String,String> map = dao.findId2Map();
		String[] idsArr = s.split(",");
		Set<String> set = new HashSet<String>();
		int c = map.keySet().size();
		for(String id:idsArr){
			if(!"".equals(id.trim())){
				int i = 0;
				set.add(id);
				getTreeList(map,id,set,i,c);
			}
		}
//		set.remove("0");//移除根节点，默认根节点id是0，如果不是，必须修改移除的节点ID
		String newIdsStr = "";
		if(set.size()>0){
			for(String id:set){
				newIdsStr+="'"+id+"'"+",";
			}
			if(newIdsStr.endsWith(",")){
				newIdsStr = newIdsStr.substring(0,newIdsStr.length()-1);
			}
		}

		return newIdsStr;
	}
	private static void getTreeList(Map<String,String> m,String start,Set<String> set,int i,int c){
		i++;
		if(!"-1".equals(m.get(start))&&i<c){
		    set.add(m.get(start));
		    getTreeList(m,m.get(start),set,i,c);
		}
	}
	/**
	 * @description 根据DeeResourceBean 获取映射配置中的字典串
	 * @date 2013-10-09
	 * @author dkywolf
	 * @param drb
	 * @return
	 * @throws com.seeyon.v3x.dee.TransformException
	 */
	private String getDictByDr(List<DeeResourceBean> drb) throws TransformException {
		if(drb == null || drb.size() == 0)
			return "";
		StringBuffer retVals = new StringBuffer();
		for(DeeResourceBean dr:drb){
			if(dr != null && dr.getResource_code() != null && !"".equals(dr.getResource_code())){
				String retStr = SourceUtil.getExprValue(dr.getResource_code());
				retVals.append(retStr);
			}
		}
		String midStr = retVals.toString();
		if(midStr.endsWith(",")){
			midStr = midStr.substring(0,midStr.length()-1);
		}
		String vals = SourceUtil.getSqlIds(midStr);
		return drDao.getJDBCRsidByVals(vals)+","+drDao.getStaticRsidByVals(vals);
	}

	/**
	 * 导出用户代码库
	 * @param resourceIds 适配器ID列表
	 * @return sql语句
	 */
	private String exportCodeLib(String resourceIds) {
		StringBuilder ids = new StringBuilder();
		try {
			List<DeeResourceBean> resourceBeans = drDao.findByIds(resourceIds);

			List<String> imports = new ArrayList<String>();
			for (DeeResourceBean bean : resourceBeans) {
				if ("14".equals(bean.getResource_template_id()) ||
						"15".equals(bean.getResource_template_id()) ||
						"16".equals(bean.getResource_template_id())) {
					String script = new ScriptBean(bean.getResource_code()).getScript();
					try {
						imports.addAll(Utils.parse2ImportList(script));
					} catch (IOException e) {
						log.error(e.getLocalizedMessage(), e);
					}
				}
			}

			for (Map.Entry<String, CodeLibBean> entry : codeLibDao.findAll().entrySet()) {
				CodeLibBean bean = entry.getValue();
				if (imports.contains(bean.getPkgName() + "." + bean.getClassName())) {
					ids.append("'").append(bean.getId()).append("',");
				}
			}
		} catch (Exception e) {
			log.error("用户代码库导出错误：" + e.getLocalizedMessage(), e);
			return null;
		}

		if (ids.length() > 1) {
			ids.deleteCharAt(ids.length() - 1);
			return "select * from dee_code_lib where id in (" + ids.toString() + ")";
		}

		return null;
	}
}
