package com.seeyon.v3x.dee.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dkywolf on 2016-10-26.
 */
public class DBConManager {
    private static DBConManager instance;
    private DBConManager(){}

//    private static ConcurrentMap<String,DruidDataSource> dsCache = new ConcurrentHashMap<String,ComboPooledDataSource>();
    private static Map<String,DruidDataSource> dsCache = new HashMap<String,DruidDataSource>();
    public static DBConManager getInstance(){
        if (instance == null){
            synchronized (DBConManager.class){
                if (instance == null){
                    instance = new DBConManager();
                }
            }
        }
        return instance;
    }

    /**
     * 获取连接池对象
     * @param driver
     * @param url
     * @param userName
     * @param password
     * @param dpoolds
     * @return
     */
    /*
    public DruidDataSource getJDBCDataSource(String driver, String url, String userName,
                                             String password,DeePooledDataSource dpoolds){
        if (dpoolds == null){
            dpoolds = InitPool();
        }
        String dsKey = driver+url+userName+password+dpoolds.getInitialPoolSize()
                +dpoolds.getMaxPoolSize()+dpoolds.getCheckoutTimeout()
                +dpoolds.getMaxIdleTime()+dpoolds.getAcquireRetryAttempts();
        DruidDataSource dds = InitJDBCDataSource(driver,url,userName,
                password,dpoolds);
        return dsCache.putIfAbsent(dsKey,dds);
    }

    public DruidDataSource getJDBCDataSource2(String driver, String url, String userName,
                                              String password,DeePooledDataSource dpoolds){
        if (dpoolds == null){
            dpoolds = InitPool();
        }
        String dsKey = driver+url+userName+password;
        synchronized (dsCache) {
            DruidDataSource dds = dsCache.get(dsKey);
            if (dds == null || !diffPool(dpoolds, dds)) {
                dds = InitJDBCDataSource(driver, url, userName,
                        password, dpoolds);
                dsCache.put(dsKey, dds);
            }
            return dds;
        }
    }
    */
    /**
     * 获取连接池对象
     * @param driver
     * @param url
     * @param userName
     * @param password
     * @param dpoolds
     * @return
     */
    public DruidDataSource getJdbcDs(String driver, String url, String userName,
                                              String password,DeePooledDataSource dpoolds) throws Exception {
        if (dpoolds == null){
            dpoolds = InitPool();
        }
        String dsKey = driver+url+userName+password;
        try {
            DruidDataSource dds = dsCache.get(dsKey);
            if (dds == null || !diffPool(dpoolds, dds)) {
                synchronized (dsCache) {
                    dds = dsCache.get(dsKey);
                    if (dds == null){
                        dds = InitJdbcDs(driver, url, userName,
                                password, dpoolds);
                        dsCache.put(dsKey, dds);
                    }
                    else if (!diffPool(dpoolds, dds)){
                        dds.close();
                        dds = InitJdbcDs(driver, url, userName,
                                password, dpoolds);
                        dsCache.put(dsKey, dds);
                    }
                }
            }
            return dds;
        }
        catch (Exception e){
            throw new Exception("获取连接池异常："+e.getLocalizedMessage());
        }
    }

    private DruidDataSource InitJdbcDs(String driver, String url, String userName,
                                               String password,DeePooledDataSource dpoolds){
        DruidDataSource dds = new DruidDataSource();
        dds.setDriverClassName(driver);
        dds.setUrl(url);
        dds.setUsername(userName);
        dds.setPassword(password);
        dds.setInitialSize(dpoolds.getInitialPoolSize());
        dds.setMaxActive(dpoolds.getMaxPoolSize());
        dds.setMaxWait(dpoolds.getCheckoutTimeout());
        //配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
        dds.setTimeBetweenEvictionRunsMillis(dpoolds.getMaxIdleTime());
        //连接出错后重试次数
        dds.setConnectionErrorRetryAttempts(dpoolds.getAcquireRetryAttempts());

        dds.setTestWhileIdle(true);
        dds.setTestOnReturn(false);
        dds.setTestOnBorrow(false);

        return dds;
    }

    private DeePooledDataSource InitPool(){
        DeePooledDataSource dpoolds = new DeePooledDataSource();
        dpoolds.setInitialPoolSize(5);
        dpoolds.setMaxPoolSize(50);
        dpoolds.setCheckoutTimeout(15*1000);
        dpoolds.setMaxIdleTime(60*1000);
        dpoolds.setAcquireRetryAttempts(1);

        return dpoolds;
    }

    /**
     * 相同返回 true
     * @param dpoolds
     * @param dds
     * @return
     */
    private boolean diffPool(DeePooledDataSource dpoolds,DruidDataSource dds){
        if (dpoolds.getInitialPoolSize() != null && dpoolds.getInitialPoolSize().intValue() != dds.getInitialSize()){
            return false;
        }
        if (dpoolds.getMaxPoolSize() != null && dpoolds.getMaxPoolSize().intValue() != dds.getMaxActive()){
            return false;
        }
        if (dpoolds.getCheckoutTimeout() != null && dpoolds.getCheckoutTimeout().intValue() != dds.getMaxWait()){
            return false;
        }
        if (dpoolds.getMaxIdleTime() != null && dpoolds.getMaxIdleTime().intValue() != dds.getTimeBetweenEvictionRunsMillis()){
            return false;
        }
        if (dpoolds.getAcquireRetryAttempts() != null && dpoolds.getAcquireRetryAttempts().intValue() != dds.getConnectionErrorRetryAttempts()){
            return false;
        }
        return true;
    }
    //关闭连接池
    public void removeDs(String driver, String url, String userName,
                       String password) throws Exception{
        String dsKey = driver+url+userName+password;
        try {
            DruidDataSource dds = dsCache.get(dsKey);
            if (dds != null){
                dds.close();
                dsCache.remove(dsKey);
            }
        }
        catch (Exception e){
            throw new Exception("移除连接池异常："+e.getLocalizedMessage());
        }
    }
}
