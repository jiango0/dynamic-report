package com.dynamic.report.configurer;

import com.dynamic.report.common.cache.DataSourceCache;
import com.dynamic.report.common.datasource.CreateDataSource;
import com.dynamic.report.common.datasource.SwitchDataSource;
import com.dynamic.report.entity.DataSourceInfo;
import com.dynamic.report.notice.event.NoticeEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.sql.DataSource;

@Configuration
public class DynamicDataSourceConfigurer extends AbstractRoutingDataSource {

    // 默认数据源，也就是主库
    @Resource(name = "masterDataSource")
    protected DataSource masterDataSource;

    @Autowired
    ApplicationContext applicationContext;

    @Override
    protected DataSource determineTargetDataSource() {
        DataSourceInfo dataSourceInfo = determineCurrentLookupKey();
        if( dataSourceInfo == null || dataSourceInfo.getId() == null) {
            return masterDataSource;
        }

        DataSource dataSource = DataSourceCache.cacheDataSource.get(dataSourceInfo.getId().toString());
        if(dataSource == null) {
            throw new RuntimeException("datasource not find");
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

//    private synchronized DataSource create(DataSourceInfo dataSourceInfo) {
//        DataSource dataSource = DataSourceCache.cacheDataSource.get(dataSourceInfo.getName());
//        if(dataSource == null) {
//            dataSource = CreateDataSource.get(
//                    dataSourceInfo.getDriverClassName(),
//                    dataSourceInfo.getUrl(),
//                    dataSourceInfo.getUsername(),
//                    dataSourceInfo.getPassword());
//
//            //放入换缓存信息
//            DataSourceCache.cacheDataSource.put(dataSourceInfo.getName(), dataSource);
//
//            applicationContext.publishEvent(new NoticeEvent(this, dataSourceInfo));
//        }
//
//        return dataSource;
//    }

}
