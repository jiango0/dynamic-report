package com.dynamic.report.service;

import com.dynamic.report.entity.BasicResult;

public interface BasicService {

    BasicResult select(String sql, String limit);

    BasicResult list(String sql);

}
