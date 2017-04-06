package com.seeyon.v3x.dee.common.db.redo.dao;

import com.seeyon.v3x.dee.TransformException;
import com.seeyon.v3x.dee.common.base.dao.BaseDAO;
import com.seeyon.v3x.dee.common.base.page.Page;
import com.seeyon.v3x.dee.common.base.util.DBUtil;
import com.seeyon.v3x.dee.common.base.util.UuidUtil;
import com.seeyon.v3x.dee.common.db.redo.model.FormFlowBean;
import com.seeyon.v3x.dee.common.db.redo.model.SyncBean;
import com.seeyon.v3x.dee.common.db.redo.model.SyncBeanLog;
import com.seeyon.v3x.dee.common.db.redo.util.SyncState;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DEESyncDAO extends BaseDAO {
	private final static String TABLE_NAME_SYNC = "dee_sync_history";
	private final static String TABLE_NAME_FLOW = "dee_flow";
	private final static String TABLE_NAME_REDO = "dee_redo";
	private final static String TABLE_NAME_FORM_FLOW = "form_flow_history";
	private static Log log = LogFactory.getLog(DEESyncDAO.class);

	public SyncBean findById(String syncId) throws TransformException {
		Connection conn = getConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		String sSql = "select a.flow_id,b.dis_name,a.sync_id,a.sync_state,a.sync_time from "+TABLE_NAME_SYNC+" a "
		+ "left join "+TABLE_NAME_FLOW+" b on a.flow_id = b.flow_id where a.sync_id=?";
		try {
			pst = conn.prepareStatement(sSql);
			pst.setString(1, syncId);
			rs = pst.executeQuery();
			List<SyncBean> syncList = rs2Bean(rs);
			if (syncList != null && syncList.size() > 0)
				return syncList.get(0);
			else
				return null;
		} catch (Throwable e) {
			log.error(e.getMessage(), e);
			throw new TransformException(e);
		} finally {
			DBUtil.close(rs, pst, conn);
		}
	}
	
	/**
	 * 根据重发ID反向查询同步日志对象
	 * @param redoId
	 * @return
	 * @throws com.seeyon.v3x.dee.TransformException
	 * @date 2012-07-04
	 */
	public SyncBean findSyncBeanByRedoId(String redoId) throws TransformException {
		Connection conn = getConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		sql.append(" select a.flow_id,b.dis_name,a.sync_id,a.sync_state,a.sync_time,r.redo_id,r.counter from ");
		sql.append(TABLE_NAME_SYNC).append(" a ").append(" left join ");
		sql.append(TABLE_NAME_FLOW).append(" b on a.flow_id = b.flow_id ").append(" left join ");
		sql.append(TABLE_NAME_REDO).append(" r on r.sync_id = a.sync_id where r.redo_id=?");
		try {
			pst = conn.prepareStatement(sql.toString());
			pst.setString(1, redoId);
			rs = pst.executeQuery();
			List<SyncBean> syncList = rs2Bean(rs);
			if (syncList != null && syncList.size() > 0)
				return syncList.get(0);
			else
				return null;
		} catch (Throwable e) {
			log.error(e.getMessage(), e);
			throw new TransformException(e);
		} finally {
			DBUtil.close(rs, pst, conn);
		}
	}
	
	public String findLastSucessTimeByFlowID(String flowid) throws TransformException {
		String sql = "select sync_time from " + TABLE_NAME_SYNC
				+ " where flow_id='" + flowid + "' and sync_state="
				+ SyncState.STATE_FLAG_SUCESS.ordinal()
				+ " order by sync_time desc limit 0,1";
		Connection conn = getConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			pst = conn.prepareStatement(sql);
			rs = pst.executeQuery();
			while(rs.next()){
				return rs.getString("sync_time");
			}
		} catch (Throwable e) {
			log.error(e.getMessage(), e);
			throw new TransformException(e);
		} finally {
			DBUtil.close(rs, pst, conn);
		}
		return null;
	}
	public List<SyncBean> findAll(SyncBean bean) throws TransformException {
		List<SyncBean> sList = null;
		Connection conn = getConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		StringBuffer sSql = new StringBuffer("select a.flow_id,b.dis_name,a.sync_id,a.sync_state,a.sync_time from ");
		sSql.append(TABLE_NAME_SYNC + " a ");
		sSql.append("left join ").append(TABLE_NAME_FLOW);
		sSql.append(" b on a.flow_id = b.flow_id where 1=1 ");
		if (bean != null) {
			if (bean.getSync_id() != null
					&& !"".equals(bean.getSync_id().trim()))
				sSql.append(" and sync_id='" + bean.getSync_id() + "' ");
			if (bean.getFlow_id() != null
					&& !"".equals(bean.getFlow_id().trim()))
				sSql.append(" and flow_id='" + bean.getFlow_id() + "' ");
		}
		sSql.append(" order by a.sync_time desc");
		try {
			pst = conn.prepareStatement(sSql.toString());
			rs = pst.executeQuery();
			sList = rs2Bean(rs);
		} catch (Throwable e) {
			log.error(e.getMessage(), e);
			throw new TransformException(e);
		} finally {
			DBUtil.close(rs, pst, conn);
		}
		return sList;
	}
	
	public String insert(SyncBean bean) throws TransformException {
		String sSql = " insert into "
				+ TABLE_NAME_SYNC
				+ " (sync_id,sender_name,target_name,sync_mode,sync_state,sync_time,flow_id)values(?,'','',-1,?,?,?)";
		Connection conn = null;
		PreparedStatement pst = null;
		try {
			if (bean.getSync_id() == null
					|| "".equals(bean.getSync_id().trim())) {
				bean.setSync_id(UuidUtil.uuid());
			}
			else{
				SyncBean newBean = this.findById(bean.getSync_id());
				if(newBean != null){
					return bean.getSync_id();
				}
			}
			conn = getConnection();
			pst = conn.prepareStatement(sSql);
			int count = 1;
			pst.setString(count++, bean.getSync_id());
			pst.setInt(count++, bean.getSync_state());
			pst.setString(count++, bean.getSync_time());
			pst.setString(count++, bean.getFlow_id());

			pst.executeUpdate();
			return bean.getSync_id();
		} catch (Throwable e) {
			log.error(e.getMessage() + "error sql=" + sSql, e);
			throw new TransformException(e);
		} finally {
			DBUtil.close(null, pst, conn);
		}
	}
	
	public String insertNewLogs(SyncBean bean, FormFlowBean ffb) throws TransformException {
		String sSql = " insert into "
				+ TABLE_NAME_SYNC
				+ " (sync_id,sender_name,target_name,sync_mode,sync_state,sync_time,flow_id)values(?,'','',-1,?,?,?)";
		Connection conn = null;
		PreparedStatement pst = null;
		try {
			if (bean.getSync_id() == null
					|| "".equals(bean.getSync_id().trim())) {
				bean.setSync_id(UuidUtil.uuid());
			}
			else{
				SyncBean newBean = this.findById(bean.getSync_id());
				if(newBean != null){
					return bean.getSync_id();
				}
			}
			conn = getConnection();
			pst = conn.prepareStatement(sSql);
			int count = 1;
			pst.setString(count++, bean.getSync_id());
			pst.setInt(count++, bean.getSync_state());
			pst.setString(count++, bean.getSync_time());
			pst.setString(count++, bean.getFlow_id());
			pst.executeUpdate();
			
			sSql = " insert into " + TABLE_NAME_FORM_FLOW + " (flow_sync_id, form_flow_id, "
					+ "form_flow_name, operate_person, flow_action)values(?,?,?,?,?)";
			pst = conn.prepareStatement(sSql);
			count = 1;
			pst.setString(count++, bean.getSync_id());
			pst.setString(count++, ffb.getForm_flow_id());
			pst.setString(count++, ffb.getForm_flow_name());
			pst.setString(count++, ffb.getOperate_person());
			pst.setString(count++, ffb.getFlow_action());
			pst.executeUpdate();
			
			return bean.getSync_id();
		} catch (Throwable e) {
			log.error(e.getMessage() + "error sql=" + sSql, e);
			throw new TransformException(e);
		} finally {
			DBUtil.close(null, pst, conn);
		}
	}
	
	public String update(SyncBean bean) throws TransformException {
		String sSql = " update "
				+ TABLE_NAME_SYNC
				+ " set sync_state=? where sync_id=?";
		Connection conn = getConnection();
		PreparedStatement pst = null;
		try {
			pst = conn.prepareStatement(sSql);
			int count = 1;
			pst.setInt(count++, bean.getSync_state());
			pst.setString(count++, bean.getSync_id());

			pst.executeUpdate();
			return bean.getSync_id();
		} catch (Throwable e) {
			log.error(e.getMessage() + "error sql=" + sSql, e);
			throw new TransformException(e);
		} finally {
			DBUtil.close(null, pst, conn);
		}
	}
	
	public String updateAll(SyncBean bean) throws TransformException {
		String sSql = " update "
				+ TABLE_NAME_SYNC
				+ " set sender_name='',target_name='',sync_mode=-1,sync_state=?,sync_time=?,flow_id=? where sync_id=?";
		Connection conn = null;
		PreparedStatement pst = null;
		try {
			conn = getConnection();
			pst = conn.prepareStatement(sSql);
			int count = 1;
			pst.setInt(count++, bean.getSync_state());
			pst.setString(count++, bean.getSync_time());
			pst.setString(count++, bean.getFlow_id());
			pst.setString(count++, bean.getSync_id());

			pst.executeUpdate();
			return bean.getSync_id();
		} catch (Throwable e) {
			log.error(e.getMessage() + "error sql=" + sSql, e);
			throw new TransformException(e);
		} finally {
			DBUtil.close(null, pst, conn);
		}
	}

	public Page query(Page page, SyncBean bean) throws TransformException {
		StringBuffer sSql = new StringBuffer("select a.flow_id,b.dis_name,a.sync_id,a.sync_state,a.sync_time from ");
		sSql.append(TABLE_NAME_SYNC + " a ");
		sSql.append("left join ").append(TABLE_NAME_FLOW);
		sSql.append(" b on a.flow_id = b.flow_id where 1=1 ");
		if (bean != null) {
			if (bean.getSync_id() != null
					&& !"".equals(bean.getSync_id().trim()))
				sSql.append(" and sync_id='" + bean.getSync_id() + "' ");
			if (bean.getFlow_id() != null
					&& !"".equals(bean.getFlow_id().trim()))
				sSql.append(" and flow_id='" + bean.getFlow_id() + "' ");
		}
		page.setTotalCount(super.getCount(sSql.toString()));
		Connection conn = getConnection();
		try {
			Statement stmt = conn.createStatement();
			stmt.setMaxRows(page.getPageNo() * page.getPageSize());// 关键代码，设置最大记录数为当前页记录的截止下标
			sSql.append(" order by a.sync_time desc");
			ResultSet rs = stmt.executeQuery(sSql.toString());
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
				page.setResult(rs2Bean(rs));
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
	
	public Page queryByCondition(Page page, SyncBeanLog bean) throws TransformException {
		StringBuffer sSql = new StringBuffer("select a.flow_id,b.dis_name,a.sync_id,"
				+ "a.sync_state,a.sync_time,c.redo_id,c.counter,d.form_flow_name,"
				+ "d.operate_person,d.flow_action from ");
		sSql.append(TABLE_NAME_SYNC + " a");
		sSql.append(" left join ").append(TABLE_NAME_FLOW);
		sSql.append(" b on a.flow_id = b.flow_id");
		sSql.append(" left join ").append(TABLE_NAME_REDO);
		sSql.append(" c on a.sync_id = c.sync_id");
		sSql.append(" left join ").append(TABLE_NAME_FORM_FLOW);
		sSql.append(" d on a.sync_id = d.flow_sync_id where 1=1 ");
		if (bean != null) {
			if (bean.getFlow_dis_name() != null
					&& !"".equals(bean.getFlow_dis_name().trim())){
				sSql.append(" and dis_name like '%" + bean.getFlow_dis_name() + "%' ");
			}
			if (bean.getSync_state() == 0 || bean.getSync_state() == 1){
				sSql.append(" and sync_state=" + bean.getSync_state() + " ");
			}
			if (bean.getSync_time()  != null
					&& !"".equals(bean.getSync_time().trim())){
				String[] strs = bean.getSync_time().split(",");
				String startTime = strs[0].replace("[", "").replace("\"", "");
				String endTime = strs[1].replace("]", "").replace("\"", "");
				sSql.append(" and sync_time between '" + startTime + "' and '" + endTime + "' ");
			}
		}
		page.setTotalCount(super.getCount(sSql.toString()));
		Connection conn = getConnection();
		try {
			Statement stmt = conn.createStatement();
			stmt.setMaxRows(page.getPageNo() * page.getPageSize());// 关键代码，设置最大记录数为当前页记录的截止下标
			sSql.append(" order by a.sync_time desc");
			ResultSet rs = stmt.executeQuery(sSql.toString());
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
				page.setResult(rsBeanLog(rs));
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
	
	private List<SyncBean> rs2Bean(ResultSet rs) throws SQLException, ClassNotFoundException {
		List<SyncBean> sList = new ArrayList<SyncBean>();
		while (rs.next()) {
			SyncBean bean = new SyncBean();
			bean.setSync_id(rs.getString("sync_id"));
			bean.setFlow_dis_name(rs.getString("dis_name"));
			bean.setSync_state(rs.getInt("sync_state"));
			bean.setSync_time(rs.getString("sync_time"));
			bean.setFlow_id(rs.getString("flow_id"));
			sList.add(bean);
		}
		return sList;
	}
	
	private List<SyncBeanLog> rsBeanLog(ResultSet rs) throws SQLException, ClassNotFoundException {
		List<SyncBeanLog> sList = new ArrayList<SyncBeanLog>();
		while (rs.next()) {
			SyncBeanLog bean = new SyncBeanLog();
			bean.setRedo_id(rs.getString("redo_id"));
			bean.setCounter(rs.getString("counter") == null ? 0 : Integer.parseInt(rs.getString("counter")));
			bean.setSync_id(rs.getString("sync_id"));
			bean.setFlow_dis_name(rs.getString("dis_name"));
			bean.setSync_state(rs.getInt("sync_state"));
			bean.setSync_time(rs.getString("sync_time"));
			bean.setFlow_id(rs.getString("flow_id"));
			bean.setForm_flow_name(rs.getString("form_flow_name"));
			bean.setOperate_person(rs.getString("operate_person"));
			bean.setFlow_action(rs.getString("flow_action"));
			sList.add(bean);
		}
		return sList;
	}
}
