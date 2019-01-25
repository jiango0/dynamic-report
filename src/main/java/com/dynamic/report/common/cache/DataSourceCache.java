package com.dynamic.report.common.cache;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

public class DataSourceCache {

    //数据源缓存
    public static final Map<String, DataSource> cacheDataSource = new HashMap<>();

}
