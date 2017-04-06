package com.seeyon.v3x.dee.common.db.resource.dao;

import com.seeyon.v3x.dee.TransformException;
import com.seeyon.v3x.dee.common.base.dao.BaseDAO;
import com.seeyon.v3x.dee.common.base.util.DBUtil;
import com.seeyon.v3x.dee.common.db.resource.model.DeeResourceTemplateBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DeeResourceTemplateDAO extends BaseDAO {

	private final static String DEE_RESOURCE_TEMPLATE = "dee_resource_template";
	private static Log log = LogFactory.getLog(DeeResourceTemplateDAO.class);

	public DeeResourceTemplateBean getOneById(String id) throws TransformException {
		Connection conn = getConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		String sql = "SELECT RESOURCE_TEMPLATE_ID, RESOURCE_TEMPLATE_NAME, TYPE_ID, TEMPLATE FROM "
				+ DEE_RESOURCE_TEMPLATE
				+ " WHERE RESOURCE_TEMPLATE_ID = '"
				+ id + "'";
		try {
			pst = conn.prepareStatement(sql);
			rs = pst.executeQuery();
			if (rs.next()) {
				DeeResourceTemplateBean bean = rs2bean(rs);
				return bean;
			}
		} catch (Throwable e) {
			log.error(e.getMessage(), e);
			throw new TransformException(e);
		} finally {
			DBUtil.close(rs, pst, conn);
		}
		return null;

	}

	private DeeResourceTemplateBean rs2bean(ResultSet rs) throws SQLException {
		DeeResourceTemplateBean bean = new DeeResourceTemplateBean();
		bean.setResource_template_id(rs.getString("RESOURCE_TEMPLATE_ID"));
		bean.setResource_template_name(rs.getString("RESOURCE_TEMPLATE_NAME"));
		bean.setType_id(rs.getString("TYPE_ID"));
		bean.setTemplate(DBUtil.getClobString(rs.getClob("TEMPLATE")));
		return bean;
	}

}
