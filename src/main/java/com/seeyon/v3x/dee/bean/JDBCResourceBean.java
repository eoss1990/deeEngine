package com.seeyon.v3x.dee.bean;

import com.seeyon.v3x.dee.common.db.resource.model.DeeResource;
import com.seeyon.v3x.dee.common.db.resource.util.SourceUtil;
import com.seeyon.v3x.dee.datasource.DeePooledDataSource;


/**
 * @author Zhang.Wei
 * @date Dec 27, 20117:06:44 PM
 * @Copyright(c) Beijing Seeyon Software Co.,LTD
 */
public class JDBCResourceBean implements DeeResource {

    /** 驱动 */
    private String driver;

    /** url */
    private String url;

    /** 用户名 */
    private String user;

    /** 密码 */
    private String password;

    /** 数据源名称 */
    private String resource_name;

    /** 数据源描述 */
    private String resoutce_desc;
    
	//连接池信息
	private DeePooledDataSource deePooledDS;    

    public JDBCResourceBean() {
    }

    public JDBCResourceBean(String xml) {
        driver = SourceUtil.getValueFromXml(xml, "", "driver");
        url = SourceUtil.getValueFromXml(xml, "", "url");
        user = SourceUtil.getValueFromXml(xml, "", "userName");
        password = SourceUtil.getValueFromXml(xml, "", "password");
        deePooledDS = new DeePooledDataSource(xml);
    }

    /**
     * 获取driver
     * @return driver
     */
    public String getDriver() {
        return driver;
    }

    /**
     * 设置driver
     * @param driver driver
     */
    public void setDriver(String driver) {
        this.driver = driver;
    }

    /**
     * 获取url
     * @return url
     */
    public String getUrl() {
        return url;
    }

    /**
     * 设置url
     * @param url url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 获取user
     * @return user
     */
    public String getUser() {
        return user;
    }

    /**
     * 设置user
     * @param user user
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * 获取password
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置password
     * @param password password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    public String toXML() {
        StringBuffer stringXml = new StringBuffer("<datasource name=\"" + resource_name + "\" class=\"com.seeyon.v3x.dee.datasource.JDBCDataSource\">");
        stringXml.append(" <property name=\"driver\" value=\"" + driver + "\"/>")
        .append(" <property name=\"url\" value=\"" + url + "\"/>")
        .append(" <property name=\"userName\" value=\"" + user + "\"/>")
        .append(" <property name=\"password\" value=\"" + password + "\"/>");
        if(deePooledDS == null){
        	stringXml.append(" </datasource>");
        }
        else{
        	stringXml.append(toPooledXML()).append(" </datasource>");
        }
        return stringXml.toString();
    }
    
    private String toPooledXML(){
    	StringBuffer dpdsXml = new StringBuffer("<pool name=\"dpds\" class=\"com.seeyon.v3x.dee.datasource.DeePooledDataSource\">");
    	dpdsXml.append(" <property name=\"maxPoolSize\" value=\"" + deePooledDS.getMaxPoolSize() + "\"/>")
    	.append(" <property name=\"minPoolSize\" value=\"" + deePooledDS.getMinPoolSize() + "\"/>")
    	.append(" <property name=\"initialPoolSize\" value=\"" + deePooledDS.getInitialPoolSize() + "\"/>")
    	.append(" <property name=\"maxIdleTime\" value=\"" + deePooledDS.getMaxIdleTime() + "\"/>")
    	.append(" <property name=\"checkoutTimeout\" value=\"" + deePooledDS.getCheckoutTimeout() + "\"/>")
    	.append(" <property name=\"acquireIncrement\" value=\"" + deePooledDS.getAcquireIncrement() + "\"/>")
    	.append(" <property name=\"acquireRetryAttempts\" value=\"" + deePooledDS.getAcquireRetryAttempts() + "\"/>")
    	.append(" <property name=\"acquireRetryDelay\" value=\"" + deePooledDS.getAcquireRetryDelay() + "\"/>")
    	.append(" <property name=\"autoCommitOnClose\" value=\"" + deePooledDS.getAutoCommitOnClose() + "\"/>")
    	.append(" <property name=\"breakAfterAcquireFailure\" value=\"" + deePooledDS.getBreakAfterAcquireFailure() + "\"/>")
    	.append(" <property name=\"maxStatements\" value=\"" + deePooledDS.getMaxStatements() + "\"/>")
    	.append(" <property name=\"maxStatementsPerConnection\" value=\"" + deePooledDS.getMaxStatementsPerConnection() + "\"/>").append(" </pool>");
    	return dpdsXml.toString();
    }
//    public String toNewXML() {
//        StringBuffer stringXml = new StringBuffer("<datasource name=\"" + resource_name + "\">");
//        stringXml.append(" <property name=\"driver\" value=\"" + driver + "\"/>").append(" <property name=\"url\" value=\"" + url + "\"/>").append(" <property name=\"userName\" value=\"" + user + "\"/>").append(" <property name=\"password\" value=\"" + password + "\"/>").append(" </datasource>");
//        return stringXml.toString();
//    }
    
	@Override
	public String toXML(String name) {
		resource_name = name;
		return toXML();
	}

    /**
     * 获取resource_name
     * @return resource_name
     */
    public String getResource_name() {
        return resource_name;
    }

    /**
     * 设置resource_name
     * @param resource_name resource_name
     */
    public void setResource_name(String resource_name) {
        this.resource_name = resource_name;
    }

    /**
     * 获取resoutce_desc
     * @return resoutce_desc
     */
    public String getResoutce_desc() {
        return resoutce_desc;
    }

    /**
     * 设置resoutce_desc
     * @param resoutce_desc resoutce_desc
     */
    public void setResoutce_desc(String resoutce_desc) {
        this.resoutce_desc = resoutce_desc;
    }

	public DeePooledDataSource getDeePooledDS() {
		return deePooledDS;
	}

	public void setDeePooledDS(DeePooledDataSource deePooledDS) {
		this.deePooledDS = deePooledDS;
	}
    
}
