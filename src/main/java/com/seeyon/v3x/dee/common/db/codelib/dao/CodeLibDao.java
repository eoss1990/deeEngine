package com.seeyon.v3x.dee.common.db.codelib.dao;

import com.seeyon.v3x.dee.TransformException;
import com.seeyon.v3x.dee.common.base.dao.BaseDAO;
import com.seeyon.v3x.dee.common.base.util.DBUtil;
import com.seeyon.v3x.dee.common.db.codelib.model.CodeLibBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangfb
 */
public class CodeLibDao extends BaseDAO {
    private static Log log = LogFactory.getLog(CodeLibDao.class);

    public Map<String, CodeLibBean> findAll() throws TransformException {
        Connection conn = getConnection();
        PreparedStatement pst = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM dee_code_lib";
        Map<String, CodeLibBean> beans = new HashMap<String, CodeLibBean>();
        try {
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                CodeLibBean bean = new CodeLibBean();
                bean.setId(rs.getString("id"));
                bean.setClassName(rs.getString("class_name"));
                bean.setPkgName(rs.getString("pkg_name"));
                bean.setSimpleDesc(rs.getString("simple_desc"));
                bean.setCode(rs.getString("code"));
                bean.setCreateTime(rs.getString("create_time"));
                bean.setModifyTime(rs.getString("modify_time"));
                beans.put(bean.getId(), bean);
            }
            return beans;
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
            throw new TransformException(e);
        } finally {
            DBUtil.close(rs, pst, conn);
        }
    }
}
