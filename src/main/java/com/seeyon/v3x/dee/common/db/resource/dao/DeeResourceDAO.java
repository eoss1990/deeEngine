package com.seeyon.v3x.dee.common.db.resource.dao;

import com.seeyon.v3x.dee.TransformException;
import com.seeyon.v3x.dee.common.base.dao.BaseDAO;
import com.seeyon.v3x.dee.common.base.page.Page;
import com.seeyon.v3x.dee.common.base.util.DBUtil;
import com.seeyon.v3x.dee.common.base.util.UuidUtil;
import com.seeyon.v3x.dee.common.db.resource.model.DeeResourceBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Zhang.Wei
 * @date Dec 26, 20114:05:21 PM
 * @Copyright(c) Beijing Seeyon Software Co.,LTD
 */
public class DeeResourceDAO extends BaseDAO {

    private final static String TABLE_NAME_RESOURCE = "dee_resource";

    private static Log log = LogFactory.getLog(DeeResourceDAO.class);

    /**
     * 查找所有的记录
     *
     * @return List<DeeResourceBean>
     * @throws com.seeyon.v3x.dee.TransformException
     */
    public List<DeeResourceBean> findAll() throws TransformException {
        Connection conn = getConnection();
        PreparedStatement pst = null;
        ResultSet rs = null;
//        String sql = "select resource_id,resource_template_id,resource_name,resource_code,resource_desc,ref_id,dis_name from " + TABLE_NAME_RESOURCE;
        String sql = "select a.*,b.resource_template_name from "
                + TABLE_NAME_RESOURCE + " a left join dee_resource_template b on a.resource_template_id=b.resource_template_id order by a.EXT1 desc";
        List<DeeResourceBean> drb = new ArrayList<DeeResourceBean>();
        try {
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                drb.add(setPara(rs));
            }
            return drb;
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            throw new TransformException(e);
        } finally {
            DBUtil.close(rs, pst, conn);
        }
    }

    public List<DeeResourceBean> find(String where) throws TransformException {
        Connection conn = getConnection();
        PreparedStatement pst = null;
        ResultSet rs = null;
        String sql = "select resource_id,resource_template_id,resource_name,resource_code,resource_desc,ref_id,dis_name,'' as resource_template_name,ext1 from "
                + TABLE_NAME_RESOURCE + where;
        List<DeeResourceBean> drb = new ArrayList<DeeResourceBean>();
        try {
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                drb.add(setPara(rs));
            }
            return drb;
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            throw new TransformException(e);
        } finally {
            DBUtil.close(rs, pst, conn);
        }
    }

    /**
     * @return
     * @description 查找数据源管理中所有的数据源
     * @date 2012-10-12
     * @author dengxj
     */
    public List<DeeResourceBean> findAllDatasource() throws TransformException {
        Connection conn = getConnection();
        PreparedStatement pst = null;
        ResultSet rs = null;
        String sql = "select dr.resource_id,dr.resource_template_id,dr.resource_name,dr.resource_code,dr.resource_desc,dr.ref_id,dr.dis_name,dt.resource_template_name,dr.ext1 from " + TABLE_NAME_RESOURCE;
        sql += " dr left join dee_resource_template dt on dr.resource_template_id=dt.resource_template_id where dt.type_id=3 ";
        List<DeeResourceBean> drb = new ArrayList<DeeResourceBean>();
        try {
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                drb.add(setPara(rs));
            }
            return drb;
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            throw new TransformException(e);
        } finally {
            DBUtil.close(rs, pst, conn);
        }
    }

    public Page getAllToPage(Page page) throws TransformException {
        String sql = "select dr.resource_id,dr.resource_template_id,dr.resource_name,dr.resource_code,dr.resource_desc,dr.ref_id,dr.dis_name,dt.resource_template_name,dr.ext1 from " + TABLE_NAME_RESOURCE;
        sql += " dr left join dee_resource_template dt on dr.resource_template_id=dt.resource_template_id where dt.type_id=3 ";
        page.setTotalCount(super.getCount(sql));
        return super.getAllToPage(page, sql.toString(), DeeResourceBean.class);
    }

    /**
     * 插入
     *
     * @param drb
     * @throws com.seeyon.v3x.dee.TransformException
     */
    public String insert(DeeResourceBean drb) throws TransformException {
        String sql = " insert into " + TABLE_NAME_RESOURCE + " (resource_id,resource_template_id,resource_name,resource_code,resource_desc,ref_id,dis_name)values(?,?,?,?,?,?,?)";
        Connection conn = getConnection();
        PreparedStatement pst = null;
        try {
            pst = conn.prepareStatement(sql);
            int count = 1;
            if (drb.getResource_id() == null || "".equals(drb.getResource_id().trim())) {
                drb.setResource_id(UuidUtil.uuid());
            }
            pst.setString(count++, drb.getResource_id());
            pst.setString(count++, drb.getResource_template_id());

            /**
             * @date 2012-02-29
             * @author lilong
             * 为了支持字典引用此处如果为字典resource保存resource_name不保存id值
             * 2012-10-19 增加系统函数的保存
             */
            if ("12".equals(drb.getResource_template_id())
                    || "13".equals(drb.getResource_template_id()) || "29".equals(drb.getResource_template_id())) {
                pst.setString(count++, drb.getResource_name());
            } else {
                pst.setString(count++, drb.getResource_id());
            }

            pst.setString(count++, drb.getDr().toXML(drb.getResource_id()));
            pst.setString(count++, drb.getResource_desc());
            pst.setString(count++, drb.getRef_id());
            pst.setString(count, drb.getDis_name());
            pst.executeUpdate();
            return drb.getResource_id();
        } catch (Throwable e) {
            log.error(e.getMessage() + "error sql=" + sql, e);
            throw new TransformException(e);
        } finally {
            DBUtil.close(null, pst, conn);
        }
    }

    /**
     * 根据id，查找对应记录
     *
     * @param id
     * @return
     * @throws com.seeyon.v3x.dee.TransformException
     */
    public DeeResourceBean findById(String id) throws TransformException {
        Connection conn = getConnection();
        PreparedStatement pst = null;
        ResultSet rs = null;
        String sql = "select resource_id,resource_template_id,resource_name,resource_code,resource_desc,ref_id,dis_name,'' as resource_template_name, ext1 from " + TABLE_NAME_RESOURCE + " where resource_id=?";
        try {
            pst = conn.prepareStatement(sql);
            pst.setString(1, id);
            rs = pst.executeQuery();
            while (rs.next()) {
                return setPara(rs);
            }
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            throw new TransformException(e);
        } finally {
            DBUtil.close(rs, pst, conn);
        }
        return null;
    }

    /**
     * 根据模板ID，查找对应记录，字典使用
     *
     * @param templateId
     * @return
     * @throws com.seeyon.v3x.dee.TransformException
     * @author lilong
     * @date 2012-02-18
     */
    public List<DeeResourceBean> findByTemplateId(String templateId) throws TransformException {
        Connection conn = getConnection();
        PreparedStatement pst = null;
        ResultSet rs = null;
        String sql = "select resource_id,resource_template_id,resource_name,resource_code,resource_desc,ref_id,dis_name,'' as resource_template_name,ext1 from " + TABLE_NAME_RESOURCE + " where resource_template_id=?";
        List<DeeResourceBean> drb = new ArrayList<DeeResourceBean>();
        try {
            pst = conn.prepareStatement(sql);
            pst.setString(1, templateId);
            rs = pst.executeQuery();
            while (rs.next()) {
                drb.add(setPara(rs));
            }
            return drb;
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            throw new TransformException(e);
        } finally {
            DBUtil.close(rs, pst, conn);
        }
    }

    /**
     * 批量删除
     *
     * @param ids 待删除的id数组
     * @return
     * @throws com.seeyon.v3x.dee.TransformException
     */
    public int deleteByIds(String[] ids) throws TransformException {
        if (ids == null || ids.length == 0)
            return 0;
        StringBuffer condition = new StringBuffer(" resource_id in ( ");
        for (String id : ids) {
            condition.append("'" + id).append("',");
        }
        return super.delete(TABLE_NAME_RESOURCE, condition.substring(0, condition.lastIndexOf(",")) + " )");
    }

    /*
    * 删除数据源
    * @param id
    * @return
    * @throws com.seeyon.v3x.dee.TransformException
    * */
    public void delDsById(String id) throws TransformException {
        Connection conn = getConnection();
        PreparedStatement pst = null;
        String sql = "delete from " + TABLE_NAME_RESOURCE + " where resource_id=?";
        try {
            pst = conn.prepareStatement(sql);
            pst.setString(1, id);
            pst.executeUpdate();
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            throw new TransformException(e);
        } finally {
            DBUtil.close(pst);
            DBUtil.close(conn);
        }
    }

    /**
     * 删除某一条记录
     *
     * @param id
     * @return
     * @throws com.seeyon.v3x.dee.TransformException
     */
    public int deleteById(String id) throws TransformException {
        return super.delete(TABLE_NAME_RESOURCE, id);
    }

    /**
     * 更新记录
     *
     * @param drb
     * @throws com.seeyon.v3x.dee.TransformException
     */
    public void update(DeeResourceBean drb) throws TransformException {
        StringBuffer sql = new StringBuffer();
        sql.append("update " + TABLE_NAME_RESOURCE + " set resource_template_id =?,resource_name =?,"
                + " resource_desc = ?,"
                + " resource_code = ?,"
                + " ref_id =?,"
                + " dis_name =?"
                + " where resource_id=?");
        /*
        super.executeUpdate(sql.toString(),drb.getResource_template_id(),
				drb.getResource_id(),drb.getResource_desc(),
				drb.getDr().toXML(drb.getResource_id()),
				drb.getRef_id(),drb.getDis_name(),drb.getResource_id() );
		*/
        Connection conn = getConnection();
        PreparedStatement pst = null;
        try {
            pst = conn.prepareStatement(sql.toString());
            int count = 1;
            pst.setString(count++, drb.getResource_template_id());
            /**
             * @date 2012-02-29
             * @author lilong
             * 为了支持字典引用此处如果为字典resource保存resource_name不保存id值
             * 2012-10-19 增加系统函数的保存
             */
            if ("12".equals(drb.getResource_template_id())
                    || "13".equals(drb.getResource_template_id()) || "29".equals(drb.getResource_template_id())) {
                pst.setString(count++, drb.getResource_name());
            } else {
                pst.setString(count++, drb.getResource_id());
            }
            pst.setString(count++, drb.getResource_desc());
            pst.setString(count++, drb.getDr().toXML(drb.getResource_id()));
            pst.setString(count++, drb.getRef_id());
            pst.setString(count++, drb.getDis_name());
            pst.setString(count, drb.getResource_id());
            pst.executeUpdate();
            //return drb.getResource_id();
        } catch (Throwable e) {
            log.error(e.getMessage() + "error sql=" + sql, e);
            throw new TransformException(e);
        } finally {
            DBUtil.close(null, pst, conn);
        }

    }

    public void updateResourceCode(DeeResourceBean drb) throws TransformException {
        String sql = "update " + TABLE_NAME_RESOURCE + " set resource_code=? where resource_id=?";
        Connection conn = getConnection();
        PreparedStatement pst = null;
        try {
            pst = conn.prepareStatement(sql);
            int count = 1;
            if (drb.getResource_id() != null && !"".equals(drb.getResource_id().trim())) {
                pst.setString(count++, drb.getResource_code());
                pst.setString(count++, drb.getResource_id());
                pst.executeUpdate();
            }
        } catch (Throwable e) {
            log.error(e.getMessage() + "error sql=" + sql, e);
            throw new TransformException(e);
        } finally {
            DBUtil.close(null, pst, conn);
        }
    }

    /**
     * @param flowId
     * @return
     * @throws com.seeyon.v3x.dee.TransformException
     * @description 根据flow id获取jdbcReader的code
     * @date 2012-4-7
     * @author liuls
     */
    public List<String> getReaderResByFlow(String flowId) throws TransformException {
        String sql = "select resource_code from  " + TABLE_NAME_RESOURCE + " where resource_template_id=0 " +
                " and resource_id in (select resource_id from dee_flow_sub where flow_id =?)";
        Connection conn = getConnection();
        PreparedStatement pst = null;
        ResultSet rs = null;
        List<String> list = new ArrayList<String>();
        try {
            pst = conn.prepareStatement(sql);
            pst.setString(1, flowId);
            rs = pst.executeQuery();
            while (rs.next()) {
                list.add(rs.getString(1));
            }

        } catch (Throwable e) {
            log.error(e.getMessage() + "error sql=" + sql, e);
            throw new TransformException(e);
        } finally {
            DBUtil.close(rs, pst, conn);
        }
        return list;
    }

    /**
     * 查询所有资源信息（flow流程处使用）
     *
     * @param flow_id
     * @return
     * @throws com.seeyon.v3x.dee.TransformException
     */
    public List<DeeResourceBean> findResourceList(String flow_id) throws TransformException {
        Connection conn = getConnection();
        PreparedStatement pst = null;
        ResultSet rs = null;
        String sql = "select dr.*,drt.resource_template_name from " +
                " dee_flow df,dee_flow_sub dfs,dee_resource dr,dee_resource_template drt " +
                " where df.flow_id=dfs.flow_id " +
                " and dfs.resource_id=dr.resource_id " +
                " and dr.resource_template_id= drt.resource_template_id " +
                " and df.flow_id=? ";
        sql += " order by dfs.sort ";
        List<DeeResourceBean> drb = new ArrayList<DeeResourceBean>();
        try {
            pst = conn.prepareStatement(sql);
            pst.setString(1, flow_id);
            rs = pst.executeQuery();
            while (rs.next()) {
                drb.add(setPara(rs));
            }
            return drb;
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            throw new TransformException(e);
        } finally {
            DBUtil.close(rs, pst, conn);
        }
    }

    private DeeResourceBean setPara(ResultSet rs) throws SQLException {
        DeeResourceBean drb = new DeeResourceBean();
        drb.setResource_id(rs.getString("resource_id"));
        drb.setResource_template_id(rs.getString("resource_template_id"));
        drb.setResource_template_name(rs.getString("resource_template_name"));
        drb.setResource_name(rs.getString("resource_name"));
//        drb.setResource_code(DBUtil.getClobString(rs.getClob("resource_code")));
        drb.setResource_code(rs.getString("resource_code"));
        drb.setResource_desc(rs.getString("resource_desc"));
        drb.setRef_id(rs.getString("ref_id"));
        drb.setDis_name(rs.getString("dis_name"));
        drb.setCreate_time(rs.getString("ext1"));
        return drb;
    }

    /**
     * @param rsId
     * @return
     * @throws com.seeyon.v3x.dee.TransformException
     * @description 根据resource id 获取Mapping资源信息
     * @date 2013-10-09
     * @author dkywolf
     */
    public List<DeeResourceBean> getExchangeMappingByIds(String rsId) throws TransformException {
        Connection conn = getConnection();
        PreparedStatement pst = null;
        ResultSet rs = null;
        String sql = "select resource_id,resource_template_id,resource_name,resource_code,"
                + "resource_desc,ref_id,dis_name,'' as resource_template_name, ext1 from " + TABLE_NAME_RESOURCE
                + " where resource_template_id='6' and resource_id in (" + rsId + ")";
        Statement stmt;
        List<DeeResourceBean> drb = new ArrayList<DeeResourceBean>();
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                drb.add(setPara(rs));
            }
            return drb;
        } catch (Throwable e) {

            log.error(e.getMessage(), e);
            throw new TransformException(e);
        } finally {
            DBUtil.close(rs, pst, conn);
        }
    }

    /**
     * @param rsId
     * @return
     * @throws com.seeyon.v3x.dee.TransformException
     * @description 根据传入字典引用名 获取JDBC字典resource_id
     * @date 2013-10-09
     * @author dkywolf
     */
    public String getJDBCRsidByVals(String vals) throws TransformException {
        Connection conn = getConnection();
        PreparedStatement pst = null;
        ResultSet rs = null;
        String sql = "select resource_id  from " + TABLE_NAME_RESOURCE + " where resource_template_id='12' and resource_id in (" + vals + ")";
        Statement stmt;
        StringBuffer dsIds = new StringBuffer();
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                dsIds.append(rs.getString("resource_id") + ",");
            }
            return dsIds.toString();
        } catch (Throwable e) {

            log.error(e.getMessage(), e);
            throw new TransformException(e);
        } finally {
            DBUtil.close(rs, pst, conn);
        }
    }

    /**
     * @param name
     * @return
     * @throws com.seeyon.v3x.dee.TransformException
     * @description 根据传入字典引用名 获取静态字典resource_id
     * @date 2013-10-09
     * @author dkywolf
     */
    public String getStaticRsidByVals(String vals) throws TransformException {
        Connection conn = getConnection();
        PreparedStatement pst = null;
        ResultSet rs = null;
        String sql = "select resource_id  from " + TABLE_NAME_RESOURCE + " where resource_template_id='13' and dis_name in (" + vals + ")";
        Statement stmt;
        StringBuffer dsIds = new StringBuffer();
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                dsIds.append(rs.getString("resource_id") + ",");
            }
            return dsIds.toString();
        } catch (Throwable e) {

            log.error(e.getMessage(), e);
            throw new TransformException(e);
        } finally {
            DBUtil.close(rs, pst, conn);
        }
    }

    /**
     * @param name
     * @return
     * @throws com.seeyon.v3x.dee.TransformException
     * @description 根据传入数据源名称 获取resource_id
     * @date 2016-03-22
     * @author zhongjj
     */
    public String getDbIdByName(String name)throws TransformException{
        Connection conn = getConnection();
        PreparedStatement pst = null;
        ResultSet rs = null;
        String sql = "select resource_id  from " + TABLE_NAME_RESOURCE + " where resource_template_id='5' and dis_name = '" + name + "'";
        Statement stmt;
        StringBuffer dsIds = new StringBuffer();
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                dsIds.append(rs.getString("resource_id"));
            }
            return dsIds.toString();
        } catch (Throwable e) {

            log.error(e.getMessage(), e);
            throw new TransformException(e);
        } finally {
            DBUtil.close(rs, pst, conn);
        }
    }



    /**
     * @param refId
     * @return
     * @throws com.seeyon.v3x.dee.TransformException
     * @description 根据数据源ID查询是否被引用
     * @date 201-03-11
     * @author zhongjj
     */
    public boolean findByRefId(String id) throws TransformException {
        Connection conn = getConnection();
        PreparedStatement pst = null;
        ResultSet rs = null;
        String sql = "select resource_id from " + TABLE_NAME_RESOURCE + "  where ref_id=?";
        boolean flag = false;
        try {
            pst = conn.prepareStatement(sql);
            pst.setString(1, id);
            rs = pst.executeQuery();
            while (rs.next()) {
                flag = true;
                break;
            }
            return flag;
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            throw new TransformException(e);
        } finally {
            DBUtil.close(rs, pst, conn);
        }
    }


    public List<DeeResourceBean> findByIds(String ids) throws TransformException {
        StringBuilder sb = new StringBuilder();

        if (ids.length() > 0) {
            String[] array = ids.split(",");
            for (String tmp : array) {
                if (tmp != null && !"".equals(tmp)) {
                    sb.append("'").append(tmp.trim()).append("'").append(",");
                }
            }
        }

        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }

        Connection conn = getConnection();
        PreparedStatement pst = null;
        ResultSet rs = null;
        String sql = "select a.*,b.resource_template_name from "
                + TABLE_NAME_RESOURCE + " a left join dee_resource_template b on a.resource_template_id=b.resource_template_id where a.resource_id in (" + sb.toString() + ") order by a.EXT1 desc";
        List<DeeResourceBean> drb = new ArrayList<DeeResourceBean>();
        try {
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                drb.add(setPara(rs));
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
