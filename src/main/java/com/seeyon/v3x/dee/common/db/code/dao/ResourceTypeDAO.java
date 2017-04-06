package com.seeyon.v3x.dee.common.db.code.dao;

import com.seeyon.v3x.dee.TransformException;
import com.seeyon.v3x.dee.common.base.dao.BaseDAO;
import com.seeyon.v3x.dee.common.base.util.DBUtil;
import com.seeyon.v3x.dee.common.db.code.model.ResourceTypeBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ResourceTypeDAO extends BaseDAO {
	private final static String TABLE_NAME_RESOURCE_TYPE = "dee_cod_resourcetype";
	private static Log log = LogFactory.getLog(ResourceTypeDAO.class);

	public List<ResourceTypeBean> findAll() throws TransformException {
		Connection conn = getConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		String sql = "select type_id,type_name from "
				+ TABLE_NAME_RESOURCE_TYPE;
		List<ResourceTypeBean> drb = new ArrayList<ResourceTypeBean>();
		try {
			pst = conn.prepareStatement(sql);
			rs = pst.executeQuery();
			while (rs.next()) {
				ResourceTypeBean bean = new ResourceTypeBean();
				bean.setType_id(rs.getString("type_id"));
				bean.setType_name(rs.getString("type_name"));
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
}
