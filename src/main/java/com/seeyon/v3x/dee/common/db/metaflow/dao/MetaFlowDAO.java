package com.seeyon.v3x.dee.common.db.metaflow.dao;

import com.seeyon.v3x.dee.TransformException;
import com.seeyon.v3x.dee.common.base.dao.BaseDAO;
import com.seeyon.v3x.dee.common.base.util.DBUtil;
import com.seeyon.v3x.dee.common.base.util.UuidUtil;
import com.seeyon.v3x.dee.common.db.metaflow.model.MetaFlowBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 存取元数据
 * 
 * @author lilong
 * 
 */
public class MetaFlowDAO extends BaseDAO {

	private final static String TABLE_NAME_METAFLOW = "dee_metaflow";

	private static Log log = LogFactory.getLog(MetaFlowDAO.class);

	public String insert(MetaFlowBean mfb) throws TransformException {
		String sql = " insert into "
				+ TABLE_NAME_METAFLOW
				+ " (metaflow_id, metaflow_name, metaflow_code) values (?,?,?)";
		Connection conn = getConnection();
		PreparedStatement pst = null;
		try {
			pst = conn.prepareStatement(sql);
			int count = 1;
			if (mfb.getMetaflow_id() == null
					|| "".equals(mfb.getMetaflow_id().trim())) {
				mfb.setMetaflow_id(UuidUtil.uuid());
			}
			pst.setString(count++, mfb.getMetaflow_id());
			pst.setString(count++, mfb.getMetaflow_name());
			// pst.setString(count++, drb.getDr().toXML(drb.getResource_id()));
			pst.setString(count++, mfb.getMetaflow_code());
			pst.executeUpdate();
			return mfb.getMetaflow_id();
		} catch (Throwable e) {
			log.error(e.getMessage()+"error sql=" + sql, e);
			throw new TransformException(e);
		} finally {
			DBUtil.close(null, pst, conn);
		}
	}
	
	/**
	 * 仅用于在插入A8元数据前检验是否存在数据<br>
	 * 与JDBCDataSource中检查内容方法类似<br>
	 * 方法内无需关闭连接，外部关闭
	 * 
	 * @param conn
	 * @return true存在，不需要插入;false不存在，需要插入
	 */
	public Boolean isExistsA8Meta() throws TransformException {
		Boolean b = false;
		Connection conn = getConnection();
		ResultSet rs = null;
		try {
			rs = conn.createStatement().executeQuery(
					"SELECT 1 FROM DEE_METAFLOW WHERE METAFLOW_ID = '001'");
			if (rs.next())
				b = true;
		} catch (SQLException e) {
		} finally{
			DBUtil.close(rs, null, conn);
		}
		return b;
	}

