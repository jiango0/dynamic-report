package com.dynamic.report.common.datasource;

import com.alibaba.druid.pool.DruidDataSource;

import javax.sql.DataSource;

public class CreateDataSource {

    public static DataSource get(String driverClassName, String url, String username, String password) {

        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setDriverClassName(driverClassName);
        druidDataSource.setUrl(url);
        druidDataSource.setUsername(username);
        druidDataSource.setPassword(password);
        druidDataSource.setTestWhileIdle(true);
        druidDataSource.setValidationQuery("SELECT 1 FROM DUAL");

        druidDataSource.setInitialSize(20);
        druidDataSource.setMinIdle(5);
        druidDataSource.setMaxActive(50);
        druidDataSource.setMaxWait(60000);
        druidDataSource.setMinEvictableIdleTimeMillis(300000);
        druidDataSource.setTestOnBorrow(false);

        return druidDataSource;
    }

}
