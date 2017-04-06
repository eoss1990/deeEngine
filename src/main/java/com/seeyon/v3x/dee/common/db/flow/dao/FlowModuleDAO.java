package com.seeyon.v3x.dee.common.db.flow.dao;

import com.seeyon.v3x.dee.TransformException;
import com.seeyon.v3x.dee.common.base.dao.BaseDAO;
import com.seeyon.v3x.dee.common.base.util.DBUtil;
import com.seeyon.v3x.dee.common.db.flow.model.FlowModuleBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Zhang.Wei
 * @date Feb 7, 20121:42:44 PM
 * @Copyright(c) Beijing Seeyon Software Co.,LTD
 */
public class FlowModuleDAO extends BaseDAO {

	private final static String TABLE_NAME_FLOW_SUB = "dee_flow_module";

	private static Log log = LogFactory.getLog(FlowModuleDAO.class);

	/**
	 * 获取配置的模块信息
	 * 
	 * @return
	 * @throws com.seeyon.v3x.dee.TransformException
	 */
	public List<FlowModuleBean> getModuleList() throws TransformException {
		Connection conn = getConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		String sql = "select module_id ,module_name ,service_flag from "
				+ TABLE_NAME_FLOW_SUB;
		List<FlowModuleBean> list = new ArrayList<FlowModuleBean>();
		try {
			pst = conn.prepareStatement(sql);
			rs = pst.executeQuery();
			while (rs.next()) {
				FlowModuleBean sub = new FlowModuleBean();
				sub.setModule_id(rs.getString("module_id"));
				sub.setModule_name(rs.getString("module_name"));
				sub.setService_flag(rs.getBoolean("service_flag"));
				list.add(sub);
			}
			return list;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new TransformException(e);
		} finally {
			DBUtil.close(rs, pst, conn);
		}
	}
	public List<FlowModuleBean> getModuleList(String ids) throws TransformException {
		Connection conn = getConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		String sql = "select module_id ,module_name ,service_flag from "
			+ TABLE_NAME_FLOW_SUB + " where module_id in ("+ids+")";
		List<FlowModuleBean> list = new ArrayList<FlowModuleBean>();
		try {
			pst = conn.prepareStatement(sql);
//			pst.setString(1, ids);

			rs = pst.executeQuery();
			while (rs.next()) {
				FlowModuleBean sub = new FlowModuleBean();
				sub.setModule_id(rs.getString("module_id"));
				sub.setModule_name(rs.getString("module_name"));
				sub.setService_flag(rs.getBoolean("service_flag"));
				list.add(sub);
			}
			return list;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new TransformException(e);
		} finally {
			DBUtil.close(rs, pst, conn);
		}
	}

	public FlowModuleBean getModuleByName(String moduleName)
			throws TransformException {
		Connection conn = getConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		String sql = "select module_id ,module_name ,service_flag from "
				+ TABLE_NAME_FLOW_SUB + " where module_name='" + moduleName
				+ "'";
		try {
			pst = conn.prepareStatement(sql);
			rs = pst.executeQuery();
			while (rs.next()) {
				FlowModuleBean sub = new FlowModuleBean();
				sub.setModule_id(rs.getString("module_id"));
				sub.setModule_name(rs.getString("module_name"));
				sub.setService_flag(rs.getBoolean("service_flag"));
				return sub;
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new TransformException(e);
		} finally {
			DBUtil.close(rs, pst, conn);
		}
		return null;
	}

	/**
	 * 更新FlowModuleBean
	 * @param bean
	 * @return
	 * @throws com.seeyon.v3x.dee.TransformException
	 */
	public void update(FlowModuleBean bean) throws TransformException {
		String sql = "UPDATE " + TABLE_NAME_FLOW_SUB
				+ " SET SERVICE_FLAG = ?, MOUDULE_NAME = ?"
				+ " where MODULE_ID = ? ";
		super.executeUpdate(sql, bean.getService_flag(), bean.getModule_name(), bean.getModule_id());
	}

	/**
	 * 更新FlowModuleBean中Flag状态
	 * @param bean
	 * @return
	 * @throws com.seeyon.v3x.dee.TransformException
	 * @author lilong
	 */
	public void updateFlag(FlowModuleBean bean) throws TransformException {
		String sql = "UPDATE " + TABLE_NAME_FLOW_SUB
				+ " SET SERVICE_FLAG = ?"
				+ " where MODULE_ID = ? ";
		super.executeUpdate(sql, bean.getService_flag(), bean.getModule_id());
	}
}
