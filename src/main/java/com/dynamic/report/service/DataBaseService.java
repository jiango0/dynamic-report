package com.dynamic.report.service;

import com.dynamic.report.entity.DataSourceInfo;
import com.dynamic.report.vo.DataSourceInfoVO;

import java.util.List;

public interface DataBaseService {

    List<DataSourceInfoVO> listAll();

    List<DataSourceInfo> list();

    void save(DataSourceInfo dataSourceInfo);

    void delete(String name);

}
