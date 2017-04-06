package com.seeyon.v3x.dee.bean;

import com.seeyon.v3x.dee.DEEConstants;
import com.seeyon.v3x.dee.util.DataChangeUtil;


public class EnvironmentParameter {

    private String deeHome;
    
    public EnvironmentParameter() {
        deeHome = DataChangeUtil.getProperty(DEEConstants.DEE_HOME);
        if (null == deeHome) {
            deeHome = "";
        } else {
            deeHome = deeHome.replaceAll("\\\\", "/");
        }
    }
    
    public String getDeeHome() {
        return deeHome;
    }

    public void setDeeHome(String deeHome) {
        this.deeHome = deeHome;
    }
}
