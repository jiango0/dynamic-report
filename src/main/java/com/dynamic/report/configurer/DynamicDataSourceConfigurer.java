package com.dynamic.report.configurer;

import com.dynamic.report.common.datasource.CreateDataSource;
import com.dynamic.report.common.datasource.SwitchDataSource;
import com.dynamic.report.entity.DataSourceInfo;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DynamicDataSourceConfigurer extends AbstractRoutingDataSource {

    //数据源缓存
    private static final Map<String, DataSource> cacheDataSource = new HashMap<>();

    // 默认数据源，也就是主库
//    @Resource(name = "masterDataSource")
//    protected DataSource masterDataSource;

    @Override
    protected DataSource determineTargetDataSource() {
        DataSourceInfo dataSourceInfo = determineCurrentLookupKey();
        if( dataSourceInfo == null || StringUtils.isEmpty(dataSourceInfo.getName())) {
            return null;
        }

        DataSource dataSource = cacheDataSource.get(dataSourceInfo.getName());
        if(dataSource == null) {
            dataSource = create(dataSourceInfo);
        }

        return dataSource;
    }

    @Override
    protected DataSourceInfo determineCurrentLookupKey() {
        return SwitchDataSource.getDataSources();
    }

    @Override
    public void afterPropertiesSet() {
    }

    private synchronized DataSource create(DataSourceInfo dataSourceInfo) {
        DataSource dataSource = cacheDataSource.get(dataSourceInfo.getName());
        if(dataSource == null) {
            dataSource = CreateDataSource.get(
                    dataSourceInfo.getDriverClassName(),
                    dataSourceInfo.getUrl(),
                    dataSourceInfo.getUsername(),
                    dataSourceInfo.getPassword());

            cacheDataSource.put(dataSourceInfo.getName(), dataSource);
        }

        return dataSource;
    }

}
