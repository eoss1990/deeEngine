package com.seeyon.v3x.dee.common.db.flow.dao;

import com.seeyon.v3x.dee.TransformException;
import com.seeyon.v3x.dee.common.base.dao.BaseDAO;
import com.seeyon.v3x.dee.common.base.util.DBUtil;
import com.seeyon.v3x.dee.common.base.util.UuidUtil;
import com.seeyon.v3x.dee.common.db.flow.model.FlowSubBean;
import com.seeyon.v3x.dee.common.db.flow.util.FlowSubSortTool;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class FlowSubDAO extends BaseDAO {
	private final static String TABLE_NAME_FLOW_SUB = "dee_flow_sub";
	private static Log log = LogFactory.getLog(FlowSubDAO.class);

	public List<FlowSubBean> getByFlowID(String flowid)
			throws TransformException {
		Connection conn = getConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		String sql = "select * from " + TABLE_NAME_FLOW_SUB
				+ " where flow_id='" + flowid + "' order by sort";
		List<FlowSubBean> rsList = new LinkedList<FlowSubBean>();
		try {
			pst = conn.prepareStatement(sql);
			rs = pst.executeQuery();
			while (rs.next()) {
				FlowSubBean sub = new FlowSubBean();
				sub.setFlow_id(rs.getString("flow_id"));
				sub.setFlow_sub_id(rs.getString("flow_sub_id"));
				sub.setResource_id(rs.getString("resource_id"));
				sub.setSort(rs.getInt("sort"));
				rsList.add(sub);
			}
			return rsList;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new TransformException(e);
		} finally {
			DBUtil.close(rs, pst, conn);
		}
	}

	public void insert(FlowSubBean bean) throws TransformException {
		String sql = " insert into " + TABLE_NAME_FLOW_SUB
				+ " (flow_sub_id,flow_id,resource_id,sort)values(?,?,?,?)";
		Connection conn = getConnection();
		PreparedStatement pst = null;
		try {
			pst = conn.prepareStatement(sql);
			pst.setString(1, UuidUtil.uuid());
			pst.setString(2, bean.getFlow_id());
			pst.setString(3, bean.getResource_id());
			pst.setInt(4, bean.getSort());
			pst.executeUpdate();
		} catch (Throwable e) {
			log.error(e.getMessage()+"error sql=" + sql, e);
			throw new TransformException(e);
		} finally {
			DBUtil.close(null, pst, conn);
		}
	}
	
	/**
	 * 为插入资源树，获取排序号方法
	 * 根据flow_id和resource_id查询该条记录的排序号
	 * @param flow_id
	 * @param resource_id
	 * @return
	 * @throws com.seeyon.v3x.dee.TransformException
	 * @throws java.sql.SQLException
	 * @author lilong
	 * @date 2012-03-02
	 */
	public int getSortByFlowIdResourceId(String flow_id, String resource_id) throws TransformException, SQLException {
		int sort = 0;
		Connection conn = getConnection();
        PreparedStatement pst = null;
        ResultSet rs = null;

		String sql = "select sort from " + TABLE_NAME_FLOW_SUB;
		sql += " where flow_id = ? and resource_id = ? ";
		pst = conn.prepareStatement(sql);
		int count = 1;
        pst.setString(count++, flow_id);
        pst.setString(count, resource_id);
        rs = pst.executeQuery();
        while (rs.next()) {
        	sort = rs.getInt(1);//TODO 没有查询到值的判断
        }
		return sort;
	}

	/**
	 * 为增加出入资源树批量更新排序号方法
	 * 批量更新sort值
	 * @param sort
	 * @param flow_id
	 * @param resource_template_id
	 * @return
	 * @throws com.seeyon.v3x.dee.TransformException
	 * @throws java.sql.SQLException
	 * @author lilong
	 * @date 2012-03-02
	 */
	public int updateSortsBySortFidRtid(int sort, String flow_id, String resource_template_id) throws TransformException, SQLException {
		int result = 0;
		Connection conn = getConnection();
        PreparedStatement pst = null;
        ResultSet rs = null;
        StringBuffer sql = new StringBuffer();
        sql.append("update ").append(TABLE_NAME_FLOW_SUB);
        sql.append(" set sort=sort+1 ");
        sql.append(" where flow_id = ? ");
        sql.append(" and sort > ?");
        
        switch (FlowSubSortTool.judgeResourceType(resource_template_id)) {
		case 1:
			sql.append(" and sort < 1999");
			break;
		case 2:
			sql.append(" and sort < 2999");
			break;
		case 3:
			sql.append(" and sort < 3999");
		default:
			break;
		}
        
        pst = conn.prepareStatement(sql.toString());
        int count = 1;
        pst.setString(count++, flow_id);
        pst.setInt(count, sort);
        result = pst.executeUpdate();
        
		return result;
	}
	
	public int getSortValue(String flowid, String resource_template_id) throws TransformException {
		String count = "0";
	    Connection conn = getConnection();
        PreparedStatement pst = null;
        ResultSet rs = null;
        String sql = " select max(dfs.sort) from " + " dee_flow_sub dfs "
                +" where dfs.flow_id='"+flowid+"' ";
        if(resource_template_id!=null&&!"".equals(resource_template_id)){
        	switch (FlowSubSortTool.judgeResourceType(resource_template_id)) {
    		case 1:
    			count = "1000";
            	sql += " and dfs.sort between 1000 and 1999";
    			break;
    		case 2:
    			count = "2000";
    			sql += " and dfs.sort between 2000 and 2999";
    			break;
    		case 3:
    			count = "3000";
    			sql += " and dfs.sort between 3000 and 3999";
    			break;
    		default:
    			break;
    		}
        }
        try {
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            String temp = "";
            while (rs.next()) {
            	temp = rs.getString(1);
                if(temp==null){
                	return Integer.parseInt(count);
                }else{
                	return Integer.parseInt(temp)+1;
                }
            }
            return Integer.parseInt(count);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new TransformException(e);
        } finally {
            DBUtil.close(rs, pst, conn);
        }  
	}
}
