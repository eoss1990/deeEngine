package com.seeyon.v3x.dee.common.base.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.Reader;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Zhang.Wei
 * @date Dec 27, 20115:35:41 PM
 * @Copyright(c) Beijing Seeyon Software Co.,LTD
 */
public class DBUtil {
    private final static Log log = LogFactory.getLog(DBUtil.class);
    public static void close(PreparedStatement pst) {
        try {
            if (pst != null) {
                pst.close();
            }
        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
        }
    }

    public static void close(ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
        }
    }

    public static void close(Connection con) {
        try {
            if (con != null) {
                con.close();
            }
        } catch (SQLException ex) {
            log.error(ex.getMessage(), ex);
        }
    }

    public static void close(ResultSet rs, PreparedStatement pst, Connection con) {
        close(rs);
        close(pst);
        close(con);
    }

    public static String getClobString(Clob clob) {
        try {
            String temp;
            //			StringBuffer conferees = new StringBuffer("");
            Reader stream = clob.getCharacterStream();
            char[] c=new char[(int)clob.length()];
            stream.read(c);
            temp=new String(c);
            stream.close();
            //			BufferedReader reader = new BufferedReader(stream);
            //			while ((temp = reader.readLine()) != null) {
            //				conferees.append(temp);
            //			}
            //			return conferees.toString();
            return temp;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return "";
        }
    }

    /**
     * 从数据库中取A8的版本信息，如果没取到，返回null
     *
     * @param conn 数据库连接
     * @return A8版本信息
     */
    public static String getA8VersionStr(Connection conn) {
        String sql = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            if (hasTable(conn, "v3x_config")) {
                sql = "SELECT config_value FROM v3x_config WHERE id='5003'";
            } else if (hasTable(conn, "ctp_config")) {
                sql = "SELECT config_value FROM ctp_config WHERE id='5003'";
            }

            if (sql != null) {
                stmt = conn.createStatement();
                rs = stmt.executeQuery(sql);
                if (rs.next()) {
                    return rs.getString("config_value");
                }
            }
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                log.error(e.getLocalizedMessage(), e);
            }
        }
        return null;
    }

    /**
     * 判断表名是否存在
     *
     * @param conn      数据库连接
     * @param tableName 表名
     * @return
     * @throws java.sql.SQLException
     */
    public static boolean hasTable(Connection conn, String tableName) throws SQLException {
        boolean result = false;
        String schemaPattern = null;
        ResultSet rs = null;
        Statement st = conn.createStatement();
        DatabaseMetaData meta = conn.getMetaData();
        try{
            if ("Oracle".equals(meta.getDatabaseProductName())) {
                schemaPattern = meta.getUserName();
    			String sql = "select * from All_tables where owner='"+schemaPattern+"' and table_name='"+tableName.toUpperCase()+"'";
    			rs = st.executeQuery(sql);
            }
            else{
                rs = meta.getTables(null, schemaPattern, tableName.toUpperCase(), null);
            }
            if (rs.next()) {
                result = true;
            }
        }
        catch(SQLException e){
            log.error(e.getMessage(), e);
        }
        finally{
            try {
                if (rs != null) {
                    rs.close();
                }
                if (st != null) {
                	st.close();
                }
            } catch (SQLException e) {
                log.error(e.getLocalizedMessage(), e);
            }
        }
        return result;
    }
}
