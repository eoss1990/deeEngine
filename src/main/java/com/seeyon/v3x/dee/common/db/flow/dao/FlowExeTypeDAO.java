package com.seeyon.v3x.dee.common.db.flow.dao;

import com.seeyon.v3x.dee.TransformException;
import com.seeyon.v3x.dee.common.base.dao.BaseDAO;
import com.seeyon.v3x.dee.common.base.util.DBUtil;
import com.seeyon.v3x.dee.common.db.flow.model.FlowExeTypeBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 */
public class FlowExeTypeDAO extends BaseDAO {

	private final static String TABLE = "dee_flow_exetype";

	private static Log log = LogFactory.getLog(FlowExeTypeDAO.class);

	/**
	 * 获取所有任务类型。
	 * 
	 * @return
	 * @throws com.seeyon.v3x.dee.TransformException
	 */
	public List<FlowExeTypeBean> getTypeList() throws TransformException {
		Connection conn = getConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		String sql = "select exetype_id,exetype_name from " + TABLE
				+ " order by exetype_id";
		List<FlowExeTypeBean> list = new ArrayList<FlowExeTypeBean>();
		try {
			pst = conn.prepareStatement(sql);
			rs = pst.executeQuery();
			while (rs.next()) {
				FlowExeTypeBean sub = new FlowExeTypeBean();
				sub.setId(rs.getString("exetype_id"));
				sub.setName(rs.getString("exetype_name"));
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
}
