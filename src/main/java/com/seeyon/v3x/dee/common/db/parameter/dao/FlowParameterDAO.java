package com.seeyon.v3x.dee.common.db.parameter.dao;

import com.seeyon.v3x.dee.TransformException;
import com.seeyon.v3x.dee.common.base.dao.BaseDAO;
import com.seeyon.v3x.dee.common.base.util.DBUtil;
import com.seeyon.v3x.dee.common.base.util.UuidUtil;
import com.seeyon.v3x.dee.common.db.parameter.model.ParameterBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FlowParameterDAO extends BaseDAO {
	private final static String TABLE_NAME_PARAMETER = "dee_flow_parameter";
    private static Log log = LogFactory.getLog(FlowParameterDAO.class);

	public void create(ParameterBean bean) throws TransformException {
//		StringBuffer sql = new StringBuffer();
		String uuid = UuidUtil.uuid();
/*		sql.append("INSERT INTO "
				+ TABLE_NAME_PARAMETER
				+ " (PARA_ID,FLOW_ID,DIS_NAME,PARA_NAME,PARA_VALUE,PARA_DESC) VALUES ('"
				+ uuid + "','" + bean.getFLOW_ID() + "','" + bean.getDIS_NAME()
				+ "','" + bean.getPARA_NAME() + "','" + bean.getPARA_VALUE()
				+ "','" + bean.getPARA_DESC() + "')");
		super.execute(sql.toString());*/
		String sql = "INSERT INTO "
				+ TABLE_NAME_PARAMETER
				+ " (PARA_ID,FLOW_ID,DIS_NAME,PARA_NAME,PARA_VALUE,PARA_DESC) VALUES (?,?,?,?,?,?)";
		super.executeUpdate(sql, uuid,bean.getFLOW_ID(),bean.getDIS_NAME() ,bean.getPARA_NAME(),bean.getPARA_VALUE(),bean.getPARA_DESC());
		bean.setFLOW_ID(uuid);
	}

    public void create(List<ParameterBean> beanlist,String flow_id)throws TransformException {
        StringBuffer sql = new StringBuffer();
        List<Object> l = new ArrayList<Object>();
        sql.append("INSERT INTO "
                + TABLE_NAME_PARAMETER
                + " (PARA_ID,FLOW_ID,DIS_NAME,PARA_NAME,PARA_VALUE,PARA_DESC) VALUES ");
        for(ParameterBean pb:beanlist){
            sql.append("(?,?,?,?,?,?),");
            l.add(UuidUtil.uuid());
            l.add(flow_id);
            l.add(pb.getDIS_NAME());
            l.add(pb.getPARA_NAME());
            l.add(pb.getPARA_VALUE());
            l.add(pb.getPARA_DESC());
        }
        super.executeUpdate(sql.substring(0,sql.lastIndexOf(",")),l.toArray());
    }
	public void delete(String id) throws TransformException {
		super.delete(TABLE_NAME_PARAMETER, "para_id='" + id + "'");
	}
	public void deleteByFlowId(String flow_id) throws TransformException {
        super.delete(TABLE_NAME_PARAMETER, "flow_id='" + flow_id + "'");
    }

	public List<ParameterBean> getById(String flow_id) throws TransformException {
        Connection conn = getConnection();
        PreparedStatement pst = null;
        ResultSet rs = null;
		String sql = "SELECT PARA_ID,FLOW_ID,PARA_NAME,PARA_VALUE,PARA_DESC,DIS_NAME FROM "
				+ TABLE_NAME_PARAMETER + " where FLOW_ID = '" + flow_id + "'";
		List<ParameterBean> paraList = new ArrayList<ParameterBean>();
        try {
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while(rs.next()) {
            	paraList.add(rs2bean(rs));
            }
            return paraList;
        } catch(Throwable e) {
            log.error(e.getMessage(), e);
            throw new TransformException(e);
        }
        finally {
            DBUtil.close(rs, pst, conn);
        }
	}
	
	private ParameterBean rs2bean(ResultSet rs)throws SQLException {
	    ParameterBean bean = new ParameterBean();
        bean.setDIS_NAME(rs.getString("DIS_NAME"));
        bean.setFLOW_ID(rs.getString("FLOW_ID"));
        bean.setPARA_DESC(rs.getString("PARA_DESC"));
        bean.setPARA_ID(rs.getString("PARA_ID"));
        bean.setPARA_NAME(rs.getString("PARA_NAME"));
        bean.setPARA_VALUE(rs.getString("PARA_VALUE"));
        //TODO
        return bean;
	}
}
