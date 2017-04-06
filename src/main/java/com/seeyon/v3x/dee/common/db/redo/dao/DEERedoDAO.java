package com.seeyon.v3x.dee.common.db.redo.dao;

import com.seeyon.v3x.dee.Parameters;
import com.seeyon.v3x.dee.TransformException;
import com.seeyon.v3x.dee.common.base.dao.BaseDAO;
import com.seeyon.v3x.dee.common.base.page.Page;
import com.seeyon.v3x.dee.common.base.util.DBUtil;
import com.seeyon.v3x.dee.common.base.util.UuidUtil;
import com.seeyon.v3x.dee.common.db.redo.model.RedoBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DEERedoDAO extends BaseDAO {
	private final static String TABLE_NAME_REDO = "dee_redo";
	private final static String TABLE_NAME_SYNC = "dee_sync_history";
	private static Log log = LogFactory.getLog(DEERedoDAO.class);

	public RedoBean findById(String id) throws TransformException {
		Connection conn = getConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		String sql = "select redo_id,redo_sid,writer_name,doc_code,flow_id,para,counter,state_flag,sync_id,errormsg from "
				+ TABLE_NAME_REDO + " where redo_id=? order by redo_sid";
		try {
			pst = conn.prepareStatement(sql);
			pst.setString(1, id);
			rs = pst.executeQuery();
			List<RedoBean> list = this.rs2bean(rs);
			if (list.size() > 0)
				return list.get(0);
			else
				return null;
		} catch (Throwable e) {
			log.error(e.getMessage(), e);
			throw new TransformException(e);
		} finally {
			DBUtil.close(rs, pst, conn);
		}
	}

	private Parameters getPara(byte[] bytes) throws IOException,
			ClassNotFoundException {
		ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
		ObjectInputStream ois = new ObjectInputStream(bis);
		Parameters para = (Parameters) ois.readObject();
		ois.close();
		bis.close();
		return para;
	}

	public List<RedoBean> findAll(RedoBean bean) throws TransformException {
		List<RedoBean> list;
		Connection conn = getConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer("select redo_id,redo_sid,writer_name,doc_code,flow_id,para,counter,state_flag,sync_id,errormsg from "
				+ TABLE_NAME_REDO +" where 1=1 ");
		if (bean != null) {
			if (bean.getSync_id() != null
					&& !"".equals(bean.getSync_id().trim()))
				sql.append(" and sync_id='" + bean.getSync_id() + "' ");
			if (bean.getFlow_id() != null
					&& !"".equals(bean.getFlow_id().trim()))
				sql.append(" and flow_id='" + bean.getFlow_id() + "' ");
			if (bean.getState_flag() != null
					&& !"".equals(bean.getState_flag().trim()))
				sql.append(" and state_flag='" + bean.getState_flag() + "' ");
		}
		sql.append(" order by redo_sid");
		try {
			pst = conn.prepareStatement(sql.toString());
			rs = pst.executeQuery();
			list = this.rs2bean(rs);
		} catch (Throwable e) {
			log.error(e.getMessage(), e);
			throw new TransformException(e);
		} finally {
			DBUtil.close(rs, pst, conn);
		}
		return list;
	}
	
	public String insert(RedoBean bean) throws TransformException {
//		bean.setRedo_id(UuidUtil.uuid());
		String sql = " insert into "
				+ TABLE_NAME_REDO
				+ " (redo_id,redo_sid,writer_name,doc_code,flow_id,para,counter,state_flag,sync_id,errormsg)values(?,?,?,?,?,?,?,?,?,?)";
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			if (bean.getRedo_id() == null
					|| "".equals(bean.getRedo_id().trim())) {
				bean.setRedo_id(UuidUtil.uuid());
			}
			else{
				RedoBean newBean = this.findById(bean.getRedo_id());
				if(newBean != null){
					bean.setCounter(newBean.getCounter()+1);
					this.update(bean);
					return bean.getRedo_id();
				}
			}
			conn = getConnection();
			pst = conn.prepareStatement(sql);
			int count = 1;
			
			pst.setString(count++, bean.getRedo_id());
			pst.setInt(count++, bean.getRedo_sid());
			pst.setString(count++, bean.getWriter_name());
			pst.setString(count++, bean.getDoc_code());
			pst.setString(count++, bean.getFlow_id());
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(bean.getPara());
			pst.setBytes(count++, bos.toByteArray());
			pst.setInt(count++, bean.getCounter());
			pst.setString(count++, bean.getState_flag());
			pst.setString(count++, bean.getSync_id());
			if(bean.getErrormsg() != null && bean.getErrormsg().length() > 1024){
					pst.setString(count++, bean.getErrormsg().substring(0,1024));
			}
			else
				pst.setString(count++, bean.getErrormsg());
				
			oos.close();

			pst.executeUpdate();
			return bean.getRedo_id();
		} catch (Throwable e) {
			log.error(e.getMessage() + "error sql=" + sql, e);
			throw new TransformException(e);
		} finally {
			DBUtil.close(null, pst, conn);
		}
	}

	public Page query(Page page, RedoBean bean) throws TransformException {
		StringBuffer sql = new StringBuffer();
		sql.append("select redo_id,redo_sid,writer_name,doc_code,flow_id,para,counter,state_flag,sync_id,errormsg from "
				+ TABLE_NAME_REDO + " where 1=1 ");
		if (bean != null) {
			if (bean.getSync_id() != null
					&& !"".equals(bean.getSync_id().trim()))
				sql.append(" and sync_id='" + bean.getSync_id() + "' ");
			if (bean.getFlow_id() != null
					&& !"".equals(bean.getFlow_id().trim()))
				sql.append(" and flow_id='" + bean.getFlow_id() + "' ");
			if (bean.getState_flag() != null
					&& !"".equals(bean.getState_flag().trim()))
				sql.append(" and state_flag='" + bean.getState_flag() + "' ");
		}
		page.setTotalCount(super.getCount(sql.toString()));
		Connection conn = getConnection();
		try {
			Statement stmt = conn.createStatement();
			stmt.setMaxRows(page.getPageNo() * page.getPageSize());// 关键代码，设置最大记录数为当前页记录的截止下标
			sql.append(" order by redo_sid");
			ResultSet rs = stmt.executeQuery(sql.toString());
			if (page.getPageNo() * page.getPageSize() > 0) {
				try {
					if (1 == page.getPageNo()) {
						rs.beforeFirst();
					} else {
						rs.absolute(page.getPrePage() * page.getPageSize());
					}
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
				page.setResult(this.rs2bean(rs));
			}
			return page;
		} catch (Throwable e) {
			log.error(e.getMessage(), e);
			throw new TransformException(e);
		} finally {
			try {
				conn.close();
			} catch (Throwable e) {
			}
		}
	}

	public void update(RedoBean bean) throws TransformException, IOException {
		if (bean != null) {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(bean.getPara());
			String sql = "update "
					+ TABLE_NAME_REDO
					+ " set writer_name=?,doc_code=?,flow_id=?,para=?,counter=?,state_flag=? where redo_id=?";
			super.executeUpdate(sql, bean.getWriter_name(), bean.getDoc_code(),
					bean.getFlow_id(), bos.toByteArray(), bean.getCounter(),
					bean.getState_flag(), bean.getRedo_id());
			oos.close();
		}
	}

	public void updateCount(String id, int counter) throws TransformException {
		String sql = " update " + TABLE_NAME_REDO
				+ " set counter=? where redo_id=? ";
		Connection conn = getConnection();
		PreparedStatement pst = null;
		try {
			int i = 1;
			pst = conn.prepareStatement(sql);
			pst.setInt(i++, counter);
			pst.setString(i++, id);
			pst.executeUpdate();
		} catch (Throwable e) {
			log.error(e.getMessage() + "error sql=" + sql, e);
			throw new TransformException(e);
		} finally {
			DBUtil.close(null, pst, conn);
		}
	}
	
	/**
	 * 根据ID直接counter+1<br>
	 * 不需要传入counter值，直接通过sql的counter=counter+1增加
	 * @param id
	 * @throws com.seeyon.v3x.dee.TransformException
	 * @author lilong
	 * @date 2012-07-04
	 */
	public void updateCountById(String id) throws TransformException {
		String sql = " UPDATE " + TABLE_NAME_REDO
				+ " SET COUNTER = COUNTER + 1 WHERE REDO_ID = ? ";
		Connection conn = getConnection();
		PreparedStatement pst = null;
		try {
			int i = 1;
			pst = conn.prepareStatement(sql);
			pst.setString(i++, id);
			pst.executeUpdate();
		} catch (Throwable e) {
			log.error("error sql=" + sql + e.getMessage(), e);
			throw new TransformException(e);
		} finally {
			DBUtil.close(null, pst, conn);
		}
	}

	public void updateState(String id, String suc) throws TransformException {
		String sql = " update " + TABLE_NAME_REDO
				+ " set state_flag=? where redo_id=? ";
		Connection conn = getConnection();
		PreparedStatement pst = null;
		try {
			int i = 1;
			pst = conn.prepareStatement(sql);
			pst.setString(i++, suc);
			pst.setString(i++, id);
			pst.executeUpdate();
		} catch (Throwable e) {
			log.error(e.getMessage() + "error sql=" + sql, e);
			throw new TransformException(e);
		} finally {
			DBUtil.close(null, pst, conn);
		}
	}
	
	@SuppressWarnings("resource")
    public int delSelect(String syncId,String ids) throws TransformException {
//		String sSql = "delete from " + TABLE_NAME_REDO + " where redo_id in(?)";
		String sSql = "delete from " + TABLE_NAME_REDO + " where redo_id=";
		String[] syIds = ids.split(",");
		int isDelAll = 0; //是否删除所有记录
		Connection conn = getConnection();
		PreparedStatement pst = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
//			pst = conn.prepareStatement(sSql);
//			pst.setString(1, ids);
//			pst.execute();
			stmt = conn.createStatement();
			for(String syId:syIds){
				stmt.addBatch(sSql+"'"+syId+"';");
			}
			stmt.executeBatch();
			
			pst = conn.prepareStatement("select sync_id from " + TABLE_NAME_REDO + " where sync_id=?");
			pst.setString(1, syncId);
			rs = pst.executeQuery();
			
			if(!rs.next()){
				pst = conn.prepareStatement("delete from " + TABLE_NAME_SYNC + " where sync_id=?");
				pst.setString(1, syncId);
				pst.executeUpdate();
				isDelAll = 1;
			}
			return isDelAll;
		} catch (Throwable e) {
			log.error(e.getMessage() + "error sql=" + sSql, e);
			throw new TransformException(e);
		} finally {
			try {
				if(stmt != null)
					stmt.close();
			} catch (Throwable e) {
				log.error(e.getMessage() + "error close Statement", e);
				throw new TransformException(e);
			}
			DBUtil.close(rs, pst, conn);
		}
	}
	public void delAll(String ids) throws TransformException {
//		String sSql = "delete from " + TABLE_NAME_REDO + " where sync_id in(?)";
		String sSql = "delete from " + TABLE_NAME_REDO + " where sync_id=";
		String[] syIds = ids.split(",");
		Connection conn = getConnection();
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			for(String syId:syIds){
				stmt.addBatch(sSql+"'"+syId+"';");
			}
			stmt.executeBatch();
			
			sSql = "delete from " + TABLE_NAME_SYNC + " where sync_id=";
			stmt = conn.createStatement();
			for(String syId:syIds){
				stmt.addBatch(sSql+"'"+syId+"';");
			}
			stmt.executeBatch();
			
		} catch (Throwable e) {
			log.error(e.getMessage() + "error sql=" + sSql, e);
			throw new TransformException(e);
		} finally {
			try {
				if(stmt != null)
					stmt.close();
			} catch (Throwable e) {
				log.error(e.getMessage() + "error close Statement", e);
				throw new TransformException(e);
			}
			DBUtil.close(null, null, conn);
		}
	}

	public void delLogs(String date) throws TransformException {
		String sql = "delete from " + TABLE_NAME_REDO + " where sync_id in "
				+ "(select sync_id from " + TABLE_NAME_SYNC + " where sync_time < '" 
				+ date + "')";
		Connection conn = getConnection();
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			stmt.execute(sql);
			
			sql = "delete from " + TABLE_NAME_SYNC + " where sync_time < '" + date + "'";
			stmt.execute(sql);
		} catch (Throwable e) {
			log.error(e.getMessage() + "error sql=" + sql, e);
			throw new TransformException(e);
		} finally {
			try {
				if(stmt != null)
					stmt.close();
			} catch (Throwable e) {
				log.error(e.getMessage() + "error close Statement", e);
				throw new TransformException(e);
			}
			DBUtil.close(null, null, conn);
		}
	}
	
	public String findLogByDate(String date) throws TransformException {
		Connection conn = getConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		String sql = "select flow_id, sync_id from " + TABLE_NAME_SYNC + " where sync_time < ?";
		try {
			pst = conn.prepareStatement(sql);
			pst.setString(1, date);
			rs = pst.executeQuery();
			String res = "";
			while (rs.next()) {
				String flow_id = rs.getString("flow_id");
				String sync_id = rs.getString("sync_id");
				if("".equals(res)){
					res = flow_id + "_" + sync_id;
				}else{
					res = res + "," + flow_id + "_" + sync_id;
				}
			}
			return res;
		} catch (Throwable e) {
			log.error(e.getMessage(), e);
			throw new TransformException(e);
		} finally {
			DBUtil.close(rs, pst, conn);
		}
	}
	
	private List<RedoBean> rs2bean(ResultSet rs) throws SQLException,
			IOException, ClassNotFoundException {
		List<RedoBean> list = new ArrayList<RedoBean>();
		while (rs.next()) {
			RedoBean bean = new RedoBean();
			bean.setCounter(rs.getInt("counter"));
			bean.setDoc_code(DBUtil.getClobString(rs.getClob("doc_code")));
			bean.setFlow_id(rs.getString("flow_id"));
			bean.setPara(getPara(rs.getBytes("para")));
			bean.setRedo_id(rs.getString("redo_id"));
			bean.setRedo_sid(rs.getInt("redo_sid"));
			bean.setState_flag(rs.getString("state_flag"));
			bean.setWriter_name(rs.getString("writer_name"));
			bean.setSync_id(rs.getString("sync_id"));
			bean.setErrormsg(rs.getString("errormsg"));
			list.add(bean);
		}
		return list;
	}

}