	/**
	 * 查询全部
	 * 
	 * @return
	 * @throws com.seeyon.v3x.dee.TransformException
	 */
	public List<MetaFlowBean> findAll() throws TransformException {
		List<MetaFlowBean> mfbList = new ArrayList<MetaFlowBean>();
		String sql = "select metaflow_id, metaflow_name, metaflow_code from "
				+ TABLE_NAME_METAFLOW + " WHERE METAFLOW_ID <> '001'";

		Connection conn = getConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			pst = conn.prepareStatement(sql);
			rs = pst.executeQuery();
			while (rs.next()) {
				MetaFlowBean mfb = new MetaFlowBean();
				mfb.setMetaflow_id(rs.getString("metaflow_id"));
				mfb.setMetaflow_name(rs.getString("metaflow_name"));
				mfb.setMetaflow_code(rs.getString("metaflow_code"));
				mfbList.add(mfb);
			}
			return mfbList;
		} catch (Throwable e) {
			log.error(e.getMessage(), e);
			throw new TransformException(e);
		} finally {
			DBUtil.close(rs, pst, conn);
		}
	}

	/**
	 * 查询一个
	 *
	 * @param id
	 * @return
	 * @throws com.seeyon.v3x.dee.TransformException
	 */
	public MetaFlowBean findById(String metaflow_id) throws TransformException {
		String sql = "select metaflow_id, metaflow_name, metaflow_code from "
				+ TABLE_NAME_METAFLOW + " where metaflow_id=?";

		Connection conn = getConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			pst = conn.prepareStatement(sql);
			pst.setString(1, metaflow_id);
			rs = pst.executeQuery();
			while (rs.next()) {
				MetaFlowBean mfb = new MetaFlowBean();
				mfb.setMetaflow_id(rs.getString("metaflow_id"));
				mfb.setMetaflow_name(rs.getString("metaflow_name"));
				mfb.setMetaflow_code(rs.getString("metaflow_code"));
				return mfb;
			}
		} catch (Throwable e) {
			log.error(e.getMessage(), e);
			throw new TransformException(e);
		} finally {
			DBUtil.close(rs, pst, conn);
		}
		return null;
	}

	/**
	 * 通过flow_type_id获取meta列表
	 *
	 * @param flow_type_id
	 * @return
	 * @throws com.seeyon.v3x.dee.TransformException
	 */
	public List<MetaFlowBean> findByTypeId(String flow_type_id)
			throws TransformException {
		List<MetaFlowBean> mfbList = new ArrayList<MetaFlowBean>();
		String sql = "select metaflow_id, metaflow_name, metaflow_code from "
				+ TABLE_NAME_METAFLOW + " where flow_type_id=?";

		Connection conn = getConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			pst = conn.prepareStatement(sql);
			pst.setString(1, flow_type_id);
			rs = pst.executeQuery();
			while (rs.next()) {
				MetaFlowBean mfb = new MetaFlowBean();
				mfb.setMetaflow_id(rs.getString("metaflow_id"));
				mfb.setMetaflow_name(rs.getString("metaflow_name"));
				mfb.setMetaflow_code(rs.getString("metaflow_code"));
				mfbList.add(mfb);
			}
			return mfbList;
		} catch (Throwable e) {
			log.error(e.getMessage(), e);
			throw new TransformException(e);
		} finally {
			DBUtil.close(rs, pst, conn);
		}
	}

	/**
	 * 更新整个实体
	 *
	 * @param mfb
	 * @throws com.seeyon.v3x.dee.TransformException
	 */
	public int update(MetaFlowBean mfb) throws TransformException {
		String sql = "update "
				+ TABLE_NAME_METAFLOW
				+ " set metaflow_name=?, metaflow_code=?  where metaflow_id=?";
		Connection conn = getConnection();
		PreparedStatement pst = null;
		try {
			pst = conn.prepareStatement(sql);
			int count = 1;
			if (mfb.getMetaflow_id() != null
					&& !"".equals(mfb.getMetaflow_id().trim())) {
				pst.setString(count++, mfb.getMetaflow_name());
				pst.setString(count++, mfb.getMetaflow_code());
				pst.setString(count++, mfb.getMetaflow_id());
				return pst.executeUpdate();
			}
		} catch (Throwable e) {
			log.error(e.getMessage()+"error sql=" + sql, e);
			throw new TransformException(e);
		} finally {
			DBUtil.close(null, pst, conn);
		}
		return 0;
	}

	/**
	 * 通过metaflow_id更新某一个元数据metaflow_code
	 *
	 * @param mfb
	 * @throws com.seeyon.v3x.dee.TransformException
	 */
	public int updateMetaFlowCode(MetaFlowBean mfb) throws TransformException {
		String sql = "update " + TABLE_NAME_METAFLOW
				+ " set metaflow_code=? where metaflow_id=?";
		Connection conn = getConnection();
		PreparedStatement pst = null;
		try {
			pst = conn.prepareStatement(sql);
			int count = 1;
			if (mfb.getMetaflow_id() != null
					&& !"".equals(mfb.getMetaflow_id().trim())) {
				pst.setString(count++, mfb.getMetaflow_code());
				pst.setString(count++, mfb.getMetaflow_id());
				return pst.executeUpdate();
			}
		} catch (Throwable e) {
			log.error(e.getMessage()+"error sql=" + sql, e);
			throw new TransformException(e);
		} finally {
			DBUtil.close(null, pst, conn);
		}
		return 0;
	}

	/**
	 * 删除
	 *
	 * @param id
	 * @return
	 * @throws com.seeyon.v3x.dee.TransformException
	 */
	public int deleteById(String id) throws TransformException {
		return super.delete(TABLE_NAME_METAFLOW, id);
	}

	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 * @throws com.seeyon.v3x.dee.TransformException
	 */
	public int deleteByIds(String[] ids) throws TransformException {
		if (ids == null || ids.length == 0)
			return 0;
		StringBuffer condition = new StringBuffer(" metaflow_id in ( ");
		for (String id : ids) {
			condition.append("'" + id).append("',");
		}
		return super.delete(TABLE_NAME_METAFLOW,
				condition.substring(0, condition.lastIndexOf(",")) + " )");
	}

}
