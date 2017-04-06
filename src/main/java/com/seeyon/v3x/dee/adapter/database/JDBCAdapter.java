package com.seeyon.v3x.dee.adapter.database;

import com.seeyon.v3x.dee.TransformException;
import com.seeyon.v3x.dee.resource.DbDataSource;

import java.sql.Connection;

/**
 * JDBC适配器基类，保证JDBC相关Reader和Writer能同时支持JDBCDataSource和JNDIDataSource。
 *
 * @author wangwenyou
 */
public abstract class JDBCAdapter {
    private DbDataSource dataSource;

    /**
     * 取得数据源
     *
     * @return 数据源
     */
    public DbDataSource getDataSource() {
        return dataSource;
    }

    /**
     * 设置数据源
     *
     * @param dataSource 数据源
     */
    public void setDataSource(DbDataSource dataSource) {
        if (dataSource == null) {
            throw new NullPointerException("dataSource is null");
        }

        this.dataSource = dataSource;
    }

    protected Connection getConnection() throws TransformException {
        if (dataSource == null) {
            return null;
        }

        try {
            return dataSource.getConnection();
        } catch (Exception e) {
            throw new TransformException(e.getLocalizedMessage(), e);
        }
    }
}
