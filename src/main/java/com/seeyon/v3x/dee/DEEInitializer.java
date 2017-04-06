package com.seeyon.v3x.dee;

/**
 * DEE系统初始化，封装DEE启动时的调用
 *
 * @author wangwenyou
 */
public class DEEInitializer {
    private final static DEEInitializer INSTANCE = new DEEInitializer();

    private DEEInitializer() {
    }

    public static DEEInitializer getInstance() {
        return INSTANCE;
    }

    /**
     * DEE初始化入口
     */
    public void init() {
        DataSourceManager.getInstance();
    }
}
