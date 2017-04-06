package com.seeyon.v3x.dee.common.db.codelib.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author zhangfb
 */
@Entity
@Table(name="dee_code_pkg")
public class CodePkgBean implements Serializable {
    private String name;

    public CodePkgBean() {
    }

    public CodePkgBean(String name) {
        this.name = name;
    }

    @Id
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
