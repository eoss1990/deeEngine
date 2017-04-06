package com.seeyon.v3x.dee.common.db.resource.util;

/**
 * @author Zhang.Wei
 * @date Jan 16, 20124:31:03 PM
 * @Copyright(c) Beijing Seeyon Software Co.,LTD
 */
public class SourceType {

    private String name;

    private String value;

    public SourceType() {
    }

    public SourceType(String aName, String aValue) {
        this.name = aName;
        this.value = aValue;
    }

    /**
     * 获取name
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * 设置name
     * @param name name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取value
     * @return value
     */
    public String getValue() {
        return value;
    }

    /**
     * 设置value
     * @param value value
     */
    public void setValue(String value) {
        this.value = value;
    }
}
