package com.dynamic.report.service;

import com.dynamic.report.entity.BasicResult;

public interface BasicService {

    BasicResult select(String sql);

    BasicResult list(String sql);

}
