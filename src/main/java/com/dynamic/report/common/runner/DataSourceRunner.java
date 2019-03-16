package com.dynamic.report.common.runner;

import com.dynamic.report.common.cache.DataSourceCache;
import com.dynamic.report.common.datasource.CreateDataSource;
import com.dynamic.report.entity.DataSourceInfo;
import com.dynamic.report.service.DataBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Component
public class DataSourceRunner implements CommandLineRunner {

    @Autowired
    DataBaseService dataBaseService;

    @Override
    public void run(String... strings) throws Exception {
        List<DataSourceInfo> list = dataBaseService.list();
        if(!CollectionUtils.isEmpty(list)) {
            for(DataSourceInfo dataSourceInfo : list) {
                DataSourceCache.cacheDataSource.put(dataSourceInfo.getId().toString(),
                        CreateDataSource.get(
                            dataSourceInfo.getDriverClassName(),
                            dataSourceInfo.getUrl(),
                            dataSourceInfo.getUsername(),
                            dataSourceInfo.getPassword()
                        ));
            }
        }
    }

}
