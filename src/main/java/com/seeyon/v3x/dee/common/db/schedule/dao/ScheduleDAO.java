package com.seeyon.v3x.dee.common.db.schedule.dao;

import com.seeyon.v3x.dee.TransformException;
import com.seeyon.v3x.dee.common.base.dao.BaseDAO;
import com.seeyon.v3x.dee.common.base.page.Page;
import com.seeyon.v3x.dee.common.base.util.DBUtil;
import com.seeyon.v3x.dee.common.base.util.Resultset2List;
import com.seeyon.v3x.dee.common.base.util.UuidUtil;
import com.seeyon.v3x.dee.common.db.schedule.model.ScheduleBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

public class ScheduleDAO extends BaseDAO {

	private static Log log = LogFactory.getLog(ScheduleDAO.class);
	private final static String TABLE_NAME = "dee_schedule";

	/**
	 * 按名称取单一Schedule
	 * 
	 * @param id
	 * @return
	 * @throws com.seeyon.v3x.dee.TransformException
	 */
	public ScheduleBean get(String id) throws TransformException {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * from " + TABLE_NAME + " where SCHEDULE_ID='" + id +"'");
		return (ScheduleBean) super.getBeanBySql(sql.toString(),
				ScheduleBean.class);
	}

	/**
	 *
	 * 判断任务中是否引用定时器
	 *
	 * @param flowId
	 * @return
	 * @throws com.seeyon.v3x.dee.TransformException
	 */
    public Boolean select(List<String> flowList) throws TransformException {
        StringBuffer in = new StringBuffer();
        for (String id : flowList) {
            in.append("'").append(id).append("',");
        }
        in.append("''");
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT * from " + TABLE_NAME + " where FLOW_ID in (" + in + ") ");
        boolean flag = false;
        Connection conn = getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(sql.toString());
            rs = stmt.executeQuery();// executeQuery(sql.toString());
            if (rs.next()) {
                flag = true;
            }
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            throw new TransformException(e);
        } finally {
            DBUtil.close(rs, stmt, conn);
        }
        return flag;
    }

    public List<ScheduleBean> queryByFlowId(List<String> flowIdList) throws TransformException {
		StringBuffer in = new StringBuffer();
		for (String id : flowIdList) {
			in.append("'").append(id).append("',");
		}
		in.append("''");
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * from " + TABLE_NAME + " where FLOW_ID in (" + in +")");

		Connection conn = getConnection();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql.toString());
			return Resultset2List.getListFromRS(rs, ScheduleBean.class);
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

	/**
	 * 更新Schedule。
	 *
	 * @param name
	 * @param xml
	 * @throws com.seeyon.v3x.dee.TransformException
	 */
	public ScheduleBean update(ScheduleBean bean) throws TransformException {
		generateScheduleCode(bean);
		String sql = "update " + TABLE_NAME
				+ " set SCHEDULE_DESC = ?,SCHEDULE_CODE =?, DIS_NAME =?,IS_ENABLE=?,QUARTZ_CODE=?,FLOW_ID=?"
				+ " where SCHEDULE_ID=?";
		super.executeUpdate(sql, bean.getSchedule_desc(),
				bean.getSchedule_code(), bean.getDis_name(),bean.getEnable(),bean.getQuartz_code(),bean.getFlow_id(),
				bean.getSchedule_id());
		return this.get(bean.getSchedule_id());
	}

	/**
	 * 新建Schedule。
	 *
	 * @param name
	 * @param xml
	 * @throws com.seeyon.v3x.dee.TransformException
	 */
	public ScheduleBean create(ScheduleBean bean) throws TransformException {
		String uuid = UuidUtil.uuid();
		bean.setSchedule_id(uuid);
		bean.setSchedule_name(uuid);
		generateScheduleCode(bean);
		String sql = "INSERT INTO "
				+ TABLE_NAME
				+ " (SCHEDULE_ID,SCHEDULE_NAME,SCHEDULE_DESC,SCHEDULE_CODE,DIS_NAME,IS_ENABLE,QUARTZ_CODE,FLOW_ID) VALUES (?,?,?,?,?,?,?,?)";
		super.executeUpdate(sql, uuid, uuid, bean.getSchedule_desc(),
				bean.getSchedule_code(), bean.getDis_name(),bean.getEnable(),bean.getQuartz_code(),bean.getFlow_id());

		return this.get(uuid);
	}

	// 删除Schedule
	public void delete(String id) throws TransformException {
		super.delete(TABLE_NAME, "SCHEDULE_ID ='" + id + "'");
	}

	/**
	 * @description 查询所有基本，不分页
	 * @date 2011-12-30
	 * @author liuls
	 * @return
	 * @throws com.seeyon.v3x.dee.TransformException
	 */
	@SuppressWarnings("unchecked")
	public List<ScheduleBean> queryAll() throws TransformException {
		Connection conn = super.getConnection();
//		String sql = "select * from  " + TABLE_NAME;
		String sql = "select a.*,b.dis_name AS flow_name from  " + TABLE_NAME
		+ " a left join dee_flow b on a.flow_id=b.flow_id";
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			return Resultset2List.getListFromRS(rs, ScheduleBean.class);
		} catch (Throwable e) {
			log.error(e.getMessage(), e);
			throw new TransformException(e);
		} finally {
			try {
				rs.close();
				stmt.close();
				conn.close();
			} catch (Throwable e) {
			}
		}

	}

	@SuppressWarnings({ "unchecked" })
	public Page query(Page page, ScheduleBean bean) throws TransformException {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM " + TABLE_NAME + " where SCHEDULE_ID = '"
				+ bean.getSchedule_id() + "'");
		page.setTotalCount(super.getCount(sql.toString()));
		return super.getAllToPage(page, sql.toString(), ScheduleBean.class);
	}    
	public Page getAllToPage(Page page) throws TransformException {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT s.*,f.dis_name AS FLOW_NAME FROM " + TABLE_NAME + " s LEFT JOIN dee_flow f ON s.flow_id=f.flow_id");
		page.setTotalCount(super.getCount(sql.toString()));
		return super.getAllToPage(page, sql.toString(), ScheduleBean.class);
 }
	
	public void generateScheduleCode(ScheduleBean bean){
		StringBuffer code = new StringBuffer();
		String cron ="";
		
		String quartzCode = bean.getQuartz_code();
		if(quartzCode!=null){
			String[] arr = quartzCode.split(",");
			if(arr.length>1){
				if("0".equalsIgnoreCase(arr[0])){
					// 秒 分 小时 月内日期 月 周内日期 年（可选字段）
					int count = Integer.parseInt(arr[1]);
					int qty = Integer.parseInt(arr[2]);
					if(qty==1){// 分钟
						cron ="0 0/" + count + " * * * ? ";
					}else if(qty==2){// 小时
						cron ="0 0 0/" + count + " * * ? ";
					}else if(qty==3){// 天
						cron ="0 0 0/" + (count*24) + " * * ? ";
					}else if(qty==4){// 周
						cron ="0 0 0/" + (count*24*7) + " * * ? ";
					}else if(qty==5){// 月
						cron ="0 0 0 1 1/" + count + " ? ";
					}
				}
				else{
					String[] weekArr = {"MON","TUE","WED","THU","FRI","SAT","SUN"};
					if("3".equalsIgnoreCase(arr[1])){ //月
						cron ="0 " + arr[4] + " " + arr[3] + " " + arr[2] + " * ?";
					}
					else if("2".equalsIgnoreCase(arr[1])){ //周
						int weekInt = Integer.parseInt(arr[2]);
						cron ="0 " + arr[4] + " " + arr[3] + " ? * " + weekArr[weekInt-1];
					}
					else if("1".equalsIgnoreCase(arr[1])){//天
						cron ="0 " + arr[4] + " " + arr[3] + " * * ?";
					}
				}
			}
		}
		code.append("<schedule name=\"").append(bean.getSchedule_id()).append("\" class=\"com.seeyon.v3x.dee.schedule.QuartzReport\">");
		
		code.append("<property name=\"flow\" value=\"").append(bean.getFlow_id()).append("\"  />");
		code.append("<property name=\"quartzTime\" value=\"").append(cron).append("\" />");
		code.append("</schedule>");
		bean.setSchedule_code(code.toString());
	}
	
}
