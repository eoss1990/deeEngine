package com.seeyon.v3x.dee.common.db.flow.dao;

import com.seeyon.v3x.dee.TransformException;
import com.seeyon.v3x.dee.common.base.dao.BaseDAO;
import com.seeyon.v3x.dee.common.base.page.Page;
import com.seeyon.v3x.dee.common.base.util.DBUtil;
import com.seeyon.v3x.dee.common.base.util.Resultset2List;
import com.seeyon.v3x.dee.common.base.util.UuidUtil;
import com.seeyon.v3x.dee.common.db.flow.model.FlowBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FlowDAO extends BaseDAO {
	
	private final static String TABLE_NAME_FLOW = "dee_flow";
	private final static String TABLE_DEE_FLOW_SUB = "dee_flow_sub";
	private final static String TABLE_DEE_RESOURCE = "dee_resource";
	private final static String DEE_FLOW_PARAMETER = "dee_flow_parameter";
	private final static String DEE_DOWNLOAD = "dee_download";
	private final static String DEE_SCHEDULE = "dee_schedule";
    private final static String DEE_SYNC_HISTORY = "dee_sync_history";
    private final static String DEE_REDO = "dee_redo";
	
	private static Log log = LogFactory.getLog(FlowDAO.class);
	/**
	 * 按名称取单一Flow,返回Flow的XML串
	 * 
	 * @param id
	 * @return
	 * @throws com.seeyon.v3x.dee.TransformException
	 */
	public FlowBean get(String id) throws TransformException {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ");
		sql.append("flow.*,type.FLOW_TYPE_NAME,schedule.SCHEDULE_NAME,exe.EXETYPE_NAME ");
		sql.append("FROM DEE_FLOW flow ");
		sql.append("LEFT JOIN DEE_FLOW_TYPE type ON flow.FLOW_TYPE_ID = type.FLOW_TYPE_ID ");
		sql.append("LEFT JOIN DEE_SCHEDULE schedule ON flow.FLOW_ID=schedule.FLOW_ID ");
		sql.append("LEFT JOIN DEE_FLOW_EXETYPE exe ON exe.EXETYPE_ID=flow.EXETYPE_ID ");
		sql.append("WHERE flow.FLOW_ID='").append(id).append("' ");
		return (FlowBean) super.getBeanBySql(sql.toString(), FlowBean.class);
	}

	/**
	 * 更新Flow。
	 *
	 * @param name
	 * @param xml
	 * @throws com.seeyon.v3x.dee.TransformException
	 */
	public FlowBean update(FlowBean flowBean) throws TransformException {

/*		StringBuffer sql = new StringBuffer();

		sql.append("update "
				+ TABLE_NAME_FLOW
				+ " set "
				+ " FLOW_TYPE_ID ='"
				+ flowBean.getFLOW_TYPE_ID()
				+ "',"
				+
				// " FLOW_NAME ='"+flowBean.getFLOW_NAME()+"',"+
				" MODULE_IDS = '" + flowBean.getMODULE_IDS() + "',"
				+ " FLOW_DESC = '" + flowBean.getFLOW_DESC() + "',"
				+ " SCHEDULE_ID = '" + flowBean.getSCHEDULE_ID() + "',"
				+ " FLOW_META = '" + flowBean.getFLOW_META() + "',"
				+ " DIS_NAME = '" + flowBean.getDIS_NAME() + "',"
				+ " EXETYPE_ID = '" + flowBean.getEXETYPE_ID() + "'"
				+ " where FLOW_ID='" + flowBean.getFLOW_ID() + "'");
		super.execute(sql.toString());*/
		String sql = "update "
				+ TABLE_NAME_FLOW
				+ " set FLOW_TYPE_ID =?, MODULE_IDS =?,FLOW_DESC = ?,FLOW_META =?,DIS_NAME =?,EXETYPE_ID = ?,EXT1 = ?"
				+ " where FLOW_ID=?";
		super.executeUpdate(sql, flowBean.getFLOW_TYPE_ID(),
				flowBean.getMODULE_IDS(), flowBean.getFLOW_DESC(),
				flowBean.getFLOW_META(),
				flowBean.getDIS_NAME(), flowBean.getEXETYPE_ID(),
				flowBean.getEXT1(),
				flowBean.getFLOW_ID());
		return this.get(flowBean.getFLOW_ID());
	}

	/**
	 * @description 存储用于form表单映射使用的xml
	 * @date 2012-2-27
	 * @author liuls
	 * @param metaXML xml字符串
	 * @throws com.seeyon.v3x.dee.TransformException
	 */
	public void updateMeta(String id,String metaXML) throws TransformException {

				String sql = "update "
						+ TABLE_NAME_FLOW
						+ " set FLOW_META =? where FLOW_ID=?";
				super.executeUpdate(sql, metaXML,id);
	}
	/**
	 * 新建Flow。
	 *
	 * @param name
	 * @param xml
	 * @throws com.seeyon.v3x.dee.TransformException
	 */
	public FlowBean create(FlowBean bean) throws TransformException {
		String uuid = UuidUtil.uuid();
/*		StringBuffer sql = new StringBuffer();
		sql.append("insert into DEE_FLOW (FLOW_ID,FLOW_TYPE_ID,FLOW_NAME,MODULE_IDS,FLOW_DESC,SCHEDULE_ID,FLOW_META,DIS_NAME)values "
				+ " ('"
				+ uuid
				+ "','"
				+ bean.getFLOW_TYPE_ID()
				+ "','"
				+ uuid
				+ "','"
				+ bean.getMODULE_IDS()
				+ "'"
				+ ",'"
				+ bean.getFLOW_DESC()
				+ "','"
				+ bean.getSCHEDULE_ID()
				+ "','"
				+ bean.getFLOW_META() + "','" + bean.getDIS_NAME() + "') ");
		super.execute(sql.toString());*/
		bean.setFLOW_ID(uuid);
		bean.setFLOW_NAME(uuid);

		String sql = "insert into DEE_FLOW (FLOW_ID,FLOW_TYPE_ID,FLOW_NAME,MODULE_IDS,FLOW_DESC,FLOW_META,DIS_NAME,EXT1)values(?,?,?,?,?,?,?,?) ";
		super.executeUpdate(sql, uuid,bean.getFLOW_TYPE_ID(),uuid,bean.getMODULE_IDS(),bean.getFLOW_DESC(),bean.getFLOW_META(),bean.getDIS_NAME(),bean.getEXT1());
		return this.get(uuid);
	}

	// 删除Flow
	public void delete(String id) throws TransformException {
		super.delete(TABLE_NAME_FLOW, "id='" + id + "'");
	}

	/**
	 * 删除Flow及子表内容及相关的其他表的内容
	 *
	 * @param id
	 * @throws com.seeyon.v3x.dee.TransformException
	 */
	public void delFlowInfo(String flow_id) throws TransformException {
		super.delete(DEE_DOWNLOAD, "resource_id in (select resource_id from dee_flow_sub where flow_id in ('"+flow_id+"'))");
		super.delete(TABLE_DEE_RESOURCE, "resource_id in (select resource_id from dee_flow_sub where flow_id in ('"+flow_id+"'))");
		super.delete(TABLE_DEE_FLOW_SUB, "flow_id='" + flow_id + "'");
		super.delete(DEE_FLOW_PARAMETER, "flow_id='" + flow_id + "'");
		super.delete(DEE_SCHEDULE, "flow_id='" + flow_id + "'");
		super.delete(TABLE_NAME_FLOW, "flow_id='" + flow_id + "'");
	}

    /**
     * 删除Flow及其他关联表
     *
     * @param flow_id
     * @throws com.seeyon.v3x.dee.TransformException
     */
    public void delFlowInfoForV5(String flow_id) throws TransformException {
        StringBuffer buffer = new StringBuffer("");
        Connection connection = null;
        PreparedStatement pstm = null;
        ResultSet rs  = null;
        try {
            connection = getConnection();
            String sql = "select resource_id from " + TABLE_DEE_FLOW_SUB + " where flow_id in ('" + flow_id + "')";
            pstm = connection.prepareStatement(sql);
            rs = pstm.executeQuery();
            while (rs.next()) {
                String resourceId = rs.getString("resource_id");
                if (resourceId != null) {
                    buffer.append("'").append(resourceId).append("',");
                }
            }
        } catch (SQLException sqle) {
            log.info(sqle);
        } finally {
            DBUtil.close(rs, pstm, connection);
        }
        String resourceStr = "";
        if (buffer.length() > 0) {
            resourceStr = buffer.substring(0, buffer.length() - 1);
        }

		super.delete(DEE_DOWNLOAD, "resource_id in (select resource_id from dee_flow_sub where flow_id in ('"+flow_id+"'))");
        super.delete(TABLE_DEE_FLOW_SUB, "flow_id='" + flow_id + "'");
        super.delete(TABLE_DEE_RESOURCE, "resource_id in (" + resourceStr + ")");
		super.delete(DEE_FLOW_PARAMETER, "flow_id='" + flow_id + "'");
		super.delete(DEE_SCHEDULE, "flow_id='" + flow_id + "'");
        super.delete(DEE_REDO, "flow_id='" + flow_id + "'");
        super.delete(DEE_SYNC_HISTORY, "flow_id='" + flow_id + "'");
        super.delete(TABLE_NAME_FLOW, "flow_id='" + flow_id + "'");
    }

	/**
	 * 为修改flow编辑页面删除某一个具体配置增加此方法
	 * 删除dee_flow_sub和dee_resouce表中的记录
	 * @param resouce_id
	 * @throws com.seeyon.v3x.dee.TransformException
	 * @author lilong
	 * @date 2012-02-29
	 */
	public int delFlowConfig4One(String resource_id) throws TransformException {
		int i = 0;
		i += super.delete(TABLE_DEE_FLOW_SUB, "resource_id='" + resource_id + "'");
		i += super.delete(TABLE_DEE_RESOURCE, "resource_id='" + resource_id + "'");
		return i;
	}

	public Page query(Page page, FlowBean bean) throws TransformException {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT flow.*,type.FLOW_TYPE_NAME,exe.EXETYPE_NAME as EXE_TYPE_NAME FROM "
				+ TABLE_NAME_FLOW
				+ " flow LEFT JOIN DEE_FLOW_TYPE type ON flow.FLOW_TYPE_ID = type.FLOW_TYPE_ID LEFT JOIN " +
				"DEE_FLOW_EXETYPE exe ON exe.EXETYPE_ID=flow.EXETYPE_ID "
				+ " where 1=1  ");
		if (bean != null) {
			if (bean.getFLOW_TYPE_ID() != null
					&& !"".equals(bean.getFLOW_TYPE_ID().trim())) {
				sql.append(" and type.FLOW_TYPE_ID='"
						+ bean.getFLOW_TYPE_ID().trim() + "'");
			}
			if (bean.getDIS_NAME() != null
					&& !"".equals(bean.getDIS_NAME().trim())) {
				sql.append(" and flow.DIS_NAME like '%" + bean.getDIS_NAME()
						+ "%'");
			}
		}
        page.setTotalCount(super.getCount(sql.toString()));
        sql.append(" group by flow.FLOW_ID order by flow.EXT4 DESC");
        return super.getAllToPage(page, sql.toString(), FlowBean.class);
	}
	
	public String findSourceById(String id) throws TransformException {
		String str = "";
		String sql = "select b.resource_code from dee_resource b left join dee_flow_sub c "
				+ "on b.resource_id = c.resource_id where c.flow_id = ? and "
				+ "(b.resource_template_id = 0 or b.resource_template_id = 2)";
		Connection conn = getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, id);
			rs = stmt.executeQuery();
			int i = 0;
			while(rs.next()){
				if(i == 0){
					str = rs.getString("resource_code");
				}else{
					str = str + "逗号," + rs.getString("resource_code");
				}
				i++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				DBUtil.close(rs, stmt, conn);
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		return str;
	}
	
	public String findSourceNameById(String id) throws TransformException {
		String str = "";
		String sql = "select dis_name from dee_resource where resource_id = ?";
		Connection conn = getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, id);
			rs = stmt.executeQuery();
			while(rs.next()){
				str = rs.getString("dis_name");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				DBUtil.close(rs, stmt, conn);
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		return str;
	}

	/**
	 * @description 检查flowName是否已经存在
	 * @date 2012-2-27
	 * @author liuls
	 * @param flowname flow名称
	 * @return
	 * @throws com.seeyon.v3x.dee.TransformException
	 */
	public boolean checkFlowName(String flowname,String flowid) throws TransformException {
		StringBuffer sql = new StringBuffer();
		sql.append("select flow.*,type.flow_type_name from dee_flow flow,dee_flow_type type "
				+ " where flow.flow_type_id = type.flow_type_id and flow.dis_name='"
				+ flowname + "' and flow.flow_id<>'"+flowid+"'");
		int i = super.getCount(sql.toString());
		return (i > 0);
	}


	/**
	 * @description 查找所有的内容
	 * @date 2012-4-1
	 * @author liuls
	 * @return
	 * @throws com.seeyon.v3x.dee.TransformException
	 */
	@SuppressWarnings("unchecked")
	public List<FlowBean> findAll() throws TransformException {
		Connection conn = getConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		String sql = "select * from " + TABLE_NAME_FLOW;
		Statement stmt;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			return Resultset2List.getListFromRS(rs, FlowBean.class);
		} catch (Throwable e) {
			log.error(e.getMessage(), e);
			throw new TransformException(e);
		} finally {
			DBUtil.close(rs, pst, conn);
		}
	}
	/**
	 * @description 查询id，和parent_id，放到map中
	 * @date 2012-4-1
	 * @author liuls
	 * @return
	 * @throws com.seeyon.v3x.dee.TransformException
	 */
	public Map<String,String> findId2Map() throws TransformException {

		Connection conn = getConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		String sql = "select flow_type_id,parent_id from dee_flow_type";
		Statement stmt;
		try {
			Map<String, String> idMap = new HashMap<String, String>();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while(rs.next()){
				idMap.put(rs.getString(1),rs.getString(2) );
			}
			return idMap;
		} catch (Throwable e) {
			log.error(e.getMessage(), e);
			throw new TransformException(e);
		} finally {
			DBUtil.close(rs, pst, conn);
		}
	}
	/**
	 * @description 获取flow相关的其他分类的ID串
	 * @date 2012-3-28
	 * @author liuls
	 * @param fids flow id串 ：'1234','2342','3234'
	 * @return
	 * @throws com.seeyon.v3x.dee.TransformException
	 */
	public String[] getRelaIds(String fids) throws TransformException {
		String[]ids = new String[3];
		Connection conn = getConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		String sql = "select flow_type_id,exetype_id,module_ids from " +
					TABLE_NAME_FLOW +" where flow_id in("+fids+")";
		try {
			pst = conn.prepareStatement(sql);
			rs = pst.executeQuery();
			StringBuffer typeIds = new StringBuffer();

			StringBuffer exetypeIds = new StringBuffer();
			StringBuffer moduleIds = new StringBuffer();
			while(rs.next()){
				typeIds.append(rs.getString("flow_type_id")+",");
				exetypeIds.append(rs.getString("exetype_id")+",");
				moduleIds.append(rs.getString("module_ids")+",");
			}
			ids[0]= typeIds.toString();
			ids[1]= exetypeIds.toString();
			ids[2]= moduleIds.toString();
		} catch (Throwable e) {
			log.error(e.getMessage(), e);
			throw new TransformException(e);
		} finally {
			DBUtil.close(rs, pst, conn);
		}
		return ids;
	}
	/**
	 * @description 根据flowId 获取所有的相关的数据源
	 * @date 2012-4-5
	 * @author liuls
	 * @throws com.seeyon.v3x.dee.TransformException
	 * @throws com.seeyon.v3x.dee.TransformException
	 */
	public String getResourceId(String sqlIds) throws TransformException {
		//"select * from dee_resource where resource_id in (select resource_id from dee_flow_sub where flow_id in ("+sqlIds+"))"
		Connection conn = getConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		String sql = "select resource_id from dee_resource where resource_id in (select resource_id from dee_flow_sub where flow_id in ("+sqlIds+"))";
		Statement stmt;
		StringBuffer dsIds = new StringBuffer();
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while(rs.next()){
				dsIds.append(rs.getString("resource_id")+",");
			}
			return dsIds.toString();
		} catch (Throwable e) {

			log.error(e.getMessage(), e);
			throw new TransformException(e);
		} finally {
			DBUtil.close(rs, pst, conn);
		}
	}
	/**
	 * @description 根据resource id 获取相关的resource id
	 * @date 2012-4-5
	 * @author liuls
	 * @param rsId
	 * @return
	 * @throws com.seeyon.v3x.dee.TransformException
	 */
	public String getRefIds(String rsId) throws TransformException {
		Connection conn = getConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		String sql = "select ref_id  from dee_resource where resource_id in ("+rsId+")";
		Statement stmt;
		StringBuffer dsIds = new StringBuffer();
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while(rs.next()){
				dsIds.append(rs.getString("ref_id")+",");
			}
			return dsIds.toString();
		} catch (Throwable e) {
			
			log.error(e.getMessage(), e);
			throw new TransformException(e);
		} finally {
			DBUtil.close(rs, pst, conn);
		}
	}
	
}
