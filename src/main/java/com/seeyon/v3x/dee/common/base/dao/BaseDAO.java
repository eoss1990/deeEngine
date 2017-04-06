package com.seeyon.v3x.dee.common.base.dao;

import com.seeyon.v3x.dee.DataSourceManager;
import com.seeyon.v3x.dee.TransformException;
import com.seeyon.v3x.dee.common.base.page.Page;
import com.seeyon.v3x.dee.common.base.util.Resultset2List;
import com.seeyon.v3x.dee.datasource.JDBCDataSource;
import com.seeyon.v3x.dee.resource.DbDataSource;
import com.seeyon.v3x.dee.util.JdbcUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

public abstract class BaseDAO {
    private static Log log = LogFactory.getLog(BaseDAO.class);

    private JDBCDataSource ds;

    /**
     * 执行update或delete类不需要返回结果的sql
     *
     * @param sql sql语句
     * @return 更新或修改的记录数
     * @throws TransformException
     */
    protected int execute(String sql) throws TransformException {
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = getConnection();
            stmt = conn.createStatement();
            return stmt.executeUpdate(sql);
        } catch (Throwable e) {
            log.error(e.getLocalizedMessage(), e);
            throw new TransformException(e);
        } finally {
            JdbcUtils.close(conn, stmt);
        }
    }

    /**
     * 执行修改
     *
     * @param sql    sql语句
     * @param params 参数
     * @return 修改的记录数
     * @throws TransformException
     */
    protected int executeUpdate(String sql, Object... params) throws TransformException {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
            return stmt.executeUpdate();
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            throw new TransformException(e);
        } finally {
            JdbcUtils.close(conn, stmt);
        }
    }

    /**
     * 获取连接
     *
     * @return Connection对象
     * @throws TransformException
     */
    protected Connection getConnection() throws TransformException {
        if (this.ds == null) {
            initDataSource();
        }

        try {
            return ds.getConnection();
        } catch (Exception e) {
            throw new TransformException(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 初始化JDBC数据源
     *
     * @throws TransformException
     */
    private synchronized void initDataSource() throws TransformException {
        DbDataSource ds1 = DataSourceManager.lookup(DataSourceManager.DEE_META);

        if (ds1 == null) {
            throw new TransformException("没有找到dee的数据源dee_meta，请确定已使用DataSourceManager.getInstance().bind绑定。");
        }

        if (ds1 instanceof JDBCDataSource) {
            ds = (JDBCDataSource) ds1;
        } else {
            throw new TransformException("dee的数据源dee_meta类型错误，只支持JDBCDataSource。" + ds1.getClass().getCanonicalName());
        }
    }

    /**
     * 获取JDBC数据源
     *
     * @return 数据源对象
     * @throws TransformException
     */
    public JDBCDataSource getDs() throws TransformException {
        if (ds == null) {
            initDataSource();
        }
        return ds;
    }

    /**
     * 删除表中指定id的记录
     *
     * @param table 表名
     * @param id    ID值
     * @return 删除的记录数
     * @throws com.seeyon.v3x.dee.TransformException
     */
    protected int delete(String table, long id) throws TransformException {
        return delete(table, " id='" + id + "'");
    }

    /**
     * 删除指定表满足条件的记录
     *
     * @param table     表名
     * @param condition 条件
     * @return 删除记录数
     * @throws TransformException
     */
    protected int delete(String table, String condition) throws TransformException {
        return execute("delete from " + table + " where " + condition);
    }

    /**
     * 取满足指定条件的数据列表,返回内容存到page对象中
     *
     * @param page  分页对象Page
     * @param sql   sql语句
     * @param clazz 要将查询结果转换的类
     * @return .
     * @throws TransformException
     */
    @SuppressWarnings("unchecked")
    protected Page getAllToPage(Page page, String sql, Class clazz) throws TransformException {
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = getConnection();
            stmt = conn.createStatement();
            stmt.setMaxRows(page.getPageNo() * page.getPageSize());//关键代码，设置最大记录数为当前页记录的截止下标
            ResultSet rs = stmt.executeQuery(sql);
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
                page.setResult(Resultset2List.getListFromRS(rs, clazz));
            }
            return page;
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            throw new TransformException(e);
        } finally {
            JdbcUtils.close(conn, stmt);
        }
    }

    /**
     * 通过sql得到一条记录
     *
     * @param sql   查询语句
     * @param clazz 需要被封装的类
     * @return 已经被赋值的对象
     * @throws TransformException
     */
    @SuppressWarnings("unchecked")
    protected Object getBeanBySql(String sql, Class clazz) throws TransformException {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            List list = Resultset2List.getListFromRS(rs, clazz);
            if (list.size() == 0) {
                throw new TransformException("没有满足条件的数据。" + sql);
            }
            return list.get(0);
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            throw new TransformException(e);
        } finally {
            JdbcUtils.close(conn, stmt, rs);
        }
    }

    /**
     * 解析sql语句生成查询数量的sql
     *
     * @param sql 原始sql
     * @return 查询到的数据个数
     * @throws TransformException
     */
    protected int getCount(String sql) throws TransformException {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();

            StringBuilder sb = new StringBuilder("select count(*) ");
            String[] sqlArr = sql.split(" ");
            String[] sqlSubArr = new String[sqlArr.length];
            int startIndex = 0;
            for (int i = 0; i < sqlArr.length; i++) {
                if ("from".equals(sqlArr[i].toLowerCase())) {
                    startIndex = i;
                    break;
                }
            }
            int count = 0;
            for (int j = startIndex; j < sqlArr.length; j++) {
                sqlSubArr[count] = sqlArr[j];
                count++;
            }
            for (String aSqlSubArr : sqlSubArr) {
                if (aSqlSubArr != null) {
                    sb.append(" ").append(aSqlSubArr).append(" ");
                }
            }

            stmt = conn.createStatement();
            rs = stmt.executeQuery(sb.toString());
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            throw new TransformException(e);
        } finally {
            JdbcUtils.close(conn, stmt, rs);
        }
    }
}
