package com.seeyon.v3x.dee.common.a8version.model;

import java.io.Serializable;

/**
 * A8版本项POJO
 *
 * @author zhangfb
 */
public class A8VersionItem implements Comparable<A8VersionItem>, Serializable {
    /**
     * id
     */
    private String id;

    /**
     * 版本号
     */
    private String version;

    /**
     * 排序号，用于标示版本的先后顺序
     */
    private int sort;

    @Override
    public int compareTo(A8VersionItem o) {
        if (o == null || this.sort < o.sort) {
            return -1;
        } else if (this.sort > o.sort) {
            return 1;
        } else {
            return 0;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }
}
