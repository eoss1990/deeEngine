package com.seeyon.v3x.dee.bean;

import com.seeyon.v3x.dee.common.db.resource.model.DeeResource;

/**
 * @author Zhang.Wei
 * @date Jan 10, 201211:38:54 AM
 * @Copyright(c) Beijing Seeyon Software Co.,LTD
 */
public class A8WSGetTokenProcessor implements DeeResource {

    /** a8WS登录名 */
    private String userName;

    /** 密码 */
    private String password;

    /** url */
    private String a8url;

    /**
     * {@inheritDoc}
     */
    @Override
    public String toXML() {
        String xml = "<processor name=\"A8WSGetToken\" class=\"com.seeyon.v3x.dee.processor.A8WSGetTokenProcessor\"><description> \"A8 WebService GetToken\"</description>" + "<property name=\"userName\" ref=\"" + userName + "\"/></processor>" + "<property name=\"password\" ref=\"" + password + "\"/></processor>" + "<property name=\"a8url\" ref=\"" + a8url + "\"/></processor>";
        return xml;
    }

	public String toXML(String name) {
		return toXML();
	}

    /**
     * 获取userName
     * @return userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * 设置userName
     * @param userName userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
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

    /**
     * 获取a8url
     * @return a8url
     */
    public String getA8url() {
        return a8url;
    }

    /**
     * 设置a8url
     * @param a8url a8url
     */
    public void setA8url(String a8url) {
        this.a8url = a8url;
    }
}
