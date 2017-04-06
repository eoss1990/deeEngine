package com.seeyon.v3x.dee.common.db.flow.model;

import com.seeyon.v3x.dee.common.base.annotation.Column;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author Zhang.Wei
 * @description 对应表DEE_FLOW_MODULE
 * @date Feb 7, 20121:44:02 PM
 * @Copyright(c) Beijing Seeyon Software Co.,LTD
 */
@Entity
@Table(name="dee_flow_module")
public class FlowModuleBean implements Serializable {

    private String module_id;

    /** 模块名称 */
    private String module_name;

    /** 设置开关 */
    private boolean service_flag;

    /**
     * 获取module_id
     * @return module_id
     */
    @Id
    public String getModule_id() {
        return module_id;
    }

    /**
     * 设置module_id
     * @param module_id module_id
     */
    @Column(name = "MODULE_ID")
    public void setModule_id(String module_id) {
        this.module_id = module_id;
    }

    /**
     * 获取module_name
     * @return module_name
     */
    public String getModule_name() {
        return module_name;
    }

    /**
     * 设置module_name
     * @param module_name module_name
     */
    @Column(name = "MODULE_NAME")
    public void setModule_name(String module_name) {
        this.module_name = module_name;
    }

    /**
     * 获取service_flag
     * @return service_flag
     */
    public boolean getService_flag() {
        return service_flag;
    }

    /**
     * 设置service_flag
     * @param service_flag service_flag
     */
    @Column(name = "SERVICE_FLAG")
    public void setService_flag(boolean service_flag) {
        this.service_flag = service_flag;
    }
}
