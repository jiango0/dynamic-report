package com.dynamic.report.dao;

import com.dynamic.report.entity.DataSourceInfo;

import java.util.List;

public interface DataSourceInfoMapper {

    void save(DataSourceInfo dataSourceInfo);

    List<DataSourceInfo> list(DataSourceInfo dataSourceInfo);

}
