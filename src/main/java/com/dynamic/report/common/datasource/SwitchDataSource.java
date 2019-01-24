package com.dynamic.report.common.datasource;

import com.dynamic.report.entity.DataSourceInfo;

public class SwitchDataSource {

    private static final ThreadLocal<DataSourceInfo> threadLocal = new ThreadLocal<>();

    public static DataSourceInfo getDataSources() {
        return threadLocal.get();
    }

    public static void setDataSources(DataSourceInfo dataSourceInfo) {
        threadLocal.set(dataSourceInfo);
    }

    public static void clear() {
        threadLocal.remove();
    }

}
