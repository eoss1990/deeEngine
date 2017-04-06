package com.seeyon.v3x.dee.common.db.download.dao;

import com.seeyon.v3x.dee.TransformException;
import com.seeyon.v3x.dee.common.base.dao.BaseDAO;
import com.seeyon.v3x.dee.common.base.util.DBUtil;
import com.seeyon.v3x.dee.common.base.util.UuidUtil;
import com.seeyon.v3x.dee.common.db.download.model.DownloadBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DownloadDAO extends BaseDAO {
	private final static String TABLE_NAME_DOWNLOAD = "dee_download";
	private static Log log = LogFactory.getLog(DownloadDAO.class);

	public String create(DownloadBean bean, String resource_id)
			throws TransformException {
		StringBuffer sql = new StringBuffer();
		String uuid = UuidUtil.uuid();
		sql.append("INSERT INTO " + TABLE_NAME_DOWNLOAD
				+ " (DOWNLOAD_ID,RESOURCE_ID,FILENEME,CONTENT) VALUES (?,?,?,?)");
		super.executeUpdate(sql.toString(),uuid,resource_id,bean.getFILENEME(),bean.getCONTENT());
		return uuid;
	}

	public DownloadBean getById(String resource_id) throws TransformException {
		Connection conn = getConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		String sql = "SELECT DOWNLOAD_ID,RESOURCE_ID,FILENEME,CONTENT FROM "
				+ TABLE_NAME_DOWNLOAD + " WHERE RESOURCE_ID = '" + resource_id
				+ "'";
		try {
			pst = conn.prepareStatement(sql);
			rs = pst.executeQuery();
			if (rs.next()) {
				DownloadBean dBean = rs2bean(rs);
				return dBean;
			}
		} catch (Throwable e) {
			log.error(e.getMessage(), e);
			throw new TransformException(e);
		} finally {
			DBUtil.close(rs, pst, conn);
		}
		return null;
	}

	private DownloadBean rs2bean(ResultSet rs) throws SQLException {
		DownloadBean bean = new DownloadBean();
		bean.setCONTENT(rs.getString("CONTENT"));
//		bean.setCONTENT(DBUtil.getClobString(rs.getClob("CONTENT")));
		bean.setDOWNLOAD_ID(rs.getString("DOWNLOAD_ID"));
		bean.setFILENEME(rs.getString("FILENEME"));
		bean.setRESOURCE_ID(rs.getString("RESOURCE_ID"));
		return bean;
	}

	public void update(DownloadBean bean) throws TransformException {
		String sql = "update " + TABLE_NAME_DOWNLOAD + " set CONTENT=? where DOWNLOAD_ID =?";
		super.executeUpdate(sql,bean.getCONTENT(),bean.getDOWNLOAD_ID());
	}
}
