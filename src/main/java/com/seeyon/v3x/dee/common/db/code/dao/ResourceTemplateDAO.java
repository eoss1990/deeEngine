package com.seeyon.v3x.dee.common.db.code.dao;

import com.seeyon.v3x.dee.TransformException;
import com.seeyon.v3x.dee.common.base.dao.BaseDAO;
import com.seeyon.v3x.dee.common.base.util.DBUtil;
import com.seeyon.v3x.dee.common.db.code.model.ResourceTemplateBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ResourceTemplateDAO extends BaseDAO {
	private final static String TABLE_NAME_RESOURCE_TEMPLATE = "dee_resource_template";
	private static Log log = LogFactory.getLog(ResourceTemplateDAO.class);

	public List<ResourceTemplateBean> findAll() throws TransformException {
		Connection conn = getConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		String sql = "select resource_template_id,resource_template_name,type_id,template from "
				+ TABLE_NAME_RESOURCE_TEMPLATE;
		List<ResourceTemplateBean> drb = new ArrayList<ResourceTemplateBean>();
		try {
			pst = conn.prepareStatement(sql);
			rs = pst.executeQuery();
			while (rs.next()) {
				ResourceTemplateBean bean = new ResourceTemplateBean();
				bean.setResource_template_id(rs
						.getString("resource_template_id"));
				bean.setResource_template_name(rs
						.getString("resource_template_name"));
				bean.setType_id(rs.getString("type_id"));
				bean.setTemplate(rs.getString("template"));
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
