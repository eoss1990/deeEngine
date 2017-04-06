package com.seeyon.v3x.dee.common.db.code.dao;

import com.seeyon.v3x.dee.TransformException;
import com.seeyon.v3x.dee.common.base.dao.BaseDAO;
import com.seeyon.v3x.dee.common.base.page.Page;
import com.seeyon.v3x.dee.common.base.util.DBUtil;
import com.seeyon.v3x.dee.common.base.util.UuidUtil;
import com.seeyon.v3x.dee.common.db.code.model.FlowTypeBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class FlowTypeDAO extends BaseDAO {
	private final static String TABLE_NAME_FLOW_TYPE = "dee_flow_type";

	private static Log log = LogFactory.getLog(FlowTypeDAO.class);

	public List<FlowTypeBean> findAll() throws TransformException {
		Connection conn = getConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		String sql = "select flow_type_id,flow_type_name,parent_id,flow_type_order,flow_type_desc from "
				+ TABLE_NAME_FLOW_TYPE +" order by flow_type_order";
		List<FlowTypeBean> drb = new ArrayList<FlowTypeBean>();
		try {
			pst = conn.prepareStatement(sql);
			rs = pst.executeQuery();
			while (rs.next()) {
				FlowTypeBean bean = new FlowTypeBean();
				bean.setFLOW_TYPE_ID(rs.getString("flow_type_id"));
				bean.setFLOW_TYPE_NAME(rs.getString("flow_type_name"));
				bean.setPARENT_ID(rs.getString("parent_id"));
				bean.setFLOW_TYPE_ORDER(rs.getInt("flow_type_order"));
				bean.setFLOW_TYPE_DESC(rs.getString("flow_type_desc"));
				drb.add(bean);
			}
			return drb;
		} catch (Throwable e) {
			log.error(e.getMessage(), e);
			throw new TransformException(e);
		} finally {
			DBUtil.close(rs, pst, conn);
		}
	}

	public Page getAllToPage(Page page) throws TransformException {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT *  from " + TABLE_NAME_FLOW_TYPE +" order by flow_type_order");
		return super.getAllToPage(page, sql.toString(), FlowTypeBean.class);

	}
	public FlowTypeBean get(String id) throws TransformException {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT *  from " + TABLE_NAME_FLOW_TYPE+" where flow_type_id='"+id+"'");
		return (FlowTypeBean) super.getBeanBySql(sql.toString(), FlowTypeBean.class);
	}

	public void insert(FlowTypeBean bean) throws TransformException {
		String sql = " insert into " + TABLE_NAME_FLOW_TYPE
				+ " (flow_type_id,flow_type_name,parent_id,flow_type_order,flow_type_desc)values(?,?,?,?,?)";
		Connection conn = getConnection();
		PreparedStatement pst = null;
		try {
			pst = conn.prepareStatement(sql);
			int count = 1;
			pst.setString(count++, UuidUtil.uuid());
			pst.setString(count++, bean.getFLOW_TYPE_NAME());
			pst.setString(count++, bean.getPARENT_ID());
			pst.setInt(count++, bean.getFLOW_TYPE_ORDER());
			pst.setString(count++, bean.getFLOW_TYPE_DESC());
			pst.executeUpdate();
		} catch (Throwable e) {
			log.error(e.getMessage() + " error sql=" + sql, e);
			throw new TransformException(e);
		} finally {
			DBUtil.close(null, pst, conn);
		}
	}
	
	public int deleteById(String flow_type_id) throws TransformException {
		
		return super.delete(TABLE_NAME_FLOW_TYPE, "flow_type_id='"+flow_type_id+"'");
	}
    public void update(FlowTypeBean typeBean) throws TransformException {
		String sql = "update "+TABLE_NAME_FLOW_TYPE +" set FLOW_TYPE_NAME =?,FLOW_TYPE_ORDER=?,FLOW_TYPE_DESC=? where flow_type_id=?";
		super.executeUpdate(sql,typeBean.getFLOW_TYPE_NAME(),typeBean.getFLOW_TYPE_ORDER(),typeBean.getFLOW_TYPE_DESC(),typeBean.getFLOW_TYPE_ID());
	}
    /**
     * @description 查询任务表，是否有跟匹配的类别相关的任务
     * @date 2012-2-3
     * @author liuls
     * @param flow_type_id 类别ID
     * @return 相关类别下的任务条数
     * @throws com.seeyon.v3x.dee.TransformException
     */
    public int excute(String flow_type_id) throws TransformException {
    	return super.getCount("select count(*) from dee_flow where flow_type_id='"+flow_type_id+"'");
    }
    /**
     * @description 查询是否有子分类存在
     * @date 2012-3-14
     * @author liuls
     * @param flow_type_id
     * @return
     * @throws com.seeyon.v3x.dee.TransformException
     */
    public int sonCounts(String flow_type_id) throws TransformException {
    	return super.getCount("select count(*) from "+TABLE_NAME_FLOW_TYPE+" where parent_id='"+flow_type_id+"'");
    }

}
