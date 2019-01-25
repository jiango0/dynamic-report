package com.dynamic.report.service;

import com.dynamic.report.entity.DataSourceInfo;

import java.util.List;

public interface DataBaseService {

    List<DataSourceInfo> list();

    void save(DataSourceInfo dataSourceInfo);

    void delete(String name);

}
